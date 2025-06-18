package com.qwerty.nexus.admin;

import com.qwerty.nexus.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.generated.tables.pojos.Admin;
import org.jooq.generated.tables.pojos.Organization;
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

    public AdminResponseDTO login(Admin admin) {
        AdminResponseDTO rst = new AdminResponseDTO();

        Optional<Admin> opt = Optional.ofNullable(adminRepository.selectOneAdmin(admin));

        if (opt.isEmpty()) {
            rst.setMessage("존재하지 않는 회원입니다.");
        }
        else{
            Admin adminInfo = opt.get();
            rst.convertPojoToDTO(adminInfo);

            // 비밀번호 매치 체크
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if(!passwordEncoder.matches(admin.getLoginPw(), adminInfo.getLoginPw())){
                rst.setMessage("잘못된 비밀번호 입니다.");
                return rst;
            }

            if(adminInfo.getAdminRole().equals(AdminRole.NO_ROLE.name())){
                rst.setMessage("권한 승인 되지 않은 회원입니다. 넥서스 관리자에게 문의하세요.");
                return rst;
            }
            else{
                rst.setMessage("로그인 성공");
                rst.setCode(1); // 임시 스테이터스 1은 성공
            }
            // 로그인을 했으니 로그인 정보를 세션에 등록하자.
            // JWT 를 써도 될거 같은데.. 그냥 운영툴이니 세션에 등록하는게 편할거 같기도 하고.
            // 세션 클러스터링도 고려해야함 (WAS 가 여러개 (docker) 인 경우가 있으니)
        }

        return rst;
    }

    public AdminResponseDTO register(AdminRequestDTO admin) {
        AdminResponseDTO rst = new AdminResponseDTO();

        // 회원 중복 확인
        boolean isUser = adminRepository.isUserAlreadyRegistered(admin) > 0;
        if(isUser) {
            rst.setMessage("이미 존재하는 회원입니다.");
            return rst;
        }

        // 이메일 중복 확인
        boolean isEmail = adminRepository.existsByEmail(admin) > 0;
        if(isEmail) {
            rst.setMessage("이미 사용중인 이메일 주소 입니다.");
            return rst;
        }

        // 총괄(SUPER) 관리자 아이디 신청 여부 확인
        // 1. 맞으면 단체 정보 등록 추가
        // 2. 아니면 바로 다음으로 넘어감
        if(admin.getAdminRole().equals(AdminRole.SUPER.name())){
            // 여기에 단체 정보 등록 하면 됨
            orgService.register(admin.getOrganization());
        }

        // 비밀번호 암호화 처리
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        admin.setLoginPw(passwordEncoder.encode(admin.getLoginPw()));

        // 중복 확인이 끝났으면 회원 등록(INSERT) 수행
        Optional<Admin> insertRst = Optional.ofNullable(adminRepository.insertAdmin(admin));
        if(insertRst.isPresent()) {
            rst.convertPojoToDTO(insertRst.get());
            rst.setMessage("회원가입이 정상적으로 수행되었습니다.");
        }
        else{
            rst.setMessage("회원가입이 실패하였습니다. 넥서스 관리자에게 문의해주세요.");
        }

        return rst;
    }

    public AdminResponseDTO update(Admin admin) {
        AdminResponseDTO rst = new AdminResponseDTO();

        // 변경할 비밀번호가 있는 경우 암호화 처리
        if(!admin.getLoginPw().isEmpty()){
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            admin.setLoginPw(passwordEncoder.encode(admin.getLoginPw()));
        }

        Optional<Admin> updateRst = Optional.ofNullable(adminRepository.updateAdmin(admin));

        if(updateRst.isPresent()) {
            rst.convertPojoToDTO(updateRst.get());
            rst.setMessage("회원정보가 정상적으로 수정되었습니다.");
        }
        else{
            rst.setMessage("회원정보 수정에 실패하였습니다. 총괄 관리자에게 문의해주세요.");
        }

        return rst;
    }

    public AdminResponseDTO selectOneAdmin(int adminId) {
        AdminResponseDTO rst = new AdminResponseDTO();

        Admin admin = new Admin();
        admin.setAdminId(adminId);

        Optional<Admin> selectRst = Optional.ofNullable(adminRepository.selectOneAdmin(admin));
        if(selectRst.isPresent()) {
            rst.convertPojoToDTO(selectRst.get());
            rst.setMessage("관리자 회원 정보가 정상적으로 검색되었습니다.");
        }else{
            rst.setMessage("관리자 회원 정보가 존재하지 않습니다.");
        }

        return rst;
    }
}
