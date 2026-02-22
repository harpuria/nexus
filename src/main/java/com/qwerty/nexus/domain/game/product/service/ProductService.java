package com.qwerty.nexus.domain.game.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.dto.ProductInfo;
import com.qwerty.nexus.domain.game.product.dto.request.ProductBuyRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.response.ProductDetailResponseDto;
import com.qwerty.nexus.domain.game.product.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.product.dto.response.ProductResponseDto;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.domain.game.product.repository.ProductRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;
    private final UserCurrencyRepository userCurrencyRepository;
    private final ObjectMapper objectMapper;

    /**
     * 상품 정보 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createProduct(ProductCreateRequestDto dto) {
        ProductEntity entity = ProductEntity.builder()
                .gameId(dto.getGameId())
                .purchaseType(dto.getPurchaseType())
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .price(dto.getPrice())
                .rewards(dto.getRewards())
                .limitType(dto.getLimitType())
                .availableStart(dto.getAvailableStart())
                .availableEnd(dto.getAvailableEnd())
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
        ProductEntity existingProduct = product.get();

        PurchaseType mergedPurchaseType = dto.getPurchaseType() != null
                ? dto.getPurchaseType()
                : existingProduct.getPurchaseType();
        Integer mergedCurrencyId = dto.getCurrencyId() != null
                ? dto.getCurrencyId()
                : existingProduct.getCurrencyId();

        if (PurchaseType.CURRENCY.equals(mergedPurchaseType) && (mergedCurrencyId == null || mergedCurrencyId <= 0)) {
            return Result.Failure.of("purchaseType이 CURRENCY인 경우 currencyId는 필수입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        ProductEntity entity = ProductEntity.builder()
                .productId(dto.getProductId())
                .gameId(dto.getGameId())
                .purchaseType(dto.getPurchaseType())
                .currencyId(dto.getCurrencyId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .price(dto.getPrice())
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

        Optional<ProductEntity> buyProductInfo = productRepository.findByProductId(dto.getProductId());
        if (buyProductInfo.isEmpty()) {
            return Result.Failure.of("구매할 상품 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        ProductEntity productEntity = buyProductInfo.get();
        if (PurchaseType.CASH.equals(productEntity.getPurchaseType())) {
            // TODO : 캐시의 경우 추후 영수증 검증 할 때 작업 할 것
            return Result.Failure.of("캐시 상품 구매 검증 로직이 준비되지 않았습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        if (PurchaseType.CURRENCY.equals(productEntity.getPurchaseType())) {
            return buyCurrencyProduct(productEntity, dto.getUserId());
        }

        return Result.Failure.of("지원하지 않는 구매 타입입니다.", ErrorCode.INVALID_REQUEST.getCode());
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

    /**
     * 상품 구매 (재화)
     * @param productEntity
     * @param userId
     * @return
     */
    private Result<Void> buyCurrencyProduct(ProductEntity productEntity, Integer userId) {
        if (productEntity.getCurrencyId() == null || productEntity.getPrice() == null) {
            return Result.Failure.of("상품 구매 설정이 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }
        if (productEntity.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.Failure.of("상품 가격 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<ProductInfo> rewardList = parseRewardList(productEntity);
        if (rewardList == null || rewardList.isEmpty()) {
            return Result.Failure.of("상품 지급 정보 파싱에 실패했습니다.", ErrorCode.INVALID_FORMAT.getCode());
        }

        UserCurrencyEntity consumeCurrency = UserCurrencyEntity.builder()
                .userId(userId)
                .currencyId(productEntity.getCurrencyId())
                .build();

        Optional<UserCurrencyEntity> currentUserCurrency = userCurrencyRepository.findByUserIdAndCurrencyId(consumeCurrency);
        if (currentUserCurrency.isEmpty()) {
            return Result.Failure.of("유저 재화 정보가 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        BigDecimal currentAmount = BigDecimal.valueOf(currentUserCurrency.get().getAmount());
        if (currentAmount.compareTo(productEntity.getPrice()) < 0) {
            return Result.Failure.of("구매 재화가 부족합니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        long consumeAmount;
        try {
            consumeAmount = productEntity.getPrice().longValueExact();
        } catch (ArithmeticException e) {
            return Result.Failure.of("상품 가격 단위가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        for (ProductInfo reward : rewardList) {
            if (reward.getCurrencyId() <= 0 || reward.getAmount() == null || reward.getAmount() <= 0) {
                return Result.Failure.of("상품 지급 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
            }

            CurrencyEntity currencyEntity = CurrencyEntity.builder()
                    .currencyId(reward.getCurrencyId())
                    .build();

            Optional<CurrencyEntity> currencyInfo = currencyRepository.findByCurrencyId(currencyEntity);
            if (currencyInfo.isEmpty()) {
                return Result.Failure.of("지급 대상 재화 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
            }

            UserCurrencyEntity rewardCurrency = UserCurrencyEntity.builder()
                    .userId(userId)
                    .currencyId(reward.getCurrencyId())
                    .build();

            Optional<UserCurrencyEntity> currentRewardCurrency = userCurrencyRepository.findByUserIdAndCurrencyId(rewardCurrency);
            if (currentRewardCurrency.isEmpty()) {
                return Result.Failure.of("지급 대상 유저 재화 정보가 없습니다.", ErrorCode.NOT_FOUND.getCode());
            }

            long calculatedAmount = currentRewardCurrency.get().getAmount() + reward.getAmount();
            if (currencyInfo.get().getMaxAmount() != null && currencyInfo.get().getMaxAmount() < calculatedAmount) {
                return Result.Failure.of("보유 가능한 최대 재화를 초과했습니다.", ErrorCode.INVALID_REQUEST.getCode());
            }
        }

        int subtractCount = userCurrencyRepository.updateUserCurrencyAmountSubtractByUserIdAndCurrencyId(
                consumeCurrency,
                consumeAmount
        );
        if (subtractCount <= 0) {
            return Result.Failure.of("재화 차감 처리에 실패했습니다.", ErrorCode.CONFLICT.getCode());
        }

        for (ProductInfo reward : rewardList) {
            UserCurrencyEntity rewardCurrency = UserCurrencyEntity.builder()
                    .userId(userId)
                    .currencyId(reward.getCurrencyId())
                    .build();

            int addCount = userCurrencyRepository.updateUserCurrencyAmountAddByUserIdAndCurrencyId(
                    rewardCurrency,
                    reward.getAmount(),
                    reward.getCurrencyId()
            );
            if (addCount <= 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.Failure.of("재화 지급 처리에 실패했습니다.", ErrorCode.CONFLICT.getCode());
            }
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
    }

    /**
     * 상품 정보 파싱
     * @param productEntity
     * @return
     */
    private List<ProductInfo> parseRewardList(ProductEntity productEntity) {
        if (productEntity.getRewards() == null || productEntity.getRewards().data() == null) {
            return null;
        }

        try {
            return objectMapper.readValue(productEntity.getRewards().data(), new TypeReference<List<ProductInfo>>() {});
        } catch (JsonProcessingException e) {
            log.error("상품 지급 정보 파싱 실패. productId={}", productEntity.getProductId(), e);
            return null;
        }
    }
}
