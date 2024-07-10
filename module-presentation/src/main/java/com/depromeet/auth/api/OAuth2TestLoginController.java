package com.depromeet.auth.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Hidden
@Controller
@RequestMapping("/api/v1/auth")
public class OAuth2TestLoginController {

    @GetMapping("/kakao/login")
    public String kakaoLogin() {
        return "login";
    }
}
