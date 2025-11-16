package com.qwerty.nexus.domain.game.data.currency.controller;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyOperateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyOperateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyListResponseDto;
import com.qwerty.nexus.domain.game.data.currency.dto.response.UserCurrencyResponseDto;
import com.qwerty.nexus.domain.game.data.currency.service.UserCurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequestMapping(ApiConstants.Path.USER_CURRENCY_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 재화", description = "유저 재화 관련 API")
public class UserCurrencyController {
    private final UserCurrencyService service;

    /*
     * 유저 재화 생성
     * >> API 는 만들어둘거긴 한데 기본적으로 이런식으로 가야할거 같음.
     *
     * 1) 특정 유저가 게임에 로그인(회원가입)
     * 2) 이 때 신규유저인 경우 Currency 테이블에 있는 정보를 토대로 유저의 재화(UserCurrency)를 생성 (service 에서 처리)
     * 3) 기존유저인 경우에는 UserCurrency 테이블에 있는 정보 GET
     *
     * 유저 재화 수정
     * 유저 재화 삭제
     * --- 딱 기본적인 거고..
     *
     * 재화 덮어 쓰기 (이게 수정하기랑 사실상 같은거고)
     * 재화 증감 처리 (operator 혹은 operator문자열(add, mul 등)을 받아서 처리
     * --- 클라이언트를 믿어야 하기 때문에 보안에 위험성이 있음
     *
     * 재화 처리 ( 퀘스트 혹은 스테이지 정보를 가져와서 처리 하는방법 )
     * --- 퀘스트 정보, 스테이지 정보 등 내용을 가져와서 재화 증감 처리
     * --- ex) quest1 를 수행하였으며 quest 테이블에서 quest1 정보를 가져온 뒤, 해당 보상을 증가 처리
     *         단, 이 경우 현재 유저가 이전 퀘스트를 수행하였는가 등의 체크도 필요함.
     * -- 이건 퀘스트 쪽에서 담당해야 할듯. quest 를 수행 > 보상 확인 > 보상 지급(userCurrency 처리) 처리
     *
     * 캐시 구매의 경우? (매우 중요할듯)
     * 1) 캐시 재화를 구매한다
     * 2) 캐시 재화의 상품명을 검색한다 (때로 캐시라는 테이블이 필요할까?)
     * 3) 상품명에 있는 재화 수량만큼 더해준다
     * 4) 그리고 클라이언트에 반환처리해준다
     *
     */

    /**
     * 유저 재화 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "유저 재화 생성")
    public ResponseEntity<ApiResponse<Void>> createUserCurrency(@RequestBody UserCurrencyCreateRequestDto dto){
        Result<Void> result = service.create(UserCurrencyCreateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 재화 수정
     * @param dto
     * @return
     */
    @PatchMapping("/{userCurrencyId}")
    @Operation(summary = "유저 재화 수정")
    public ResponseEntity<ApiResponse<Void>> updateUserCurrency(@PathVariable("userCurrencyId") int userCurrencyId, @RequestBody UserCurrencyUpdateRequestDto dto){
        dto.setUserCurrencyId(userCurrencyId);

        Result<Void> result = service.update(UserCurrencyUpdateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 재화 삭제 (논리적 삭제)
     * @param userCurrencyId
     * @return
     */
    @DeleteMapping("/{userCurrencyId}")
    @Operation(summary = "유저 재화 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteUserCurrency(@PathVariable("userCurrencyId") int userCurrencyId){
        UserCurrencyUpdateRequestDto dto = new UserCurrencyUpdateRequestDto();
        dto.setUserCurrencyId(userCurrencyId);
        dto.setIsDel("Y");

        Result<Void> result = service.update(UserCurrencyUpdateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 재화 연산
     * @param userCurrencyId
     * @param dto
     * @return
     */
    @PatchMapping("/operation/{userCurrencyId}")
    @Operation(summary = "유저 재화 연산")
    public ResponseEntity<ApiResponse<UserCurrencyResponseDto>> operationUserCurrency(@PathVariable("userCurrencyId") int userCurrencyId, @RequestBody UserCurrencyOperateRequestDto dto){
        dto.setCurrencyId(userCurrencyId);
        /**
         *
         * 연산자 (+, -, *, /) 중에 하나를 받아서 연산값 을 연산해주면 됨.
         * 그리고 그 결과를 DB 에 넣어주고, 클라이언트에 반환처리.
         *
         * 단, 이건 클라이언트를 믿어야 하기 때문에 검증하기가 어려울 수 있음.
         * 예를들어.. 음.. 현재 골드가 100개 인데, 50개를 증가하는 연산을 한다고 치자.
         *
         * 그러면 현재 유저가 가지고 있는 골드 정보를 가져와서 100인지 먼저 체크함. (아니면 팅겨내기)
         * 100개가 맞으면 여기서 50개를 증가하는 연산을 진행한 뒤 DB 에 저장 (골드 150개)
         * 클라이언트에 150개를 반환 처리. 이런식으로 하면될까
         *
         */

        Result<UserCurrencyResponseDto> result = service.operate(UserCurrencyOperateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    @Operation(summary = "유저 재화 목록 조회")
    public ResponseEntity<ApiResponse<UserCurrencyListResponseDto>> listUserCurrencies(
            @PathVariable("userId") int userId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_FIELD) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction,
            @RequestParam(required = false) Integer currencyId,
            @RequestParam(required = false) Integer gameId
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<UserCurrencyListResponseDto> result = service.selectAllUserCurrency(
                PagingCommand.from(pagingRequestDto),
                userId,
                gameId,
                currencyId
        );

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

}
