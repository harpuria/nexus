package com.qwerty.nexus.domain.game.store.service;

import com.qwerty.nexus.domain.game.store.dto.request.ProductBuyRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.request.ProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductDetailResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.store.dto.response.ProductResponseDto;
import com.qwerty.nexus.domain.game.store.entity.ProductEntity;
import com.qwerty.nexus.domain.game.store.repository.ProductRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.paging.PagingEntity;
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

    /**
     * 상품 정보 생성
     * @param dto
     * @return
     */
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
            return Result.Failure.of("상품 생성 실패", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 상품 정보 수정 (논리적 삭제 포함)
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> updateProduct(ProductUpdateRequestDto dto) {
        if (dto.getProductId() == null || dto.getProductId() <= 0) {
            return Result.Failure.of("상품 ID가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<ProductEntity> product = productRepository.findByProductId(dto.getProductId());
        if (product.isEmpty()) {
            return Result.Failure.of("상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
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
        String type = "수정";
        if (dto.getIsDel() != null && dto.getIsDel().equalsIgnoreCase("Y")) {
            type = "삭제";
        }

        if (updateCount <= 0) {
            return Result.Failure.of(String.format("상품 %s 실패", type), ErrorCode.INTERNAL_ERROR.getCode());
        }

        if ("삭제".equals(type)) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
    }

    /**
     * 상품 목록 가져오기
     * @param dto
     * @param gameId
     * @return
     */
    @Transactional(readOnly = true)
    public Result<ProductListResponseDto> listProducts(PagingRequestDto dto, int gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
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

    /**
     * 하나의 상품 정보 가져오기
     * @param productId
     * @return
     */
    @Transactional(readOnly = true)
    public Result<ProductDetailResponseDto> getProduct(int productId) {
        Optional<ProductEntity> product = productRepository.findByProductId(productId);

        if (product.isEmpty()) {
            return Result.Failure.of("상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        ProductDetailResponseDto response = ProductDetailResponseDto.from(product.get());
        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }

    /**
     * 상품 구매 및 지급
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> buyProduct(ProductBuyRequestDto dto) {
        if (dto.getProductId() <= 0 || dto.getUserId() <= 0) {
            return Result.Failure.of("상품 ID 또는 유저 ID가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        return Result.Failure.of("SHOP_PRODUCT 기반 구매 정책으로 전환되어, 상품 구매 API는 별도 구현이 필요합니다.", ErrorCode.INVALID_REQUEST.getCode());
    }

    /**
     * 상품 정보 삭제
     * @param productId
     * @return
     */
    @Transactional
    public Result<Void> deleteProduct(Integer productId) {
        if (productId == null || productId <= 0) {
            return Result.Failure.of("상품 ID가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<ProductEntity> product = productRepository.findByProductId(productId);
        if (product.isEmpty()) {
            return Result.Failure.of("상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        int deleteCount = productRepository.deleteProduct(
                ProductEntity.builder()
                        .productId(productId)
                        .isDel("Y")
                        .build()
        );

        if (deleteCount <= 0) {
            return Result.Failure.of("상품 삭제 실패", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }
}
