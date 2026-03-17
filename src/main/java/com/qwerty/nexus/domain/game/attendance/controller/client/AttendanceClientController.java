package com.qwerty.nexus.domain.game.attendance.controller.client;

import com.qwerty.nexus.domain.game.attendance.dto.request.CheckInAttendanceRequestDto;
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
@RequestMapping(ApiConstants.Path.CLIENT_ATTENDANCE_PATH)
@RequiredArgsConstructor
@Tag(name = "출석부 (클라이언트)", description = "출석부 관련 API (클라이언트)")
public class AttendanceClientController {
    private final AttendanceService attendanceService;

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

        Result<AttendanceListResponseDto> result = attendanceService.listAttendance(pagingRequestDto, gameId, true);
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

    @PostMapping("/{attendanceId}/check-in")
    @Operation(summary = "출석 체크")
    public ResponseEntity<ApiResponse<Void>> checkInAttendance(
            @PathVariable Integer attendanceId,
            @Valid @RequestBody CheckInAttendanceRequestDto requestDto
    ) {
        Result<Void> result = attendanceService.checkInAttendance(attendanceId, requestDto);
        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }
}
