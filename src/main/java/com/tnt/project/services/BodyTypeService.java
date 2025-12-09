package com.tnt.project.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.BodyTypeDAO;
import com.tnt.project.dto.BodyTypeDTO;

@Service
public class BodyTypeService {

    @Autowired
    private BodyTypeDAO bodyTypeDAO;

    public Map<String, Object> findBodyResult(String body_type,String gender) {
        return bodyTypeDAO.findBodyResult(body_type,gender);
    }
}
