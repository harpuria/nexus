package com.qwerty.nexus.domain.management.admin.controller;

import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.dto.request.*;
import com.qwerty.nexus.domain.management.admin.service.AdminService;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.ResponseEntityUtils;
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
    private final AdminService service;

    /**
     * 초기 사용자 등록 (SUPER 관리자)
     * @param dto 초기 생성할 관리자 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PostMapping("/initialize")
    @Operation(summary = "초기 사용자 등록 (SUPER 관리자)")
    public ResponseEntity<ApiResponse<Void>> initializeAdmin(
            @Parameter @RequestBody AdminInitCreateRequestDto dto){
        // 초기 사용자는 무조건 SUPER 관리자로 등록
        dto.setAdminRole(AdminRole.SUPER.name());

        Result<AdminResponseDto> result = service.register(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 관리자 생성
     * @param dto 생성할 관리자 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PostMapping
    @Operation(summary = "관리자 생성 (SUPER 관리자가 생성)")
    public ResponseEntity<ApiResponse<Void>> createAdmin(
            @Parameter @RequestBody AdminCreateRequestDto dto){
        Result<AdminResponseDto> result = service.register(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.CREATED);
    }

    /**
     * 관리자 정보 수정
     * @param adminId 관리자 아이디 (PK)
     * @param dto 수정할 관리자 정보를 담은 객체 (DTO)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @PatchMapping("/{adminId}")
    @Operation(summary = "관리자 정보 수정")
    public ResponseEntity<ApiResponse<Void>> updateAdmin(@PathVariable("adminId") Integer adminId,
                                                                     @Parameter @RequestBody AdminUpdateRequestDto dto){
        dto.setAdminId(adminId);

        Result<AdminResponseDto> result = service.update(dto.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 관리자 삭제 (논리적 삭제 처리)
     * @param adminId 관리자 아이디 (PK)
     * @return 성공 혹은 실패 메시지, 오류코드 (실패시)
     */
    @DeleteMapping("/{adminId}")
    @Operation(summary = "관리자 삭제 (논리적 삭제 처리)")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(@PathVariable("adminId") Integer adminId){
        AdminUpdateRequestDto admin = new AdminUpdateRequestDto();
        admin.setAdminId(adminId);
        admin.setIsDel("Y");

        Result<AdminResponseDto> result = service.update(admin.toCommand());

        return ResponseEntityUtils.toResponseEntityVoid(result, HttpStatus.OK);
    }

    /**
     * 한 건의 관리자 정보 조회
     * @param adminId 관리자 아이디 (PK)
     * @return 한 건의 관리자 정보를 담은 객체 (DTO)
     */
    @GetMapping("/{adminId}")
    @Operation(summary = "한 건의 관리자 정보 조회")
    public ResponseEntity<ApiResponse<AdminResponseDto>> selectOneAdmin(@PathVariable("adminId") Integer adminId){
        Result<AdminResponseDto> result = service.selectOneAdmin(adminId);

        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 관리자 목록 조회
     * @param dto 관리자 목록 조회에 필요한 정보(페이징 처리 등) 관련된 객체 (DTO)
     * @return 복수의 관리자 정보를 담은 리스트 객체 (DTO)
     */
    @GetMapping("/list")
    @Operation(summary = "관리자 목록 조회")
    public ResponseEntity<ApiResponse<List<AdminResponseDto>>> selectAllAdmin(@RequestBody AdminSearchRequestDto dto){
        Result<List<AdminResponseDto>> result = service.selectAllAdmin(dto.toCommand());
        return ResponseEntityUtils.toResponseEntity(result, HttpStatus.OK);
    }


    /**
     * 관리자 로그인
     * @param dto 관리자 정보를 담은 객체 (DTO)
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 (미개발)")
    public ResponseEntity<ApiResponse<AdminResponseDto>> login(@RequestBody AdminLoginRequestDto dto){
        return null;
    }

    /**
     * 관리자 로그아웃
     * @param dto 관리자 정보를 담은 객체 (DTO)
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "관리자 로그아웃 (미개발)")
    public ResponseEntity<ApiResponse<AdminResponseDto>> logout(@RequestBody AdminLogoutRequestDto dto){
        return null;
    }
}
