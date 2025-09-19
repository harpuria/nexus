package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyOperateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyResponseDto;
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
    public Result<Void> create(UserCurrencyCreateCommand command) {
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

    /**
     * 유저 재화 연산
     * @param from
     * @return
     */
    public Result<UserCurrencyResponseDto> operate(UserCurrencyOperateCommand from) {
        UserCurrencyEntity entity = UserCurrencyEntity.builder().build();

        // 이거는 클라이언트를 믿어야하는 API 이기 때문에 어느정도 보안 누수는 감안해야함.
        // 최대한 막는 방법을 강구해야할듯. 클라쪽에서도, 서버쪽에서도.

        // 현재 재화 상태(갯수 등) 가져오기 (어디에 연산해야할지 알아야 하니께)

        // 여기서 연산처리 한다음에
        switch(from.getOperation()) {
            case "+" -> {}
            case "-" -> {}
            case "*" -> {}
            case "/" -> {}
        }

        // update 처리
        UserCurrencyEntity updateRst = repository.updateUserCurrency(entity);

        return Result.Success.of(null, "성공");
    }
}
