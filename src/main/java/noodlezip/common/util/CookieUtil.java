package noodlezip.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CookieUtil {

    private static final String DEFAULT_PATH = "/";
    private static final int DEFAULT_MAX_AGE = 60 * 60 * 24;

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null || request.getCookies().length == 0) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(cookie -> {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                })
                .orElse(null);
    }

    /**
     * 새로운 쿠키를 생성하여 응답에 추가합니다.
     *
     * @param response   HttpServletResponse 객체
     * @param name       쿠키 이름
     * @param value      쿠키 값
     * @param maxAge     쿠키 유효 기간 (초 단위)
     * @param path       쿠키 유효 경로
     * @param httpOnly   JavaScript 접근 가능 여부
     * @param secure     HTTPS 전용 여부
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path, boolean httpOnly, boolean secure) {
        try {
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie(name, encodedValue);
            cookie.setPath(path);
            cookie.setMaxAge(maxAge);
            cookie.setHttpOnly(httpOnly);
            cookie.setSecure(secure);
            response.addCookie(cookie);
        } catch (Exception e) {
            System.err.println("Error encoding cookie value: " + e.getMessage());
        }
    }

    /**
     * 특정 ID를 최근 본 항목 목록 쿠키에 추가하거나 업데이트합니다.
     * 이 메소드는 순서를 유지하고 중복을 제거하며, 최대 개수를 관리합니다.
     *
     * @param itemId      현재 본 항목의 ID (Long)
     * @param cookieName  쿠키 이름 (예: "recentViewedBoards")
     * @param maxItems    쿠키에 저장할 최대 항목 개수
     * @param request     HttpServletRequest 객체
     * @param response    HttpServletResponse 객체
     */
    public static void updateRecentViewedItemsCookie(Long itemId, String cookieName, int maxItems,
                                                     HttpServletRequest request, HttpServletResponse response) {

        // 1. 기존 쿠키에서 항목 ID 목록을 가져옴
        String existingCookieValue = getCookieValue(request, cookieName);
        LinkedHashSet<String> recentIds = new LinkedHashSet<>();
        if (existingCookieValue != null && !existingCookieValue.isEmpty()) {
            recentIds.addAll(Arrays.asList(existingCookieValue.split(",")));
        }

        // 2. 현재 항목 ID를 목록의 맨 앞에 추가 (LinkedHashSet은 삽입 순서를 유지)
        String currentIdStr = String.valueOf(itemId);
        recentIds.remove(currentIdStr); // 기존에 있다면 제거 (가장 최근으로 이동시키기 위해)

        // 새로운 LinkedHashSet을 만들어 현재 ID를 맨 앞에 추가
        LinkedHashSet<String> updatedRecentIds = new LinkedHashSet<>();
        updatedRecentIds.add(currentIdStr);
        updatedRecentIds.addAll(recentIds); // 기존 ID들 추가

        // 3. 최대 개수 초과 시 오래된 것부터 제거
        while (updatedRecentIds.size() > maxItems) {
            String firstElement = updatedRecentIds.iterator().next(); // 가장 오래된 요소
            updatedRecentIds.remove(firstElement);
        }

        // 4. 업데이트된 ID 목록을 문자열로 변환
        String newCookieValue = String.join(",", updatedRecentIds);

        // 5. 쿠키 생성 및 응답에 추가 (기본 설정 사용)
        addCookie(response, cookieName, newCookieValue, DEFAULT_MAX_AGE, DEFAULT_PATH, true, false); // secure는 운영 환경에 맞춰 조정
    }

    /**
     * 특정 쿠키 이름으로 저장된 최근 본 항목 ID들을 Long 타입 List로 반환합니다.
     *
     * @param request    HttpServletRequest 객체
     * @param cookieName 읽어올 쿠키의 이름
     * @return 파싱된 Long 타입 ID 목록 (쿠키에 저장된 순서 유지), 없으면 빈 리스트
     */
    public static List<Long> getRecentViewedItemIds(HttpServletRequest request, String cookieName) {
        String cookieValue = getCookieValue(request, cookieName);
        if (cookieValue == null || cookieValue.isEmpty()) {
            return new java.util.ArrayList<>();
        }

        return Arrays.stream(cookieValue.split(","))
                .map(idStr -> {
                    try {
                        return Long.parseLong(idStr.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid ID in cookie: " + idStr + " - " + e.getMessage());
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .distinct() // 중복 제거
                .collect(Collectors.toList());
    }
}
