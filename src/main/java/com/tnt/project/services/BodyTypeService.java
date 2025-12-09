package com.tnt.project.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.project.dao.BodyTypeDAO;

@Service
public class BodyTypeService {

    @Autowired
    private BodyTypeDAO bodyTypeDAO;

    public Map<String, Object> findBodyResult(String body_type) {
        return bodyTypeDAO.findBodyResult(body_type);
    }
}
