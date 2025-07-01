package com.qwerty.nexus.domain.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.generated.tables.pojos.Organization;
import org.jooq.generated.tables.records.AdminRecord;

/**
 * 관리자 생성 요청용 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateRequestDto {
    @Schema(name = "관리자 로그인 아이디", example = "admin")
    private String loginId;

    @Schema(name = "관리자 로그인 패스워드", example = "admin")
    private String loginPw;

    @Schema(name = "관리자 이메일", example = "nexus@qwerty.io")
    private String adminEmail;

    @Schema(name = "관리자 이름", example = "홍길동")
    private String adminNm;

    @Schema(name = "관리자 역할", examples = "SUPER")
    private String adminRole;

    @Schema(name = "단체 이름", example = "쿼티")
    private String orgNm;

    @Schema(name = "단체 코드", example = "123-456-78912")
    private String orgCd;

    // jOOQ Record Type 으로 변환하는 메서드
    public AdminRecord toAdminRecord() {
        AdminRecord record = new AdminRecord();
        record.setLoginId(this.loginId);
        record.setLoginPw(this.loginPw);
        record.setAdminEmail(this.adminEmail);
        record.setAdminNm(this.adminNm);
        return record;
    }
}
