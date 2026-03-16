package com.qwerty.nexus.domain.game.store.service;

import com.qwerty.nexus.domain.game.store.dto.request.ShopCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ShopUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.ShopListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ShopResponseDto;
import com.qwerty.nexus.domain.game.store.entity.ShopEntity;
import com.qwerty.nexus.domain.game.store.repository.ShopRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    @Transactional
    public Result<Void> createShop(ShopCreateRequestDto requestDto) {
        ShopEntity entity = ShopEntity.builder()
                .gameId(requestDto.getGameId())
                .shopCode(requestDto.getShopCode())
                .name(requestDto.getName())
                .desc(requestDto.getDesc())
                .shopType(requestDto.getShopType())
                .timeLimitType(requestDto.getTimeLimitType())
                .openAt(requestDto.getOpenAt())
                .closeAt(requestDto.getCloseAt())
                .openCondition(requestDto.getOpenCondition())
                .sortOrder(requestDto.getSortOrder())
                .isVisible(requestDto.getIsVisible())
                .createdBy(requestDto.getCreatedBy())
                .updatedBy(requestDto.getCreatedBy())
                .build();

        Integer shopId = shopRepository.insertShop(entity);
        if (shopId == null) {
            return Result.Failure.of("상점 생성에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    @Transactional
    public Result<Void> updateShop(ShopUpdateRequestDto requestDto) {
        if (shopRepository.findByShopId(requestDto.getShopId()).isEmpty()) {
            return Result.Failure.of("상점을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        ShopEntity entity = ShopEntity.builder()
                .shopId(requestDto.getShopId())
                .shopCode(requestDto.getShopCode())
                .name(requestDto.getName())
                .desc(requestDto.getDesc())
                .shopType(requestDto.getShopType())
                .timeLimitType(requestDto.getTimeLimitType())
                .openAt(requestDto.getOpenAt())
                .closeAt(requestDto.getCloseAt())
                .openCondition(requestDto.getOpenCondition())
                .sortOrder(requestDto.getSortOrder())
                .isVisible(requestDto.getIsVisible())
                .updatedBy(requestDto.getUpdatedBy())
                .build();

        int updateCount = shopRepository.updateShop(entity);
        if (updateCount <= 0) {
            return Result.Failure.of("상점 수정에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    @Transactional
    public Result<Void> deleteShop(Integer shopId, String updatedBy) {
        if (shopRepository.findByShopId(shopId).isEmpty()) {
            return Result.Failure.of("상점을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        ShopEntity entity = ShopEntity.builder()
                .shopId(shopId)
                .isDel("Y")
                .updatedBy(updatedBy)
                .build();

        int deleteCount = shopRepository.updateShop(entity);
        if (deleteCount <= 0) {
            return Result.Failure.of("상점 삭제에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }

    @Transactional(readOnly = true)
    public Result<ShopResponseDto> getShop(Integer shopId) {
        Optional<ShopEntity> shop = shopRepository.findByShopId(shopId);
        if (shop.isEmpty()) {
            return Result.Failure.of("상점을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(ShopResponseDto.from(shop.get()), ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional(readOnly = true)
    public Result<ShopListResponseDto> listShops(PagingRequestDto pagingRequestDto, int gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingRequestDto);

        List<ShopResponseDto> shops = shopRepository.findAllByGameIdAndKeyword(pagingEntity, gameId).stream()
                .map(ShopResponseDto::from)
                .collect(Collectors.toList());

        long totalCount = shopRepository.countByGameIdAndKeyword(pagingEntity, gameId);
        int page = pagingEntity.getPage();
        int size = pagingEntity.getSize();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalCount / size);
        boolean hasNext = page + 1 < totalPages;
        boolean hasPrevious = page > 0 && totalPages > 0;

        ShopListResponseDto responseDto = ShopListResponseDto.builder()
                .shops(shops)
                .page(page)
                .size(size)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional(readOnly = true)
    public Result<ShopListResponseDto> listStoreShops(PagingRequestDto pagingRequestDto, int gameId) {
        return listShops(pagingRequestDto, gameId);
    }

    @Transactional(readOnly = true)
    public Result<ShopResponseDto> getStoreShop(int gameId, String shopCode) {
        Optional<ShopEntity> shop = shopRepository.findByGameIdAndShopCode(gameId, shopCode);
        if (shop.isEmpty()) {
            return Result.Failure.of("상점을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(ShopResponseDto.from(shop.get()), ApiConstants.Messages.Success.RETRIEVED);
    }
}