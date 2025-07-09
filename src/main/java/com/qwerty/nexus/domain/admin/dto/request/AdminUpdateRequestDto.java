package com.qwerty.nexus.domain.admin.dto.request;

import com.qwerty.nexus.domain.admin.command.AdminUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 관리자 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminUpdateRequestDto {
    @Schema(example = "1")
    private int adminId;

    @Schema(example = "1")
    private int gameId;

    @Schema(example = "1")
    private String loginPw;

    @Schema(example = "SUPER")
    private String adminRole;

    @Schema(example = "update@qwerty.io")
    private String adminEmail;

    @Schema(example = "1")
    private String adminNm;

    @Schema(example = "N")
    private String isDel;

    // Service 전달 파라미터로 쓸 Command 객체 변환
    public AdminUpdateCommand toAdminCommand() {
        return AdminUpdateCommand.builder()
                .adminId(this.adminId)
                .gameId(this.gameId)
                .loginPw(this.loginPw)
                .adminRole(this.adminRole)
                .adminEmail(this.adminEmail)
                .adminNm(this.adminNm)
                .isDel(this.isDel)
                .build();
    }
}
