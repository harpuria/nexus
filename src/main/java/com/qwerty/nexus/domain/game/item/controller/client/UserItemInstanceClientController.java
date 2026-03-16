package com.qwerty.nexus.domain.game.item.controller.client;

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

@RequestMapping(ApiConstants.Path.CLIENT_USER_ITEM_INSTANCE_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "유저 인스턴스형 아이템 (클라이언트)", description = "유저 인스턴스형(ex:장비, 영웅, 스킨 등) 아이템 관련 API (클라이언트)")
public class UserItemInstanceClientController {
    private final UserItemInstanceService service;

    /*
        TODO - 인스턴스형형 추가할만한 API 목록
        1) 아이템 장착
        2) 아이템 버리기 (IS_DEL Y 처리)
        3) 아이템 강화
     */

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
    @Operation(summary = "유저 인스턴스형 아이템 목록 조회 (페이징 없음 : 미작업)")
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
