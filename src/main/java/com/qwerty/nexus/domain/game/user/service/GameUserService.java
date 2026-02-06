package com.qwerty.nexus.domain.game.user.service;

import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserBlockRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserCreateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserUpdateRequestDto;
import com.qwerty.nexus.domain.game.user.dto.request.GameUserWithdrawalRequestDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserListResponseDto;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserResponseDto;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameUserService {
    private final GameUserRepository repository;

    private final CurrencyRepository currencyRepository;
    private final UserCurrencyRepository userCurrencyRepository;

    /**
     * 게임 유저 생성
     * @param dto
     * @return
     */
    public Result<Void> createGameUser(GameUserCreateRequestDto dto) {
        GameUserEntity entity = GameUserEntity.builder()
                .gameId(dto.getGameId())
                .userLId(dto.getUserLId())
                .userLPw(dto.getUserLPw())
                .nickname(dto.getNickname())
                .provider(dto.getProvider())
                .socialId(dto.getSocialId())
                .device(dto.getDevice())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .build();

        Optional<GameUserEntity> insertRst = Optional.ofNullable(repository.insertGameUser(entity));

        if(insertRst.isPresent()){
            // 신규회원에게 USER_XXX 에블에 있는 모든 정보 INSERT 처리 (ex : USER_CURRENCY)
            createUserData(dto.getGameId(), insertRst.get().getUserId(), entity.getSocialId());

            return Result.Success.of(null, "유저 생성 성공.");
        }else{
            return Result.Failure.of("유저 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 수정
     * @param dto
     * @return
     */
    public Result<Void> updateGameUser(GameUserUpdateRequestDto dto) {
        GameUserEntity entity = GameUserEntity.builder()
                .userId(dto.getUserId())
                .gameId(dto.getGameId())
                .userLId(dto.getUserLId())
                .userLPw(dto.getUserLPw())
                .nickname(dto.getNickname())
                .provider(dto.getProvider())
                .device(dto.getDevice())
                .blockStartDate(dto.getBlockStartDate())
                .blockEndDate(dto.getBlockEndDate())
                .blockReason(dto.getBlockReason())
                .isWithdrawal(dto.getIsWithdrawal())
                .withdrawalDate(dto.getWithdrawalDate())
                .withdrawalReason(dto.getWithdrawalReason())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(repository.updateGameUser(entity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "유저 정보 수정 성공.");
        }else{
            return Result.Failure.of("유저 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 정지
     * @param dto
     * @return
     */
    public Result<Void> blockGameUser(GameUserBlockRequestDto dto) {
        GameUserEntity entity = GameUserEntity.builder()
                .userId(dto.getUserId())
                .blockStartDate(dto.getBlockStartDate())
                .blockEndDate(dto.getBlockEndDate())
                .blockReason(dto.getBlockReason())
                .updatedBy(dto.getUpdatedBy())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(repository.updateGameUser(entity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "유저 정지 성공.");
        }else{
            return Result.Failure.of("유저 정지 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 탈퇴
     * @param dto
     * @return
     */
    public Result<Void> withdrawalGameUser(GameUserWithdrawalRequestDto dto) {
        GameUserEntity entity = GameUserEntity.builder()
                .userId(dto.getUserId())
                .isWithdrawal(dto.getIsWithdrawal())
                .withdrawalDate(dto.getWithdrawalDate())
                .withdrawalReason(dto.getWithdrawalReason())
                .updatedBy(dto.getUpdatedBy())
                .build();

        Optional<GameUserEntity> updateRst = Optional.ofNullable(repository.updateGameUser(entity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "유저 탈퇴 성공.");
        }else{
            return Result.Failure.of("유저 탈퇴 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 유저 목록 조회
     * @param pagingRequestDto
     * @return
     */
    public Result<GameUserListResponseDto> listGameUsers(PagingRequestDto pagingRequestDto, int gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingRequestDto);
        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<GameUserEntity> gameUsers = Optional.ofNullable(repository.findAllByGameId(pagingEntity, gameId))
                .orElseGet(Collections::emptyList);

        long totalCount = gameUsers.size();

        List<GameUserResponseDto> responses = gameUsers.stream()
                .map(GameUserResponseDto::from)
                .collect(Collectors.toList());

        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = (long) (safePage + 1) * validatedSize < totalCount;
        boolean hasPrevious = safePage > 0 && totalCount > 0;

        GameUserListResponseDto responseDto = GameUserListResponseDto.builder()
                .users(responses)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(responseDto, "게임 유저 목록 조회 완료.");
    }

    /**
     * 신규 유저 데이터 생성
     */
    private void createUserData(int gameId, int userId, String socialId){
        // 사용자 정의 테이블의 경우 초반 테이블 만들때 유저데이터 컬럼(가칭)이 Y 인 경우에는 생성하게 끔 처리하면 될듯
        // 먼저 현재 있는건 유저재화니까 이거부터 정리 해봄
        // 1) 현재 이 게임의 재화 목록을 모두 가져오기 (currencyId)

        // 유저 재화
        List<Integer> currencyIdList = currencyRepository.findAllCurrencyIdsByGameId(CurrencyEntity.builder().gameId(gameId).build());
        if(!currencyIdList.isEmpty()){
            currencyIdList.forEach(currencyId -> {
                UserCurrencyEntity userCurrencyEntity = UserCurrencyEntity.builder()
                        .currencyId(currencyId)
                        .userId(userId)
                        .createdBy(socialId)
                        .updatedBy(socialId)
                        .build();

                userCurrencyRepository.insertUserCurrency(userCurrencyEntity);
            });
        }

        // 유저 데이터 추가될 때 마다 아래에 추가
    }

    /**
     * 한 건의 게임 유저 조회
     * @param gameId
     * @param userId
     * @return
     */
    public Result<GameUserResponseDto> getGameUser(int gameId, int userId) {
        GameUserEntity entity = GameUserEntity.builder()
                .gameId(gameId)
                .userId(userId)
                .build();

        Optional<GameUserEntity> result = repository.findByGameIdAndUserId(entity);

        if(result.isEmpty()){
            return Result.Failure.of("유저 정보가 존재하지 않음", ErrorCode.INTERNAL_ERROR.getCode());
        } else{
          return Result.Success.of(GameUserResponseDto.from(result.get()));
        }
    }
}
