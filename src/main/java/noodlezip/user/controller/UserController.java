package noodlezip.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ErrorReasonDto;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.User;
import noodlezip.user.service.EmailVerificationService;
import noodlezip.user.service.UserService;
import noodlezip.user.status.UserErrorStatus;
import noodlezip.user.status.UserSuccessStatus;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    private final EmailVerificationService emailVerificationService;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

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
    public String signup(@Validated(ValidationGroups.OnCreate.class) @ModelAttribute UserDto user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

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
            User newUser = modelMapper.map(user, User.class);
            userService.registUser(newUser);
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

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("email") String email,
                              @RequestParam("code") String code,
                              Model model) {

        // 유효성 검증
        if (email == null || email.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            model.addAttribute("message", "이메일 또는 인증 코드가 누락되었습니다.");
            model.addAttribute("status", "error");
            return "/user/verification-result";
        }
        log.info("email: {}", email);
        log.info("code: {}", code);

        try {
            userService.verifyUserEmail(email, code);
            model.addAttribute("message", "이메일 인증이 성공적으로 완료되었습니다!");
            model.addAttribute("status", "success");
            log.info("이메일 인증 성공 - Email: {}", email);
            return "redirect:/";
        } catch (CustomException e) {
            log.warn("이메일 인증 실패 - Email: {}, Code: {}, Status: {}", email, code, e.getErrorCode());

            // 뷰에 실패 메시지와 에러 상태 전달
            model.addAttribute("message", e.getMessage());
            model.addAttribute("status", "failure");
            model.addAttribute("errorCode", e.getErrorCode().getReason().getCode());
            model.addAttribute("emailForResend", email);

            // 에러 종류에 따른 분기 처리
            if (e.getErrorCode() == UserErrorStatus._MISS_MATCH_AUTH_CODE) {
                model.addAttribute("showResendButton", true); // 코드 불일치 시 재전송 버튼 표시 플래그
            } else if (e.getErrorCode() == UserErrorStatus._EXPIRED_AUTH_CODE) {
                model.addAttribute("showResendButton", true); // 만료 시에도 재전송 버튼 표시
            }

            return "/user/verification-result";
        } catch (Exception e) {
            log.error("이메일 인증 중 서버 오류 발생 - Email: {}", email, e);
            model.addAttribute("message", "서버 오류로 인해 이메일 인증에 실패했습니다. 잠시 후 다시 시도해주세요.");
            model.addAttribute("status", "error");
            return "/user/verification-result";
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new CustomException(UserErrorStatus._NOT_FOUND_USER);
        }
        try {
            // 코드 재전송
            emailVerificationService.sendVerificationCode(email);
            return noodlezip.common.dto.ApiResponse.onSuccess(UserSuccessStatus._OK_EMAIL_RESEND);
        } catch (Exception e) {
            log.error("인증 코드 재전송 중 오류 발생 - Email: {}", email, e);
            throw new CustomException(UserErrorStatus._UNAUTHORIZED_ACCESS);
        }
    }

    @GetMapping("/user/edit-profile")
    public String editProfilePage(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {

        User loggedInUser = userDetails.getUser();

        if (loggedInUser == null) {
            log.error("User entity is null within MyUserDetails for loginId: {}", userDetails.getUsername());
            return "redirect:/login";
        }

        UserDto userDto = modelMapper.map(loggedInUser, UserDto.class);

        model.addAttribute("userProfile", userDto);

        return "/user/edit-profile";
    }

    @PostMapping("user/profile/update")
    public String updateProfile(
            @Validated(ValidationGroups.OnUpdate.class) @ModelAttribute("userProfile") UserDto userDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile, // 프로필 이미지 파일
            @RequestParam(value = "bannerImageFile", required = false) MultipartFile bannerImageFile,   // 배너 이미지 파일
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 에러 메시지 폼으로 다시 전달
            log.error("validation error: {}", bindingResult.getAllErrors());
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            });
            redirectAttributes.addFlashAttribute("validationError", errorMessage.toString());
            redirectAttributes.addFlashAttribute("userProfile", userDto); // 사용자가 입력한 값 유지
            return "redirect:/user/edit-profile";
        }

        try {
            Long userId = userDetails.getUser().getId();

            userService.updateUser(userId, userDto, profileImageFile, bannerImageFile); // 서비스 호출
            redirectAttributes.addFlashAttribute("successMessage", "프로필이 성공적으로 업데이트되었습니다!");
        } catch (Exception e) {
            log.error("프로필 업데이트 중 오류 발생: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "프로필 업데이트에 실패했습니다: " + e.getMessage());
            redirectAttributes.addFlashAttribute("userProfile", userDto); // 사용자가 입력한 값 유지
        }

        return "redirect:/user/edit-profile";
    }

}
