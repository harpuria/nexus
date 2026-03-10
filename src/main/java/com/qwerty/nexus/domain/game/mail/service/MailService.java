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
    public Result<Void> sendMail(int mailId, List<Integer> userIds) {
        // TODO : 우편 발송 (즉시 - 이거는 API 호출 방식)
        // TODO : 우편 발송 (스케쥴러 - 이거는 별도의 스케쥴러를 만들어서 등록해야 함)
        /*
            1. ALL 인 경우 (전체 유저)
            2. USER 인 경우 (특정 유저)

            1. mailId 가져옴 -> 어떤 메일 보낼 건지 메일 정보 불러옴
            2. 확인해보니...
                2.1 전체메일임(ALL) -> 해당 게임의 삭제, 탈퇴된 유저 제외하고 모두 보내기 (정지는 보내야하나?)
                2.2 개별메일임(USER) -> 해당 유저에게만 보냄 (누구한테 보낼건지는 웹콘솔 등에서 처리하면 될듯)
            3. 템플릿 문자열 {} 변환 처리 (템플릿 문자열은 몇가지 미리 정해놓은 방식을 사용하게 함. ex) nickname, ranking, level, stage 등
                3.1 템플릿 문자열이 하나도 없으면 -> 그냥 그대로 보내면 됨
                3.2 템플릿 문자열이 하나라도 있으면 -> {} 이름에 알맞게 치환 처리
         */

        // 메일 내용을 가져오고
        Optional<MailEntity> mailEntity = mailRepository.findByMailId(mailId);
        if (mailEntity.isEmpty()) {
            return Result.Failure.of("우편 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND.getCode());
        }

        UserMailEntity userMailEntity = UserMailEntity.builder()
                .mailId(mailId)
                .title(mailEntity.get().getTitle())
                .content(mailEntity.get().getContent())
                .rewards(mailEntity.get().getRewards())
                .build();

        if(mailEntity.get().getRecipientsType() == MailRecipientsType.ALL){
            // 전체 대상 (ALL)
            // 보낼 대상 전체 유저부터 조회 (정지, 탈퇴, 삭제 된 유저는 제외. 활성화된 유저라고 하면 되겠군)
            // 해당 유저의 id(pk)를 리스트로 묶어서 메일 보내기 처리
            GameUserEntity gameUserEntity = GameUserEntity.builder().gameId(mailEntity.get().getGameId()).build();
            List<Integer> allUserIds = gameUserRepository.findAllUserIdsByGameId(gameUserEntity);

            userMailRepository.insertUserMail(userMailEntity, allUserIds);

        }else{
            // 사용자 대상 (USER)
            List<Integer> insertRst = userMailRepository.insertUserMail(userMailEntity, userIds);
        }

        return Result.Success.of(null, "메일 보내기 성공!");
    }
}
