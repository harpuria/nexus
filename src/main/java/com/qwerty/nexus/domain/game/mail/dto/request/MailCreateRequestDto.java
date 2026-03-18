package com.qwerty.nexus.domain.game.mail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.qwerty.nexus.domain.game.mail.MailRecipientsType;
import com.qwerty.nexus.domain.game.mail.MailSendType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.JSONB;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MailCreateRequestDto {
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    @Schema(example = "1")
    private Integer gameId;

    @NotBlank(message = "title은 필수입니다.")
    @Size(max = 255, message = "title은 255자 이하여야 합니다.")
    @Schema(example = "sample")
    private String title;

    @NotBlank(message = "content는 필수입니다.")
    @Size(max = 4000, message = "content는 4000자 이하여야 합니다.")
    @Schema(example = "sample")
    private String content;

    @NotNull(message = "rewards는 필수입니다.")
    @Schema(example = "[{\"itemId\":1,\"qty\":100}]")
    private JSONB rewards;

    @NotNull(message = "sendType은 필수입니다.")
    @Schema(example = "sample")
    private MailSendType sendType;

    @NotNull(message = "recipientsType is required")
    @Schema(example = "sample")
    private MailRecipientsType recipientsType;

    @NotNull(message = "expireAt은 필수입니다.")
    @Schema(example = "2026-03-18T09:00:00+09:00")
    private OffsetDateTime expireAt;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    @Schema(example = "admin")
    private String updatedBy;
}
