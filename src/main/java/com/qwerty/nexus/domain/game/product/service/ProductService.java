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
import com.qwerty.nexus.domain.game.product.entity.ProductSearchEntity;
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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CurrencyRepository currencyRepository;
    private final UserCurrencyRepository userCurrencyRepository;

    /**
     * 상품 정보 생성
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> create(ProductCreateRequestDto dto) {
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

        Optional<ProductEntity> createRst = Optional.ofNullable(repository.create(entity));

        if(createRst.isPresent()){
            return Result.Success.of(null, "상품 생성 완료");
        }else{
            return Result.Failure.of("상품 생성 실패", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 상품 정보 수정 (논리적 삭제 포함)
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> update(ProductUpdateRequestDto dto) {
        ProductEntity entity = ProductEntity.builder()
                .productId(dto.getProductId())
                .name(dto.getName())
                .desc(dto.getDesc())
                .price(dto.getPrice())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        Optional<ProductEntity> updateRst = Optional.ofNullable(repository.update(entity));

        String type = "수정";
        if(dto.getIsDel() != null && dto.getIsDel().equalsIgnoreCase("Y"))
            type = "삭제";

        if(updateRst.isPresent()){
            return Result.Success.of(null, String.format("상품 %s 성공", type));
        }
        else{
            return Result.Failure.of(String.format("상품 %s 실패", type), ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 상품 목록 가져오기
     * @param dto
     * @param gameId
     * @return
     */
    @Transactional(readOnly = true)
    public Result<ProductListResponseDto> list(PagingRequestDto dto, int gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<ProductResponseDto> products = repository.selectProducts(pagingEntity, gameId).stream()
                .map(ProductResponseDto::from)
                .toList();

        long totalCount = repository.countProducts(pagingEntity, gameId);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0;

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
    public Result<ProductDetailResponseDto> selectOneProduct(int productId) {
        Optional<ProductEntity> product = repository.selectOne(productId);

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
    public Result<Void> buy(ProductBuyRequestDto dto) throws JsonProcessingException {
        // 구매할 상품 정보 가져오기
        Optional<ProductEntity> buyProductInfo = repository.selectOne(dto.getProductId());
        if(buyProductInfo.isPresent()){
            ProductEntity productEntity = buyProductInfo.get();
            switch (productEntity.getPurchaseType()) {
                case PurchaseType.CASH -> {
                    // 캐시재화 구매인 경우 (영수증 검증 관련 로직 별도 필요)
                }
                case PurchaseType.CURRENCY -> {
                    // 내부재화 구매인 경우 내부 재화 차감 후 지급 처리
                    // 유저 재화 차감 - s
                    UserCurrencyEntity userCurrencyEntity = UserCurrencyEntity.builder()
                            .userId(dto.getUserId())
                            .currencyId(buyProductInfo.get().getCurrencyId())
                            .build();

                    Optional<UserCurrencyEntity> curUserConsumeCurrency = userCurrencyRepository.findByUserIdAndCurrencyId(userCurrencyEntity);
                    if(curUserConsumeCurrency.isEmpty()){
                        return Result.Failure.of("유저 재화 정보 없음.", ErrorCode.INTERNAL_ERROR.getCode());
                    }

                    // 재화 차감 전 비교 (BigDecimal 은 compareTo 를 통해서 같으면0, 앞 숫자가 더 크면 1, 뒷 숫자가 더 크면 1 반환)
                    BigDecimal curAmount = BigDecimal.valueOf(curUserConsumeCurrency.get().getAmount());
                    if(curAmount.compareTo(buyProductInfo.get().getPrice()) < 0){
                        return Result.Failure.of("구매 재화 부족.", ErrorCode.INTERNAL_ERROR.getCode());
                    }

                    // 재화 차감처리
                    userCurrencyRepository.updateUserCurrencyAmountSubtractByUserIdAndCurrencyId(userCurrencyEntity, buyProductInfo.get().getPrice().longValue());
                    // 유저 재화 차감 - e

                    // 유저 재화 지급 - s
                    // 이거도 불러와서 계산하기 말고 DB 단에서 연산 처리 할것
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<ProductInfo> rewardList = objectMapper.readValue(buyProductInfo.get().getRewards().data(), new TypeReference<List<ProductInfo>>() {});
                    for(ProductInfo reward : rewardList){

                        // 현재 재화 정보를 가져와서
                        CurrencyEntity currencyEntity = CurrencyEntity.builder().currencyId(reward.getCurrencyId()).build();
                        Optional<CurrencyEntity> currencyInfo = currencyRepository.findByCurrencyId(currencyEntity);
                        if(currencyInfo.isEmpty()){
                            return Result.Failure.of("재화 정보 없음", ErrorCode.INTERNAL_ERROR.getCode());
                        }

                        userCurrencyEntity = UserCurrencyEntity.builder()
                                .userId(dto.getUserId())
                                .currencyId(reward.getCurrencyId())
                                .build();

                        Optional<UserCurrencyEntity> curUserSuppliesCurrency = userCurrencyRepository.findByUserIdAndCurrencyId(userCurrencyEntity);
                        if(curUserSuppliesCurrency.isEmpty()){
                            return Result.Failure.of("유저 재화 정보 없음.", ErrorCode.INTERNAL_ERROR.getCode());
                        }

                        Long calAddCurrency = curUserSuppliesCurrency.get().getAmount() + reward.getAmount();
                        if(currencyInfo.get().getMaxAmount() < calAddCurrency){
                            return Result.Failure.of("보유가능한 최대 재화 초과.", ErrorCode.INTERNAL_ERROR.getCode());
                        }

                        userCurrencyRepository.updateUserCurrencyAmountAddByUserIdAndCurrencyId(userCurrencyEntity, reward.getAmount(), reward.getCurrencyId());
                    }
                    // 유저 재화 지급 - e
                }
            }
        }

        return Result.Success.of(null, "상품 구매 및 지급 성공");
    }
}
