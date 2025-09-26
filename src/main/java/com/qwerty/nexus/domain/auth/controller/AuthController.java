package com.qwerty.nexus.domain.auth.controller;

import com.qwerty.nexus.domain.auth.service.AuthService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.Path.AUTH_PATH)
public class AuthController {
    private final AuthService service;

    /**
     * 소셜 로그인
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "소셜 로그인")
    public ResponseEntity<ApiResponse<Void>> login(){
        // jwt (or session) 등록 처리
        /*
            1) 유저가 모바일 기기에서 소셜로그인을 시도, 성공하면 idToken 값이 나오는데 이거랑, clientId 를 컨트롤러에서 받음.
            2) 받은 내용으로 우선 백엔드 단에서 정말 올바른 idToken 값인지 인증
            3) 유저의 정보 (subjectid, email 등)를 가지고 현재 가입된 유저인지 확인
            3-1) 가입된 유저가 아니라면 INSERT 작업 (유저, 유저재화, 유저퀘스트, 유저업적 등등 유저관련 기본 데이터들)
            3-2) 가입된 유저라면 넘어감
            4) jwt 로 accesstoken 을 생성한 뒤에 반환처리함
            5) 여기서는 로그인 관련 정보만 보내주고, 나머지 유저재화 등의 정보는 별도의 API 를 호출하여 받는 식으로 함 (책임 분리)
         */
        return null;
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
