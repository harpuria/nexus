package com.qwerty.nexus.domain.management.admin.service;

import com.qwerty.nexus.domain.management.admin.command.*;
import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.domain.management.admin.repository.AdminRepository;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.jwt.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repository;
    private final OrganizationRepository organizationRepository;

    private final JwtUtil jwtUtil;
    private final AdminTokenBlacklist adminTokenBlacklist;

    /**
     * 초기 관리자 등록 + 단체 정보 등록
     * @param command
     * @return
     */
    @Transactional
    public Result<Void> initialize(AdminInitCreateCommand command) {
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

        // 단체 정보 생성 후 orgId 넣기
        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .orgNm(command.getOrgNm())
                .orgCd(command.getOrgCd())
                .createdBy(command.getLoginId())
                .updatedBy(command.getLoginId())
                .build();

        organizationEntity = organizationRepository.insert(organizationEntity);

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

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<AdminEntity> insertRst = Optional.ofNullable(repository.insertAdmin(adminEntity));
        if(insertRst.isEmpty()) {
            return Result.Failure.of("회원가입 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "회원가입 완료.");
    }

    /**
     * 관리자 생성
     * @param command
     * @return
     */
    public Result<Void> create(AdminCreateCommand command) {
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

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<AdminEntity> insertRst = Optional.ofNullable(repository.insertAdmin(adminEntity));
        if(insertRst.isEmpty()) {
            return Result.Failure.of("회원가입 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "회원가입 완료.");
    }

    /**
     * 관리자 정보 수정
     * @param command
     * @return
     */
    public Result<Void> update(AdminUpdateCommand command) {
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

        return Result.Success.of(null, String.format("회원정보 %s 성공.", type));
    }

    /**
     * 하나의 관리자 정보 조회
     * @param adminId
     * @return
     */
    public Result<AdminResponseDto> selectOne(int adminId) {
        AdminEntity admin = AdminEntity.builder()
                .adminId(adminId)
                .build();

        Optional<AdminEntity> selectRst = Optional.ofNullable(repository.selectOneAdmin(admin));
        if(selectRst.isPresent()) {
            return Result.Success.of(AdminResponseDto.from(selectRst.get()), "관리자 회원 정보 조회 완료.");
        }else{
            return Result.Failure.of("관리자 회원 정보 존재하지 않음.",  ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 관리자 목록 조회
     * @param command
     * @return
     */
    public Result<List<AdminResponseDto>> selectAll(AdminSearchCommand command) {
        List<AdminResponseDto> rst = new ArrayList<>();

        AdminEntity entity = AdminEntity.builder()
                .sort(command.getSort())
                .keyword(command.getKeyword())
                .page(command.getPage())
                .size(command.getSize())
                .direction(command.getDirection())
                .build();

        Optional<List<AdminEntity>> selectRst = Optional.ofNullable(repository.selectAllAdmin(entity));
        if(selectRst.isPresent()) {
            selectRst.get().forEach(adminEntity -> {
                rst.add(AdminResponseDto.from(adminEntity));
            });
        }else{
            return Result.Failure.of("관리자 목록이 존재하지 않음.",  ErrorCode.INTERNAL_ERROR.getCode());
        }
        System.out.println("=======");
        rst.forEach(System.out::println);
        System.out.println("=======");

        return Result.Success.of(rst, "관리자 목록 조회 완료.");
    }

    /**
     * 관리자 로그인
     * @param command
     * @return
     */
    public Result<Void> logout(AdminLogoutCommand command) {
        String accessToken = command.getAccessToken();
        String refreshToken = command.getRefreshToken();

        if (!hasText(accessToken) && !hasText(refreshToken)) {
            return Result.Failure.of("로그아웃에 필요한 토큰 정보가 없습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        try {
            if (hasText(accessToken)) {
                if (!jwtUtil.validateToken(accessToken)) {
                    return Result.Failure.of("유효하지 않은 액세스 토큰입니다.", ErrorCode.INVALID_TOKEN.getCode());
                }
                adminTokenBlacklist.blacklist(accessToken, jwtUtil.getTimeUntilExpiration(accessToken));
            }

            if (hasText(refreshToken)) {
                if (!jwtUtil.validateToken(refreshToken)) {
                    return Result.Failure.of("유효하지 않은 리프레시 토큰입니다.", ErrorCode.INVALID_TOKEN.getCode());
                }
                adminTokenBlacklist.blacklist(refreshToken, jwtUtil.getTimeUntilExpiration(refreshToken));
            }
        } catch (Exception e) {
            log.error("관리자 로그아웃 처리 중 오류가 발생했습니다.", e);
            return Result.Failure.of("로그아웃 처리 중 오류가 발생했습니다.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "관리자 로그아웃 완료.");
    }

    public Result<AdminResponseDto> login(AdminLoginCommand command) {
        AdminEntity adminEntity = AdminEntity.builder()
                .loginId(command.getLoginId())
                .build();

        AdminEntity selectedAdmin = repository.selectOneAdmin(adminEntity);
        if(selectedAdmin == null) {
            return Result.Failure.of("관리자 정보가 존재하지 않음.", ErrorCode.USER_NOT_FOUND.getCode());
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(command.getLoginPw(), selectedAdmin.getLoginPw())) {
            return Result.Failure.of("아이디 또는 비밀번호가 올바르지 않음.", ErrorCode.INVALID_CREDENTIALS.getCode());
        }

        JwtTokenGenerationData jwtData = JwtTokenGenerationData.builder()
                .email(selectedAdmin.getAdminEmail())
                .build();

        jwtUtil.generateAdminAccessToken(jwtData);

        return Result.Success.of(AdminResponseDto.from(selectedAdmin), "관리자 로그인 완료");
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
