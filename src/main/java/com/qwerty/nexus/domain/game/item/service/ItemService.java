package com.qwerty.nexus.domain.game.item.service;

import com.qwerty.nexus.domain.game.item.dto.request.ItemCreateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.request.ItemUpdateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.response.ItemListResponseDto;
import com.qwerty.nexus.domain.game.item.dto.response.ItemResponseDto;
import com.qwerty.nexus.domain.game.item.entity.ItemEntity;
import com.qwerty.nexus.domain.game.item.entity.UserItemStackEntity;
import com.qwerty.nexus.domain.game.item.repository.ItemRepository;
import com.qwerty.nexus.domain.game.item.repository.UserItemStackRepository;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
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
    private final GameUserRepository gameUserRepository;
    private final UserItemStackRepository userItemStackRepository;

    /**
     * 아이템 정보 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createItem(ItemCreateRequestDto dto) {
        ItemEntity entity = ItemEntity.builder()
                .gameId(dto.getGameId())
                .itemCode(dto.getItemCode())
                .name(dto.getName())
                .desc(dto.getDesc())
                .itemType(dto.getItemType())
                .isStackable(dto.getIsStackable())
                .defaultStack(dto.getDefaultStack())
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

        // 재화 생성 후, 게임에 유저가 있을경우 각 유저들에게 유저재화 데이터 추가
        List<Integer> userIdList = gameUserRepository.findAllUserIdsByGameId(
                GameUserEntity.builder().gameId(dto.getGameId()).build()
        );

        for (Integer userId : userIdList) {
            UserItemStackEntity userItemStackEntity = UserItemStackEntity.builder()
                    .userId(userId)
                    .amount(dto.getDefaultStack())
                    .itemId(createdId)
                    .createdBy(dto.getCreatedBy())
                    .updatedBy(dto.getCreatedBy())
                    .build();

            userItemStackRepository.insertUserItemStack(userItemStackEntity);
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 아이템 정보 수정
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> updateItem(ItemUpdateRequestDto dto) {
        ItemEntity entity = ItemEntity.builder()
                .itemId(dto.getItemId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .itemType(dto.getItemType())
                .isStackable(dto.getIsStackable())
                .defaultStack(dto.getDefaultStack())
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

    /**
     * 아이템 정보 삭제 (논리적 삭제)
     * @param dto
     * @return
     */
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

    /**
     * 아이템 단건 조회
     * @param itemId
     * @return
     */
    public Result<ItemResponseDto> getItem(Integer itemId) {
        Optional<ItemEntity> item = repository.findByItemId(ItemEntity.builder().itemId(itemId).build());
        if (item.isPresent()) {
            return Result.Success.of(ItemResponseDto.from(item.get()), ApiConstants.Messages.Success.RETRIEVED);
        }

        return Result.Failure.of("아이템 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
    }

    /**
     * 아이템 목록 조회
     * @param dto
     * @param gameId
     * @return
     */
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
