package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserCurrencyService {
    private final UserCurrencyRepository repository;

    /**
     * 유저 재화 생성
     * @param command
     * @return
     */
    public Result<Void> createUserCurrency(UserCurrencyCreateCommand command) {
        UserCurrencyEntity entity = UserCurrencyEntity.builder().build();

        UserCurrencyEntity createRst = repository.createUserCurrency(entity);

        return Result.Success.of(null, "성공");
    }

    /**
     * 유저 재화 수정
     * @param command
     * @return
     */
    public Result<Void> update(UserCurrencyUpdateCommand command) {
        UserCurrencyEntity entity = UserCurrencyEntity.builder().build();

        UserCurrencyEntity updateRst = repository.updateUserCurrency(entity);

        return Result.Success.of(null, "성공");
    }
}
