package com.qwerty.nexus.domain.game.item.controller;

import com.qwerty.nexus.domain.game.item.dto.request.UserItemInstanceCreateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.request.UserItemInstanceUpdateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.response.UserItemInstanceListResponseDto;
import com.qwerty.nexus.domain.game.item.dto.response.UserItemInstanceResponseDto;
import com.qwerty.nexus.domain.game.item.service.UserItemInstanceService;
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

@RequestMapping(ApiConstants.Path.USER_ITEM_INSTANCE_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 인스턴스형 아이템", description = "유저 인스턴스형(ex:장비, 영웅, 스킨 등) 아이템 관련 API")
public class UserItemInstanceController {
    private final UserItemInstanceService service;

    /**
     * 유저 인스턴스형 아이템 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "유저 인스턴스형 아이템 생성")
    public ResponseEntity<ApiResponse<Void>> createUserItemInstance(@Valid @RequestBody UserItemInstanceCreateRequestDto dto) {
        Result<Void> result = service.createUserItemInstance(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 유저 인스턴스형 아이템 수정
     * @param userItemId
     * @param dto
     * @return
     */
    @PatchMapping("/{userItemId}")
    @Operation(summary = "유저 인스턴스형 아이템 수정")
    public ResponseEntity<ApiResponse<Void>> updateUserItemInstance(@PathVariable int userItemId, @Valid @RequestBody UserItemInstanceUpdateRequestDto dto) {
        dto.setUserItemId(userItemId);
        Result<Void> result = service.updateUserItemInstance(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 인스턴스형 아이템 삭제
     * @param userItemId
     * @return
     */
    @DeleteMapping("/{userItemId}")
    @Operation(summary = "유저 인스턴스형 아이템 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteUserItemInstance(@PathVariable int userItemId) {
        UserItemInstanceUpdateRequestDto dto = new UserItemInstanceUpdateRequestDto();
        dto.setUserItemId(userItemId);
        dto.setIsDel("Y");
        Result<Void> result = service.deleteUserItemInstance(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 유저 인스턴스형 아이템 단건 조회
     * @param userItemId
     * @return
     */
    @GetMapping("/{userItemId}")
    @Operation(summary = "유저 인스턴스형 아이템 단건 조회")
    public ResponseEntity<ApiResponse<UserItemInstanceResponseDto>> getUserItemInstance(@PathVariable int userItemId) {
        Result<UserItemInstanceResponseDto> result = service.getUserItemInstance(userItemId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 유저 인스턴스형 아이템 목록 조회
     * @param userId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @param itemId
     * @param gameId
     * @return
     */
    @GetMapping("/list/{userId}")
    @Operation(summary = "유저 인스턴스형 아이템 목록 조회")
    public ResponseEntity<ApiResponse<UserItemInstanceListResponseDto>> listUserItemInstances(
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

        Result<UserItemInstanceListResponseDto> result = service.listUserItemInstances(pagingRequestDto, userId, itemId, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
