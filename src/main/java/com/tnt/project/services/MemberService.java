package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.MemberDAO;
import com.tnt.project.dto.MemberDTO;

@Service
public class MemberService {

	@Autowired
	private MemberDAO dao;

	@Autowired
	private MailService mailService;  // 인증 메일 보내기 위해 

	@Autowired
	private AuthService authService;

	public void signup(MemberDTO member) {

		// 1) 이메일 인증 여부 확인
		boolean verified = authService.isEmailVerified(member.getEmail());
		if (!verified) {
			// 필요하면 커스텀 예외로 바꿔도 됨
			throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
		}

		// 2) 인증 완료된 이메일이면 회원 DB에 저장
		dao.insertMember(member);
	}

	public MemberDTO findById(String id) {
		return dao.findByUserId(id);
	}

	public void updateMyPage(MemberDTO member) {
		dao.updateMyPage(member);
	}

	// 체형 진단 결과 후 결과 저장하기 버튼 눌렀을 때 member에 body_shape 업데이트
	public int updateBodyShape(String id, String body_shape) {
		return dao.updateBodyShape(id, body_shape);
	}
}
