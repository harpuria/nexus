package com.qwerty.nexus.domain.game.data.mail.service;

import com.qwerty.nexus.domain.game.data.mail.dto.request.MailCreateRequestDto;
import com.qwerty.nexus.domain.game.data.mail.dto.request.MailUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.mail.dto.response.MailListResponseDto;
import com.qwerty.nexus.domain.game.data.mail.dto.response.MailResponseDto;
import com.qwerty.nexus.domain.game.data.mail.entity.MailEntity;
import com.qwerty.nexus.domain.game.data.mail.repository.MailRepository;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.paging.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.paging.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;

    /**
     * 우편 생성
     * @param requestDto
     * @return
     */
    @Transactional
    public Result<Void> createMail(MailCreateRequestDto requestDto) {
        MailEntity entity = MailEntity.builder()
                .gameId(requestDto.getGameId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .rewards(requestDto.getRewards())
                .sendType(requestDto.getSendType().name())
                .recipientsType(requestDto.getRecipientsType().name())
                .expireAt(requestDto.getExpireAt())
                .createdBy(requestDto.getCreatedBy())
                .updatedBy(requestDto.getUpdatedBy())
                .build();

        Integer createdMailId = mailRepository.insertMail(entity);
        if (createdMailId == null) {
            return Result.Failure.of("우편 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // WHY: 우편 발송(ALL/USER 대상 분배) 및 보상 지급 로직은 추후 UserMail 도메인과 함께 확장합니다.
        return Result.Success.of(null, ApiConstants.Messages.Success.CREATED);
    }

    /**
     * 우편 수정
     * @param requestDto
     * @return
     */
    @Transactional
    public Result<Void> updateMail(MailUpdateRequestDto requestDto) {
        MailEntity entity = MailEntity.builder()
                .mailId(requestDto.getMailId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .rewards(requestDto.getRewards())
                .sendType(requestDto.getSendType() != null ? requestDto.getSendType().name() : null)
                .recipientsType(requestDto.getRecipientsType() != null ? requestDto.getRecipientsType().name() : null)
                .expireAt(requestDto.getExpireAt())
                .updatedBy(requestDto.getUpdatedBy())
                .isDel(requestDto.getIsDel())
                .build();

        int updateCount = mailRepository.updateMail(entity);
        if (updateCount > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.UPDATED);
        }

        return Result.Failure.of("우편 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 우편 삭제
     * @param mailId
     * @param updatedBy
     * @return
     */
    @Transactional
    public Result<Void> deleteMail(Integer mailId, String updatedBy) {
        MailEntity entity = MailEntity.builder()
                .mailId(mailId)
                .updatedBy(updatedBy)
                .isDel("Y")
                .build();

        int updateCount = mailRepository.updateMail(entity);
        if (updateCount > 0) {
            return Result.Success.of(null, ApiConstants.Messages.Success.DELETED);
        }

        return Result.Failure.of("우편 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
    }

    /**
     * 우편 단건 조회
     * @param mailId
     * @return
     */
    public Result<MailResponseDto> getMail(Integer mailId) {
        Optional<MailEntity> mailEntity = mailRepository.findByMailId(mailId);
        if (mailEntity.isEmpty()) {
            return Result.Failure.of("우편 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        return Result.Success.of(MailResponseDto.from(mailEntity.get()), ApiConstants.Messages.Success.RETRIEVED);
    }

    /**
     * 우편 목록 조회
     * @param pagingRequestDto
     * @param gameId
     * @return
     */
    public Result<MailListResponseDto> listMails(PagingRequestDto pagingRequestDto, Integer gameId) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingRequestDto);
        if (pagingEntity == null) {
            return Result.Failure.of("페이징 정보가 올바르지 않습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        List<MailResponseDto> mails = mailRepository.findAllByGameIdAndKeyword(pagingEntity, gameId)
                .stream()
                .map(MailResponseDto::from)
                .toList();

        long totalCount = mailRepository.countByGameIdAndKeyword(pagingEntity, gameId);
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        MailListResponseDto responseDto = MailListResponseDto.builder()
                .mails(mails)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(responseDto, ApiConstants.Messages.Success.RETRIEVED);
    }
}
