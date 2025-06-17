package com.qwerty.nexus.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.pojos.Admin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * 관리자 등록
     * @param admin 등록할 관리자의 회원 정보
     * @return ResponseEntity<AdminResponseDTO>
     * @since 250617
     */
    @PostMapping
    public ResponseEntity<AdminResponseDTO> registerAdmin(@RequestBody Admin admin){
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        adminService.register(admin);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 관리자 정보 수정
     * @param admin 수정할 관리자의 회원 정보
     * @return ResponseEntity<AdminResponseDTO>
     */
    @PatchMapping
    public ResponseEntity<AdminResponseDTO> updateAdmin(@RequestBody Admin admin){
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        adminService.update(admin);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 하나의 관리자 정보 조회
     * @param id 수정할 관리자의 회원 번호
     * @return ResponseEntity<AdminResponseDTO>
     */
    @GetMapping
    public ResponseEntity<AdminResponseDTO> getOneAdmin(@RequestParam Integer id){
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        adminService.selectOneAdmin(id);
        return ResponseEntity.ok(responseDTO);
    }
}
