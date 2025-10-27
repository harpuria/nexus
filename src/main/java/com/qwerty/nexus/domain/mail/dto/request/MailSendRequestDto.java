package com.qwerty.nexus.domain.mail.dto.request;

import com.qwerty.nexus.domain.mail.MailTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MailSendRequestDto {
    @Schema(example = "1")
    private Long templateId;

    @Schema(example = "ALL")
    private MailTargetType targetType;

    @Schema(example = "[1,2,3]")
    private List<Long> recipientIds = new ArrayList<>();
}

