package com.qwerty.nexus.organization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.pojos.Organization;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationRequestDTO extends Organization {
    private Admin admin;
}
