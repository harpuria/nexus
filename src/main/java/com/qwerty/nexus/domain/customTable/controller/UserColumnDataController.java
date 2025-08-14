package com.qwerty.nexus.domain.customTable.controller;

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
//@Tag(name = "유저 데이터", description = "유저 데이터 관련 API")
public class UserColumnDataController {
    /*
     * 유저 데이터 생성
     * 유저 데이터 수정 (덮어쓰기, 덧셈, 뺄샘, 곱셈, 나눗셈 등 연산 처리)
     * 유저 데이터 삭제
     */
}
