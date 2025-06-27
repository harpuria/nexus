package com.qwerty.nexus.domain.organization;

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
public class OrganizationController {
    private final OrganizationService organizationService;

    /**
     *
     * 개발할 API 정리
     *
     * 단체 정보 수정	PUT /api/v1/orgs/{orgId}
     *
     */

    /**
     *
     * @param organization
     * @return
     */
    @PatchMapping
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(@RequestBody OrganizationRequestDTO organization){
        OrganizationResponseDTO responseDTO = new OrganizationResponseDTO();
        organizationService.update(organization);
        return ResponseEntity.ok(responseDTO);
    }
}
