package com.qwerty.nexus.domain.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.auth.dto.request.AuthRequestDto;
import com.qwerty.nexus.domain.auth.dto.response.AuthResponseDto;
import com.qwerty.nexus.domain.auth.service.AppleVerifierService;
import com.qwerty.nexus.domain.auth.service.AuthService;
import com.qwerty.nexus.domain.auth.service.GoogleVerifierService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.jwt.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.Path.AUTH_PATH)
@Tag(name = "인증", description = "인증, 로그인, 로그아웃 관련 API")
public class AuthController {
    private final AuthService service;
    private final GoogleVerifierService googleVerifierService;
    private final AppleVerifierService appleVerifierService;

    private final JwtUtil jwtUtil; // 이거는 아래 테스트 컨트롤러 삭제하면 같이 삭제 (service 레이어에서 사용할것)

    // 테스트 컨트롤러 시작
    @PostMapping("/test-login")
    public Map<String, String> testLogin(@RequestParam String email){
        String token = jwtUtil.generateAccessToken(JwtTokenGenerationData.builder().email(email).build());
        return Map.of("jwt", token);
    }

    @PostMapping("/test-google")
    public Map<String, String> testGoogle() throws GeneralSecurityException, IOException {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setClientId("407408718192.apps.googleusercontent.com");
        dto.setIdToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IjkyN2I4ZmI2N2JiYWQ3NzQ0NWU1ZmVhNGM3MWFhOTg0NmQ3ZGRkMDEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTY5MDE4MDkwODgyNTYwMzMzMTkiLCJlbWFpbCI6ImhhcnB1cmlhODdAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJpamtwWGJnZnN6d2tiNjM5SkxYdUpnIiwibmFtZSI6IkhvbmcgSHVuIFl1biIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NLc2Zyb24xU25PZGdEU3FLa2NETk5CQ3l6dHFUcmV3UU5jbk9udFBnTkRXWHJ1TTRRPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IkhvbmcgSHVuIiwiZmFtaWx5X25hbWUiOiJZdW4iLCJpYXQiOjE3NTg3NjYxODUsImV4cCI6MTc1ODc2OTc4NX0.dxYP79Ww_It8sNTgbNI3y-Brg99vD1Bd2NQbob1jU7uqnBCkBQHfkL7Ww4fLvop6v3Hrht9yOIm5wWek3mOFmaZW05qfhQFjCxzSxe1m-1kAyWrTznkN90gG6FHj0-0ahlqxEaQD2qXoO0TNmKVrYp68z4i-6r1jOwX7WMzQkdZ32PqDomIwgQVrbL5snIEC5xRuIbt5ZlOjW_zfm2nIRR6sdVhYwOhC9oMp26CwIC6b-Rv-ECOisqi19Lt46GUP9G6EG02wmGAQNWqxSaYAXpJCqjMEXOwvwQagDfx7t-1cGq8EyGYakhD3f2DTnwMIEdSmIYLHuHlbURkVqnsCJw");

        GoogleIdToken rst = googleVerifierService.verifyGoogleIdToken(dto);
        // subjectid 로 검색했을 때 존재하지 않는 경우, subject 이걸 저장하면 됨 (고유값)
        // 각 provider 마다 subjectid 는 고유한 값임.
        // 존재하면 insert 하는 부분은 넘어가고 accesstoken 만들어서 던져주기
        System.out.println(rst.getPayload().getSubject());

        return Map.of("jwt", "");
    }

    @GetMapping("/profile")
    public Map<String, String> profile(Authentication authentication){
        // 인증정보 가져오기
        String email = (String)authentication.getPrincipal();
        return Map.of("email", email, "status", "ok");
    }
    // 테스트 컨트롤러 끝

    /**
     * 소셜 로그인
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "소셜 로그인")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody AuthRequestDto dto) throws GeneralSecurityException, IOException {


        // 소셜 로그인 방식에 따른 토큰 검증 처리 (소셜 로그인 자체는 클라이언트에서 수행, 서버에서는 검증 및 인증처리)
        // 1, 2번 수행
        switch(dto.getProvider()){
            case Provider.GOOGLE -> dto.setGoogleIdToken(googleVerifierService.verifyGoogleIdToken(dto));
            case Provider.APPLE -> {}
            // 기타 등등 추가
        }
        // 검증이 실패하면 바로 return 처리

        // 로그인 서비스 (여기에서 3, 4번 수행)
        Result<AuthResponseDto> rst = service.login(dto);

        return ResponseEntityUtils.toResponseEntity(rst, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "유저 로그아웃 (개발중)")
    public ResponseEntity<ApiResponse<Void>> logout(){
        // jwt (or session) 초기화 처리
        return null;
    }

    /**
     * 토큰 갱신
     * @return
     */
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신 (개발중)")
    public ResponseEntity<ApiResponse<Void>> tokenRefresh(){
        return null;
    }

    /**
     * 게스트 로그인
     * @return
     */
    @PostMapping("/guest-login")
    @Operation(summary = "게스트 로그인 (개발중)")
    public ResponseEntity<ApiResponse<Void>> guestLogin(){
        /*
            - 디바이스 식별자만 검증
            - 임시 계정 생성
            - 제한된 권한 부여
         */

        /*
        // 게스트 로그인 request/response 구조 (대략적임)
        {
            "deviceId": "uuid-string",
                "deviceType": "android/ios"
        }
         */

        return null;
    }

    /**
     * 게스트 -> 소셜 전환
     * @return
     */
    @PostMapping("/guest-upgrade")
    @Operation(summary = "게스트 -> 소셜 전환")
    public ResponseEntity<ApiResponse<Void>> guestUpgrade(){
        return null;
    }
}
