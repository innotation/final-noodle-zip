package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.store.dto.LocationInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {

    /**
     * safeCutAddress() = 유틸
     * trySearchAddress() = 주소 API 호출
     * searchByKeyword() = 키워드 fallback
     * buildLocationDto() = 위경도 → 법정동 코드 변환
     */

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // 안전하게 주소 자르는 함수
    private String safeCutAddress(String address, int maxEncodedLength) {
        if (address == null) return null;

        int end = address.length();
        while (end > 0) {
            String sub = address.substring(0, end);
            String encoded = UriUtils.encode(sub, StandardCharsets.UTF_8);
            if (encoded.length() <= maxEncodedLength) {
                return sub;
            }
            end--;
        }
        return "";
    }

    // 주소 검색
    private LocationInfoDto trySearchAddress(String address) {
        if (address == null || address.isBlank()) return null;

        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = res.getBody();

        if (responseBody == null) return null;

        List<?> docs = (List<?>) responseBody.get("documents");
        if (docs == null || docs.isEmpty()) {
            log.warn("주소 검색 결과 없음: {}", address);
            return null;
        }

        Map<String, Object> firstDoc = (Map<String, Object>) docs.get(0);
        String x = (String) firstDoc.get("x");
        String y = (String) firstDoc.get("y");
        log.info("주소 검색 결과 - x: {}, y: {}", x, y);

        return buildLocationDto(x, y);
    }

    // 메인 메서드 (자른 주소로 먼저 시도, 실패하면 원본 주소로 재시도, 그 후 키워드 검색)
    public LocationInfoDto getLocationInfo(String roadAddress) {
        if (roadAddress == null || roadAddress.isBlank()) {
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }
        // 1) 자른 주소로 시도
        String safeAddress = safeCutAddress(roadAddress, 100);
        LocationInfoDto location = trySearchAddress(safeAddress);

        // 2) 실패하면 원본 주소로 재시도
        if (location == null) {
            location = trySearchAddress(roadAddress);
        }

        // 3) 그래도 실패하면 키워드 검색 fallback
        if (location == null) {
            location = searchByKeyword(roadAddress);
        }

        return location;
    }

    /*
     주소 검색 결과가 없을 때 호출되는 키워드 기반 검색 메서드
     입력 키워드를 받아 카카오 키워드 검색 API를 호출해 위도, 경도, 법정동 코드를 조회함
     */
    private LocationInfoDto searchByKeyword(String keyword) {

        if (keyword == null) {
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        String safeKeyword = safeCutAddress(keyword, 100);
        String encodedKeyword = UriUtils.encode(safeKeyword, StandardCharsets.UTF_8);

        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + encodedKeyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> responseBody = res.getBody();
        List<?> docs = (List<?>) responseBody.get("documents");

        if (docs == null || docs.isEmpty()) {
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        Map<String, Object> firstDoc = (Map<String, Object>) docs.get(0);
        String x = (String) firstDoc.get("x");
        String y = (String) firstDoc.get("y");
        log.info("키워드 검색 결과 - x: {}, y: {}", x, y);

        return buildLocationDto(x, y);
    }

    /*
     위도, 경도 좌표를 받아 카카오 좌표 -> 법정동 코드 변환 API를 호출하여
     법정동 코드를 포함한 LocationInfoDto 생성
     */

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private LocationInfoDto buildLocationDto(String x, String y) {
        Double lat = Double.parseDouble(y);
        Double lng = Double.parseDouble(x);
        log.info("buildLocationDto - lat: {}, lng: {}", lat, lng);


        String coordUrl = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + x + "&y=" + y;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> coordRes = restTemplate.exchange(coordUrl, HttpMethod.GET, entity, Map.class);

        List<?> coordDocs = (List<?>) coordRes.getBody().get("documents");

        if (coordDocs == null || coordDocs.isEmpty()) {
            throw new IllegalArgumentException("좌표 → 법정동 코드 응답이 없습니다.");
        }

        String legalCode = coordDocs.stream()
                .map(o -> (Map<?, ?>) o)
                .filter(doc -> "B".equals(doc.get("region_type")))
                .map(doc -> (String) doc.get("code"))
                .findFirst()
                .orElse(null);

        if (legalCode == null) {
            log.warn("법정동 코드 추출 실패 - x: {}, y: {}, 응답: {}", x, y, coordDocs);
            throw new IllegalArgumentException("법정동 코드가 존재하지 않습니다. 응답: " + coordDocs);
        }
        log.info("법정동 코드 추출 성공 - legalCode: {}", legalCode); // 추가

        Long legalCodeLong;
        try {
            legalCodeLong = Long.parseLong(legalCode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("법정동 코드 숫자 파싱 실패: " + legalCode, e);
        }

        return new LocationInfoDto(lat, lng, legalCodeLong);
    }
}