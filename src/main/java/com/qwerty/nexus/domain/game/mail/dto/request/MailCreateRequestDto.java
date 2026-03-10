package com.qwerty.nexus.domain.game.mail.dto.request;

import com.qwerty.nexus.domain.game.mail.MailRecipientsType;
import com.qwerty.nexus.domain.game.mail.MailSendType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "1")
    @NotNull(message = "gameId는 필수입니다.")
    @Positive(message = "gameId는 1 이상이어야 합니다.")
    private Integer gameId;

    @Schema(example = "오픈 기념 우편")
    @NotBlank(message = "title은 필수입니다.")
    @Size(max = 255, message = "title은 255자 이하여야 합니다.")
    private String title;

    @Schema(example = "게임 오픈을 축하합니다!")
    @NotBlank(message = "content는 필수입니다.")
    @Size(max = 4000, message = "content는 4000자 이하여야 합니다.")
    private String content;

    @Schema(example = "[{\"itemCode\":\"GOLD\",\"amount\":1000}]")
    @NotNull(message = "rewards는 필수입니다.")
    private JSONB rewards;

    @NotNull(message = "sendType은 필수입니다.")
    private MailSendType sendType;

    @NotNull(message = "recipientsType is required")
    private MailRecipientsType recipientsType;

    @Schema(example = "2026-12-31T23:59:59Z")
    @NotNull(message = "expireAt은 필수입니다.")
    private OffsetDateTime expireAt;

    @NotBlank(message = "createdBy는 필수입니다.")
    @Size(max = 64, message = "createdBy는 64자 이하여야 합니다.")
    private String createdBy;

    @NotBlank(message = "updatedBy는 필수입니다.")
    @Size(max = 64, message = "updatedBy는 64자 이하여야 합니다.")
    private String updatedBy;
}
