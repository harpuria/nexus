package com.qwerty.nexus.admin;

import com.qwerty.nexus.global.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.pojos.Organization;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminRequestDTO extends Admin {
    private Organization organization;
}
