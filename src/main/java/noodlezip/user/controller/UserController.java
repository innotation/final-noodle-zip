package noodlezip.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ErrorReasonDto;
import noodlezip.common.exception.CustomException;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.User;
import noodlezip.user.service.EmailVerificationService;
import noodlezip.user.service.UserService;
import noodlezip.user.status.UserErrorStatus;
import noodlezip.user.status.UserSuccessStatus;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Tag(name = "유저 관리", description = "사용자 관련 API")
@Slf4j
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final ModelMapper modelMapper;

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지", description = "사용자 로그인 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 페이지 반환 성공")
    })
    public void loginPage() {
    }

    @GetMapping("/signup")
    @Operation(summary = "회원 가입 페이지", description = "사용자 회원 가입 폼 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 페이지 반환 성공")
    })
    public void signupPage(Model model) {
        model.addAttribute("user", new UserDto());
    }

    @PostMapping("/signup")
    @Operation(summary = "회원 가입 처리", description = "새로운 사용자 계정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공 및 루트 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 (Bad Request)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE,
                            schema = @Schema(implementation = String.class)))
    })
    @Parameters({
            @Parameter(name = "user", description = "회원 가입 정보를 담은 UserDto 객체", required = true,
                    schema = @Schema(implementation = UserDto.class))
    })
    public String signup(@Validated(ValidationGroups.OnCreate.class) @ModelAttribute UserDto user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("\n"));
            log.error("유효성 검사 실패: \n{}", errorMessage);
            model.addAttribute("validationError", errorMessage);
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
    @ResponseBody
    @Operation(summary = "아이디 중복 검사", description = "회원 가입 시 사용자 아이디의 중복 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 사용 가능 (중복 아님)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = noodlezip.common.dto.ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "아이디 입력 누락 (Bad Request)",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "409", description = "아이디 중복 (Conflict)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class)))
    })
    @Parameter(name = "loginId", description = "중복 검사할 사용자 로그인 아이디", required = true, example = "user123")
    public ResponseEntity<?> checkLoginIdDuplication(@RequestParam("loginId") String loginId) {
        log.error("아이디 중복 검사 요청: {}", loginId);
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
    @Operation(summary = "이메일 중복 검사", description = "회원 가입 시 이메일 주소의 중복 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 사용 가능 (중복 아님)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = noodlezip.common.dto.ApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "이메일 중복 (Conflict)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class)))
    })
    @Parameter(name = "email", description = "중복 검사할 이메일 주소", required = true, example = "test@example.com")
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
    @Operation(summary = "이메일 인증", description = "사용자가 받은 인증 코드를 확인하여 이메일 인증을 완료합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공 및 루트 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "이메일 또는 인증 코드 누락 (Bad Request)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "401", description = "인증 코드 불일치 또는 만료 (Unauthorized)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 오류 (Internal Server Error)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(name = "email", description = "인증할 사용자 이메일 주소", required = true, example = "test@example.com"),
            @Parameter(name = "code", description = "이메일로 전송된 인증 코드", required = true, example = "123456")
    })
    public String verifyEmail(@RequestParam("email") String email,
                              @RequestParam("code") String code,
                              Model model) {

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
            model.addAttribute("message", e.getMessage());
            model.addAttribute("status", "failure");
            model.addAttribute("errorCode", e.getErrorCode().getReason().getCode());
            model.addAttribute("emailForResend", email);

            if (e.getErrorCode() == UserErrorStatus._MISS_MATCH_AUTH_CODE) {
                model.addAttribute("showResendButton", true);
            } else if (e.getErrorCode() == UserErrorStatus._EXPIRED_AUTH_CODE) {
                model.addAttribute("showResendButton", true);
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
    @ResponseBody
    @Operation(summary = "인증 코드 재전송", description = "이메일 인증 코드를 재전송합니다. 이미 인증된 이메일이거나, 유효하지 않은 이메일인 경우 오류가 발생할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드 재전송 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = noodlezip.common.dto.ApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음 (Not Found)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 접근 (Unauthorized)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class)))
    })
    @Parameter(name = "request", description = "이메일 주소를 담은 JSON 객체 (예: `{\"email\": \"test@example.com\"}`)", required = true)
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new CustomException(UserErrorStatus._NOT_FOUND_USER);
        }
        try {
            emailVerificationService.sendVerificationCode(email);
            return noodlezip.common.dto.ApiResponse.onSuccess(UserSuccessStatus._OK_EMAIL_RESEND);
        } catch (Exception e) {
            log.error("인증 코드 재전송 중 오류 발생 - Email: {}", email, e);
            throw new CustomException(UserErrorStatus._UNAUTHORIZED_ACCESS);
        }
    }

    @GetMapping("/user/edit-profile")
    @Operation(summary = "프로필 수정 페이지", description = "로그인한 사용자의 프로필 수정 폼 페이지를 반환합니다. 현재 프로필 정보를 미리 채웁니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 수정 페이지 반환 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "302", description = "로그인하지 않은 경우 로그인 페이지로 리다이렉트")
    })
    @Parameter(hidden = true, name = "userDetails", description = "현재 로그인된 사용자 정보 (Spring Security)")
    public String editProfilePage(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {

        if (userDetails == null || userDetails.getUser() == null) {
            log.error("User entity is null within MyUserDetails.");
            return "redirect:/login";
        }

        User loggedInUser = userDetails.getUser();
        UserDto userDto = modelMapper.map(loggedInUser, UserDto.class);
        model.addAttribute("userProfile", userDto);

        return "/user/edit-profile";
    }

    @PostMapping("user/profile/update")
    @Operation(summary = "프로필 업데이트 처리", description = "로그인한 사용자의 프로필 정보(닉네임, 프로필 이미지, 배너 이미지)를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 업데이트 성공 및 프로필 수정 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "302", description = "로그인하지 않은 경우 로그인 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 (Bad Request)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 오류 (Internal Server Error)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(name = "userDto", description = "업데이트할 사용자 정보를 담은 UserDto 객체", required = true,
                    schema = @Schema(implementation = UserDto.class)),
            @Parameter(name = "profileImageFile", description = "새로운 프로필 이미지 파일 (선택 사항)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)),
            @Parameter(name = "bannerImageFile", description = "새로운 배너 이미지 파일 (선택 사항)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)),
            @Parameter(hidden = true, name = "userDetails", description = "현재 로그인된 사용자 정보 (Spring Security)")
    })
    public String updateProfile(
            @Validated(ValidationGroups.OnUpdate.class) @ModelAttribute("userProfile") UserDto userDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            @RequestParam(value = "bannerImageFile", required = false) MultipartFile bannerImageFile,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            log.error("유효성 검사 실패: {}", bindingResult.getAllErrors());
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            });
            redirectAttributes.addFlashAttribute("validationError", errorMessage.toString());
            redirectAttributes.addFlashAttribute("userProfile", userDto);
            return "redirect:/user/edit-profile";
        }

        try {
            Long userId = userDetails.getUser().getId();
            userService.updateUser(userId, userDto, profileImageFile, bannerImageFile);
            redirectAttributes.addFlashAttribute("successMessage", "프로필이 성공적으로 업데이트되었습니다!");
        } catch (Exception e) {
            log.error("프로필 업데이트 중 오류 발생: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "프로필 업데이트에 실패했습니다: " + e.getMessage());
            redirectAttributes.addFlashAttribute("userProfile", userDto);
        }

        return "redirect:/user/edit-profile";
    }

    @PostMapping("user/signout")
    @Operation(summary = "회원 탈퇴 처리", description = "로그인한 사용자의 계정을 탈퇴 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공 및 로그아웃 처리 후 로그인 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "302", description = "로그인하지 않은 경우 로그인 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 오류 (Internal Server Error) 또는 회원 탈퇴 실패",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(hidden = true, name = "userDetails", description = "현재 로그인된 사용자 정보 (Spring Security)"),
            @Parameter(hidden = true, name = "request", description = "HttpServletRequest 객체 (세션 무효화를 위함)")
    })
    public String deleteUser(
            @AuthenticationPrincipal MyUserDetails userDetails,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        Long userId = userDetails.getUser().getId();
        userService.signoutUser(userId);

        // 회원 탈퇴 성공 시, 현재 세션 무효화 및 로그아웃 처리
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        redirectAttributes.addFlashAttribute("successMessage", "성공적으로 회원 탈퇴가 완료되었습니다.");
        return "redirect:/login"; // 탈퇴 후 로그인 페이지로 리다이렉트
    }
}