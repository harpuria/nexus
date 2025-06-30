package com.qwerty.nexus.domain.admin;

import com.qwerty.nexus.global.constant.ApiConstants;
import com.qwerty.nexus.global.response.ApiResponse;
import com.qwerty.nexus.global.response.Result;
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
public class AdminController {
    private final AdminService adminService;

    /**
     * 초기 사용자 등록 (SUPER 관리자)
     * @param admin
     * @return
     */
    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> initializeAdmin(@RequestBody AdminRequestDTO admin){
        // 초기 사용자는 무조건 SUPER 관리자로 등록
        admin.setAdminRole(AdminRole.SUPER.name());

        Result<AdminResponseDTO> result = adminService.register(admin);

        return switch(result){
            case Result.Success<AdminResponseDTO> success ->
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDTO> failure ->
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
    public ResponseEntity<ApiResponse<AdminResponseDTO>> createAdmin(@RequestBody AdminRequestDTO admin){
        Result<AdminResponseDTO> result = adminService.register(admin);

        return switch(result){
            case Result.Success<AdminResponseDTO> success ->
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDTO> failure ->
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
    public ResponseEntity<ApiResponse<AdminResponseDTO>> updateAdmin(@PathVariable("adminId") Integer adminId, @RequestBody AdminRequestDTO admin){
        admin.setAdminId(adminId);

        Result<AdminResponseDTO> result = adminService.update(admin);

        return switch(result) {
            case Result.Success<AdminResponseDTO> success -> ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDTO> failure -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 관리자 정보 삭제 (논리적 삭제)
     * @param adminId
     * @return
     */
    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> deleteAdmin(@PathVariable("adminId") Integer adminId){
        AdminRequestDTO admin = new AdminRequestDTO();
        admin.setAdminId(adminId);
        admin.setIsDel("Y");

        Result<AdminResponseDTO> result = adminService.update(admin);

        return switch(result){
            case Result.Success<AdminResponseDTO> success ->
                ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDTO> failure ->
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
    public ResponseEntity<ApiResponse<AdminResponseDTO>> selectOneAdmin(@PathVariable("adminId") Integer adminId){
        Result<AdminResponseDTO> result = adminService.selectOneAdmin(adminId);

        return switch(result){
            case Result.Success<AdminResponseDTO> success ->
                ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<AdminResponseDTO> failure ->
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
    public ResponseEntity<ApiResponse<List<AdminResponseDTO>>> selectAdminList(@RequestBody AdminRequestDTO admin){
        return null;
    }


    /**
     * 관리자 로그인
     * @param admin
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> login(@RequestBody AdminRequestDTO admin){
        return null;
    }

    /**
     * 관리자 로그아웃
     * @param admin
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> logout(@RequestBody AdminRequestDTO admin){
        return null;
    }
}
