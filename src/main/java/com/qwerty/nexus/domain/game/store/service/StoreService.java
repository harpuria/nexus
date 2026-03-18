package com.qwerty.nexus.domain.game.store.service;

import com.qwerty.nexus.domain.game.store.dto.request.StoreProductCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.StoreProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.StoreProductListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.StoreProductResponseDto;
import com.qwerty.nexus.domain.game.store.entity.ShopEntity;
import com.qwerty.nexus.domain.game.store.entity.StoreProductEntity;
import com.qwerty.nexus.domain.game.store.repository.ProductRepository;
import com.qwerty.nexus.domain.game.store.repository.ShopProductRepository;
import com.qwerty.nexus.domain.game.store.repository.ShopRepository;
import com.qwerty.nexus.domain.game.store.result.ProductResult;
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
public class StoreService {
    private final ShopProductRepository shopProductRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Result<Void> createShopProduct(StoreProductCreateRequestDto requestDto) {
        Result<Void> validationResult = validateShopProductTarget(requestDto.getGameId(), requestDto.getShopId(), requestDto.getProductId());
        if (validationResult instanceof Result.Failure<Void> failure) {
            return failure;
        }

        if (shopProductRepository.findByGameIdAndShopIdAndProductId(
                requestDto.getGameId(),
                requestDto.getShopId(),
                requestDto.getProductId()
        ).isPresent()) {
            return Result.Failure.of("이미 등록된 상점 판매 상품입니다.", ErrorCode.CONFLICT.getCode());
        }

        StoreProductEntity entity = StoreProductEntity.builder()
                .gameId(requestDto.getGameId())
                .shopId(requestDto.getShopId())
                .productId(requestDto.getProductId())
                .sortOrder(requestDto.getSortOrder())
                .isVisible(requestDto.getIsVisible())
                .timeLimitType(requestDto.getTimeLimitType())
                .saleStartAt(requestDto.getSaleStartAt())
                .saleEndAt(requestDto.getSaleEndAt())
                .priceType(requestDto.getPriceType())
                .priceItemCode(requestDto.getPriceItemCode())
                .priceQty(requestDto.getPriceQty())
                .storeSku(requestDto.getStoreSku())
                .purchaseLimitType(requestDto.getPurchaseLimitType())
                .purchaseLimitCount(requestDto.getPurchaseLimitCount())
                .buyCondition(requestDto.getBuyCondition())
                .discount(requestDto.getDiscount())
                .tags(requestDto.getTags())
                .createdBy(requestDto.getCreatedBy())
                .updatedBy(requestDto.getCreatedBy())
                .build();

        Integer createdShopProductId = shopProductRepository.insertShopProduct(entity);
        if (createdShopProductId == null) {
            return Result.Failure.of("상점 판매 상품 등록에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    @Transactional
    public Result<Void> updateShopProduct(StoreProductUpdateRequestDto requestDto) {
        if (requestDto.getShopProductId() == null || requestDto.getShopProductId() <= 0) {
            return Result.Failure.of("유효하지 않은 shopProductId 입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<StoreProductEntity> storeProduct = shopProductRepository.findByShopProductId(requestDto.getShopProductId());
        if (storeProduct.isEmpty()) {
            return Result.Failure.of("상점 판매 상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        StoreProductEntity origin = storeProduct.get();
        Integer targetGameId = requestDto.getGameId() != null ? requestDto.getGameId() : origin.getGameId();
        Integer targetShopId = requestDto.getShopId() != null ? requestDto.getShopId() : origin.getShopId();
        Integer targetProductId = requestDto.getProductId() != null ? requestDto.getProductId() : origin.getProductId();

        Result<Void> validationResult = validateShopProductTarget(targetGameId, targetShopId, targetProductId);
        if (validationResult instanceof Result.Failure<Void> failure) {
            return failure;
        }

        Optional<StoreProductEntity> duplicated = shopProductRepository.findByGameIdAndShopIdAndProductId(
                targetGameId,
                targetShopId,
                targetProductId
        );

        if (duplicated.isPresent() && !duplicated.get().getShopProductId().equals(requestDto.getShopProductId())) {
            return Result.Failure.of("이미 등록된 상점 판매 상품입니다.", ErrorCode.CONFLICT.getCode());
        }

        StoreProductEntity entity = StoreProductEntity.builder()
                .shopProductId(requestDto.getShopProductId())
                .gameId(requestDto.getGameId())
                .shopId(requestDto.getShopId())
                .productId(requestDto.getProductId())
                .sortOrder(requestDto.getSortOrder())
                .isVisible(requestDto.getIsVisible())
                .timeLimitType(requestDto.getTimeLimitType())
                .saleStartAt(requestDto.getSaleStartAt())
                .saleEndAt(requestDto.getSaleEndAt())
                .priceType(requestDto.getPriceType())
                .priceItemCode(requestDto.getPriceItemCode())
                .priceQty(requestDto.getPriceQty())
                .storeSku(requestDto.getStoreSku())
                .purchaseLimitType(requestDto.getPurchaseLimitType())
                .purchaseLimitCount(requestDto.getPurchaseLimitCount())
                .buyCondition(requestDto.getBuyCondition())
                .discount(requestDto.getDiscount())
                .tags(requestDto.getTags())
                .isDel(requestDto.getIsDel())
                .updatedBy(requestDto.getUpdatedBy())
                .build();

        int updatedCount = shopProductRepository.updateShopProduct(entity);
        if (updatedCount <= 0) {
            return Result.Failure.of("상점 판매 상품 수정에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        if ("Y".equalsIgnoreCase(requestDto.getIsDel())) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    @Transactional
    public Result<Void> deleteShopProduct(Integer shopProductId, String updatedBy) {
        if (shopProductId == null || shopProductId <= 0) {
            return Result.Failure.of("유효하지 않은 shopProductId 입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        if (shopProductRepository.findByShopProductId(shopProductId).isEmpty()) {
            return Result.Failure.of("상점 판매 상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        int deletedCount = shopProductRepository.deleteShopProduct(
                StoreProductEntity.builder()
                        .shopProductId(shopProductId)
                        .updatedBy(updatedBy)
                        .isDel("Y")
                        .build()
        );

        if (deletedCount <= 0) {
            return Result.Failure.of("상점 판매 상품 삭제에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }

    @Transactional(readOnly = true)
    public Result<StoreProductResponseDto> getShopProduct(Integer shopProductId) {
        if (shopProductId == null || shopProductId <= 0) {
            return Result.Failure.of("유효하지 않은 shopProductId 입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<StoreProductEntity> storeProduct = shopProductRepository.findByShopProductId(shopProductId);
        if (storeProduct.isEmpty()) {
            return Result.Failure.of("상점 판매 상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(StoreProductResponseDto.from(storeProduct.get()), ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional(readOnly = true)
    public Result<StoreProductListResponseDto> listShopProducts(PagingRequestDto pagingRequestDto, int gameId, int shopId) {
        Result<Void> shopValidationResult = validateShopBelongsToGame(gameId, shopId);
        if (shopValidationResult instanceof Result.Failure<Void> failure) {
            return Result.Failure.of(failure.message(), failure.errorCode());
        }

        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingRequestDto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<StoreProductResponseDto> storeProducts = shopProductRepository.findAllByGameIdAndShopId(pagingEntity, gameId, shopId).stream()
                .map(StoreProductResponseDto::from)
                .toList();

        long totalCount = shopProductRepository.countByGameIdAndShopId(gameId, shopId);
        int totalPages = pagingEntity.getSize() == 0 ? 0 : (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        StoreProductListResponseDto responseDto = StoreProductListResponseDto.builder()
                .storeProducts(storeProducts)
                .page(pagingEntity.getPage())
                .size(pagingEntity.getSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(pagingEntity.getPage() + 1 < totalPages)
                .hasPrevious(pagingEntity.getPage() > 0 && totalPages > 0)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }

    private Result<Void> validateShopProductTarget(int gameId, int shopId, int productId) {
        Result<Void> shopValidationResult = validateShopBelongsToGame(gameId, shopId);
        if (shopValidationResult instanceof Result.Failure<Void> failure) {
            return failure;
        }

        Optional<ProductResult> product = productRepository.findByProductId(productId);
        if (product.isEmpty()) {
            return Result.Failure.of("상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        if (!Integer.valueOf(gameId).equals(product.get().getGameId())) {
            return Result.Failure.of("상품과 gameId가 일치하지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.RETRIEVED);
    }

    private Result<Void> validateShopBelongsToGame(int gameId, int shopId) {
        Optional<ShopEntity> shop = shopRepository.findByShopId(shopId);
        if (shop.isEmpty()) {
            return Result.Failure.of("상점 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        if (!Integer.valueOf(gameId).equals(shop.get().getGameId())) {
            return Result.Failure.of("상점과 gameId가 일치하지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.RETRIEVED);
    }
}
