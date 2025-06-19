package com.qwerty.nexus.organization;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.pojos.Organization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController("/org")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

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
