package com.qwerty.nexus.domain.admin;

import com.qwerty.nexus.domain.organization.OrganizationRequestDTO;
import com.qwerty.nexus.domain.organization.OrganizationService;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.AdminRecord;
import org.jooq.generated.tables.records.OrganizationRecord;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final OrganizationService orgService;

    /**
     * 관리자 등록
     * @param admin
     * @return
     */
    public Result<AdminResponseDTO> register(AdminRequestDTO admin) {
        AdminResponseDTO rst = new AdminResponseDTO();

        // 회원 중복 확인
        boolean isUser = adminRepository.isUserAlreadyRegistered(admin.toAdminRecord()) > 0;
        if(isUser) {
            return Result.Failure.of("이미 존재하는 회원입니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 이메일 중복 확인
        boolean isEmail = adminRepository.existsByEmail(admin.toAdminRecord()) > 0;
        if(isEmail) {
            return Result.Failure.of("이미 사용중인 이메일 주소 입니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 총괄(SUPER) 관리자 아이디 신청 여부 확인
        // 1. 맞으면 단체 정보 등록 추가
        // 2. 아니면 바로 다음으로 넘어감
        if(admin.getAdminRole().equals(AdminRole.SUPER.name())){
            // 단체 정보 생성 후 orgId 넣기
            OrganizationRequestDTO orgRecord = new OrganizationRequestDTO();
            orgRecord.setOrgNm(admin.getOrganization().getOrgNm());
            orgRecord.setOrgCd(admin.getOrganization().getOrgCd());
            orgRecord.setCreatedBy(admin.getCreatedBy());
            orgRecord.setUpdatedBy(admin.getUpdatedBy());
            admin.setOrgId(orgService.register(orgRecord));
        }

        // 비밀번호 암호화 처리
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        admin.setLoginPw(passwordEncoder.encode(admin.getLoginPw()));

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<AdminRecord> insertRst = Optional.ofNullable(adminRepository.insertAdmin(admin.toAdminRecord()));
        if(insertRst.isPresent()) {
            rst.convertPojoToDTO(insertRst.get());
            rst.setMessage("회원가입이 정상적으로 수행되었습니다.");
        }
        else{
            return Result.Failure.of("회원가입이 실패하였습니다. 넥서스 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }

    /**
     * 관리자 정보 수정
     * @param admin
     * @return
     */
    public Result<AdminResponseDTO> update(AdminRequestDTO admin) {
        AdminResponseDTO rst = new AdminResponseDTO();

        // 변경할 비밀번호가 있는 경우 암호화 처리
        Optional.ofNullable(admin.getLoginPw()).ifPresent(loginPw -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            admin.setLoginPw(passwordEncoder.encode(loginPw));
        });

        Optional<AdminRecord> updateRst = Optional.ofNullable(adminRepository.updateAdmin(admin.toAdminRecord()));

        if(updateRst.isPresent()) {
            rst.convertPojoToDTO(updateRst.get());

            if(admin.getIsDel().equals("Y")){
                rst.setMessage("회원정보가 정상적으로 삭제되었습니다.");
            }else{
                rst.setMessage("회원정보가 정상적으로 수정되었습니다.");
            }

        }
        else{
            return Result.Failure.of("회원정보 수정에 실패하였습니다. 총괄 관리자에게 문의해주세요.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }

    public AdminResponseDTO selectOneAdmin(int adminId) {
        AdminResponseDTO rst = new AdminResponseDTO();

        AdminRecord admin = new AdminRecord();
        admin.setAdminId(adminId);

        Optional<AdminRecord> selectRst = Optional.ofNullable(adminRepository.selectOneAdmin(admin));
        if(selectRst.isPresent()) {
            rst.convertPojoToDTO(selectRst.get());
            rst.setMessage("관리자 회원 정보가 정상적으로 검색되었습니다.");
        }else{
            rst.setMessage("관리자 회원 정보가 존재하지 않습니다.");
        }

        return rst;
    }
}
