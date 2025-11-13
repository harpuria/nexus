package com.qwerty.nexus.domain.game.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.product.PurchaseType;
import com.qwerty.nexus.domain.game.product.command.ProductSearchCommand;
import com.qwerty.nexus.domain.game.product.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.product.entity.ProductEntity;
import com.qwerty.nexus.domain.game.product.entity.ProductSearchEntity;
import com.qwerty.nexus.domain.game.product.repository.ProductRepository;
import com.qwerty.nexus.global.response.Result;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private UserCurrencyRepository userCurrencyRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 리스트 조회시 페이지 정보를 계산한다")
    void list_calculatesPagingMetadata() {
        ProductSearchCommand command = ProductSearchCommand.builder()
                .gameId(1)
                .page(1)
                .size(200)
                .build();

        ProductEntity entity = ProductEntity.builder()
                .productId(10)
                .gameId(1)
                .purchaseType(PurchaseType.CURRENCY)
                .currencyId(2)
                .name("Sword")
                .desc("desc")
                .price(BigDecimal.TEN)
                .build();

        when(productRepository.selectProducts(any(ProductSearchEntity.class)))
                .thenReturn(List.of(entity));
        when(productRepository.countProducts(any(ProductSearchEntity.class)))
                .thenReturn(250L);

        Result<ProductListResponseDto> result = productService.list(command);

        assertThat(result).isInstanceOf(Result.Success.class);
        ProductListResponseDto response = ((Result.Success<ProductListResponseDto>) result).data();
        assertThat(response.products()).hasSize(1);
        assertThat(response.size()).isEqualTo(100);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.totalCount()).isEqualTo(250L);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.hasPrevious()).isTrue();

        ArgumentCaptor<ProductSearchEntity> captor = ArgumentCaptor.forClass(ProductSearchEntity.class);
        verify(productRepository).selectProducts(captor.capture());
        ProductSearchEntity passedEntity = captor.getValue();
        assertThat(passedEntity.getLimit()).isEqualTo(100);
        assertThat(passedEntity.getOffset()).isEqualTo(100);
    }

    @Test
    @DisplayName("검색어가 있는 경우 트림 후 Repository 로 전달한다")
    void list_trimsKeywordBeforeSearching() {
        ProductSearchCommand command = ProductSearchCommand.builder()
                .gameId(2)
                .page(0)
                .size(10)
                .keyword("  Sword  ")
                .build();

        when(productRepository.selectProducts(any(ProductSearchEntity.class)))
                .thenReturn(List.of());
        when(productRepository.countProducts(any(ProductSearchEntity.class)))
                .thenReturn(0L);

        productService.list(command);

        ArgumentCaptor<ProductSearchEntity> captor = ArgumentCaptor.forClass(ProductSearchEntity.class);
        verify(productRepository).selectProducts(captor.capture());
        assertThat(captor.getValue().getKeyword()).isEqualTo("Sword");
    }
}
