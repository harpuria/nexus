package com.qwerty.nexus.domain.game.mail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailTemplateCreateRequestDto {
    @Schema(example = "신규 이벤트 안내")
    private String title;

    @Schema(example = "신규 이벤트 참여 보상을 확인하세요.")
    private String content;

    @Schema(example = "EVENT_GIFT_BOX")
    private String rewardItem;
}

