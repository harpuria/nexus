package com.qwerty.nexus.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.pojos.Admin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(responseDTO);
    }
}
