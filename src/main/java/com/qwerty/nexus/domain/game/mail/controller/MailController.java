package com.qwerty.nexus.domain.game.mail.controller;

import com.qwerty.nexus.domain.game.mail.command.MailSendCommand;
import com.qwerty.nexus.domain.game.mail.command.MailTemplateCreateCommand;
import com.qwerty.nexus.domain.game.mail.dto.request.MailSendRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.MailTemplateCreateRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailSendResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailTemplateResponseDto;
import com.qwerty.nexus.domain.game.mail.service.MailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.command.PagingCommand;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO : 메일 관련 작업
 * 메일 관련된 작업은 Codex 에서 생성한 상태임.
 * 다른 작업들 한 뒤에 추후 세세한 부분 수정할 필요 있음.
 */

@RestController
@RequestMapping(ApiConstants.Path.MAIL_PATH)
@RequiredArgsConstructor
@Tag(name = "메일", description = "템플릿 및 발송 API")
public class MailController {

    private final MailService mailService;

    @GetMapping("/templates")
    @Operation(summary = "메일 템플릿 목록 조회")
    public ResponseEntity<ApiResponse<List<MailTemplateResponseDto>>> listTemplates(
            PagingRequestDto pagingRequestDto
    ) {
        PagingCommand command = pagingRequestDto == null ? null : PagingCommand.from(pagingRequestDto);
        Result<List<MailTemplateResponseDto>> result = mailService.listTemplates(command);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "메일 템플릿 생성")
    public ResponseEntity<ApiResponse<MailTemplateResponseDto>> createTemplate(
            @RequestBody MailTemplateCreateRequestDto dto
    ) {
        Result<MailTemplateResponseDto> result = mailService.createTemplate(MailTemplateCreateCommand.from(dto));
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.CREATED);
    }

    @PostMapping("/send")
    @Operation(summary = "메일 발송")
    public ResponseEntity<ApiResponse<MailSendResponseDto>> sendMail(
            @RequestBody MailSendRequestDto dto
    ) {
        Result<MailSendResponseDto> result = mailService.send(MailSendCommand.from(dto));
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}

