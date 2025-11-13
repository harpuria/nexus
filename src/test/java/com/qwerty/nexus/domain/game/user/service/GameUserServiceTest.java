package com.qwerty.nexus.domain.game.user.service;

import com.qwerty.nexus.domain.auth.Provider;
import com.qwerty.nexus.domain.game.user.dto.response.GameUserListResponseDto;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameUserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GameUserRepository gameUserRepository;

    @InjectMocks
    private GameUserService gameUserService;

    @Test
    @DisplayName("게임 유저 목록 조회 - 데이터 존재")
    void listGameUsers_success() {
        PagingCommand command = PagingCommand.builder()
                .page(0)
                .size(5)
                .sort("nickname")
                .direction("asc")
                .keyword("nick")
                .build();

        GameUserEntity entity = GameUserEntity.builder()
                .userId(1)
                .gameId(10)
                .userLId("login")
                .userLPw("password")
                .nickname("nick")
                .provider(Provider.GOOGLE)
                .device("mobile")
                .build();

        when(gameUserRepository.selectGameUsers(any())).thenReturn(List.of(entity));
        when(gameUserRepository.countGameUsers(any())).thenReturn(1L);

        Result<GameUserListResponseDto> result = gameUserService.listGameUsers(command);

        assertThat(result).isInstanceOf(Result.Success.class);
        GameUserListResponseDto response = ((Result.Success<GameUserListResponseDto>) result).data();
        assertThat(response.users()).hasSize(1);
        assertThat(response.totalCount()).isEqualTo(1);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.hasNext()).isFalse();
        assertThat(response.hasPrevious()).isFalse();
        assertThat(response.size()).isEqualTo(5);

        verify(gameUserRepository).selectGameUsers(any());
        verify(gameUserRepository).countGameUsers(any());
    }

    @Test
    @DisplayName("게임 유저 목록 조회 - 결과 없음")
    void listGameUsers_empty() {
        PagingCommand command = PagingCommand.builder()
                .page(2)
                .size(20)
                .sort("createdAt")
                .direction("desc")
                .build();

        when(gameUserRepository.selectGameUsers(any())).thenReturn(List.of());
        when(gameUserRepository.countGameUsers(any())).thenReturn(0L);

        Result<GameUserListResponseDto> result = gameUserService.listGameUsers(command);

        assertThat(result).isInstanceOf(Result.Success.class);
        GameUserListResponseDto response = ((Result.Success<GameUserListResponseDto>) result).data();
        assertThat(response.users()).isEmpty();
        assertThat(response.totalCount()).isZero();
        assertThat(response.totalPages()).isZero();
        assertThat(response.hasNext()).isFalse();
        assertThat(response.hasPrevious()).isFalse();
    }
}
