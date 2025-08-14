package com.qwerty.nexus.domain.customTable.controller;

import com.qwerty.nexus.global.constant.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.TABLE_COLUMN_PATH)
@RequiredArgsConstructor
//@Tag(name = "테이블 컬럼", description = "테이블 컬럼 관련 API")
public class TableColumnController {
    /*
     * 컬럼 생성
     * 컬럼 수정
     * 컬럼 삭제
     */
}
