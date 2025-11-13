package com.qwerty.nexus.domain.mail.service;

import com.qwerty.nexus.domain.game.mail.MailTargetType;
import com.qwerty.nexus.domain.game.mail.UserMailStatus;
import com.qwerty.nexus.domain.game.mail.command.MailSendCommand;
import com.qwerty.nexus.domain.game.mail.command.MailTemplateCreateCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailDeleteCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailListCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailReadCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailReceiveCommand;
import com.qwerty.nexus.domain.game.mail.dto.response.MailSendResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailTemplateResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.game.mail.repository.MailTemplateRepository;
import com.qwerty.nexus.domain.game.mail.repository.UserMailRepository;
import com.qwerty.nexus.domain.game.mail.service.MailService;
import com.qwerty.nexus.domain.game.mail.service.UserMailService;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.response.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MailServiceTest {

    private MailTemplateRepository mailTemplateRepository;
    private UserMailRepository userMailRepository;
    private MailService mailService;
    private UserMailService userMailService;

    @BeforeEach
    void setUp() {
        mailTemplateRepository = new MailTemplateRepository();
        userMailRepository = new UserMailRepository();
        mailService = new MailService(mailTemplateRepository, userMailRepository);
        userMailService = new UserMailService(userMailRepository);
    }

    @Test
    @DisplayName("템플릿 생성 시 ID가 발급된다")
    void createTemplate() {
        MailTemplateCreateCommand command = new MailTemplateCreateCommand("안내", "내용", "REWARD");

        Result<MailTemplateResponseDto> result = mailService.createTemplate(command);

        assertThat(result).isInstanceOf(Result.Success.class);
        MailTemplateResponseDto responseDto = ((Result.Success<MailTemplateResponseDto>) result).data();
        assertAll(
                () -> assertThat(responseDto.getTemplateId()).isNotNull(),
                () -> assertThat(responseDto.getTitle()).isEqualTo("안내"),
                () -> assertThat(responseDto.getRewardItem()).isEqualTo("REWARD")
        );
    }

    @Test
    @DisplayName("메일 발송과 수신함 흐름을 처리한다")
    void sendAndUserFlow() {
        MailTemplateResponseDto template = ((Result.Success<MailTemplateResponseDto>) mailService.createTemplate(
                new MailTemplateCreateCommand("이벤트", "참여하세요", "GIFT")
        )).data();

        MailSendCommand sendCommand = new MailSendCommand(
                template.getTemplateId(),
                MailTargetType.ALL,
                List.of(10L, 20L)
        );

        Result<MailSendResponseDto> sendResult = mailService.send(sendCommand);
        assertThat(sendResult).isInstanceOf(Result.Success.class);
        MailSendResponseDto sendResponse = ((Result.Success<MailSendResponseDto>) sendResult).data();
        assertThat(sendResponse.getDispatchedCount()).isEqualTo(2);

        Result<List<UserMailResponseDto>> inboxResult = userMailService.getInbox(UserMailListCommand.of(10L));
        assertThat(inboxResult).isInstanceOf(Result.Success.class);
        List<UserMailResponseDto> inbox = ((Result.Success<List<UserMailResponseDto>>) inboxResult).data();
        assertThat(inbox).hasSize(1);
        UserMailResponseDto mail = inbox.getFirst();

        Result<Void> readResult = userMailService.markAsRead(UserMailReadCommand.of(mail.getUserMailId(), 10L));
        assertThat(readResult).isInstanceOf(Result.Success.class);

        Result<UserMailResponseDto> receiveResult = userMailService.receive(UserMailReceiveCommand.of(mail.getUserMailId(), 10L));
        assertThat(receiveResult).isInstanceOf(Result.Success.class);
        UserMailResponseDto received = ((Result.Success<UserMailResponseDto>) receiveResult).data();
        assertThat(received.getStatus()).isEqualTo(UserMailStatus.RECEIVED);
        assertThat(received.getRewardItem()).isEqualTo("GIFT");

        Result<Void> deleteResult = userMailService.delete(UserMailDeleteCommand.of(mail.getUserMailId(), 10L));
        assertThat(deleteResult).isInstanceOf(Result.Success.class);
        Result<List<UserMailResponseDto>> afterDelete = userMailService.getInbox(UserMailListCommand.of(10L));
        assertThat(((Result.Success<List<UserMailResponseDto>>) afterDelete).data()).isEmpty();
    }

    @Test
    @DisplayName("템플릿 목록을 최신순으로 조회한다")
    void listTemplates_defaultOrdering() {
        MailTemplateResponseDto first = ((Result.Success<MailTemplateResponseDto>) mailService.createTemplate(
                new MailTemplateCreateCommand("공지1", "내용1", null)
        )).data();
        MailTemplateResponseDto second = ((Result.Success<MailTemplateResponseDto>) mailService.createTemplate(
                new MailTemplateCreateCommand("공지2", "내용2", null)
        )).data();

        Result<List<MailTemplateResponseDto>> result = mailService.listTemplates(null);

        assertThat(result).isInstanceOf(Result.Success.class);
        List<MailTemplateResponseDto> templates = ((Result.Success<List<MailTemplateResponseDto>>) result).data();
        assertThat(templates).hasSize(2);
        assertThat(templates.getFirst().getTemplateId()).isEqualTo(second.getTemplateId());
        assertThat(templates.get(1).getTemplateId()).isEqualTo(first.getTemplateId());
    }

    @Test
    @DisplayName("템플릿 목록을 제목 오름차순으로 페이징한다")
    void listTemplates_withPaging() {
        mailService.createTemplate(new MailTemplateCreateCommand("C", "내용", null));
        mailService.createTemplate(new MailTemplateCreateCommand("A", "내용", null));
        mailService.createTemplate(new MailTemplateCreateCommand("B", "내용", null));

        PagingCommand pagingCommand = PagingCommand.builder()
                .page(0)
                .size(2)
                .sort("title")
                .direction("asc")
                .build();

        Result<List<MailTemplateResponseDto>> result = mailService.listTemplates(pagingCommand);

        assertThat(result).isInstanceOf(Result.Success.class);
        List<MailTemplateResponseDto> templates = ((Result.Success<List<MailTemplateResponseDto>>) result).data();
        assertThat(templates).hasSize(2);
        assertThat(templates.get(0).getTitle()).isEqualTo("A");
        assertThat(templates.get(1).getTitle()).isEqualTo("B");
    }
}

