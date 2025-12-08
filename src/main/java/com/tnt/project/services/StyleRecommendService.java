package com.tnt.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.StyleRecommendDAO;
import com.tnt.project.dto.PersonalcolorDTO;
import com.tnt.project.dto.StyleRecommendDTO;

@Service
public class StyleRecommendService {

    @Autowired
    private StyleRecommendDAO styleRecommendDAO;

    public List<StyleRecommendDTO> getRecommendList(String body_type, String gender, String cloth_type) {
        return styleRecommendDAO.getRecommendList(body_type, gender, cloth_type);
    }
  
}
