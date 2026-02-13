package com.qwerty.nexus.domain.game.data.mail.dto.request;

import com.qwerty.nexus.domain.game.data.mail.MailSendType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

@Getter
@Setter
@NoArgsConstructor
public class MailUpdateRequestDto {
    @NotNull(message = "mailId는 필수입니다.")
    @Positive(message = "mailId는 1 이상이어야 합니다.")
    private Integer mailId;

    @Schema(example = "오픈 기념 우편(수정)")
    @Size(max = 255, message = "title은 255자 이하여야 합니다.")
    private String title;

    @Schema(example = "보상이 일부 조정되었습니다.")
    @Size(max = 4000, message = "content는 4000자 이하여야 합니다.")
    private String content;

    private JSONB rewards;

    private MailSendType sendType;

    private Long expireAt;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;

    @Size(max = 1, message = "isDel은 Y/N만 허용됩니다.")
    private String isDel;
}
