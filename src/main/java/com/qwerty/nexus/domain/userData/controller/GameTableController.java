package com.qwerty.nexus.domain.userData.controller;

import com.qwerty.nexus.global.constant.ApiConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.GAME_TABLE_PATH)
@RequiredArgsConstructor
public class GameTableController {
}
