package com.qwerty.nexus.domain.game.product.service;

import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.product.ProductType;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.command.ProductCreateCommand;
import com.qwerty.nexus.domain.game.product.command.ProductUpdateCommand;
import com.qwerty.nexus.domain.game.product.dto.request.ProductInfo;
import com.qwerty.nexus.domain.game.product.entity.MultipleProductEntity;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.domain.game.product.entity.SingleProductEntity;
import com.qwerty.nexus.domain.game.product.repository.MultipleProductRepository;
import com.qwerty.nexus.domain.game.product.repository.ProductRepository;
import com.qwerty.nexus.domain.game.product.repository.SingleProductRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final SingleProductRepository singleProductRepository;
    private final MultipleProductRepository multipleProductRepository;
    private final UserCurrencyRepository userCurrencyRepository;

    /**
     * 상품 정보 생성
     * @param command
     * @return
     */
    @Transactional
    public Result<Void> create(ProductCreateCommand command) {
        ProductEntity entity = ProductEntity.builder()
                .gameId(command.getGameId())
                .productType(command.getProductType())
                .purchaseType(command.getPurchaseType())
                .currencyId(command.getCurrencyId())
                .name(command.getName())
                .desc(command.getDesc())
                .price(command.getPrice())
                .createdBy(command.getCreatedBy())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<ProductEntity> createRst = Optional.ofNullable(repository.create(entity));

        // TODO : 싱글 멀티플 테이블 삭제. PRODUCT 하나로 관리하되, jsonb 타입으로 상품 지급 처리
        // 생성할 때 싱글, 멀티에냐 따라서 추가로 repository 작업해야함
        switch (command.getProductType()) {
            case ProductType.SINGLE -> {
                // 싱글인 경우 SINGLE_PRODUCT insert
                SingleProductEntity singleProductEntity = SingleProductEntity.builder()
                        .productId(command.getCurrencyId())
                        .currencyId(command.getProductInfoList().getFirst().getCurrencyId())
                        .amount(command.getProductInfoList().getFirst().getAmount())
                        .build();

                Optional<SingleProductEntity> rst = Optional.ofNullable(singleProductRepository.createSingleProduct(singleProductEntity));
                if(rst.isEmpty()){
                    return Result.Failure.of("상품 생성 실패 (SINGLE_PRODUCT)", ErrorCode.INTERNAL_ERROR.getCode());
                }
            }
            case ProductType.MULTIPLE -> {
                // 멀티인 경우 MULTIPLE_PRODUCT insert
                for(ProductInfo productInfo : command.getProductInfoList()){
                    MultipleProductEntity multipleProductEntity = MultipleProductEntity.builder()
                            .productId(command.getCurrencyId())
                            .currencyId(productInfo.getCurrencyId())
                            .amount(productInfo.getAmount())
                            .build();

                    Optional<MultipleProductEntity> rst = Optional.ofNullable(multipleProductRepository.createMultipleProduct(multipleProductEntity));
                    if(rst.isEmpty()){
                        return Result.Failure.of("상품 생성 실패 (MULTIPLE_PRODUCT)", ErrorCode.INTERNAL_ERROR.getCode());
                    }
                }
            }
        }

        if(createRst.isPresent()){
            return Result.Success.of(null, "상품 생성 완료");
        }else{
            return Result.Failure.of("상품 생성 실패", ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 상품 정보 수정 (논리적 삭제 포함)
     * @param command
     * @return
     */
    public Result<Void> update(ProductUpdateCommand command) {

        ProductEntity entity = ProductEntity.builder().build();
        Optional<ProductEntity> updateRst = Optional.ofNullable(repository.update(entity));

        String type = "수정";
        if(command.getIsDel() != null && command.getIsDel().equalsIgnoreCase("Y"))
            type = "삭제";

        if(updateRst.isPresent()){
            return Result.Success.of(null, String.format("상품 %s 성공", type));
        }
        else{
            return Result.Failure.of(String.format("상품 %s 실패", type), ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 상품 구매 및 지급
     * @param productId
     * @return
     */
    @Transactional
    public Result<Void> buy(int productId) {
        /*
         * 1) 어떤 상품을 구매할지 받아와서 (controller > service 에 id 전달)
         * 2) 해당 상품을 구매 (이 때 캐시재화구매(CASH)인지 내부재화구매(CURRENCY)인지 확인)
         * 2-1) 캐시재화인 경우 영수증 검증 관련 로직 필요 (추후 구현)
         * 2-2) 내부재화구매인 경우 PRODUCT 테이블에서 데이터를 가져온 뒤, 내부재화차감 및 상품 지급 처리
         * 3) 반환할 때는 현재 DB 에 있는 재화 정보를 줘야하나? 동기화가 안되면? 이런문제도 생각해봐야...
         */
        Optional<ProductEntity> selectRst = Optional.ofNullable(repository.selectOne(productId));
        if(selectRst.isPresent()){
            ProductEntity productEntity = selectRst.get();
            switch (productEntity.getPurchaseType()) {
                case PurchaseType.CASH -> {
                    // 캐시재화 구매인 경우 (영수증 검증 관련 로직 별도 필요)
                }
                case PurchaseType.CURRENCY -> {
                    // 내부재화 구매인 경우
                    // 내부재화 차감
                }
            }

            // TODO : 싱글 멀티플 테이블 삭제. PRODUCT 하나로 관리하되, jsonb 타입으로 상품 지급 처리
            // 상품 지급 처리 (유저 재화 테이블에 데이터 추가)
            switch (productEntity.getProductType()){
                case ProductType.SINGLE -> {
                    // 싱글 상품인 경우 SINGLE_PRODUCT 에서 가져옴
                }
                case ProductType.MULTIPLE -> {
                    // 멀티 상품인 경우 MULTIPLE_PRODUCT 에서 가져옴
                }
            }
        }

        return Result.Success.of(null, "상품 구매 및 지급 성공");
    }
}
