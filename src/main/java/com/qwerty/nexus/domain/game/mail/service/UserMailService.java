package com.qwerty.nexus.domain.game.mail.service;

import com.qwerty.nexus.domain.game.mail.dto.request.UserMailActionRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.UserMailBulkReceiveRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.UserMailDeleteRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailListResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import com.qwerty.nexus.domain.game.mail.repository.UserMailRepository;
import com.qwerty.nexus.domain.game.reward.service.RewardService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.PagingEntity;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.paging.PagingUtil;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserMailService {
    private final UserMailRepository userMailRepository;
    private final RewardService rewardService;

    public Result<UserMailListResponseDto> listUserMail(PagingRequestDto dto, Integer userId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(dto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        List<UserMailResponseDto> userMails = userMailRepository.findAllByUserId(pagingEntity, userId)
                .stream()
                .map(UserMailResponseDto::from)
                .toList();

        long totalCount = userMailRepository.countByUserId(userId);
        int totalPages = pagingEntity.getSize() == 0 ? 0 : (int) Math.ceil((double) totalCount / pagingEntity.getSize());

        return Result.Success.of(UserMailListResponseDto.builder()
                .userMails(userMails)
                .page(pagingEntity.getPage())
                .size(pagingEntity.getSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(pagingEntity.getPage() + 1 < totalPages)
                .hasPrevious(pagingEntity.getPage() > 0 && totalPages > 0)
                .build(), ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional
    public Result<UserMailResponseDto> getUserMail(UserMailActionRequestDto dto) {
        Optional<UserMailEntity> userMailOptional = userMailRepository.findByUserMailId(dto.getUserMailId());
        if (userMailOptional.isEmpty()) {
            return Result.Failure.of("유저 우편 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        UserMailEntity updateEntity = UserMailEntity.builder()
                .userMailId(dto.getUserMailId())
                .isRead("Y")
                .readAt(OffsetDateTime.now())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateCount = userMailRepository.updateUserMail(updateEntity);
        if (updateCount == 0) {
            return Result.Failure.of("유저 우편 읽음 처리 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        Optional<UserMailEntity> updatedUserMail = userMailRepository.findByUserMailId(dto.getUserMailId());
        if (updatedUserMail.isEmpty()) {
            return Result.Failure.of("유저 우편 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(UserMailResponseDto.from(updatedUserMail.get()), ApiConstants.Messages.Success.RETRIEVED);
    }

    @Transactional
    public Result<Void> receiveUserMail(UserMailActionRequestDto dto) {
        Optional<UserMailEntity> userMailOptional = userMailRepository.findByUserMailId(dto.getUserMailId());
        if (userMailOptional.isEmpty()) {
            return Result.Failure.of("유저 우편 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        UserMailEntity userMailEntity = userMailOptional.get();
        if ("Y".equals(userMailEntity.getIsReceived())) {
            return Result.Failure.of("이미 보상을 수령한 우편입니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        UserMailEntity updateEntity = UserMailEntity.builder()
                .userMailId(dto.getUserMailId())
                .isRead("Y")
                .readAt(userMailEntity.getReadAt() != null ? userMailEntity.getReadAt() : OffsetDateTime.now())
                .isReceived("Y")
                .receivedAt(OffsetDateTime.now())
                .updatedBy(dto.getUpdatedBy())
                .build();

        // 보상 지급 처리


        int updateCount = userMailRepository.updateUserMail(updateEntity);
        if (updateCount > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
        }

        return Result.Failure.of("유저 우편 보상 수령 처리 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    @Transactional
    public Result<Void> receiveAllUserMail(UserMailBulkReceiveRequestDto dto) {
        List<UserMailEntity> notReceivedMails = userMailRepository.findAllByUserIdAndIsReceived(dto.getUserId(), "N");
        if (notReceivedMails.isEmpty()) {
            return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
        }

        for (UserMailEntity notReceivedMail : notReceivedMails) {
            UserMailEntity updateEntity = UserMailEntity.builder()
                    .userMailId(notReceivedMail.getUserMailId())
                    .isRead("Y")
                    .readAt(notReceivedMail.getReadAt() != null ? notReceivedMail.getReadAt() : OffsetDateTime.now())
                    .isReceived("Y")
                    .receivedAt(OffsetDateTime.now())
                    .updatedBy(dto.getUpdatedBy())
                    .build();

            int updateCount = userMailRepository.updateUserMail(updateEntity);
            if (updateCount == 0) {
                return Result.Failure.of("유저 우편 전체 보상 수령 처리 실패.", ErrorCode.INTERNAL_ERROR.getCode());
            }
        }

        return Result.Success.of(null, ApiConstants.Messages.Success.PROCESSED);
    }

    @Transactional
    public Result<Void> deleteUserMail(UserMailDeleteRequestDto dto) {
        UserMailEntity updateEntity = UserMailEntity.builder()
                .userMailId(dto.getUserMailId())
                .updatedBy(dto.getUpdatedBy())
                .isDel(dto.getIsDel())
                .build();

        int updateCount = userMailRepository.updateUserMail(updateEntity);
        if (updateCount > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Failure.of("유저 우편 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }
}
