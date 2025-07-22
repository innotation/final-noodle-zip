package noodlezip.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.dto.ApiResponse;
import noodlezip.common.status.ErrorStatus;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class GlobalWebExceptionHandler {

    /**
     * Controller 클래스의 CustomException에 대한 HTML 에러 페이지 처리
     */

    @ExceptionHandler(CustomException.class)
    public Object handleCustomException(CustomException e, Model model, HttpServletRequest request) { // HttpServletRequest 파라미터 추가
        log.error("CustomException 발생: {}", e.getMessage(), e);

        // 요청이 AJAX (Fetch/XMLHttpRequest) 요청인지 확인
        String requestedWith = request.getHeader("X-Requested-With");
        // Accept 헤더에 application/json이 포함되어 있는지 확인
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        boolean isAjaxRequest = "XMLHttpRequest".equals(requestedWith) || (acceptHeader != null && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE));


        if (isAjaxRequest) {
            // AJAX 요청인 경우 JSON 응답 반환
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST);
        } else {
            // 일반 웹 요청인 경우 HTML 에러 페이지 반환
            model.addAttribute("errorCode", e.getErrorCode().getReason().getCode());
            model.addAttribute("errorMessage", e.getErrorCode().getReason().getMessage());
            return "error/customError";
        }
    }
}