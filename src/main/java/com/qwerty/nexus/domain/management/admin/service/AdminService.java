package com.qwerty.nexus.domain.management.admin.service;

import com.qwerty.nexus.domain.management.admin.command.AdminCreateCommand;
import com.qwerty.nexus.domain.management.admin.command.AdminSearchCommand;
import com.qwerty.nexus.domain.management.admin.command.AdminUpdateCommand;
import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.domain.management.admin.repository.AdminRepository;
import com.qwerty.nexus.domain.management.admin.AdminRole;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repository;
    private final OrganizationRepository organizationRepository;

    /**
     * 관리자 등록
     * @param command
     * @return
     */
    public Result<AdminResponseDto> register(AdminCreateCommand command) {
        AdminResponseDto rst = new AdminResponseDto();

        // 비밀번호 암호화 인코더
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(command.getLoginPw());

        AdminEntity adminEntity = AdminEntity.builder()
                .loginId(command.getLoginId())
                .loginPw(encodedPassword)
                .adminNm(command.getAdminNm())
                .adminEmail(command.getAdminEmail())
                .adminRole(command.getAdminRole())
                .createdBy(command.getLoginId())
                .updatedBy(command.getLoginId())
                .orgId(command.getOrgId())
                .build();

        // 회원 중복 확인
        boolean isUser = repository.isUserAlreadyRegistered(adminEntity) > 0;
        if(isUser) {
            return Result.Failure.of("이미 사용중인 회원 아이디.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 이메일 중복 확인
        boolean isEmail = repository.existsByEmail(adminEntity) > 0;
        if(isEmail) {
            return Result.Failure.of("이미 사용중인 이메일 주소.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 총괄(SUPER) 관리자 아이디 신청 여부 확인
        // 1. 맞으면 단체 정보 등록 추가
        // 2. 아니면 바로 다음으로 넘어감
        if(command.getAdminRole().equals(AdminRole.SUPER.name())){
            // orgId 가 없는 경우 단체 정보 생성
            if(command.getOrgId() <= 0) {
                // 단체 정보 생성 후 orgId 넣기
                OrganizationEntity organizationEntity = OrganizationEntity.builder()
                        .orgNm(command.getOrgNm())
                        .orgCd(command.getOrgCd())
                        .createdBy(command.getLoginId())
                        .updatedBy(command.getLoginId())
                        .build();

                organizationEntity = organizationRepository.insertOrganization(organizationEntity);

                adminEntity = AdminEntity.builder()
                        .loginId(command.getLoginId())
                        .orgId(organizationEntity.getOrgId())
                        .loginPw(encodedPassword)
                        .adminNm(command.getAdminNm())
                        .adminEmail(command.getAdminEmail())
                        .adminRole(command.getAdminRole())
                        .createdBy(command.getLoginId())
                        .updatedBy(command.getLoginId())
                        .build();
            }
        }

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<AdminEntity> insertRst = Optional.ofNullable(repository.insertAdmin(adminEntity));
        if(insertRst.isEmpty()) {
            return Result.Failure.of("회원가입 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "회원가입 완료.");
    }

    /**
     * 관리자 정보 수정
     * @param command
     * @return
     */
    public Result<AdminResponseDto> update(AdminUpdateCommand command) {
        AdminResponseDto rst = new AdminResponseDto();

        // 변경할 비밀번호가 있는 경우 암호화 처리
        String modifiedPw = null;
        if(command.getLoginPw() != null){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            modifiedPw = passwordEncoder.encode(command.getLoginPw());
        }

        AdminEntity adminEntity = AdminEntity.builder()
                .adminId(command.getAdminId())
                .adminNm(command.getAdminNm())
                .loginPw(modifiedPw)
                .adminEmail(command.getAdminEmail())
                .adminRole(command.getAdminRole())
                .isDel(command.getIsDel())
                .updatedBy(command.getUpdatedBy())
                .build();

        Optional<AdminEntity> updateRst = Optional.ofNullable(repository.updateAdmin(adminEntity));

        String type = "수정";
        if(command.getIsDel() != null && command.getIsDel().equalsIgnoreCase("Y"))
            type = "삭제";

        if(updateRst.isEmpty()) {
            return Result.Failure.of(String.format("회원정보 %s 실패.", type), ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, String.format("회원정보 %s 성공.", type));
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

        Optional<AdminEntity> selectRst = Optional.ofNullable(repository.selectOneAdmin(admin));
        if(selectRst.isPresent()) {
            rst.convertEntityToDto(selectRst.get());
        }else{
            return Result.Failure.of("관리자 회원 정보 존재하지 않음.",  ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(rst, "관리자 회원 정보 조회 완료.");
    }

    /**
     * 관리자 목록 조회
     * @param command
     * @return
     */
    public Result<List<AdminResponseDto>> selectAllAdmin(AdminSearchCommand command) {
        List<AdminResponseDto> rst = new ArrayList<>();

        AdminEntity entity = AdminEntity.builder()
                .sort(command.getSort())
                .keyword(command.getKeyword())
                .page(command.getPage())
                .size(command.getSize())
                .direction(command.getDirection())
                .build();

        List<AdminEntity> selectRst = repository.selectAllAdmin(entity);

        return Result.Success.of(rst, "관리자 목록 조회 완료.");
    }
}
