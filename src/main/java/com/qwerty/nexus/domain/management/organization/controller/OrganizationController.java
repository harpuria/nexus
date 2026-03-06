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
@RequestMapping(ApiConstants.Path.ORG_PATH)
@RequiredArgsConstructor
@Tag(name = "Organization", description = "Organization API")
public class OrganizationController {
    private final OrganizationService service;

    /**
     * Create organization information.
     *
     * @param dto organization create request payload
     * @return success or failure response
     */
    @PostMapping
    @Operation(summary = "Create organization")
    public ResponseEntity<ApiResponse<Void>> createOrganization(@RequestBody OrganizationCreateRequestDto dto) {
        Result<Void> result = service.createOrganization(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * Update organization information.
     *
     * @param orgId organization primary key
     * @param dto organization update request payload
     * @return success or failure response
     */
    @PatchMapping("/{orgId}")
    @Operation(summary = "Update organization")
    public ResponseEntity<ApiResponse<Void>> updateOrganization(
            @PathVariable("orgId") int orgId,
            @Parameter @RequestBody OrganizationUpdateRequestDto dto
    ) {
        dto.setOrgId(orgId);
        Result<Void> result = service.updateOrganization(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * Delete organization information (logical delete).
     *
     * @param orgId organization primary key
     * @return success or failure response
     */
    @DeleteMapping("/{orgId}")
    @Operation(summary = "Delete organization (logical)")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable("orgId") int orgId) {
        OrganizationUpdateRequestDto dto = new OrganizationUpdateRequestDto();
        dto.setOrgId(orgId);
        dto.setIsDel("Y");

        Result<Void> result = service.deleteOrganization(dto);

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * Get a single organization information.
     *
     * @param orgId organization primary key
     * @return single organization response
     */
    @GetMapping("/{orgId}")
    @Operation(summary = "Get organization")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getOrganization(@PathVariable("orgId") int orgId) {
        Result<OrganizationResponseDto> result = service.getOrganization(orgId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * List organization information.
     *
     * @param page page number
     * @param size page size
     * @param sort sort field
     * @param direction sort direction
     * @return organization list response
     */
    @GetMapping("/list")
    @Operation(summary = "List organizations")
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

        Result<OrganizationListResponseDto> result = service.listOrganizations(pagingRequestDto);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}

