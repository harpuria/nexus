package com.qwerty.nexus.domain.mail.controller;

import com.qwerty.nexus.domain.mail.command.MailSendCommand;
import com.qwerty.nexus.domain.mail.command.MailTemplateCreateCommand;
import com.qwerty.nexus.domain.mail.dto.request.MailSendRequestDto;
import com.qwerty.nexus.domain.mail.dto.request.MailTemplateCreateRequestDto;
import com.qwerty.nexus.domain.mail.dto.response.MailSendResponseDto;
import com.qwerty.nexus.domain.mail.dto.response.MailTemplateResponseDto;
import com.qwerty.nexus.domain.mail.service.MailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.Path.MAIL_PATH)
@RequiredArgsConstructor
@Tag(name = "메일", description = "템플릿 및 발송 API")
public class MailController {

    private final MailService mailService;

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

