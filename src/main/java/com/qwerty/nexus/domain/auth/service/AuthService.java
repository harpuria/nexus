package com.qwerty.nexus.domain.auth.service;

import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.auth.commnad.AuthCommand;
import com.qwerty.nexus.domain.auth.dto.AuthResponseDto;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final GameUserRepository gameUserRepository;

    /**
     * 로그인
     * @param command
     * @return
     */
    public Result<AuthResponseDto> login(AuthCommand command) {
        // 유저의 가입 정보를 확인하고,
        // 없으면 가입(INSERT) 후 jwt 토큰 반환 처리
        // 없으면 바로 jwt 토큰 반환 처리 (아니면 최종 로그인 시간 정도 넣어서 업데이트?)

        GameUserEntity entity = GameUserEntity.builder().build();
        boolean isUser = gameUserRepository.isUserAlreadyRegistered(entity) > 0;
        if(!isUser){
            // 없으면 가입처리
            gameUserRepository.createGameUser(entity);
            // 회원가입할 때는 USER_XXX 테이블에 있는 모든 정보들을 넣어줘야함.
            // 일단은 그런정보 없으니 기본 회원정보만 INSERT 처리
        }

        // jwt 토큰 생성 (이거도 provider 가 google, apple 인지에 따라서 달라질듯)
        String socialId = "";
        String email = "";
        switch(command.getProvider()){
            case Provider.GOOGLE -> {
                socialId = command.getGoogleIdToken().getPayload().getSubject();
                email = command.getGoogleIdToken().getPayload().getEmail();
            }
            case Provider.APPLE ->  {}
        }

        JwtTokenGenerationData tokenData = JwtTokenGenerationData.builder()
                                                        .socialId(socialId)
                                                        .email(email)
                                                        .build();

        String accessToken = jwtUtil.generateAccessToken(tokenData);


        AuthResponseDto rst = AuthResponseDto.builder()
                .accessToken(accessToken)
                .build();

        return Result.Success.of(rst, "로그인 성공");
    }

    /**
     * 로그아웃
     * @param command
     * @return
     */
    public Result<AuthResponseDto> logout(AuthCommand command) {
        return Result.Success.of(null, "성공이다");
    }
}
