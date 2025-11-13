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
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public Result<List<MailTemplateResponseDto>> listTemplates(PagingCommand command) {
        List<MailTemplateEntity> templates = new ArrayList<>(mailTemplateRepository.findAll());

        Comparator<MailTemplateEntity> comparator = resolveComparator(command);
        if (comparator != null) {
            templates.sort(comparator);
        }

        List<MailTemplateEntity> paged = applyPaging(templates, command);
        List<MailTemplateResponseDto> response = paged.stream()
                .map(MailTemplateResponseDto::from)
                .toList();

        return Result.Success.of(response, ApiConstants.Messages.Success.RETRIEVED);
    }

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

    private Comparator<MailTemplateEntity> resolveComparator(PagingCommand command) {
        String sortField = command != null && StringUtils.hasText(command.getSort())
                ? command.getSort()
                : ApiConstants.Pagination.DEFAULT_SORT_FIELD;

        Comparator<MailTemplateEntity> comparator = switch (sortField) {
            case "title" -> Comparator.comparing(MailTemplateEntity::title, Comparator.nullsLast(String::compareToIgnoreCase));
            case "templateId" -> Comparator.comparing(MailTemplateEntity::templateId, Comparator.nullsLast(Long::compareTo));
            case "createdAt" -> Comparator.comparing(MailTemplateEntity::createdAt, Comparator.nullsLast(OffsetDateTime::compareTo));
            default -> Comparator.comparing(MailTemplateEntity::createdAt, Comparator.nullsLast(OffsetDateTime::compareTo));
        };

        String direction = command != null && StringUtils.hasText(command.getDirection())
                ? command.getDirection()
                : ApiConstants.Pagination.DEFAULT_SORT_DIRECTION;
        if (ApiConstants.Pagination.SORT_DESC.equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private List<MailTemplateEntity> applyPaging(List<MailTemplateEntity> templates, PagingCommand command) {
        if (CollectionUtils.isEmpty(templates)) {
            return List.of();
        }

        int page = command != null && command.getPage() >= 0
                ? command.getPage()
                : ApiConstants.Pagination.DEFAULT_PAGE_NUMBER;

        int size;
        if (command == null) {
            size = templates.size();
        } else {
            int requestedSize = command.getSize();
            size = requestedSize <= 0
                    ? ApiConstants.Pagination.DEFAULT_PAGE_SIZE
                    : Math.min(requestedSize, ApiConstants.Pagination.MAX_PAGE_SIZE);
        }

        int fromIndex = Math.min(page * size, templates.size());
        int toIndex = Math.min(fromIndex + size, templates.size());
        return templates.subList(fromIndex, toIndex);
    }
}

