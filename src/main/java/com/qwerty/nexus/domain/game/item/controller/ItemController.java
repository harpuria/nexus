package com.qwerty.nexus.domain.game.item.controller;

import com.qwerty.nexus.domain.game.item.dto.request.ItemCreateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.request.ItemUpdateRequestDto;
import com.qwerty.nexus.domain.game.item.dto.response.ItemListResponseDto;
import com.qwerty.nexus.domain.game.item.dto.response.ItemResponseDto;
import com.qwerty.nexus.domain.game.item.service.ItemService;
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

@RequestMapping(ApiConstants.Path.ITEM_PATH)
@RestController
@RequiredArgsConstructor
@Tag(name = "아이템", description = "아이템 마스터 관련 API")
public class ItemController {
    private final ItemService service;

    /**
     * 아이템 정보 생성
     * @param dto
     * @return
     */
    @PostMapping
    @Operation(summary = "아이템 정보 생성")
    public ResponseEntity<ApiResponse<Void>> createItem(@Valid @RequestBody ItemCreateRequestDto dto) {
        Result<Void> result = service.createItem(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 아이템 정보 수정
     * @param itemId
     * @param dto
     * @return
     */
    @PatchMapping("/{itemId}")
    @Operation(summary = "아이템 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateItem(@PathVariable("itemId") int itemId, @Valid @RequestBody ItemUpdateRequestDto dto) {
        dto.setItemId(itemId);
        Result<Void> result = service.updateItem(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 아이템 정보 삭제 (논리적 삭제)
     * @param itemId
     * @return
     */
    @DeleteMapping("/{itemId}")
    @Operation(summary = "아이템 정보 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable("itemId") int itemId) {
        ItemUpdateRequestDto dto = new ItemUpdateRequestDto();
        dto.setItemId(itemId);
        dto.setIsDel("Y");
        Result<Void> result = service.deleteItem(dto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 아이템 단건 조회
     * @param itemId
     * @return
     */
    @GetMapping("/{itemId}")
    @Operation(summary = "아이템 단건 조회")
    public ResponseEntity<ApiResponse<ItemResponseDto>> getItem(@PathVariable("itemId") int itemId) {
        Result<ItemResponseDto> result = service.getItem(itemId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 아이템 목록 조회
     * @param gameId
     * @param page
     * @param size
     * @param sort
     * @param keyword
     * @param direction
     * @return
     */
    @GetMapping("/list/{gameId}")
    @Operation(summary = "아이템 목록 조회")
    public ResponseEntity<ApiResponse<ItemListResponseDto>> listItems(
            @PathVariable("gameId") int gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setKeyword(keyword);
        pagingRequestDto.setDirection(direction);

        Result<ItemListResponseDto> result = service.listItems(pagingRequestDto, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }
}
