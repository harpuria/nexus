package com.qwerty.nexus.domain.game.data.item.service;

import com.qwerty.nexus.domain.game.data.item.dto.request.ItemCreateRequestDto;
import com.qwerty.nexus.domain.game.data.item.dto.request.ItemUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.item.dto.response.ItemListResponseDto;
import com.qwerty.nexus.domain.game.data.item.dto.response.ItemResponseDto;
import com.qwerty.nexus.domain.game.data.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.data.item.repository.ItemRepository;
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
public class ItemService {
    private final ItemRepository repository;

    @Transactional
    public Result<Void> createItem(ItemCreateRequestDto dto) {
        ItemEntity entity = ItemEntity.builder()
                .gameId(dto.getGameId())
                .itemCode(dto.getItemCode())
                .name(dto.getName())
                .desc(dto.getDesc())
                .itemType(dto.getItemType())
                .isStackable(dto.getIsStackable())
                .maxStack(dto.getMaxStack())
                .rarity(dto.getRarity())
                .iconPath(dto.getIconPath())
                .metaJson(dto.getMetaJson())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getCreatedBy())
                .build();

        Integer createdId = repository.insertItem(entity);
        if (createdId == null) {
            return Result.Failure.of("아이템 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    @Transactional
    public Result<Void> updateItem(ItemUpdateRequestDto dto) {
        ItemEntity entity = ItemEntity.builder()
                .itemId(dto.getItemId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .itemType(dto.getItemType())
                .isStackable(dto.getIsStackable())
                .maxStack(dto.getMaxStack())
                .rarity(dto.getRarity())
                .iconPath(dto.getIconPath())
                .metaJson(dto.getMetaJson())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateCnt = repository.updateItem(entity);
        if (updateCnt > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
        }

        return Result.Failure.of("아이템 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    @Transactional
    public Result<Void> deleteItem(ItemUpdateRequestDto dto) {
        ItemEntity entity = ItemEntity.builder()
                .itemId(dto.getItemId())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateCnt = repository.updateItem(entity);
        if (updateCnt > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Failure.of("아이템 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    public Result<ItemResponseDto> getItem(Integer itemId) {
        Optional<ItemEntity> item = repository.findByItemId(ItemEntity.builder().itemId(itemId).build());
        if (item.isPresent()) {
            return Result.Success.of(ItemResponseDto.from(item.get()), ApiConstants.Messages.Success.RETRIEVED);
        }

        return Result.Failure.of("아이템 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
    }

    public Result<ItemListResponseDto> listItems(PagingRequestDto dto, Integer gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<ItemResponseDto> items = repository.findAllByGameId(pagingEntity, gameId).stream().map(ItemResponseDto::from).toList();
        long totalCount = repository.countByGameIdAndKeyword(pagingEntity, gameId);
        int totalPages = pagingEntity.getSize() == 0 ? 0 : (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        ItemListResponseDto response = ItemListResponseDto.builder()
                .items(items)
                .page(pagingEntity.getPage())
                .size(pagingEntity.getSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(pagingEntity.getPage() + 1 < totalPages)
                .hasPrevious(pagingEntity.getPage() > 0 && totalPages > 0)
                .build();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }
}
