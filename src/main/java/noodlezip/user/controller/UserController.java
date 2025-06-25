package noodlezip.user.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.user.entity.User;
import noodlezip.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public void loginPage(){}

    @GetMapping("/signup")
    public void signupPage(){}

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user){
        Map<String, String> map = userService.registUser(user);
        return "redirect:/";
    }
}
