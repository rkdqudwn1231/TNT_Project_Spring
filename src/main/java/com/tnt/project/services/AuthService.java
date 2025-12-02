package com.tnt.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.AuthDAO;
import com.tnt.project.dto.MemberDTO;
import com.tnt.project.utils.Encrypt;

@Service
public class AuthService {

    @Autowired
    private AuthDAO AuthDAO;

    public List<String> login(String id, String rawPw) {
   
        // 1) userId로 DTO 가져오기
        MemberDTO member = AuthDAO.findByUserId(id);
      
        if (member == null) {
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

        // 2) 입력 PW SHA-512 해싱
        String encPw = Encrypt.encrypt(rawPw);
        System.out.println(member.getPassword());

        System.out.println(encPw);
        // 3) 비밀번호 비교
        if (!encPw.equals(member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 4) 권한 생성
        List<String> roles = new ArrayList<>();

        if ("admin".equals(member.getId())) {
            roles.add("ADMIN");
        } else {
            roles.add("MEMBER");
        }

        return roles;
    }
}
