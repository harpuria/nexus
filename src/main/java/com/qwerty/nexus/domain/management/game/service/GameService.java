package com.qwerty.nexus.domain.management.game.service;

import com.qwerty.nexus.domain.management.game.GameStatus;
import com.qwerty.nexus.domain.management.game.dto.request.GameCreateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.request.GameUpdateRequestDto;
import com.qwerty.nexus.domain.management.game.dto.response.GameListResponseDto;
import com.qwerty.nexus.domain.management.game.dto.response.GameResponseDto;
import com.qwerty.nexus.domain.management.game.entity.GameEntity;
import com.qwerty.nexus.domain.management.game.repository.GameRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository repository;

    /**
     * 게임 정보 생성
     * @param dto
     * @return
     */
    public Result<Void> createGame(GameCreateRequestDto dto) {
        GameEntity gameEntity = GameEntity.builder()
                .orgId(dto.getOrgId())
                .name(dto.getName())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .status(GameStatus.STOPPED)
                .clientAppId(UUID.randomUUID())
                .signatureKey(UUID.randomUUID())
                .version(dto.getVersion())
                .build();

        Optional<GameEntity> insertRst = Optional.ofNullable(repository.insertGame(gameEntity));

        if(insertRst.isPresent()) {
            return Result.Success.of(null, "게임 생성 성공.");
        }
        else{
            return Result.Failure.of("게임 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 정보 수정
     * @param dto
     * @return
     */
    public Result<Void> updateGame(GameUpdateRequestDto dto){
        GameEntity gameEntity = GameEntity.builder()
                .gameId(dto.getGameId())
                .name(dto.getName())
                .status(dto.getStatus())
                .isDel(dto.getIsDel())
                .version(dto.getVersion())
                .updatedBy(dto.getUpdatedBy())
                .build();

        Optional<GameEntity> updateRst = Optional.ofNullable(repository.updateGame(gameEntity));

        if(updateRst.isPresent()){
            return Result.Success.of(null, "게임 정보 수정 성공.");
        }
        else{
            return Result.Failure.of("게임 정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 하나의 게임 정보 조회
     * @param id
     * @return
     */
    public Result<GameResponseDto> getGame(Integer id){
        Optional<GameEntity> selectRst = Optional.ofNullable(repository.findByGameId(id));
        if(selectRst.isPresent()){
            return Result.Success.of(GameResponseDto.from(selectRst.get()), "게임 정보 조회 완료.");
        }
        else{
            return Result.Failure.of("게임 정보가 존재하지 않음.", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 게임 목록 조회 (페이징)
     *
     * @param pagingDto
     * @return 페이징 메타데이터와 함께 게임 목록
     */
    public Result<GameListResponseDto> listGames(PagingRequestDto pagingDto){
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingDto);
        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        Optional<List<GameEntity>> selectRst = Optional.ofNullable(repository.findAllByKeyword(pagingEntity));

        if(selectRst.isEmpty()){
            return Result.Failure.of("게임 목록이 존재하지 않음.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<GameResponseDto> games = selectRst.get().stream()
                .map(GameResponseDto::from)
                .collect(Collectors.toList());

        long totalCount = repository.countActiveGames();
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        GameListResponseDto response = GameListResponseDto.builder()
                .games(games)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, "게임 목록 조회 완료.");
    }
}
