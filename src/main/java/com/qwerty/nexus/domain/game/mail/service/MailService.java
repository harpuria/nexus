package com.qwerty.nexus.domain.game.mail.service;

import com.qwerty.nexus.domain.game.mail.MailRecipientsType;
import com.qwerty.nexus.domain.game.mail.dto.request.MailCreateRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.MailUpdateRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailListResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailResponseDto;
import com.qwerty.nexus.domain.game.mail.entity.MailEntity;
import com.qwerty.nexus.domain.game.mail.repository.MailRepository;
import com.qwerty.nexus.domain.game.mail.entity.UserMailEntity;
import com.qwerty.nexus.domain.game.mail.repository.UserMailRepository;
import com.qwerty.nexus.domain.game.user.entity.GameUserEntity;
import com.qwerty.nexus.domain.game.user.repository.GameUserRepository;
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
    private final UserMailRepository userMailRepository;
    private final GameUserRepository gameUserRepository;

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
                .sendType(requestDto.getSendType())
                .recipientsType(requestDto.getRecipientsType())
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
                .sendType(requestDto.getSendType())
                .recipientsType(requestDto.getRecipientsType())
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

    /**
     * 우편 발송 (즉시)
     * @param mailId
     * @return
     */
    @Transactional
    public Result<Void> sendMail(int mailId, List<Integer> userIds) {
        // TODO : 우편 발송 (스케쥴러 - 이거는 별도의 스케쥴러를 만들어서 등록해야 함. 스케쥴러는 mail 을 select 하면서 시간되는 놈 보내는거로?)

        // 메일 내용 가져오기
        Optional<MailEntity> mailEntity = mailRepository.findByMailId(mailId);
        if (mailEntity.isEmpty()) {
            return Result.Failure.of("우편 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        // TODO : 이 시점에서 템플릿 문자열 {nickname} 같은거 있으면 치환하는 작업하면 될거 같음.
        // TODO : 템플릿 문자열은 몇 가지 미리 정해놓은 방식을 사용할거임 (nickname, ranking, level, stage 등...)
        UserMailEntity userMailEntity = UserMailEntity.builder()
                .gameId(mailEntity.get().getGameId())
                .mailId(mailId)
                .title(mailEntity.get().getTitle())
                .content(mailEntity.get().getContent())
                .rewards(mailEntity.get().getRewards())
                .expireAt(mailEntity.get().getExpireAt())
                .createdBy("NEXUS_SYSTEM")
                .updatedBy("NEXUS_SYSTEM")
                .build();

        if(mailEntity.get().getRecipientsType() == MailRecipientsType.ALL){
            // 전체 대상 (ALL) - 활성화된 유저(정지, 탈퇴, 삭제 유저 제외)에게만 발송
            GameUserEntity gameUserEntity = GameUserEntity.builder().gameId(mailEntity.get().getGameId()).build();
            List<Integer> allUserIds = gameUserRepository.findAllUserIdsByGameId(gameUserEntity, ApiConstants.Domain.MAIL);

            List<Integer> insertRst = userMailRepository.insertUserMail(userMailEntity, allUserIds);
            if(allUserIds.size() != insertRst.size()){
               return Result.Failure.of("전체 유저에게 우편 보내기 실패", ErrorCode.INTERNAL_ERROR.getCode());
            }
        }else{
            // 사용자 대상 (USER)
            List<Integer> insertRst = userMailRepository.insertUserMail(userMailEntity, userIds);
            if(userIds.size() != insertRst.size()){
                return Result.Failure.of("유저에게 우편 보내기 실패", ErrorCode.INTERNAL_ERROR.getCode());
            }
        }

        return Result.Success.of(null, "우편 보내기 성공!");
    }
}
