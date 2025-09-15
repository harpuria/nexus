package com.qwerty.nexus.domain.game.data.currency.controller;

import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyCreateCommand;
import com.qwerty.nexus.domain.game.data.currency.command.UserCurrencyUpdateCommand;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyCreateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.dto.request.UserCurrencyUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.currency.service.UserCurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
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
     */

    /**
     * 유저 재화 생성
     * @param dto
     * @return
     */
    @PostMapping
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
    public ResponseEntity<ApiResponse<Void>> updateUserCurrency(@PathVariable("userCurrencyId") int userCurrencyId, @RequestBody UserCurrencyUpdateRequestDto dto){
        dto.setUserCurrencyId(userCurrencyId);
        Result<Void> result = service.update(UserCurrencyUpdateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 재화 삭제 (논리적 삭제)
     * @param dto
     * @return
     */
    @DeleteMapping("/{userCurrencyId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserCurrency(@PathVariable("userCurrencyId") int userCurrencyId, @RequestBody UserCurrencyUpdateRequestDto dto){
        dto.setUserCurrencyId(userCurrencyId);
        dto.setIsDel("Y");
        Result<Void> result = service.update(UserCurrencyUpdateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /*
    @PatchMapping("/operation/{userCurrencyId}")
    public
     */

}
