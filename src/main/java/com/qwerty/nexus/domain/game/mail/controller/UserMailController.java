package com.qwerty.nexus.domain.game.mail.controller;

import com.qwerty.nexus.domain.game.mail.dto.request.UserMailActionRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.UserMailBulkReceiveRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.request.UserMailDeleteRequestDto;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailListResponseDto;
import com.qwerty.nexus.domain.game.mail.dto.response.UserMailResponseDto;
import com.qwerty.nexus.domain.game.mail.service.UserMailService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.Path.USER_MAIL_PATH)
@Tag(name = "유저 우편", description = "유저 우편함 API")
public class UserMailController {
    private final UserMailService userMailService;

    /**
     * 우편함 열기 (우편 목록)
     * @param userId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @return
     */
    @GetMapping("/list/{userId}")
    @Operation(summary = "유저 우편 목록 조회")
    public ResponseEntity<ApiResponse<UserMailListResponseDto>> listUserMail(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_FIELD) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<UserMailListResponseDto> result = userMailService.listUserMail(pagingRequestDto, userId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 우편 단건 읽기 (읽음 Y)
     * @param userMailId
     * @param requestDto
     * @return
     */
    @PatchMapping("/{userMailId}/read")
    @Operation(summary = "유저 우편 단건 읽기")
    public ResponseEntity<ApiResponse<UserMailResponseDto>> getUserMail(
            @PathVariable Integer userMailId,
            @Valid @RequestBody UserMailActionRequestDto requestDto
    ) {
        requestDto.setUserMailId(userMailId);
        Result<UserMailResponseDto> result = userMailService.getUserMail(requestDto);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 우편 단건 보상 수령 (보상 Y)
     * @param userMailId
     * @param requestDto
     * @return
     */
    @PatchMapping("/{userMailId}/receive")
    @Operation(summary = "유저 우편 단건 보상 수령")
    public ResponseEntity<ApiResponse<Void>> receiveUserMail(
            @PathVariable Integer userMailId,
            @Valid @RequestBody UserMailActionRequestDto requestDto
    ) {
        requestDto.setUserMailId(userMailId);
        Result<Void> result = userMailService.receiveUserMail(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 우편 전체 보상 수령 (읽음 Y, 보상 Y)
     * @param requestDto
     * @return
     */
    @PatchMapping("/receive-all")
    @Operation(summary = "유저 우편 전체 보상 수령")
    public ResponseEntity<ApiResponse<Void>> receiveAllUserMail(@Valid @RequestBody UserMailBulkReceiveRequestDto requestDto) {
        Result<Void> result = userMailService.receiveAllUserMail(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 우편 삭제
     * @param userMailId
     * @param updatedBy
     * @return
     */
    @DeleteMapping("/{userMailId}")
    @Operation(summary = "유저 우편 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteUserMail(
            @PathVariable Integer userMailId,
            @RequestParam String updatedBy
    ) {
        UserMailDeleteRequestDto requestDto = new UserMailDeleteRequestDto();
        requestDto.setUserMailId(userMailId);
        requestDto.setUpdatedBy(updatedBy);
        requestDto.setIsDel("Y");

        Result<Void> result = userMailService.deleteUserMail(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 우편 전체 삭제
     * @param userId
     * @return
     */
    @DeleteMapping("/all/{userId}")
    @Operation(summary = "유저 우편 전체 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteAllUserMail(@PathVariable Integer userId) {
        Result<Void> result = userMailService.deleteAllUserMail(userId);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}
