package com.qwerty.nexus.domain.admin.dto.request;

import com.qwerty.nexus.domain.admin.command.AdminCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 초기 관리자 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminInitCreateRequestDto {
    @Schema(example = "admin")
    private String loginId;

    @Schema(example = "admin")
    private String loginPw;

    @Schema(example = "nexus@qwerty.io")
    private String adminEmail;

    @Schema(example = "홍길동")
    private String adminNm;

    @Schema(examples = "SUPER")
    private String adminRole;

    @Schema(example = "쿼티")
    private String orgNm;

    @Schema(example = "123-456-78912")
    private String orgCd;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public AdminCreateCommand toCommand() {
        return AdminCreateCommand.builder()
                .loginId(this.loginId)
                .loginPw(this.loginPw)
                .adminEmail(this.adminEmail)
                .adminNm(this.adminNm)
                .adminRole(this.adminRole)
                .orgNm(this.orgNm)
                .orgCd(this.orgCd)
                .build();
    }
}
