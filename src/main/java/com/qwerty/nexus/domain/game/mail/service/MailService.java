package com.qwerty.nexus.domain.game.mail.service;

import com.qwerty.nexus.domain.game.mail.MailTargetType;
import com.qwerty.nexus.domain.game.mail.UserMailStatus;
import com.qwerty.nexus.domain.game.mail.command.MailSendCommand;
import com.qwerty.nexus.domain.game.mail.command.MailTemplateCreateCommand;
import com.qwerty.nexus.domain.game.mail.dto.response.MailSendResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailTemplateResponseDto;
import com.qwerty.nexus.domain.game.mail.entity.MailTemplateEntity;
import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import com.qwerty.nexus.domain.game.mail.repository.MailTemplateRepository;
import com.qwerty.nexus.domain.game.mail.repository.UserMailRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailTemplateRepository mailTemplateRepository;
    private final UserMailRepository userMailRepository;

    public Result<MailTemplateResponseDto> createTemplate(MailTemplateCreateCommand command) {
        if (!StringUtils.hasText(command.title())) {
            return Result.Failure.of("제목은 필수입니다.", ErrorCode.MISSING_REQUIRED_FIELD.getCode());
        }

        if (!StringUtils.hasText(command.content())) {
            return Result.Failure.of("내용은 필수입니다.", ErrorCode.MISSING_REQUIRED_FIELD.getCode());
        }

        MailTemplateEntity entity = MailTemplateEntity.builder()
                .templateId(null)
                .title(command.title())
                .content(command.content())
                .rewardItem(command.rewardItem())
                .createdAt(null)
                .build();

        MailTemplateEntity saved = mailTemplateRepository.save(entity);
        return Result.Success.of(
                MailTemplateResponseDto.from(saved),
                ApiConstants.Messages.Success.CREATED
        );
    }

    public Result<MailSendResponseDto> send(MailSendCommand command) {
        if (command.templateId() == null) {
            return Result.Failure.of("템플릿 ID는 필수입니다.", ErrorCode.MISSING_REQUIRED_FIELD.getCode());
        }

        Optional<MailTemplateEntity> optionalTemplate = mailTemplateRepository.findById(command.templateId());
        if (optionalTemplate.isEmpty()) {
            return Result.Failure.of("템플릿을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        MailTargetType targetType = Objects.requireNonNullElse(command.targetType(), MailTargetType.INDIVIDUAL);
        List<Long> recipients = command.recipientIds();
        if (CollectionUtils.isEmpty(recipients)) {
            String message = switch (targetType) {
                case ALL -> "전체 발송을 위해 대상 사용자가 필요합니다.";
                case GROUP -> "그룹 발송을 위해 대상 사용자가 필요합니다.";
                case INDIVIDUAL -> "개별 발송을 위해 대상 사용자가 필요합니다.";
            };
            return Result.Failure.of(message, ErrorCode.MISSING_REQUIRED_FIELD.getCode());
        }

        Set<Long> uniqueRecipients = new LinkedHashSet<>(recipients);
        MailTemplateEntity template = optionalTemplate.get();

        uniqueRecipients.stream()
                .filter(Objects::nonNull)
                .forEach(userId -> {
                    UserMailEntity entity = UserMailEntity.builder()
                            .userMailId(null)
                            .templateId(template.templateId())
                            .userId(userId)
                            .title(template.title())
                            .content(template.content())
                            .rewardItem(template.rewardItem())
                            .status(UserMailStatus.UNREAD)
                            .createdAt(null)
                            .readAt(null)
                            .receivedAt(null)
                            .build();
                    userMailRepository.save(entity);
                });

        MailSendResponseDto responseDto = MailSendResponseDto.builder()
                .templateId(template.templateId())
                .dispatchedCount(uniqueRecipients.size())
                .recipientIds(uniqueRecipients)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.PROCESSED);
    }
}

