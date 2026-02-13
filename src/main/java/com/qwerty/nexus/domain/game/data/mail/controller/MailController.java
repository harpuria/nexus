package com.qwerty.nexus.domain.game.data.mail.controller;

import com.qwerty.nexus.domain.game.data.mail.dto.request.MailCreateRequestDto;
import com.qwerty.nexus.domain.game.data.mail.dto.request.MailUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.mail.dto.response.MailListResponseDto;
import com.qwerty.nexus.domain.game.data.mail.dto.response.MailResponseDto;
import com.qwerty.nexus.domain.game.data.mail.service.MailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(ApiConstants.Path.MAIL_PATH)
@Tag(name = "우편", description = "우편 기본 API")
public class MailController {
    private final MailService mailService;

    @PostMapping
    @Operation(summary = "우편 생성")
    public ResponseEntity<ApiResponse<Void>> createMail(@Valid @RequestBody MailCreateRequestDto requestDto) {
        Result<Void> result = mailService.createMail(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{mailId}")
    @Operation(summary = "우편 수정")
    public ResponseEntity<ApiResponse<Void>> updateMail(
            @PathVariable("mailId") Integer mailId,
            @Valid @RequestBody MailUpdateRequestDto requestDto
    ) {
        requestDto.setMailId(mailId);
        Result<Void> result = mailService.updateMail(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @DeleteMapping("/{mailId}")
    @Operation(summary = "우편 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteMail(
            @PathVariable("mailId") Integer mailId,
            @RequestParam @NotBlank(message = "updatedBy는 필수입니다.") String updatedBy
    ) {
        Result<Void> result = mailService.deleteMail(mailId, updatedBy);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @GetMapping("/{mailId}")
    @Operation(summary = "우편 단건 조회")
    public ResponseEntity<ApiResponse<MailResponseDto>> getMail(@PathVariable("mailId") Integer mailId) {
        Result<MailResponseDto> result = mailService.getMail(mailId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/list/{gameId}")
    @Operation(summary = "우편 목록 조회")
    public ResponseEntity<ApiResponse<MailListResponseDto>> listMails(
            @PathVariable("gameId") Integer gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction,
            @RequestParam(required = false) String keyword
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);
        pagingRequestDto.setKeyword(keyword);

        Result<MailListResponseDto> result = mailService.listMails(pagingRequestDto, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
