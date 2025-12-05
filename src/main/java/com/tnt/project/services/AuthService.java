package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.AuthDAO;
import com.tnt.project.dto.MemberDTO;
import com.tnt.project.utils.Encrypt;

@Service
public class AuthService {

    @Autowired
    private AuthDAO AuthDAO;

    /**
     * 로그인 검증 후, MemberDTO 반환
     */
    public MemberDTO login(String id, String rawPw) {

        // 1) id로 회원 조회
        MemberDTO member = AuthDAO.findByUserId(id);

        if (member == null) {
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

        // 2) 입력 PW SHA-512 해싱
        String encPw = Encrypt.encrypt(rawPw);

        // 3) 비밀번호 비교
        if (!encPw.equals(member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 4) 여기서는 "로그인 성공"만 보장해주고
        //    권한 판별은 컨트롤러에서 수행
        return member;
    }
}
