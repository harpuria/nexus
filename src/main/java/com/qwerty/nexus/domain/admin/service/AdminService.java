package com.qwerty.nexus.domain.admin.service;

import com.qwerty.nexus.domain.admin.command.AdminCreateCommand;
import com.qwerty.nexus.domain.admin.command.AdminUpdateCommand;
import com.qwerty.nexus.domain.admin.entity.AdminEntity;
import com.qwerty.nexus.domain.admin.repository.AdminRepository;
import com.qwerty.nexus.domain.admin.AdminRole;
import com.qwerty.nexus.domain.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.domain.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.organization.repository.OrganizationRepository;
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
    private final OrganizationRepository organizationRepository;

    /**
     * 관리자 등록
     * @param admin
     * @return
     */
    public Result<AdminResponseDto> register(AdminCreateCommand admin) {
        AdminResponseDto rst = new AdminResponseDto();

        // 비밀번호 암호화 인코더
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(admin.getLoginPw());

        AdminEntity adminEntity = AdminEntity.builder()
                .loginId(admin.getLoginId())
                .loginPw(encodedPassword)
                .adminNm(admin.getAdminNm())
                .adminEmail(admin.getAdminEmail())
                .adminRole(admin.getAdminRole())
                .createdBy(admin.getLoginId())
                .updatedBy(admin.getLoginId())
                .orgId(admin.getOrgId())
                .build();

        // 회원 중복 확인
        boolean isUser = adminRepository.isUserAlreadyRegistered(adminEntity) > 0;
        if(isUser) {
            return Result.Failure.of("이미 존재하는 회원입니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 이메일 중복 확인
        boolean isEmail = adminRepository.existsByEmail(adminEntity) > 0;
        if(isEmail) {
            return Result.Failure.of("이미 사용중인 이메일 주소 입니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 총괄(SUPER) 관리자 아이디 신청 여부 확인
        // 1. 맞으면 단체 정보 등록 추가
        // 2. 아니면 바로 다음으로 넘어감
        if(admin.getAdminRole().equals(AdminRole.SUPER.name())){
            // orgId 가 없는 경우 단체 정보 생성
            if(admin.getOrgId() <= 0) {
                // 단체 정보 생성 후 orgId 넣기
                OrganizationEntity organizationEntity = OrganizationEntity.builder()
                        .orgNm(admin.getOrgNm())
                        .orgCd(admin.getOrgCd())
                        .createdBy(admin.getLoginId())
                        .updatedBy(admin.getLoginId())
                        .build();

                organizationEntity = organizationRepository.insertOrganization(organizationEntity);

                adminEntity = AdminEntity.builder()
                        .loginId(admin.getLoginId())
                        .orgId(organizationEntity.getOrgId())
                        .loginPw(encodedPassword)
                        .adminNm(admin.getAdminNm())
                        .adminEmail(admin.getAdminEmail())
                        .adminRole(admin.getAdminRole())
                        .createdBy(admin.getLoginId())
                        .updatedBy(admin.getLoginId())
                        .build();
            }
        }

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<AdminEntity> insertRst = Optional.ofNullable(adminRepository.insertAdmin(adminEntity));
        if(insertRst.isPresent()) {
            rst.setMessage(String.format("ADMIN_ID : %s,  회원가입이 정상적으로 수행되었습니다.", insertRst.get().getAdminId()));
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

        // 변경할 비밀번호가 있는 경우 암호화 처리
        String modifiedPw = null;
        if(admin.getLoginPw() != null){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            modifiedPw = passwordEncoder.encode(admin.getLoginPw());
        }

        // TODO : 추가로 update 니까 updateBy 를 넣어주는 것이 좋을듯.
        AdminEntity adminEntity = AdminEntity.builder()
                .adminId(admin.getAdminId())
                .adminNm(admin.getAdminNm())
                .loginPw(modifiedPw)
                .adminEmail(admin.getAdminEmail())
                .adminRole(admin.getAdminRole())
                .isDel(admin.getIsDel())
                .build();

        Optional<AdminEntity> updateRst = Optional.ofNullable(adminRepository.updateAdmin(adminEntity));

        if(updateRst.isPresent()) {
            if(admin.getIsDel() != null && admin.getIsDel().equalsIgnoreCase("Y")){
                // TODO : 추후 실제 삭제처리가 되었는지 여부가 필요. (ex :이미 삭제된 회원입니다. 같은 메시지 나오게)
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

        AdminEntity admin = AdminEntity.builder()
                .adminId(adminId)
                .build();

        Optional<AdminEntity> selectRst = Optional.ofNullable(adminRepository.selectOneAdmin(admin));
        if(selectRst.isPresent()) {
            st.convertEntityToDTO(selectRst.get());
            rst.setMessage("관리자 회원 정보가 정상적으로 검색되었습니다.");
        }else{
            return Result.Failure.of("관리자 회원 정보가 존재하지 않습니다.",  ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst);
    }
}
