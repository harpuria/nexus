package com.qwerty.nexus.domain.organization.controller;

import com.qwerty.nexus.domain.organization.command.OrganizationUpdateCommand;
import com.qwerty.nexus.domain.organization.dto.request.OrganizationRequestDTO;
import com.qwerty.nexus.domain.organization.dto.response.OrganizationResponseDTO;
import com.qwerty.nexus.domain.organization.service.OrganizationService;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/org")
@RequiredArgsConstructor
@Tag(name = "단체", description = "단체 관련 API")
public class OrganizationController {
    private final OrganizationService organizationService;

    /**
     * 단체 정보 수정
     * @param organization
     * @return
     */
    @PatchMapping
    @Operation(description = "단체 정보 수정")
    public ResponseEntity<ApiResponse<OrganizationResponseDTO>> updateOrganization(@RequestBody OrganizationUpdateCommand organization){
        Result<OrganizationResponseDTO> result = organizationService.update(organization.toOrganizationCommand());

        return ResponseEntity.ok(responseDTO);
    }
}
