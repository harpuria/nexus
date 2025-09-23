package com.qwerty.nexus.global;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class TestUserController {
    @GetMapping("/profile")
    public Map<String, String> profile(Authentication authentication){
        String email = (String)authentication.getPrincipal();
        return Map.of("email", email, "status", "ok");
    }
}
