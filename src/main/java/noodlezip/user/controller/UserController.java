package noodlezip.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.User;
import noodlezip.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Tag(name = "유저 관리", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public void loginPage(){}

    @GetMapping("/signup")
    public void signupPage(){}


    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 가입 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    public String signup(@Validated @ModelAttribute UserDto user){
        userService.registUser(user);
        return "redirect:/";
    }
}
