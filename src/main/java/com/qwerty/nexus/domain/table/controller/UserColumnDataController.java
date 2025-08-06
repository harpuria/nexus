package com.qwerty.nexus.domain.table.controller;

import com.qwerty.nexus.global.constant.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.USER_COLUMN_DATA_PATH)
@RequiredArgsConstructor
@Tag(name = "유저 데이터", description = "유저 데이터 관련 API")
public class UserColumnDataController {
}
