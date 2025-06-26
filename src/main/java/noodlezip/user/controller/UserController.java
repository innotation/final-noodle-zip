package noodlezip.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.User;
import noodlezip.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public void loginPage(){}

    @GetMapping("/signup")
    public void signupPage(Model model){
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
        }
        else {
            redirectAttributes.addFlashAttribute("message", "success");
            userService.registUser(user);
            return "redirect:/";
        }
    }
}
