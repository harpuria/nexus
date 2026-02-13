package com.qwerty.nexus.domain.management.admin.service;

import com.qwerty.nexus.domain.management.admin.dto.request.*;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminListResponseDto;
import com.qwerty.nexus.domain.management.admin.entity.AdminEntity;
import com.qwerty.nexus.domain.management.admin.repository.AdminRepository;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminLoginResponseDto;
import com.qwerty.nexus.domain.management.admin.dto.response.AdminResponseDto;
import com.qwerty.nexus.domain.management.organization.entity.OrganizationEntity;
import com.qwerty.nexus.domain.management.organization.repository.OrganizationRepository;
import com.qwerty.nexus.global.exception.ErrorCode;
import com.qwerty.nexus.global.paging.dto.PagingRequestDto;
import com.qwerty.nexus.global.paging.entity.PagingEntity;
import com.qwerty.nexus.global.response.Result;
import com.qwerty.nexus.global.util.PagingUtil;
import com.qwerty.nexus.global.util.jwt.JwtTokenGenerationData;
import com.qwerty.nexus.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param dto
     * @return
     */
    @Transactional
    public Result<Void> createInitialAdmin(AdminInitCreateRequestDto dto) {
        // 비밀번호 암호화 인코더
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(dto.getLoginPw());

        AdminEntity adminEntity = AdminEntity.builder()
                .loginId(dto.getLoginId())
                .loginPw(encodedPassword)
                .adminNm(dto.getAdminNm())
                .adminEmail(dto.getAdminEmail())
                .adminRole(dto.getAdminRole())
                .createdBy(dto.getLoginId())
                .updatedBy(dto.getLoginId())
                .build();

        // 회원 중복 확인
        boolean isUser = repository.existsByLoginId(adminEntity) > 0;
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
                .orgNm(dto.getOrgNm())
                .orgCd(dto.getOrgCd())
                .createdBy(dto.getLoginId())
                .updatedBy(dto.getLoginId())
                .build();

        Integer orgId = organizationRepository.insertOrganization(organizationEntity);
        if (orgId == null) {
            return Result.Failure.of("단체 정보 생성 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        adminEntity = AdminEntity.builder()
                .loginId(dto.getLoginId())
                .orgId(orgId)
                .loginPw(encodedPassword)
                .adminNm(dto.getAdminNm())
                .adminEmail(dto.getAdminEmail())
                .adminRole(dto.getAdminRole())
                .createdBy(dto.getLoginId())
                .updatedBy(dto.getLoginId())
                .build();

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<Integer> insertRst = Optional.ofNullable(repository.insertAdmin(adminEntity));
        if(insertRst.isEmpty()) {
            return Result.Failure.of("회원가입 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "회원가입 완료.");
    }

    /**
     * 관리자 생성
     * @param dto
     * @return
     */
    public Result<Void> createAdmin(AdminCreateRequestDto dto) {
        // 비밀번호 암호화 인코더
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(dto.getLoginPw());

        AdminEntity adminEntity = AdminEntity.builder()
                .loginId(dto.getLoginId())
                .loginPw(encodedPassword)
                .adminNm(dto.getAdminNm())
                .adminEmail(dto.getAdminEmail())
                .adminRole(dto.getAdminRole())
                .createdBy(dto.getLoginId())
                .updatedBy(dto.getLoginId())
                .orgId(dto.getOrgId())
                .build();

        // 회원 중복 확인
        boolean isUser = repository.existsByLoginId(adminEntity) > 0;
        if(isUser) {
            return Result.Failure.of("이미 사용중인 회원 아이디.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 이메일 중복 확인
        boolean isEmail = repository.existsByEmail(adminEntity) > 0;
        if(isEmail) {
            return Result.Failure.of("이미 사용중인 이메일 주소.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<Integer> insertRst = Optional.ofNullable(repository.insertAdmin(adminEntity));
        if(insertRst.isEmpty()) {
            return Result.Failure.of("회원가입 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "회원가입 완료.");
    }

    /**
     * 관리자 정보 수정
     * @param dto
     * @return
     */
    public Result<Void> updateAdmin(AdminUpdateRequestDto dto) {
        // 변경할 비밀번호가 있는 경우 암호화 처리
        String modifiedPw = null;
        if(dto.getLoginPw() != null){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            modifiedPw = passwordEncoder.encode(dto.getLoginPw());
        }

        AdminEntity adminEntity = AdminEntity.builder()
                .adminId(dto.getAdminId())
                .adminNm(dto.getAdminNm())
                .loginPw(modifiedPw)
                .adminEmail(dto.getAdminEmail())
                .adminRole(dto.getAdminRole())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateRstCnt = repository.updateAdmin(adminEntity);

        if(updateRstCnt <= 0) {
            return Result.Failure.of("회원정보 수정 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "회원정보 수정 성공.");
    }

    /**
     * 관리자 정보 삭제 (논리적 삭제)
     * @param dto
     * @return
     */
    public Result<Void> deleteAdmin(AdminUpdateRequestDto dto) {
        AdminEntity adminEntity = AdminEntity.builder()
                .adminId(dto.getAdminId())
                .isDel(dto.getIsDel())
                .updatedBy(dto.getUpdatedBy())
                .build();

        int updateRstCnt = repository.updateAdmin(adminEntity);

        if(updateRstCnt <= 0) {
            return Result.Failure.of("회원정보 삭제 실패.", ErrorCode.INTERNAL_ERROR.getCode());
        }

        return Result.Success.of(null, "회원정보 삭제 성공.");
    }


    /**
     * 하나의 관리자 정보 조회
     * @param adminId
     * @return
     */
    public Result<AdminResponseDto> getAdmin(int adminId) {
        AdminEntity admin = AdminEntity.builder()
                .adminId(adminId)
                .build();

        Optional<AdminEntity> selectRst = Optional.ofNullable(repository.findByAdminId(admin.getAdminId()));
        if(selectRst.isPresent()) {
            return Result.Success.of(AdminResponseDto.from(selectRst.get()), "관리자 회원 정보 조회 완료.");
        }else{
            return Result.Failure.of("관리자 회원 정보 존재하지 않음.",  ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    /**
     * 관리자 목록 조회
     * @param pagingDto
     * @return
     */
    public Result<AdminListResponseDto> listAdmins(PagingRequestDto pagingDto) {
        PagingEntity pagingEntity = PagingUtil.getPagingEntity(pagingDto);
        int validatedSize = pagingEntity.getSize();
        int safePage = pagingEntity.getPage();

        Optional<List<AdminEntity>> selectRst = Optional.ofNullable(repository.findAllByKeyword(pagingEntity));
        if(selectRst.isEmpty()) {
            return Result.Failure.of("관리자 목록이 존재하지 않음.",  ErrorCode.INTERNAL_ERROR.getCode());
        }

        List<AdminResponseDto> admins = selectRst.get().stream().map(AdminResponseDto::from).toList();

        long totalCount = repository.countActiveAdmins();
        int totalPages = validatedSize == 0 ? 0 : (int) Math.ceil((double) totalCount / validatedSize);
        boolean hasNext = safePage + 1 < totalPages;
        boolean hasPrevious = safePage > 0 && totalPages > 0;

        AdminListResponseDto response = AdminListResponseDto.builder()
                .admins(admins)
                .page(safePage)
                .size(validatedSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        return Result.Success.of(response, "관리자 목록 조회 완료.");
    }

    /**
     * 관리자 로그인
     * @param dto
     * @return
     */
    public Result<AdminLoginResponseDto> loginAdmin(AdminLoginRequestDto dto) {
        if (dto.getLoginId() == null || dto.getLoginId().isBlank()
                || dto.getLoginPw() == null || dto.getLoginPw().isBlank()) {
            return Result.Failure.of("로그인 아이디 또는 비밀번호가 누락되었습니다.", ErrorCode.INVALID_REQUEST.getCode());
        }

        Optional<AdminEntity> adminOptional = repository.findByLoginId(dto.getLoginId());

        if (adminOptional.isEmpty()) {
            return Result.Failure.of("관리자 계정이 존재하지 않습니다.", ErrorCode.USER_NOT_FOUND.getCode());
        }

        AdminEntity admin = adminOptional.get();

        if ("Y".equalsIgnoreCase(admin.getIsDel())) {
            return Result.Failure.of("삭제된 관리자 계정입니다.", ErrorCode.ACCOUNT_DISABLED.getCode());
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(dto.getLoginPw(), admin.getLoginPw())) {
            return Result.Failure.of("아이디 또는 비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_CREDENTIALS.getCode());
        }

        // Jwt token 생성
        JwtTokenGenerationData jwtData = JwtTokenGenerationData.builder()
                .socialId(admin.getAdminId() + "9630")
                .email(admin.getAdminEmail())
                .build();

        String accessToken = jwtUtil.generateAdminAccessToken(jwtData);
        String refreshToken = jwtUtil.generateAdminRefreshToken(jwtData);

        AdminLoginResponseDto response = AdminLoginResponseDto.of(admin, accessToken, refreshToken);

        return Result.Success.of(response, "관리자 로그인 완료");
    }

    /**
     * 관리자 로그아웃
     * @param dto
     * @return
     */
    public Result<Void> logoutAdmin(AdminLogoutRequestDto dto) {
        String accessToken = dto.getAccessToken();
        String refreshToken = dto.getRefreshToken();

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

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
