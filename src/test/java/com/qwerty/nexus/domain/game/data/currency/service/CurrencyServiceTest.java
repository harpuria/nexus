package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.dto.response.CurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.entity.CurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.CurrencyRepository;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private GameUserRepository gameUserRepository;

    @Mock
    private UserCurrencyRepository userCurrencyRepository;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(currencyRepository, gameUserRepository, userCurrencyRepository);
    }

    @Test
    @DisplayName("재화 목록 조회 - 페이징과 함께 DTO 반환")
    void listCurrencies_withPaging_returnsDtos() {
        PagingCommand pagingCommand = PagingCommand.builder()
                .page(0)
                .size(5)
                .sort("name")
                .direction("asc")
                .build();

        CurrencyEntity entity = CurrencyEntity.builder()
                .currencyId(1)
                .gameId(10)
                .name("골드")
                .desc("기본 재화")
                .maxAmount(1000L)
                .build();

        when(currencyRepository.selectCurrencies(any())).thenReturn(List.of(entity));
        when(currencyRepository.countCurrencies(any())).thenReturn(1L);

        Result<List<CurrencyResponseDto>> result = currencyService.listCurrencies(pagingCommand, 10, false);

        assertInstanceOf(Result.Success.class, result);
        Result.Success<List<CurrencyResponseDto>> success = (Result.Success<List<CurrencyResponseDto>>) result;
        assertThat(success.data()).hasSize(1);
        assertThat(success.data().get(0).getName()).isEqualTo("골드");
        assertThat(success.message()).contains("총 1건");

        verify(currencyRepository).selectCurrencies(any());
        verify(currencyRepository).countCurrencies(any());
    }

    @Test
    @DisplayName("재화 목록 조회 - 결과가 없는 경우 빈 리스트 반환")
    void listCurrencies_emptyResult_returnsEmptyList() {
        when(currencyRepository.selectCurrencies(any())).thenReturn(Collections.emptyList());

        Result<List<CurrencyResponseDto>> result = currencyService.listCurrencies(null, null, null);

        assertInstanceOf(Result.Success.class, result);
        Result.Success<List<CurrencyResponseDto>> success = (Result.Success<List<CurrencyResponseDto>>) result;
        assertThat(success.data()).isEmpty();
        assertThat(success.message()).contains("존재하지 않습니다");

        verify(currencyRepository).selectCurrencies(any());
        verify(currencyRepository, never()).countCurrencies(any());
    }
}
