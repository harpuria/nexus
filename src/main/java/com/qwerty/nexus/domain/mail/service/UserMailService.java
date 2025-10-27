package com.qwerty.nexus.domain.mail.service;

import com.qwerty.nexus.domain.mail.UserMailStatus;
import com.qwerty.nexus.domain.mail.command.UserMailDeleteCommand;
import com.qwerty.nexus.domain.mail.command.UserMailListCommand;
import com.qwerty.nexus.domain.mail.command.UserMailReadCommand;
import com.qwerty.nexus.domain.mail.command.UserMailReceiveCommand;
import com.qwerty.nexus.domain.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.mail.entity.UserMailEntity;
import com.qwerty.nexus.domain.mail.repository.UserMailRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMailService {

    private final UserMailRepository userMailRepository;

    public Result<List<UserMailResponseDto>> getInbox(UserMailListCommand command) {
        if (command.userId() == null) {
            return Result.Failure.of("사용자 ID는 필수입니다.", ErrorCode.MISSING_REQUIRED_FIELD.getCode());
        }

        List<UserMailResponseDto> mails = userMailRepository.findByUserId(command.userId())
                .stream()
                .map(UserMailResponseDto::from)
                .toList();

        return Result.Success.of(mails, ApiConstants.Messages.Success.RETRIEVED);
    }

    public Result<Void> markAsRead(UserMailReadCommand command) {
        Optional<UserMailEntity> optionalMail = findOwnedMail(command.userMailId(), command.userId());
        if (optionalMail.isEmpty()) {
            return Result.Failure.of("메일을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        UserMailEntity mail = optionalMail.get();
        if (mail.isReceived()) {
            return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
        }

        if (mail.isUnread()) {
            UserMailEntity updated = mail.toBuilder()
                    .status(UserMailStatus.READ)
                    .readAt(OffsetDateTime.now())
                    .build();
            userMailRepository.update(updated);
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
    }

    public Result<UserMailResponseDto> receive(UserMailReceiveCommand command) {
        Optional<UserMailEntity> optionalMail = findOwnedMail(command.userMailId(), command.userId());
        if (optionalMail.isEmpty()) {
            return Result.Failure.of("메일을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        UserMailEntity mail = optionalMail.get();
        if (mail.isReceived()) {
            return Result.Success.of(UserMailResponseDto.from(mail), ApiConstants.Messages.Success.PROCESSED);
        }

        OffsetDateTime now = OffsetDateTime.now();
        UserMailStatus nextStatus = UserMailStatus.RECEIVED;
        OffsetDateTime readAt = mail.readAt() != null ? mail.readAt() : now;

        UserMailEntity updated = mail.toBuilder()
                .status(nextStatus)
                .readAt(readAt)
                .receivedAt(now)
                .build();
        userMailRepository.update(updated);

        return Result.Success.of(UserMailResponseDto.from(updated), ApiConstants.Messages.Success.PROCESSED);
    }

    public Result<Void> delete(UserMailDeleteCommand command) {
        Optional<UserMailEntity> optionalMail = findOwnedMail(command.userMailId(), command.userId());
        if (optionalMail.isEmpty()) {
            return Result.Failure.of("메일을 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        userMailRepository.delete(command.userMailId());
        return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
    }

    private Optional<UserMailEntity> findOwnedMail(Long userMailId, Long userId) {
        if (userMailId == null || userId == null) {
            return Optional.empty();
        }
        return userMailRepository.findById(userMailId)
                .filter(mail -> mail.userId().equals(userId));
    }
}

