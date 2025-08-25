package com.qwerty.nexus.domain.game.data.currency.controller;

import com.qwerty.nexus.domain.game.data.currency.service.UserCurrencyService;
import com.qwerty.nexus.global.constant.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequestMapping(ApiConstants.Path.USER_CURRENCY_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 재화", description = "유저 재화 관련 API")
public class UserCurrencyController {
    private final UserCurrencyService service;

    /*
     * 유저 재화 생성
     * 유저 재화 수정
     * 유저 재화 삭제
     * --- 딱 기본적인 거고..
     *
     * 재화 덮어 쓰기
     * 재화 증감 처리
     * --- 클라이언트를 믿어야 하기 때문에 보안에 위험성이 있음
     *
     * 재화 처리
     * --- 퀘스트 정보, 스테이지 정보 등 내용을 가져와서 재화 증감 처리
     * --- ex) quest1 를 수행하였으며 quest 테이블에서 quest1 정보를 가져온 뒤, 해당 보상을 증가 처리
     *         단, 이 경우 현재 유저가 이전 퀘스트를 수행하였는가 등의 체크도 필요함.
     *
     */
}
