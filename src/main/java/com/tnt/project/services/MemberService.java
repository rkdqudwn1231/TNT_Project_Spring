package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.MemberDAO;
import com.tnt.project.dto.MemberDTO;
import com.tnt.project.utils.Encrypt;

@Service
public class MemberService {

    @Autowired
    private MemberDAO dao;
    

    @Autowired
    private AuthService authService;

    public void signup(MemberDTO member) {

        // 1) 이메일 인증 여부 확인
        boolean verified = authService.isEmailVerified(member.getEmail());
        if (!verified) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        // 2) 비밀번호 암호화
        String rawPw = member.getPassword();
        String encPw = Encrypt.encrypt(rawPw);
        member.setPassword(encPw);

        // 3) 암호화된 비밀번호로 저장
        dao.insertMember(member);
    }

    public MemberDTO findById(String id) {
        return dao.findByUserId(id);
    }

    public void updateMyPage(MemberDTO member) {
        dao.updateMyPage(member);
    }

	public boolean checkId(String id) {
		int cnt = dao.checkId(id);
        return cnt > 0;
	}

	public boolean checkNickname(String nickname) {
		 int cnt = dao.checkNickname(nickname);
	     return cnt > 0;
	}

	public String findIdByEmail(String email) {
		return dao.findIdByEmail(email);
	}

	 public boolean updatePassword(String email, String rawPassword) {
	        // 비밀번호 암호화
	        String encryptedPw = Encrypt.encrypt(rawPassword);

	        int result = dao.updatePassword(email, encryptedPw);

	        return result > 0; // 업데이트 성공 여부
	    }
	// 체형 진단 결과 후 결과 저장하기 버튼 눌렀을 때 member에 body_shape 업데이트
	public int updateBodyShape(String id, String body_shape) {
		return dao.updateBodyShape(id, body_shape);
	}
}
