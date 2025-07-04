package com.qwerty.nexus.domain.admin.service;

import com.qwerty.nexus.domain.admin.command.AdminCreateCommand;
import com.qwerty.nexus.domain.admin.command.AdminUpdateCommand;
import com.qwerty.nexus.domain.admin.repository.AdminRepository;
import com.qwerty.nexus.domain.admin.AdminRole;
import com.qwerty.nexus.domain.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.domain.organization.command.OrganizationCreateCommand;
import com.qwerty.nexus.domain.organization.service.OrganizationService;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.records.AdminRecord;
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
    public Result<AdminResponseDto> register(AdminCreateCommand admin) {
        AdminResponseDto rst = new AdminResponseDto();

        AdminRecord adminRecord = new AdminRecord();
        adminRecord.setLoginId(admin.getLoginId());
        adminRecord.setLoginPw(admin.getLoginPw());
        adminRecord.setAdminEmail(admin.getAdminEmail());
        adminRecord.setAdminNm(admin.getAdminNm());
        adminRecord.setAdminRole(admin.getAdminRole());
        adminRecord.setCreatedBy(admin.getLoginId());
        adminRecord.setUpdatedBy(admin.getLoginId());
        adminRecord.setOrgId(admin.getOrgId());

        // 회원 중복 확인
        boolean isUser = adminRepository.isUserAlreadyRegistered(adminRecord) > 0;
        if(isUser) {
            return Result.Failure.of("이미 존재하는 회원입니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 이메일 중복 확인
        boolean isEmail = adminRepository.existsByEmail(adminRecord) > 0;
        if(isEmail) {
            return Result.Failure.of("이미 사용중인 이메일 주소 입니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 총괄(SUPER) 관리자 아이디 신청 여부 확인
        // 1. 맞으면 단체 정보 등록 추가
        // 2. 아니면 바로 다음으로 넘어감
        if(admin.getAdminRole().equals(AdminRole.SUPER.name())){
            // 단체 정보 생성 후 orgId 넣기
            OrganizationCreateCommand orgCmd = OrganizationCreateCommand.builder()
                    .orgNm(admin.getOrgNm())
                    .orgCd(admin.getOrgCd())
                    .createBy(admin.getLoginId())
                    .build();

            // TODO : SUPER 가 SUPER 를 만드는 상황도 고려해야할듯
            // 이미 조직 정보가 만들어져 있는지 여부 판단하는 거 넣고, 있으면 orgService,register 제외
            // 근데 service 에서는 다른 service 호출하는거보다는, repository 호출해서 사용하는 것이 바람직함.

            adminRecord.setOrgId(orgService.register(orgCmd));
        }

        // 비밀번호 암호화 처리
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminRecord.setLoginPw(passwordEncoder.encode(admin.getLoginPw()));

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<AdminRecord> insertRst = Optional.ofNullable(adminRepository.insertAdmin(adminRecord));
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
    public Result<AdminResponseDto> update(AdminUpdateCommand admin) {
        AdminResponseDto rst = new AdminResponseDto();

        AdminRecord adminRecord = new AdminRecord();
        adminRecord.setAdminId(admin.getAdminId());
        adminRecord.setAdminNm(admin.getAdminNm());
        adminRecord.setAdminEmail(admin.getAdminEmail());
        adminRecord.setAdminRole(admin.getAdminRole());

        // admin id 를 가지고 admin login id 를 가져오는 repository 하나 만들어서 넣기
        // String loginId = adminRepository.selectOneLoginId(admin.getAdminId());

        // 변경할 비밀번호가 있는 경우 암호화 처리
        Optional.ofNullable(admin.getLoginPw()).ifPresent(loginPw -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            adminRecord.setLoginPw(passwordEncoder.encode(loginPw));
        });

        Optional<AdminRecord> updateRst = Optional.ofNullable(adminRepository.updateAdmin(adminRecord));

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

    /**
     * 하나의 관리자 정보 조회
     * @param adminId
     * @return
     */
    public Result<AdminResponseDto> selectOneAdmin(int adminId) {
        AdminResponseDto rst = new AdminResponseDto();

        AdminRecord admin = new AdminRecord();
        admin.setAdminId(adminId);

        Optional<AdminRecord> selectRst = Optional.ofNullable(adminRepository.selectOneAdmin(admin));
        if(selectRst.isPresent()) {
            rst.convertPojoToDTO(selectRst.get());
            rst.setMessage("관리자 회원 정보가 정상적으로 검색되었습니다.");
        }else{
            return Result.Failure.of("관리자 회원 정보가 존재하지 않습니다.",  ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }
}
