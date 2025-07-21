package noodlezip.common.exception;

import lombok.extern.slf4j.Slf4j;
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
    public String handleCustomException(CustomException e, Model model) {
        log.error("Web CustomException 발생: {}", e.getMessage(), e); // 로깅

        model.addAttribute("errorCode", e.getErrorCode().getReason().getCode());
        model.addAttribute("errorMessage", e.getErrorCode().getReason().getMessage());

        return "error/customError";
    }
}