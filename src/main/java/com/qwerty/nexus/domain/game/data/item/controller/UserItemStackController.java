package com.qwerty.nexus.domain.game.data.item.controller;

import com.qwerty.nexus.domain.game.data.item.dto.request.UserItemStackCreateRequestDto;
import com.qwerty.nexus.domain.game.data.item.dto.request.UserItemStackUpdateRequestDto;
import com.qwerty.nexus.domain.game.data.item.dto.response.UserItemStackListResponseDto;
import com.qwerty.nexus.domain.game.data.item.dto.response.UserItemStackResponseDto;
import com.qwerty.nexus.domain.game.data.item.service.UserItemStackService;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.paging.PagingRequestDto;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiConstants.Path.USER_ITEM_STACK_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 아이템 스택", description = "유저 스택형 아이템 관련 API")
public class UserItemStackController {
    private final UserItemStackService service;

    @PostMapping
    @Operation(summary = "유저 아이템 스택 생성")
    public ResponseEntity<ApiResponse<Void>> createUserItemStack(@Valid @RequestBody UserItemStackCreateRequestDto dto) {
        Result<Void> result = service.createUserItemStack(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{userItemStackId}")
    @Operation(summary = "유저 아이템 스택 수정")
    public ResponseEntity<ApiResponse<Void>> updateUserItemStack(@PathVariable int userItemStackId, @Valid @RequestBody UserItemStackUpdateRequestDto dto) {
        dto.setUserItemStackId(userItemStackId);
        Result<Void> result = service.updateUserItemStack(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @DeleteMapping("/{userItemStackId}")
    @Operation(summary = "유저 아이템 스택 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteUserItemStack(@PathVariable int userItemStackId) {
        UserItemStackUpdateRequestDto dto = new UserItemStackUpdateRequestDto();
        dto.setUserItemStackId(userItemStackId);
        dto.setIsDel("Y");
        Result<Void> result = service.deleteUserItemStack(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @GetMapping("/{userItemStackId}")
    @Operation(summary = "유저 아이템 스택 단건 조회")
    public ResponseEntity<ApiResponse<UserItemStackResponseDto>> getUserItemStack(@PathVariable int userItemStackId) {
        Result<UserItemStackResponseDto> result = service.getUserItemStack(userItemStackId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    @Operation(summary = "유저 아이템 스택 목록 조회")
    public ResponseEntity<ApiResponse<UserItemStackListResponseDto>> listUserItemStacks(
            @PathVariable int userId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_FIELD) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction,
            @RequestParam(required = false) Integer itemId,
            @RequestParam(required = false) Integer gameId
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<UserItemStackListResponseDto> result = service.listUserItemStacks(pagingRequestDto, userId, itemId, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
