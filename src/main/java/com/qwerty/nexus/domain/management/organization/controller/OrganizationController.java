package com.qwerty.nexus.domain.management.organization.controller;

import com.qwerty.nexus.domain.management.organization.command.OrganizationUpdateCommand;
import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationResponseDto;
import com.qwerty.nexus.domain.management.organization.service.OrganizationService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.ORG_PATH)
@RequiredArgsConstructor
@Tag(name = "단체", description = "단체 관련 API")
public class OrganizationController {
    private final OrganizationService service;

    /**
     * 단체 정보 수정
     * @param dto 수정할 단체 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PatchMapping
    @Operation(summary = "단체 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateOrganization(@Parameter @RequestBody OrganizationUpdateRequestDto dto){
        Result<Void> result = service.update(OrganizationUpdateCommand.from(dto));

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 한 건의 단체 정보 가져오기
     * @param orgId 단체 아이디 (PK)
     * @return 한 건의 단체 정보를 담은 객체 (DTO)
     */
    @GetMapping("/{orgId}")
    @Operation(summary = "한 건의 단체 정보 가져오기")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> selectOneOrganization(@PathVariable("orgId") int orgId){
        Result<OrganizationResponseDto> result = service.selectOne(orgId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
