package com.tnt.project.dao;

import org.springframework.stereotype.Repository;

import com.tnt.project.dto.BodySizeDTO;

@Repository
public interface BodySizeDAO {

    // 로그인 사용자만 저장
    int insertBodySize(BodySizeDTO dto);
}
