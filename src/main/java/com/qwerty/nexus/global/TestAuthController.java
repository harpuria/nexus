package com.qwerty.nexus.global;

import com.qwerty.nexus.global.util.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TestAuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/test-login")
    public Map<String, String> testLogin(@RequestParam String email){
        String token = jwtUtil.generateAccessToken(JwtTokenGenerationData.builder().email(email).build());
        return Map.of("jwt", token);
    }
}
