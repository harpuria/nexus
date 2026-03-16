package com.qwerty.nexus.domain.game.item.controller.client;

import com.qwerty.nexus.domain.game.item.dto.request.UserItemStackCreateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.request.UserItemStackUpdateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.response.UserItemStackListResponseDto;
import com.qwerty.nexus.domain.game.item.dto.response.UserItemStackResponseDto;
import com.qwerty.nexus.domain.game.item.service.UserItemStackService;
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

@RequestMapping(ApiConstants.Path.CLIENT_USER_ITEM_STACK_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 스택형 아이템 (클라이언트)", description = "유저 스택형(ex: 재화, 소모품 등) 아이템 관련 API (클라이언트)")
public class UserItemStackClientController {
    private final UserItemStackService service;

    /*
        TODO - 스택형 추가할만한 API 목록
        1) 소모품 아이템 사용
        2) 아이템 버리기 (DEL 이 아니고 수량을 0 으로 만들면 될듯)
     */

    /**
     * 유저 스택형 아이템 목록 조회
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
    @Operation(summary = "유저 스택형 아이템 목록 조회 (페이징 없음 : 미작업)")
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
