package com.tnt.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.project.dto.MemberDTO;
import com.tnt.project.services.MemberService;


@RestController
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
    @PostMapping("/signup")
    public String signup(@RequestBody MemberDTO member) {
        memberService.signup(member);
        return "{\"message\":\"회원가입 성공\"}";
    }

	
	
}
