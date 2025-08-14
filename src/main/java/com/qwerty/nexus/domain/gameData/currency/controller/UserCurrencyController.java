package com.qwerty.nexus.domain.gameData.currency.controller;

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
    private final UserCurrencyController userCurrencyController;
}
