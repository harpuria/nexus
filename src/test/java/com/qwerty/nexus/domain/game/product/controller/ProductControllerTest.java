package com.qwerty.nexus.domain.game.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.nexus.domain.game.product.dto.request.ProductCreateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.request.ProductUpdateRequestDto;
import com.qwerty.nexus.domain.game.product.dto.response.ProductListResponseDto;
import com.qwerty.nexus.domain.game.product.service.ProductService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("상품 생성 성공")
    void createProduct_success() throws Exception {
        ProductCreateRequestDto request = new ProductCreateRequestDto();
        request.setGameId(1);
        request.setName("상품");

        when(productService.create(any())).thenReturn(Result.Success.of(null, "created"));

        mockMvc.perform(post(ApiConstants.Path.PRODUCT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("created"));

        verify(productService).create(any());
    }

    @Test
    @DisplayName("상품 생성 실패")
    void createProduct_failure() throws Exception {
        ProductCreateRequestDto request = new ProductCreateRequestDto();

        when(productService.create(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.PRODUCT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("상품 수정 성공")
    void updateProduct_success() throws Exception {
        ProductUpdateRequestDto request = new ProductUpdateRequestDto();
        request.setName("수정");

        when(productService.update(any())).thenReturn(Result.Success.of(null, "updated"));

        mockMvc.perform(patch(ApiConstants.Path.PRODUCT_PATH + "/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("updated"));
    }

    @Test
    @DisplayName("상품 수정 실패")
    void updateProduct_failure() throws Exception {
        ProductUpdateRequestDto request = new ProductUpdateRequestDto();

        when(productService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(patch(ApiConstants.Path.PRODUCT_PATH + "/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProduct_success() throws Exception {
        when(productService.update(any())).thenReturn(Result.Success.of(null, "deleted"));

        mockMvc.perform(delete(ApiConstants.Path.PRODUCT_PATH + "/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("deleted"));
    }

    @Test
    @DisplayName("상품 삭제 실패")
    void deleteProduct_failure() throws Exception {
        when(productService.update(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(delete(ApiConstants.Path.PRODUCT_PATH + "/{productId}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

    @Test
    @DisplayName("상품 구매 성공")
    void buyProduct_success() throws Exception {
        when(productService.buy(any())).thenReturn(Result.Success.of(null, "구매"));

        mockMvc.perform(post(ApiConstants.Path.PRODUCT_PATH + "/buy")
                        .param("productId", "1")
                        .param("userId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("구매"));
    }

    @Test
    @DisplayName("상품 구매 실패")
    void buyProduct_failure() throws Exception {
        when(productService.buy(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(post(ApiConstants.Path.PRODUCT_PATH + "/buy")
                        .param("productId", "1")
                        .param("userId", "2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }
    @Test
    @DisplayName("상품 목록 조회 성공")
    void listProducts_success() throws Exception {
        ProductListResponseDto response = ProductListResponseDto.builder()
                .products(List.of())
                .page(0)
                .size(10)
                .totalCount(0)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(productService.list(any())).thenReturn(Result.Success.of(response, "ok"));

        mockMvc.perform(get(ApiConstants.Path.PRODUCT_PATH)
                        .param("gameId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("ok"));

        verify(productService).list(any());
    }

    @Test
    @DisplayName("상품 목록 조회 실패")
    void listProducts_failure() throws Exception {
        when(productService.list(any())).thenReturn(Result.Failure.of("에러", "ERROR_CODE"));

        mockMvc.perform(get(ApiConstants.Path.PRODUCT_PATH)
                        .param("gameId", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERROR_CODE"));
    }

}
