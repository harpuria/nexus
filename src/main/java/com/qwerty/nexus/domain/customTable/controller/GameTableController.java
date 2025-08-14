package com.qwerty.nexus.domain.customTable.controller;

import com.qwerty.nexus.domain.customTable.dto.request.TableCreateRequestDto;
import com.qwerty.nexus.domain.customTable.dto.request.TableUpdateRequestDto;
import com.qwerty.nexus.domain.customTable.service.GameTableService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.GAME_TABLE_PATH)
@RequiredArgsConstructor
//@Tag(name = "게임 테이블", description = "게임 테이블 관련 API")
public class GameTableController {

    private final GameTableService gameTableService;

    /**
     * 테이블 생성
     * @param dto 생셩할 테이블 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PostMapping
    //@Operation(summary = "테이블 생성")
    public ResponseEntity<ApiResponse<Void>> createTable(@Parameter @RequestBody TableCreateRequestDto dto){
        return null;
    }

    /**
     * 테이블 수정
     * @param tableId 수정할 테이블 아이디 (PK)
     * @param dto 수정할 테이블 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PatchMapping("/{tableId}")
    //@Operation(summary = "테이블 수정")
    public ResponseEntity<ApiResponse<Void>> updateTable(@PathVariable("tableId") int tableId,
                                                         @Parameter @RequestBody TableUpdateRequestDto dto){
        return null;
    }

    /**
     * 테이블 삭제 (논리적 삭제)
     * @param tableId 삭제할 테이블 아이디 (PK)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @DeleteMapping("/{tableId}")
    //@Operation(summary = "테이블 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteTable(@PathVariable("tableId") int tableId){
        return null;
    }
}
