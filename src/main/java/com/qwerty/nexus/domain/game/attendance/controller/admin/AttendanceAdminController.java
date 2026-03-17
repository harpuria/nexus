package com.qwerty.nexus.domain.game.attendance.controller.admin;

import com.qwerty.nexus.domain.game.attendance.dto.request.*;
import com.qwerty.nexus.domain.game.attendance.dto.response.AttendanceListResponseDto;
import com.qwerty.nexus.domain.game.attendance.dto.response.AttendanceResponseDto;
import com.qwerty.nexus.domain.game.attendance.service.AttendanceService;
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

@RestController
@RequestMapping(ApiConstants.Path.ADMIN_ATTENDANCE_PATH)
@RequiredArgsConstructor
@Tag(name = "출석부 (관리자)", description = "출석부 관련 API (관리자)")
public class AttendanceAdminController {
    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "출석 이벤트 생성")
    public ResponseEntity<ApiResponse<Void>> createAttendance(@Valid @RequestBody CreateAttendanceRequestDto requestDto) {
        Result<Void> result = attendanceService.createAttendance(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    @PutMapping("/{attendanceId}")
    @Operation(summary = "출석 이벤트 수정")
    public ResponseEntity<ApiResponse<Void>> updateAttendance(
            @PathVariable Integer attendanceId,
            @Valid @RequestBody UpdateAttendanceRequestDto requestDto
    ) {
        requestDto.setAttendanceId(attendanceId);
        Result<Void> result = attendanceService.updateAttendance(requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "출석 이벤트 목록 조회")
    public ResponseEntity<ApiResponse<AttendanceListResponseDto>> listAttendance(
            @RequestParam Integer gameId,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = "" + ApiConstants.Pagination.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = ApiConstants.Pagination.DEFAULT_SORT_DIRECTION) String direction
    ) {
        PagingRequestDto pagingRequestDto = new PagingRequestDto();
        pagingRequestDto.setPage(page);
        pagingRequestDto.setSize(size);
        pagingRequestDto.setSort(sort);
        pagingRequestDto.setDirection(direction);

        Result<AttendanceListResponseDto> result = attendanceService.listAttendance(pagingRequestDto, gameId, false);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/{attendanceId}")
    @Operation(summary = "출석 이벤트 단건 조회")
    public ResponseEntity<ApiResponse<AttendanceResponseDto>> getAttendance(
            @PathVariable Integer attendanceId,
            @RequestParam Integer gameId
    ) {
        Result<AttendanceResponseDto> result = attendanceService.getAttendance(attendanceId, gameId);
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    @PutMapping("/{attendanceId}/rewards")
    @Operation(summary = "출석 보상 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateAttendanceRewards(
            @PathVariable Integer attendanceId,
            @Valid @RequestBody UpdateAttendanceRewardsRequestDto requestDto
    ) {
        Result<Void> result = attendanceService.updateAttendanceRewards(attendanceId, requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @PatchMapping("/{attendanceId}/activate")
    @Operation(summary = "출석 이벤트 활성화")
    public ResponseEntity<ApiResponse<Void>> activateAttendance(
            @PathVariable Integer attendanceId,
            @Valid @RequestBody UpdateAttendanceStatusRequestDto requestDto
    ) {
        Result<Void> result = attendanceService.activateAttendance(attendanceId, requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    @PatchMapping("/{attendanceId}/deactivate")
    @Operation(summary = "출석 이벤트 비활성화")
    public ResponseEntity<ApiResponse<Void>> deactivateAttendance(
            @PathVariable Integer attendanceId,
            @Valid @RequestBody UpdateAttendanceStatusRequestDto requestDto
    ) {
        Result<Void> result = attendanceService.deactivateAttendance(attendanceId, requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}
