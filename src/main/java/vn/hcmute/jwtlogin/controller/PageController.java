package vn.hcmute.jwtlogin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.hcmute.jwtlogin.entity.User;
import vn.hcmute.jwtlogin.mapper.UserMapper;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final UserMapper userMapper;

    @GetMapping("/")
    public String home() {
        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userMapper.toResponse(user));
        return "profile";
    }
}
