package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tnt.project.dao.PersonalcolorDAO;
import com.tnt.project.dto.PersonalcolorDTO;

@Service
public class PersonalcolorService {

    @Autowired
    private PersonalcolorDAO dao;

    public int insert(PersonalcolorDTO dto) {

        System.out.println("service 왔어여");

       
        if (dto.getMember_id() == null ) {
            System.out.println("⚠ member_id 가 없어 저장 중단");
            return 0;  // 또는 예외 던져도 됨
        }

        return dao.insert(dto);
    }
    
    public int update(PersonalcolorDTO dto) {
    	System.out.println("업데이트 할게용");
    	return dao.update(dto);
    }
}
