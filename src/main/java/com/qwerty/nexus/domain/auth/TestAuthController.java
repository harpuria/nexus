package com.qwerty.nexus.domain.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.auth.service.GoogleVerifierService;
import com.qwerty.nexus.global.util.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TestAuthController {

    private final JwtUtil jwtUtil;
    private final GoogleVerifierService googleVerifierService;

    @PostMapping("/test-login")
    public Map<String, String> testLogin(@RequestParam String email){
        String token = jwtUtil.generateAccessToken(JwtTokenGenerationData.builder().email(email).build());
        return Map.of("jwt", token);
    }

    @PostMapping("/test-google")
    public Map<String, String> testGoogle() throws GeneralSecurityException, IOException {
        // 작업 흐름
        // 1) 구글 id_token(얘도 jwt 형식임) 을 받아서 서버단에서 추가 검증
        // 2) 검증이 완료되었으면 claims 추출
        // 3) 추출한 claims 을 통해서 해당 사용자의 정보가 존재하는지 DB 에서 확인
        //  3-1) 없으면 INSERT 처리
        //  3-2) 있으면 다음으로 넘어가고
        // 4) jwt generateAccessToken 진행 후 AccessToken 반환 처리


        GoogleIdToken rst = googleVerifierService.verifyGoogleIdToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IjkyN2I4ZmI2N2JiYWQ3NzQ0NWU1ZmVhNGM3MWFhOTg0NmQ3ZGRkMDEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTY5MDE4MDkwODgyNTYwMzMzMTkiLCJlbWFpbCI6ImhhcnB1cmlhODdAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJpamtwWGJnZnN6d2tiNjM5SkxYdUpnIiwibmFtZSI6IkhvbmcgSHVuIFl1biIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NLc2Zyb24xU25PZGdEU3FLa2NETk5CQ3l6dHFUcmV3UU5jbk9udFBnTkRXWHJ1TTRRPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IkhvbmcgSHVuIiwiZmFtaWx5X25hbWUiOiJZdW4iLCJpYXQiOjE3NTg3NjYxODUsImV4cCI6MTc1ODc2OTc4NX0.dxYP79Ww_It8sNTgbNI3y-Brg99vD1Bd2NQbob1jU7uqnBCkBQHfkL7Ww4fLvop6v3Hrht9yOIm5wWek3mOFmaZW05qfhQFjCxzSxe1m-1kAyWrTznkN90gG6FHj0-0ahlqxEaQD2qXoO0TNmKVrYp68z4i-6r1jOwX7WMzQkdZ32PqDomIwgQVrbL5snIEC5xRuIbt5ZlOjW_zfm2nIRR6sdVhYwOhC9oMp26CwIC6b-Rv-ECOisqi19Lt46GUP9G6EG02wmGAQNWqxSaYAXpJCqjMEXOwvwQagDfx7t-1cGq8EyGYakhD3f2DTnwMIEdSmIYLHuHlbURkVqnsCJw", "407408718192.apps.googleusercontent.com");
        // subjectid 로 검색했을 때 존재하지 않는 경우, subject 이걸 저장하면 됨 (고유값)
        // 각 provider 마다 subjectid 는 고유한 값임.
        // 존재하면 insert 하는 부분은 넘어가고 accesstoken 만들어서 던져주기
        System.out.println(rst.getPayload().getSubject());

        return Map.of("jwt", "");
    }
}
