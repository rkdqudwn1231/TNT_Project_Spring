package com.tnt.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tnt.project.dao.ClosetDAO;
import com.tnt.project.dto.ClosetDTO;
@Service
public class ClosetService {

	@Autowired
	private ClosetDAO closetdao;

	@Autowired
	private FileService Fserv;
	
	
	public int insertCloset(String memberId, String category, String lowerCategory,String clothType, MultipartFile cloth_image,
			MultipartFile lower_cloth_image,
			Integer upperClothColorR, Integer upperClothColorG, Integer upperClothColorB, 
			Integer lowerClothColorR, Integer lowerClothColorG, Integer lowerClothColorB) {
		
		ClosetDTO dto = new ClosetDTO();
		dto.setMemberId(memberId);
		dto.setClothType(clothType);
		System.out.println(memberId);
		
		try {
		    // 상의 이미지 업로드
		    if (cloth_image != null) {
		        String upperUrl = Fserv.upload(
		                cloth_image.getBytes(),
		                "closet/upper/" + System.currentTimeMillis() + "_" + cloth_image.getOriginalFilename(),
		                cloth_image.getContentType()
		        );
		        dto.setUpperImageUrl(upperUrl);
		        dto.setUpperName(cloth_image.getOriginalFilename());

		        // 상의 색상 세팅
		        dto.setUpperColorR(upperClothColorR);
		        dto.setUpperColorG(upperClothColorG);
		        dto.setUpperColorB(upperClothColorB);
		        
		        //카테고리
		        dto.setCategory(category);
		    }

		    // 하의 이미지 업로드
		    if (lower_cloth_image != null) {
		        String lowerUrl = Fserv.upload(
		                lower_cloth_image.getBytes(),
		                "closet/lower/" + System.currentTimeMillis() + "_" + lower_cloth_image.getOriginalFilename(),
		                lower_cloth_image.getContentType()
		        );
		        dto.setLowerImageUrl(lowerUrl);
		        dto.setLowerName(lower_cloth_image.getOriginalFilename());

		        // 하의 색상 세팅
		        dto.setLowerColorR(lowerClothColorR);
		        dto.setLowerColorG(lowerClothColorG);
		        dto.setLowerColorB(lowerClothColorB);
		        
		        //카테고리
		    	dto.setLowerCategory(lowerCategory);
		    }

		} catch(Exception e) {
		    e.printStackTrace();
		}

		
		return closetdao.insertCloset(dto);
		
	}
	
	
	
	
	public List<ClosetDTO> getClosetList(String memberId) {

		return closetdao.getClosetList(memberId);


	}

	public int deleteCloth(Object seq) {

		return closetdao.deleteCloth(seq);
	}




	public int editCloth(Object seq, String name, String type,String category , String url) {
		
		Map<String , Object> param = new HashMap<>();
		param.put("seq", seq);
		param.put("name", name);
		param.put("type", type);
		param.put("category", category);
		param.put("url", url);
		
		return closetdao.editCloth(param);
	}


}
