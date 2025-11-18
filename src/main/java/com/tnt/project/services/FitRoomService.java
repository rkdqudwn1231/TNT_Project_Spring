package com.tnt.project.services;


import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tnt.project.dao.ClosetDAO;
import com.tnt.project.dao.FitRoomDAO;
import com.tnt.project.dao.HistoryDAO;
import com.tnt.project.dao.ModelDAO;
import com.tnt.project.dto.ClosetDTO;
import com.tnt.project.dto.FitRoomDTO;
import com.tnt.project.dto.HistoryDTO;
import com.tnt.project.dto.ModelDTO;

@Service
public class FitRoomService {



	@Autowired
	private FitRoomDAO fdao;

	@Autowired
	private ClosetDAO cdao;

	@Autowired
	private ModelDAO mdao;

	@Autowired
	private HistoryDAO hdao;

	//	@Autowired
	//	private FileService fileService; // 파일 서버/로컬 저장 서비스

	@Value("${fitroom.api-key}")
	private String apiKey;

	// FitRoom API는 REST 기반이므로 POST, GET 요청을 처리
	private final RestTemplate restTemplate = new RestTemplate();

	// 이미지 업로드 
	public String uploadImage(MultipartFile file) {
		String url = "https://platform.fitroom.app/api/images";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-API-KEY", apiKey);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", toResource(file));

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
		ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

		Map data = (Map) response.getBody().get("data");
		return (String) data.get("id");
	}


	//TryOn Task 생성 + 결과 가져오기
	public String createTryOnAndGetResult(MultipartFile modelImage,
			MultipartFile upperImage,
			MultipartFile lowerImage,
			String clothType) {

		// 1. Task 생성
		String taskId = createTryOnTask(modelImage, upperImage, lowerImage, clothType);

		if (taskId == null) throw new RuntimeException("TryOn Task 생성 실패");

		// 2. Task 완료될 때까지 동기 폴링
		String resultImageUrl = waitForCompletion(taskId);

		if (resultImageUrl == null) throw new RuntimeException("TryOn 완료 실패");


		return resultImageUrl; // 최종 이미지 URL 반환
	}



	public String createTryOnTask(MultipartFile modelImage, MultipartFile upperImage,
			MultipartFile lowerImage, String clothType) {


		System.out.println("Creating TryOn task with clothType=" + clothType + "중복1");

		String url = "https://platform.fitroom.app/api/tryon/v2/tasks";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-API-KEY", apiKey);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("model_image", toResource(modelImage));
		body.add("cloth_type", clothType);


		if ("combo".equals(clothType)) {
			if (upperImage != null) body.add("cloth_image", toResource(upperImage));
			if (lowerImage != null) body.add("lower_cloth_image", toResource(lowerImage));
		} else {
			if (upperImage != null) body.add("cloth_image", toResource(upperImage));
		}

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
		ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
		return (String) response.getBody().get("task_id");
	}

	// 대기시간
	public String waitForCompletion(String taskId) {
		String url = "https://platform.fitroom.app/api/tryon/v2/tasks/" + taskId;
		int maxWait = 60; // 최대 60초
		int waited = 0;

		while (waited < maxWait) {
			ResponseEntity<Map> res = restTemplate.getForEntity(url, Map.class);
			Map body = res.getBody();
			if ("completed".equalsIgnoreCase((String) body.get("status"))) {
				return (String) body.get("download_signed_url");
			}
			try {
				Thread.sleep(2000);
				waited += 2;
			} catch (InterruptedException e) {
				break;
			}
		}
		return null;
	}

	// MultipartFile → Resource 변환
	private Resource toResource(MultipartFile file) {
		try {
			return new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			};
		} catch (IOException e) {
			throw new RuntimeException("파일 변환 실패", e);
		}
	}
	  

	
	public void saveToDB(String imageUrl, String clothType, MultipartFile modelImage,
			MultipartFile clothImage, MultipartFile lowerImage, String memberId) {
		System.out.println("UpperImage: " + (clothImage != null));
		System.out.println("LowerImage: " + (lowerImage != null));
		try {
			// ================== 1️⃣ 모델 DB 저장 ==================
			ModelDTO modelDTO = new ModelDTO();
			if (modelImage != null) {
				modelDTO.setModelUrl(Base64.getEncoder().encodeToString(modelImage.getBytes()));
				modelDTO.setModelName(modelImage.getOriginalFilename());
				modelDTO.setMemberId(memberId);
				mdao.insertModel(modelDTO);
			}

			// ================== 2️⃣ FitRoom 기록 DB 저장 ==================
			FitRoomDTO fitRoomDTO = new FitRoomDTO();
			fitRoomDTO.setClothType(clothType);
			fitRoomDTO.setResultUrl(imageUrl);
			fitRoomDTO.setMemberId(memberId);

			if (modelImage != null) {
				fitRoomDTO.setModelImageUrl(Base64.getEncoder().encodeToString(modelImage.getBytes()));
				fitRoomDTO.setModelName(modelImage.getOriginalFilename());
			}
			if (clothImage != null) {
				fitRoomDTO.setUpperImageUrl(Base64.getEncoder().encodeToString(clothImage.getBytes()));
				fitRoomDTO.setUpperName(clothImage.getOriginalFilename());
			}
			if (lowerImage != null) {
				fitRoomDTO.setLowerImageUrl(Base64.getEncoder().encodeToString(lowerImage.getBytes()));
				fitRoomDTO.setLowerName(lowerImage.getOriginalFilename());
			}

			fdao.insertFitRoom(fitRoomDTO);

			// ================== 3️⃣ History DB 저장 ==================
			HistoryDTO historyDTO = new HistoryDTO();
			historyDTO.setMemberId(memberId);
			historyDTO.setResultUrl(imageUrl);
			historyDTO.setName(modelDTO.getModelName());
			historyDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
			historyDTO.setUpperName(fitRoomDTO.getUpperName());
			historyDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
			historyDTO.setLowerName(fitRoomDTO.getLowerName());

			hdao.insertHistory(historyDTO);

			// ================== 4️⃣ Closet DB 저장 ==================
			ClosetDTO closetDTO = new ClosetDTO();
			closetDTO.setMemberId(memberId);
			closetDTO.setClothType(clothType);
			closetDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
			closetDTO.setUpperName(fitRoomDTO.getUpperName());
			closetDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
			closetDTO.setLowerName(fitRoomDTO.getLowerName());

			cdao.insertCloset(closetDTO);

		} catch (IOException e) {
			throw new RuntimeException("이미지 변환 오류", e);
		}
	}










	//
	//	public String createTryOnTaskAndSave(MultipartFile modelImage,
	//			MultipartFile upperImage,
	//			MultipartFile lowerImage,
	//			String clothType) {
	//
	//		// 1️⃣ Task 생성 (이미지 업로드는 나중에 결과가 나오면 수행)
	//		String taskId = createTryOnTask(modelImage, upperImage, lowerImage, clothType);
	//
	//		// 2️⃣ FitRoomDTO에 Task ID만 저장 (DB 저장 최소화)
	//		FitRoomDTO fitRoomDTO = new FitRoomDTO();
	//		fitRoomDTO.setTaskId(taskId);
	//		fitRoomDTO.setClothType(clothType);
	//		fdao.insertFitRoom(fitRoomDTO);
	//
	//		// 3️⃣ Task ID 반환
	//		return taskId;
	//	}


}