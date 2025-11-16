package com.qwerty.nexus.domain.game.data.currency.service;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyOperateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyListResponseDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.entity.UserCurrencyEntity;
import com.qwerty.nexus.domain.game.data.currency.repository.UserCurrencyRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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

    /**
     * 유저 재화 단건 조회
     * @param userCurrencyId
     * @return Result<UserCurrencyResponseDto>
     */
    public Result<UserCurrencyResponseDto> selectOneUserCurrency(int userCurrencyId) {
        return repository.findByUserCurrencyId(userCurrencyId)
                .map(UserCurrencyResponseDto::from)
                .<Result<UserCurrencyResponseDto>>map(Result.Success::of)
                .orElseGet(() -> Result.Failure.of(
                        ErrorCode.NOT_FOUND.getMessage(),
                        ErrorCode.NOT_FOUND.getCode()
                ));
    public Result<UserCurrencyListResponseDto> selectAllUserCurrency(
            PagingCommand command,
            Integer userId,
            Integer gameId,
            Integer currencyId
    ) {
        PagingCommand safeCommand = command == null
                ? PagingCommand.builder()
                        .page(ApiConstants.Pagination.DEFAULT_PAGE_NUMBER)
                        .size(ApiConstants.Pagination.DEFAULT_PAGE_SIZE)
                        .sort(ApiConstants.Pagination.DEFAULT_SORT_FIELD)
                        .direction(ApiConstants.Pagination.DEFAULT_SORT_DIRECTION)
                        .build()
                : command;

        int validatedSize = ApiConstants.validatePageSize(safeCommand.getSize());
        int safePage = Math.max(safeCommand.getPage(), ApiConstants.Pagination.DEFAULT_PAGE_NUMBER);
        String sort = StringUtils.hasText(safeCommand.getSort())
                ? safeCommand.getSort()
                : ApiConstants.Pagination.DEFAULT_SORT_FIELD;
        String direction = StringUtils.hasText(safeCommand.getDirection())
                ? safeCommand.getDirection()
                : ApiConstants.Pagination.DEFAULT_SORT_DIRECTION;

        PagingEntity pagingEntity = PagingEntity.builder()
                .page(safePage)
                .size(validatedSize)
                .sort(sort)
                .direction(direction)
                .keyword(safeCommand.getKeyword())
                .build();

        List<UserCurrencyEntity> userCurrencies = repository.selectUserCurrencies(
                pagingEntity,
                userId,
                gameId,
                currencyId
        );

        if (userCurrencies == null || userCurrencies.isEmpty()) {
            return Result.Failure.of(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode());
        }

        List<UserCurrencyResponseDto> currencyResponses = userCurrencies.stream()
                .map(UserCurrencyResponseDto::from)
                .toList();

        long totalCount = currencyResponses.size();
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        UserCurrencyListResponseDto responseDto = UserCurrencyListResponseDto.builder()
                .userCurrencies(currencyResponses)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }
}
