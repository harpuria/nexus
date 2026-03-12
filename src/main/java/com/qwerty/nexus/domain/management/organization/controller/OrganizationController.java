package com.qwerty.nexus.domain.management.organization.controller;

import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationCreateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.request.OrganizationUpdateRequestDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationListResponseDto;
import com.qwerty.nexus.domain.management.organization.dto.response.OrganizationResponseDto;
import com.qwerty.nexus.domain.management.organization.service.OrganizationService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.ADMIN_ORG_PATH)
@RequiredArgsConstructor
@Tag(name = "단체 (관리자)", description = "단체 관련 API (관리자)")
public class OrganizationController {
    private final OrganizationService organizationService;

    /**
     * 단체 생성
     * @param dto 생성할 단체 정보
     * @return 성공 혹은 실패 응답
     */
    @PostMapping
    @Operation(summary = "단체 생성")
    public ResponseEntity<ApiResponse<Void>> createOrganization(@Parameter @RequestBody OrganizationCreateRequestDto dto) {
        Result<Void> result = organizationService.createOrganization(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 단체 정보 수정
     * @param orgId 단체 PK
     * @param dto 수정할 단체 정보
     * @return 성공 혹은 실패 응답
     */
    @PatchMapping("/{orgId}")
    @Operation(summary = "단체 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateOrganization(
            @PathVariable("orgId") int orgId,
            @Parameter @RequestBody OrganizationUpdateRequestDto dto
    ) {
        dto.setOrgId(orgId);
        Result<Void> result = organizationService.updateOrganization(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 단체 삭제 (논리 삭제)
     * @param orgId 단체 PK
     * @return 성공 혹은 실패 응답
     */
    @DeleteMapping("/{orgId}")
    @Operation(summary = "단체 삭제 (논리 삭제)")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable("orgId") int orgId) {
        OrganizationUpdateRequestDto dto = new OrganizationUpdateRequestDto();
        dto.setOrgId(orgId);
        dto.setIsDel("Y");

        Result<Void> result = organizationService.deleteOrganization(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 단체 단건 조회
     * @param orgId 단체 PK
     * @return 단체 정보 응답
     */
    @GetMapping("/{orgId}")
    @Operation(summary = "단체 단건 조회")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getOrganization(@PathVariable("orgId") int orgId) {
        Result<OrganizationResponseDto> result = organizationService.getOrganization(orgId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 단체 목록 조회
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sort 정렬 컬럼
     * @param direction 정렬 방향
     * @return 단체 목록 응답
     */
    @GetMapping("/list")
    @Operation(summary = "단체 목록 조회")
    public ResponseEntity<ApiResponse<OrganizationListResponseDto>> listOrganizations(
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<OrganizationListResponseDto> result = organizationService.listOrganizations(pagingRequestDto);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
