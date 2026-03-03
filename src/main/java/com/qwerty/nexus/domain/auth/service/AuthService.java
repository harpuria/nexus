package com.qwerty.nexus.domain.auth.service;

import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.auth.dto.request.AuthRequestDto;
import com.qwerty.nexus.domain.auth.dto.response.AuthResponseDto;
import com.qwerty.nexus.domain.game.data.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.data.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.data.item.repository.ItemRepository;
import com.qwerty.nexus.domain.game.data.item.repository.UserItemStackRepository;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.jwt.JwtTokenGenerationData;
import com.qwerty.nexus.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final GameUserRepository gameUserRepository;

    private final ItemRepository itemRepository;
    private final UserItemStackRepository userItemStackRepository;

    /**
     * 로그인
     * @param dto
     * @return
     */
    public Result<AuthResponseDto> login(AuthRequestDto dto) {
        // 유저의 가입 정보를 확인하고,
        // 없으면 가입(INSERT) 후 jwt 토큰 반환 처리
        // 없으면 바로 jwt 토큰 반환 처리 (아니면 최종 로그인 시간 정도 넣어서 업데이트?)

        // provider 에 따른 분기
        String socialId = "";
        String email = "";
        switch(dto.getProvider()){
            case Provider.GOOGLE -> {
                socialId = dto.getGoogleIdToken().getPayload().getSubject();
                email = dto.getGoogleIdToken().getPayload().getEmail();

                GameUserEntity gameUserEntity = GameUserEntity.builder()
                        .provider(Provider.GOOGLE)
                        .gameId(dto.getGameId())
                        .socialId(socialId)
                        .nickname(UUID.randomUUID().toString()) // 초기 닉네임은 일단 랜덤으로 처리 (나중엔 UUID 말고 다른거로..)
                        .createdBy(socialId)
                        .updatedBy(socialId)
                        .build();

                boolean isUser = gameUserRepository.existsByGameIdAndSocialId(gameUserEntity);
                if(!isUser){
                    // 유저가 없으면 등록 처리 후 userId 가져오기
                    Integer userId = gameUserRepository.insertGameUser(gameUserEntity);
                    if(userId == null){
                        return Result.Failure.of("로그인 실패 (유저 생성 실패)", ErrorCode.INTERNAL_ERROR.getCode());
                    }

                    // 신규회원에게 USER_XXX 에블에 있는 모든 정보 INSERT 처리 (ex : USER_CURRENCY)
                    createUserData(dto, userId, socialId);
                }
            }
            case Provider.APPLE ->  {
                System.out.println("애플 가입입니다.");
            }
        }

        // Jwt 토큰 생성 - s
        JwtTokenGenerationData tokenData = JwtTokenGenerationData.builder()
                                                        .socialId(socialId)
                                                        .email(email)
                                                        .build();

        String accessToken = jwtUtil.generateAccessToken(tokenData);    // 액세스 토큰 생성
        long expiresIn = jwtUtil.getTimeUntilExpiration(accessToken);   // 토큰 만료시간
        // Jwt 토큰 생성 - e

        AuthResponseDto rst = AuthResponseDto.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .build();

        return Result.Success.of(rst, "로그인 성공");
    }

    /**
     * 로그아웃
     * @param dto
     * @return
     */
    public Result<AuthResponseDto> logout(AuthRequestDto dto) {
        return Result.Success.of(null, "성공이다");
    }

    /**
     * 신규 유저 데이터 생성
     */
    private void createUserData(AuthRequestDto dto, int userId, String socialId){
        // 사용자 정의 테이블의 경우 초반 테이블 만들때 유저데이터 컬럼(가칭)이 Y 인 경우에는 생성하게 끔 처리하면 될듯
        // 유저 아이템
        ItemEntity entity = ItemEntity.builder()
                .gameId(dto.getGameId())
                .isStackable("Y") // 스택형 아이템
                .build();

        List<Integer> itemIdList = itemRepository.findAllItemIdsByGameId(entity);
        if(!itemIdList.isEmpty()){
            itemIdList.forEach(itemId -> {
                // 스택형 아이템 추가
                // ** 인스턴스형은 고정된 아이템이 아니기 때문에 여기에서 추가하지 않음 **
                UserItemStackEntity userItemStackEntity = UserItemStackEntity.builder()
                        .itemId(itemId)
                        .userId(userId)
                        .createdBy(socialId)
                        .updatedBy(socialId)
                        .build();

                userItemStackRepository.insertUserItemStack(userItemStackEntity);
            });
        }
    }
}
