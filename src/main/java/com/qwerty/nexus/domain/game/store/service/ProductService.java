package com.qwerty.nexus.domain.game.store.service;

import com.qwerty.nexus.domain.game.store.dto.request.ProductBuyRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ShopProductPurchaseRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductDetailResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductResponseDto;
import com.qwerty.nexus.domain.game.store.entity.ProductEntity;
import com.qwerty.nexus.domain.game.store.repository.ProductRepository;
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
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Result<Void> createProduct(ProductCreateRequestDto dto) {
        ProductEntity entity = ProductEntity.builder()
                .gameId(dto.getGameId())
                .productCode(dto.getProductCode())
                .name(dto.getName())
                .desc(dto.getDesc())
                .imageUrl(dto.getImageUrl())
                .productType(dto.getProductType())
                .rewards(dto.getRewards())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();

        Integer createdProductId = productRepository.insertProduct(entity);
        if (createdProductId == null) {
            return Result.Failure.of("상품 생성에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    @Transactional
    public Result<Void> updateProduct(ProductUpdateRequestDto dto) {
        if (dto.getProductId() == null || dto.getProductId() <= 0) {
            return Result.Failure.of("유효하지 않은 상품 ID입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<ProductResult> product = productRepository.findByProductId(dto.getProductId());
        if (product.isEmpty()) {
            return Result.Failure.of("상품을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        ProductEntity entity = ProductEntity.builder()
                .productId(dto.getProductId())
                .gameId(dto.getGameId())
                .productCode(dto.getProductCode())
                .name(dto.getName())
                .desc(dto.getDesc())
                .imageUrl(dto.getImageUrl())
                .productType(dto.getProductType())
                .rewards(dto.getRewards())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateCount = productRepository.updateProduct(entity);
        boolean isDeleteRequest = "Y".equalsIgnoreCase(dto.getIsDel());

        if (updateCount <= 0) {
            String failureMessage = isDeleteRequest ? "상품 삭제에 실패했습니다." : "상품 수정에 실패했습니다.";
            return Result.Failure.of(failureMessage, ErrorCode.INTERNAL_ERROR.getCode());
        }

        if (isDeleteRequest) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    @Transactional(readOnly = true)
    public Result<ProductListResponseDto> listProducts(PagingRequestDto dto, int gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("유효하지 않은 페이징 요청입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<ProductResponseDto> products = productRepository.findAllByGameIdAndKeyword(pagingEntity, gameId).stream()
                .map(ProductResponseDto::from)
                .toList();

        long totalCount = productRepository.countByGameIdAndKeyword(pagingEntity, gameId);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        ProductListResponseDto response = ProductListResponseDto.builder()
                .products(products)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional(readOnly = true)
    public Result<ProductListResponseDto> listStoreProducts(PagingRequestDto dto, int gameId, String shopCode) {
        if (shopCode == null || shopCode.isBlank()) {
            return Result.Failure.of("유효하지 않은 상점 코드입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("유효하지 않은 페이징 요청입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<ProductResponseDto> products = productRepository.findAllByGameIdAndShopCode(pagingEntity, gameId, shopCode).stream()
                .map(ProductResponseDto::from)
                .toList();

        long totalCount = productRepository.countByGameIdAndShopCode(gameId, shopCode);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        ProductListResponseDto response = ProductListResponseDto.builder()
                .products(products)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional(readOnly = true)
    public Result<ProductDetailResponseDto> getProduct(int productId) {
        Optional<ProductResult> product = productRepository.findByProductId(productId);

        if (product.isEmpty()) {
            return Result.Failure.of("상품을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        ProductDetailResponseDto response = ProductDetailResponseDto.from(product.get());
        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional
    public Result<Void> purchaseStoreProduct(ShopProductPurchaseRequestDto dto, int gameId) {
        if (dto.getShopProductId() == null || dto.getShopProductId() <= 0 || dto.getUserId() <= 0) {
            return Result.Failure.of("유효하지 않은 상점 상품 ID 또는 유저 ID입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<ProductResult> product = productRepository.findByGameIdAndShopProductId(gameId, dto.getShopProductId());
        if (product.isEmpty()) {
            return Result.Failure.of("상품을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Failure.of("상점 상품 구매 로직이 아직 구현되지 않았습니다.", ErrorCode.INVALID_REQUEST.getCode());
    }

    @Transactional
    public Result<Void> deleteProduct(Integer productId) {
        if (productId == null || productId <= 0) {
            return Result.Failure.of("유효하지 않은 상품 ID입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<ProductResult> product = productRepository.findByProductId(productId);
        if (product.isEmpty()) {
            return Result.Failure.of("상품을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        int deleteCount = productRepository.deleteProduct(
                ProductEntity.builder()
                        .productId(productId)
                        .isDel("Y")
                        .build()
        );

        if (deleteCount <= 0) {
            return Result.Failure.of("상품 삭제에 실패했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }
}
