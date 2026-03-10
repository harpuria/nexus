package com.qwerty.nexus.domain.game.mail.user.controller;

import com.qwerty.nexus.domain.game.mail.user.service.UserMailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.Path.USER_MAIL_PATH)
public class UserMailController {
    private final UserMailService userMailService;
    /*
        TODO : 우편함 열기 (우편 목록)
        TODO : 우편 단건 읽기 (읽음 Y)
        TODO : 우편 단건 보상 수령 (보상 Y)
        TODO : 우편 전체 보상 수령 (읽음 Y, 보상 Y)
        TODO : 우편 삭제
     */
}
