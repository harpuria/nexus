package com.qwerty.nexus.domain.game.mail.controller;

import com.qwerty.nexus.domain.game.mail.command.UserMailDeleteCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailListCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailReadCommand;
import com.qwerty.nexus.domain.game.mail.command.UserMailReceiveCommand;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.game.mail.service.UserMailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.Path.USER_MAIL_PATH)
@RequiredArgsConstructor
@Tag(name = "유저 메일", description = "수신함 및 상태 변경 API")
public class UserMailController {

    private final UserMailService userMailService;

    @GetMapping
    @Operation(summary = "유저 메일 목록 조회")
    public ResponseEntity<ApiResponse<List<UserMailResponseDto>>> getInbox(
            @RequestParam("userId") Long userId
    ) {
        Result<List<UserMailResponseDto>> result = userMailService.getInbox(UserMailListCommand.of(userId));
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @PatchMapping("/{userMailId}/read")
    @Operation(summary = "메일 읽음 처리")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable("userMailId") Long userMailId,
            @RequestParam("userId") Long userId
    ) {
        Result<Void> result = userMailService.markAsRead(UserMailReadCommand.of(userMailId, userId));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @PatchMapping("/{userMailId}/receive")
    @Operation(summary = "메일 수령 및 보상 지급")
    public ResponseEntity<ApiResponse<UserMailResponseDto>> receive(
            @PathVariable("userMailId") Long userMailId,
            @RequestParam("userId") Long userId
    ) {
        Result<UserMailResponseDto> result = userMailService.receive(UserMailReceiveCommand.of(userMailId, userId));
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @DeleteMapping("/{userMailId}")
    @Operation(summary = "메일 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("userMailId") Long userMailId,
            @RequestParam("userId") Long userId
    ) {
        Result<Void> result = userMailService.delete(UserMailDeleteCommand.of(userMailId, userId));
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}

