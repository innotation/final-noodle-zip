package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.store.dto.LocationInfoDto;
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
/*
    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public LocationInfoDto getLocationInfo(String roadAddress) {
        // 1. 주소 → 좌표 변환

        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" +
                UriUtils.encode(roadAddress, StandardCharsets.UTF_8);
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 6d0ae0f815d04ec1b24f32b1bf55c30c");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        System.out.println(res.getBody());
        List<?> docs = (List<?>) res.getBody().get("documents");
        Map<String, Object> responseBody = res.getBody();

        // 에러 확인
        if (responseBody.containsKey("errorType")) {
            System.out.println("API 에러: " + responseBody.get("errorType"));
            System.out.println("에러 메시지: " + responseBody.get("message"));
        }

        // meta 정보 확인
        Map<String, Object> meta = (Map<String, Object>) responseBody.get("meta");
        if (meta != null) {
            System.out.println("검색 결과 개수: " + meta.get("total_count"));
        }

//        if (docs == null || docs.isEmpty()) {
//            throw new CustomException(ErrorStatus._BAD_REQUEST);
//        }

        Map firstDoc = (Map) docs.get(0);
        String x = (String) firstDoc.get("x"); // 경도
        String y = (String) firstDoc.get("y"); // 위도

        Double lat;
        Double lng;
        try {
            lat = Double.parseDouble(y);
            lng = Double.parseDouble(x);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("위도 또는 경도 변환 실패", e);
        }

        // 2. 좌표 → 법정동 코드 조회
        String coordUrl = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + x + "&y=" + y;
        ResponseEntity<Map> coordRes = restTemplate.exchange(coordUrl, HttpMethod.GET, entity, Map.class);
        List<?> coordDocs = (List<?>) coordRes.getBody().get("documents");

        String legalCode = coordDocs.stream()
                .map(o -> (Map<?, ?>) o)
                .filter(doc -> "B".equals(doc.get("region_type")))
                .map(doc -> (String) doc.get("code"))
                .findFirst()
                .orElse(null);

        Integer legalCodeInt = null;
        if (legalCode != null) {
            try {
                legalCodeInt = Integer.parseInt(legalCode);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("법정동 코드 변환 실패", e);
            }
        }

        return new LocationInfoDto(lat, lng, legalCodeInt);
    }

 */



    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public LocationInfoDto getLocationInfo(String roadAddress) {
        // 1. 입력 주소 확인 및 전처리
        String cleanAddress = "부산광역시 해운대구 센텀남대로 35";
        System.out.println("=== 카카오 API 호출 디버깅 ===");
        System.out.println("원본 주소: [" + roadAddress + "]");
        System.out.println("정리된 주소: [" + cleanAddress + "]");
        System.out.println("API 키 존재 여부: " + (kakaoRestApiKey != null && !kakaoRestApiKey.isEmpty()));

        // 2. URL 생성 및 확인
        String encodedAddress = UriUtils.encode(cleanAddress, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedAddress;
        System.out.println("요청 URL: " + url);
        System.out.println("인코딩된 주소: " + encodedAddress);

        // 3. HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        System.out.println("Authorization 헤더: " + headers.get("Authorization"));

        try {
            // 4. API 호출
            ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = res.getBody();

            System.out.println("HTTP 상태코드: " + res.getStatusCode());
            System.out.println("전체 응답: " + responseBody);

            // 5. 응답 분석
            if (responseBody == null) {
                System.out.println("응답 본문이 null입니다.");
                throw new CustomException(ErrorStatus._BAD_REQUEST);
            }

            // meta 정보 확인
            Map<String, Object> meta = (Map<String, Object>) responseBody.get("meta");
            if (meta != null) {
                System.out.println("Meta 정보: " + meta);
                System.out.println("총 결과 수: " + meta.get("total_count"));
                System.out.println("검색 가능한 결과 수: " + meta.get("pageable_count"));
            }

            // documents 확인
            List<?> docs = (List<?>) responseBody.get("documents");
            System.out.println("Documents 존재 여부: " + (docs != null));
            System.out.println("Documents 크기: " + (docs != null ? docs.size() : "null"));

            if (docs != null && !docs.isEmpty()) {
                System.out.println("첫 번째 document: " + docs.get(0));
            }

            // 6. 결과가 없는 경우 키워드 검색 시도
            if (docs == null || docs.isEmpty()) {
                System.out.println("주소 검색 결과가 없습니다. 키워드 검색을 시도합니다.");
                return searchByKeyword(cleanAddress);
            }

            // 7. 좌표 추출
            Map<String, Object> firstDoc = (Map<String, Object>) docs.get(0);
            String x = (String) firstDoc.get("x"); // 경도
            String y = (String) firstDoc.get("y"); // 위도

            System.out.println("추출된 좌표 - x(경도): " + x + ", y(위도): " + y);

            Double lat, lng;
            try {
                lat = Double.parseDouble(y);
                lng = Double.parseDouble(x);
            } catch (NumberFormatException e) {
                System.out.println("좌표 변환 실패: " + e.getMessage());
                throw new IllegalArgumentException("위도 또는 경도 변환 실패", e);
            }

            // 8. 법정동 코드 조회
            Integer legalCodeInt = getLegalCode(x, y);

            return new LocationInfoDto(lat, lng, legalCodeInt);

        } catch (Exception e) {
            System.out.println("API 호출 중 예외 발생: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }
    }

    // 키워드 검색 메서드
    private LocationInfoDto searchByKeyword(String address) {
        System.out.println("=== 키워드 검색 시도 ===");
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" +
                UriUtils.encode(address, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = res.getBody();

            System.out.println("키워드 검색 응답: " + responseBody);

            List<?> docs = (List<?>) responseBody.get("documents");
            if (docs == null || docs.isEmpty()) {
                System.out.println("키워드 검색도 결과가 없습니다.");
                throw new CustomException(ErrorStatus._BAD_REQUEST);
            }

            Map<String, Object> firstDoc = (Map<String, Object>) docs.get(0);
            String x = (String) firstDoc.get("x");
            String y = (String) firstDoc.get("y");

            Double lat = Double.parseDouble(y);
            Double lng = Double.parseDouble(x);

            Integer legalCodeInt = getLegalCode(x, y);

            return new LocationInfoDto(lat, lng, legalCodeInt);

        } catch (Exception e) {
            System.out.println("키워드 검색 중 예외 발생: " + e.getMessage());
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }
    }

    // 법정동 코드 조회 메서드
    private Integer getLegalCode(String x, String y) {
        String coordUrl = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + x + "&y=" + y;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> coordRes = restTemplate.exchange(coordUrl, HttpMethod.GET, entity, Map.class);
            List<?> coordDocs = (List<?>) coordRes.getBody().get("documents");

            String legalCode = coordDocs.stream()
                    .map(o -> (Map<?, ?>) o)
                    .filter(doc -> "B".equals(doc.get("region_type")))
                    .map(doc -> (String) doc.get("code"))
                    .findFirst()
                    .orElse(null);

            if (legalCode != null) {
                return Integer.parseInt(legalCode);
            }

        } catch (Exception e) {
            System.out.println("법정동 코드 조회 실패: " + e.getMessage());
        }

        return null;
    }
}