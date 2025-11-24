package com.tnt.project.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tnt.project.dto.ChatHistoryDTO;

@Repository
public class ChatbotDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;


	public List<ChatHistoryDTO> getHistory(String userId) {
		return mybatis.selectList("Chatbot.getHistory",userId);
	}
	
	public void insertHistory(String userId, String role, String message) {	
		ChatHistoryDTO chatHistoryDTO = new ChatHistoryDTO(0,userId,role,message,null);
	    
	    mybatis.insert("Chatbot.insertHistory", chatHistoryDTO);
	}
	
	public void insertSummary(String userId, String summary) {	
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", userId);
		map.put("summary", summary);
		
	    mybatis.insert("Chatbot.insertSummary", map);
	}
	
	public int removeHistory(String userId) {
		return mybatis.insert("Chatbot.removeHistory", userId); 
	}
	
	public void removeOldHistory(String userId) {
		mybatis.delete("Chatbot.removeOldHistory", userId); 
	}

}
