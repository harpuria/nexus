package com.qwerty.nexus.domain.admin.controller;

import com.qwerty.nexus.domain.admin.AdminRole;
import com.qwerty.nexus.domain.admin.dto.request.*;
import com.qwerty.nexus.domain.admin.service.AdminService;
import com.qwerty.nexus.domain.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "초기 사용자 등록 (SUPER 관리자)")
    public ResponseEntity<ApiResponse<AdminResponseDto>> initializeAdmin(
            @Parameter @RequestBody AdminInitCreateRequestDto admin){
        // 초기 사용자는 무조건 SUPER 관리자로 등록
        admin.setAdminRole(AdminRole.SUPER.name());

        Result<AdminResponseDto> result = adminService.register(admin.toAdminCommand());

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
    @Operation(summary = "관리자 생성 (SUPER 관리자가 생성)")
    public ResponseEntity<ApiResponse<AdminResponseDto>> createAdmin(
            @Parameter @RequestBody AdminCreateRequestDto admin){
        Result<AdminResponseDto> result = adminService.register(admin.toAdminCommand());

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
    @Operation(summary = "관리자 정보 수정")
    public ResponseEntity<ApiResponse<AdminResponseDto>> updateAdmin(@PathVariable("adminId") Integer adminId,
                                                                     @Parameter @RequestBody AdminUpdateRequestDto admin){
        admin.setAdminId(adminId);

        Result<AdminResponseDto> result = adminService.update(admin.toAdminCommand());

        return switch(result) {
            case Result.Success<AdminResponseDto> success -> ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDto> failure -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 관리자 삭제 (논리적 삭제 처리)
     * @param adminId
     * @return
     */
    @DeleteMapping("/{adminId}")
    @Operation(summary = "관리자 삭제 (논리적 삭제 처리)")
    public ResponseEntity<ApiResponse<AdminResponseDto>> deleteAdmin(@PathVariable("adminId") Integer adminId){
        AdminUpdateRequestDto admin = new AdminUpdateRequestDto();
        admin.setAdminId(adminId);
        admin.setIsDel("Y");

        Result<AdminResponseDto> result = adminService.update(admin.toAdminCommand());

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
    @Operation(summary = "한 건의 관리자 정보 조회")
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
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "관리자 목록 조회")
    public ResponseEntity<ApiResponse<List<AdminResponseDto>>> selectAdminList(){
        return null;
    }


    /**
     * 관리자 로그인
     * @param admin
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "관리자 로그인")
    public ResponseEntity<ApiResponse<AdminResponseDto>> login(@RequestBody AdminLoginRequestDto admin){
        return null;
    }

    /**
     * 관리자 로그아웃
     * @param admin
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "관리자 로그아웃")
    public ResponseEntity<ApiResponse<AdminResponseDto>> logout(@RequestBody AdminLogoutRequestDto admin){
        return null;
    }
}
