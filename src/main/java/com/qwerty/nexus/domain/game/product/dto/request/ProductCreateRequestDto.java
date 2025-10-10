package com.qwerty.nexus.domain.game.product.dto.request;

import com.qwerty.nexus.domain.game.product.ProductType;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequestDto {
    // PRODUCT TABLE
    private Integer gameId;
    private ProductType productType;
    private PurchaseType purchaseType;
    private Integer currencyId;
    private String name;
    private String desc;
    private BigDecimal price;
    private String createdBy;
    private String updatedBy;

    // 지급 재화 정보 (SINGLE, MULTIPLE PRODUCT TABLE)
    private List<ProductInfo> productInfoList;
}
