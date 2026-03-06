package com.qwerty.nexus.domain.game.item.service;

import com.qwerty.nexus.domain.game.item.dto.request.UserItemInstanceCreateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.request.UserItemInstanceUpdateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.response.UserItemInstanceListResponseDto;
import com.qwerty.nexus.domain.game.item.dto.response.UserItemInstanceResponseDto;
import com.qwerty.nexus.domain.game.item.entity.UserItemInstanceEntity;
import com.qwerty.nexus.domain.game.item.repository.UserItemInstanceRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.PagingEntity;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.paging.PagingUtil;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserItemInstanceService {
    private final UserItemInstanceRepository repository;

    /**
     * 유저 인스턴스형 아이템 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createUserItemInstance(UserItemInstanceCreateRequestDto dto) {
        UserItemInstanceEntity entity = UserItemInstanceEntity.builder()
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .stateJson(dto.getStateJson())
                .acquiredAt(dto.getAcquiredAt())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .build();

        Integer createdId = repository.insertUserItemInstance(entity);
        if (createdId == null) {
            return Result.Failure.of("유저 인스턴스형 아이템 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 유저 인스턴스형 아이템 수정
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> updateUserItemInstance(UserItemInstanceUpdateRequestDto dto) {
        UserItemInstanceEntity entity = UserItemInstanceEntity.builder()
                .userItemId(dto.getUserItemId())
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .stateJson(dto.getStateJson())
                .acquiredAt(dto.getAcquiredAt())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateCnt = repository.updateUserItemInstance(entity);
        if (updateCnt > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
        }

        return Result.Failure.of("유저 인스턴스형 아이템 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 유저 인스턴스형 아이템 삭제
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> deleteUserItemInstance(UserItemInstanceUpdateRequestDto dto) {
        UserItemInstanceEntity entity = UserItemInstanceEntity.builder()
                .userItemId(dto.getUserItemId())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateCnt = repository.updateUserItemInstance(entity);
        if (updateCnt > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Failure.of("유저 인스턴스형 아이템 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 유저 인스턴스형 아이템 단건 조회
     * @param userItemId
     * @return
     */
    public Result<UserItemInstanceResponseDto> getUserItemInstance(Integer userItemId) {
        Optional<UserItemInstanceEntity> itemInstance = repository.findByUserItemId(UserItemInstanceEntity.builder()
                .userItemId(userItemId)
                .build());

        if (itemInstance.isPresent()) {
            return Result.Success.of(UserItemInstanceResponseDto.from(itemInstance.get()), ApiConstants.Messages.Success.RETRIEVED);
        }

        return Result.Failure.of("유저 인스턴스형 아이템 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
    }

    /**
     * 유저 인스턴스형 아이템 목록 조회
     * @param dto
     * @param userId
     * @param itemId
     * @param gameId
     * @return
     */
    public Result<UserItemInstanceListResponseDto> listUserItemInstances(PagingRequestDto dto, Integer userId, Integer itemId, Integer gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<UserItemInstanceResponseDto> userItemInstances = repository.findAllByUserIdAndItemId(pagingEntity, userId, itemId, gameId)
                .stream()
                .map(UserItemInstanceResponseDto::from)
                .toList();

        long totalCount = repository.countByUserIdAndItemId(userId, itemId, gameId);
        int totalPages = pagingEntity.getSize() == 0 ? 0 : (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        return Result.Success.of(UserItemInstanceListResponseDto.builder()
                        .userItemInstances(userItemInstances)
                        .page(pagingEntity.getPage())
                        .size(pagingEntity.getSize())
                        .totalCount(totalCount)
                        .totalPages(totalPages)
                        .hasNext(pagingEntity.getPage() + 1 < totalPages)
                        .hasPrevious(pagingEntity.getPage() > 0 && totalPages > 0)
                        .build(),
                ApiConstants.Messages.Success.RETRIEVED);
    }
}
