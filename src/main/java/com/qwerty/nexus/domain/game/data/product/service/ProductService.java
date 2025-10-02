package com.qwerty.nexus.domain.game.data.product.service;

import com.qwerty.nexus.domain.game.data.product.command.ProductCreateCommand;
import com.qwerty.nexus.domain.game.data.product.command.ProductUpdateCommand;
import com.qwerty.nexus.domain.game.data.product.dto.response.ProductResponseDto;
import com.qwerty.nexus.domain.game.data.product.entity.ProductEntity;
import com.qwerty.nexus.domain.game.data.product.repository.ProductRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.pojos.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    /**
     * 상품 정보 생성
     * @param command
     * @return
     */
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
        String type = "수정";
        if(command.getIsDel() != null && command.getIsDel().equalsIgnoreCase("Y"))
            type = "삭제";

        return Result.Success.of(null, String.format("상품 %s 성공", type));
    }
}
