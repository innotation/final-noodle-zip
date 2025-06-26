package noodlezip.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.exception.CustomException;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.User;
import noodlezip.user.service.UserService;
import noodlezip.user.status.UserErrorStatus;
import noodlezip.user.status.UserSuccessStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Tag(name = "유저 관리", description = "사용자 관련 API")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public void loginPage() {
    }

    @GetMapping("/signup")
    public void signupPage(Model model) {
        model.addAttribute("user", new UserDto());
    }


    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 가입 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    public String signup(@Validated @ModelAttribute UserDto user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // 모든 에러 메세지 다 담음
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("\n"));
            log.error("유효성 검사 실패: \n{}", errorMessage);
            model.addAttribute("validationError", errorMessage);
            // 폼 데이터를 유지를 위해 모델에 다시 데이터 담기
            model.addAttribute("user", user);
            return "signup";
        } else {
            redirectAttributes.addFlashAttribute("message", "success");
            userService.registUser(user);
            return "redirect:/";
        }
    }

    @GetMapping("/check-login-id")
    @ResponseBody // @RestController를 사용했다면 필요 없음. @Controller라면 붙여야 함.
    public ResponseEntity<?> checkLoginIdDuplication(@RequestParam("loginId") String loginId) {
        log.error("아이디 중복 검사 요청: {}", loginId);
        // 클라이언트에서 먼저 기본적인 형식 검사 후 요청한다고 가정
        if (loginId == null || loginId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("아이디를 입력해주세요.");
        }

        boolean isDuplicated = userService.isLoginIdDuplicated(loginId);
        if (isDuplicated) {
            log.info("아이디 중복: {}", loginId);
            throw new CustomException(UserErrorStatus._ALREADY_EXIST_LOGIN_ID);
        } else {
            log.info("아이디 사용 가능: {}", loginId);
            return noodlezip.common.dto.ApiResponse.onSuccess(UserSuccessStatus._OK_LOGIN_ID_NOT_DUPLICATE);
        }
    }

    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<?> checkEmailDuplication(@RequestParam("email") String email) {
        log.info("이메일 중복 검사 요청: {}", email);

        boolean isDuplicated = userService.isEmailDuplicated(email);
        if (isDuplicated) {
            throw new CustomException(UserErrorStatus._ALREADY_EXIST_EMAIL);
        } else {
            return noodlezip.common.dto.ApiResponse.onSuccess(UserSuccessStatus._OK_EMAIL_NOT_DUPLICATE);
        }
    }
}
