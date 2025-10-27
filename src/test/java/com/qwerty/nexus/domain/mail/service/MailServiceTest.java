package com.qwerty.nexus.domain.mail.service;

import com.qwerty.nexus.domain.mail.MailTargetType;
import com.qwerty.nexus.domain.mail.UserMailStatus;
import com.qwerty.nexus.domain.mail.command.MailSendCommand;
import com.qwerty.nexus.domain.mail.command.MailTemplateCreateCommand;
import com.qwerty.nexus.domain.mail.command.UserMailDeleteCommand;
import com.qwerty.nexus.domain.mail.command.UserMailListCommand;
import com.qwerty.nexus.domain.mail.command.UserMailReadCommand;
import com.qwerty.nexus.domain.mail.command.UserMailReceiveCommand;
import com.qwerty.nexus.domain.mail.dto.response.MailSendResponseDto;
import com.qwerty.nexus.domain.mail.dto.response.MailTemplateResponseDto;
import com.qwerty.nexus.domain.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.mail.repository.MailTemplateRepository;
import com.qwerty.nexus.domain.mail.repository.UserMailRepository;
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
}

