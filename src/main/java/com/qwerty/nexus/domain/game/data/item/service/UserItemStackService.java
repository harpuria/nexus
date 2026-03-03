package com.qwerty.nexus.domain.game.data.item.service;

import com.qwerty.nexus.domain.game.data.item.dto.request.UserItemStackCreateRequestDto;
import com.qwerty.nexus.domain.game.data.item.dto.request.UserItemStackUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.item.dto.response.UserItemStackListResponseDto;
import com.qwerty.nexus.domain.game.data.item.dto.response.UserItemStackResponseDto;
import com.qwerty.nexus.domain.game.data.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.data.item.repository.UserItemStackRepository;
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
public class UserItemStackService {
    private final UserItemStackRepository repository;

    @Transactional
    public Result<Void> createUserItemStack(UserItemStackCreateRequestDto dto) {
        UserItemStackEntity entity = UserItemStackEntity.builder()
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .amount(dto.getAmount())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .build();

        Integer createdId = repository.insertUserItemStack(entity);
        if (createdId == null) {
            return Result.Failure.of("유저 아이템 스택 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    @Transactional
    public Result<Void> updateUserItemStack(UserItemStackUpdateRequestDto dto) {
        UserItemStackEntity entity = UserItemStackEntity.builder()
                .userItemStackId(dto.getUserItemStackId())
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .amount(dto.getAmount())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateCnt = repository.updateUserItemStack(entity);
        if (updateCnt > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
        }

        return Result.Failure.of("유저 아이템 스택 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    @Transactional
    public Result<Void> deleteUserItemStack(UserItemStackUpdateRequestDto dto) {
        UserItemStackEntity entity = UserItemStackEntity.builder()
                .userItemStackId(dto.getUserItemStackId())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateCnt = repository.updateUserItemStack(entity);
        if (updateCnt > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Failure.of("유저 아이템 스택 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    public Result<UserItemStackResponseDto> getUserItemStack(Integer userItemStackId) {
        Optional<UserItemStackEntity> itemStack = repository.findByUserItemStackId(UserItemStackEntity.builder()
                .userItemStackId(userItemStackId)
                .build());

        if (itemStack.isPresent()) {
            return Result.Success.of(UserItemStackResponseDto.from(itemStack.get()), ApiConstants.Messages.Success.RETRIEVED);
        }

        return Result.Failure.of("유저 아이템 스택 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
    }

    public Result<UserItemStackListResponseDto> listUserItemStacks(PagingRequestDto dto, Integer userId, Integer itemId, Integer gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<UserItemStackResponseDto> userItemStacks = repository.findAllByUserIdAndItemId(pagingEntity, userId, itemId, gameId)
                .stream()
                .map(UserItemStackResponseDto::from)
                .toList();

        long totalCount = repository.countByUserIdAndItemId(userId, itemId, gameId);
        int totalPages = pagingEntity.getSize() == 0 ? 0 : (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        return Result.Success.of(UserItemStackListResponseDto.builder()
                        .userItemStacks(userItemStacks)
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
