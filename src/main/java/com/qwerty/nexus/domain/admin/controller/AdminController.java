package com.qwerty.nexus.domain.admin.controller;

import com.qwerty.nexus.domain.admin.AdminRole;
import com.qwerty.nexus.domain.admin.dto.request.AdminCreateRequestDto;
import com.qwerty.nexus.domain.admin.service.AdminService;
import com.qwerty.nexus.domain.admin.dto.request.AdminRequestDto;
import com.qwerty.nexus.domain.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping(ApiConstants.Path.ADMIN_PATH)
@RequiredArgsConstructor
@Tag(name = "관리자", description = "관리자 관련 API")
public class AdminController {
    private final AdminService adminService;

    /**
     * 초기 사용자 등록 (SUPER 관리자)
     * @param admin
     * @return
     */
    @PostMapping("/initialize")
    @Operation(summary = "초기 사용자 등록", description = "설치 후 첫 사용자(SUPER 관리자) 등록 API")
    public ResponseEntity<ApiResponse<AdminResponseDto>> initializeAdmin(
            @Parameter @RequestBody AdminCreateRequestDto admin){
        // 초기 사용자는 무조건 SUPER 관리자로 등록
        admin.setAdminRole(AdminRole.SUPER.name());

        Result<AdminResponseDto> result = adminService.register(admin);

        return switch(result){
            case Result.Success<AdminResponseDto> success ->
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDto> failure ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 관리자 생성
     * @param admin
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdminResponseDto>> createAdmin(@RequestBody AdminRequestDto admin){
        Result<AdminResponseDto> result = adminService.register(admin);

        return switch(result){
            case Result.Success<AdminResponseDto> success ->
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDto> failure ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 관리자 정보 수정
     * @param adminId
     * @param admin
     * @return
     */
    @PatchMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminResponseDto>> updateAdmin(@PathVariable("adminId") Integer adminId, @RequestBody AdminRequestDto admin){
        admin.setAdminId(adminId);

        Result<AdminResponseDto> result = adminService.update(admin);

        return switch(result) {
            case Result.Success<AdminResponseDto> success -> ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDto> failure -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 관리자 정보 삭제 (논리적 삭제)
     * @param adminId
     * @return
     */
    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminResponseDto>> deleteAdmin(@PathVariable("adminId") Integer adminId){
        AdminRequestDto admin = new AdminRequestDto();
        admin.setAdminId(adminId);
        admin.setIsDel("Y");

        Result<AdminResponseDto> result = adminService.update(admin);

        return switch(result){
            case Result.Success<AdminResponseDto> success ->
                ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDto> failure ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 한 건의 관리자 정보 조회
     * @param adminId
     * @return
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminResponseDto>> selectOneAdmin(@PathVariable("adminId") Integer adminId){
        Result<AdminResponseDto> result = adminService.selectOneAdmin(adminId);

        return switch(result){
            case Result.Success<AdminResponseDto> success ->
                ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDto> failure ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 관리자 목록 조회
     * @param admin
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AdminResponseDto>>> selectAdminList(@RequestBody AdminRequestDto admin){
        return null;
    }


    /**
     * 관리자 로그인
     * @param admin
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminResponseDto>> login(@RequestBody AdminRequestDto admin){
        return null;
    }

    /**
     * 관리자 로그아웃
     * @param admin
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<AdminResponseDto>> logout(@RequestBody AdminRequestDto admin){
        return null;
    }
}
