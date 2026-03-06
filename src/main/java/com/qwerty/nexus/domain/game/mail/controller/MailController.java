package com.qwerty.nexus.domain.game.mail.controller;

import com.qwerty.nexus.domain.game.mail.dto.request.MailCreateRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.MailUpdateRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailListResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.MailResponseDto;
import com.qwerty.nexus.domain.game.mail.service.MailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
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

    /**
     * 우편 생성
     * @param requestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "우편 생성")
    public ResponseEntity<ApiResponse<Void>> createMail(@Valid @RequestBody MailCreateRequestDto requestDto) {
        Result<Void> result = mailService.createMail(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 우편 수정
     * @param mailId
     * @param requestDto
     * @return
     */
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

    /**
     * 우편 삭제
     * @param mailId
     * @param updatedBy
     * @return
     */
    @DeleteMapping("/{mailId}")
    @Operation(summary = "우편 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteMail(
            @PathVariable("mailId") Integer mailId,
            @RequestParam @NotBlank(message = "updatedBy는 필수입니다.") String updatedBy
    ) {
        Result<Void> result = mailService.deleteMail(mailId, updatedBy);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 우편 단건 조회
     * @param mailId
     * @return
     */
    @GetMapping("/{mailId}")
    @Operation(summary = "우편 단건 조회")
    public ResponseEntity<ApiResponse<MailResponseDto>> getMail(@PathVariable("mailId") Integer mailId) {
        Result<MailResponseDto> result = mailService.getMail(mailId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 우편 목록 조회
     * @param gameId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @param keyword
     * @return
     */
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

    /**
     * 우편 발송 (즉시)
     * @return
     */
    @PostMapping("/send")
    @Operation(summary = "우편 발송")
    public ResponseEntity<ApiResponse<Void>> sendMail(){
        // TODO : 우편 발송 (즉시 - 이거는 API 호출 방식)
        /*
            1. mailId 가져옴 -> 어떤 메일 보낼 건지 메일 정보 불러옴
            2. 확인해보니...
                2.1 전체메일임(ALL) -> 해당 게임의 삭제, 탈퇴된 유저 제외하고 모두 보내기 (정지는 보내야하나?)
                2.2 개별메일임(USER) -> 해당 유저에게만 보냄 (누구한테 보낼건지는 웹콘솔 등에서 처리하면 될듯)
            3. 템플릿 문자열 {} 변환 처리 (템플릿 문자열은 몇가지 미리 정해놓은 방식을 사용하게 함. ex) nickname, ranking, level, stage 등
                3.1 템플릿 문자열이 하나도 없으면 -> 그냥 그대로 보내면 됨
                3.2 템플릿 문자열이 하나라도 있으면 -> {} 이름에 알맞게 치환 처리
         */
        return ResponseEntityUtils.toResponseEntityVoid(null, HttpStatus.OK);
    }

    // TODO : 우편 발송 (스케쥴러 - 이거는 별도의 스케쥴러를 만들어서 등록해야 함)
}
