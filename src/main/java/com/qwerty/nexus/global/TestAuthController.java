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

    @PostMapping("/test-google")
    public Map<String, String> testGoogle(){
        // 1) 구글 id_token(얘도 jwt 형식임) 을 받아서 여기서 claims 추출
        // 2) 추출한 claims 을 통해서 해당 사용자의 정보가 존재하는지 DB 에서 확인
        // 2-1) 없으면 INSERT 처리
        // 2-2) 있으면 다음으로 넘어가고
        // 3) jwt generateAccessToken 진행 후 AccessToken 반환 처리
        return Map.of();
    }
}
