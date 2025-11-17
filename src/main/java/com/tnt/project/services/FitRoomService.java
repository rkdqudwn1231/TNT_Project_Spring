package com.tnt.project.services;


import java.io.IOException;
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



	public String performFitRoom(MultipartFile modelImage,
			MultipartFile upperImage,
			MultipartFile lowerImage,
			String clothType) {

		// 1️⃣ TryOn Task 생성 + 결과 가져오기
		String resultUrl = createTryOnAndGetResult(modelImage, upperImage, lowerImage, clothType);

		// 2️⃣ 모델 이미지 업로드 (DB 저장용)
		String modelUrl = uploadImage(modelImage);
		String upperUrl = upperImage != null ? uploadImage(upperImage) : null;
		String lowerUrl = lowerImage != null ? uploadImage(lowerImage) : null;

		// 3️⃣ 모델 DB 저장
		ModelDTO modelDTO = new ModelDTO();
		modelDTO.setModelUrl(modelUrl);
		modelDTO.setModelName(modelImage.getOriginalFilename());
		mdao.insertModel(modelDTO);

		// 4️⃣ FitRoom 기록 DB 저장
		FitRoomDTO fitRoomDTO = new FitRoomDTO();
		fitRoomDTO.setModelImageUrl(modelUrl);
		fitRoomDTO.setModelName(modelImage.getOriginalFilename());
		fitRoomDTO.setClothType(clothType);
		fitRoomDTO.setUpperImageUrl(upperUrl);
		fitRoomDTO.setUpperName(upperImage != null ? upperImage.getOriginalFilename() : null);
		fitRoomDTO.setLowerImageUrl(lowerUrl);
		fitRoomDTO.setLowerName(lowerImage != null ? lowerImage.getOriginalFilename() : null);
		fitRoomDTO.setResultUrl(resultUrl);
		fdao.insertFitRoom(fitRoomDTO);

		// 5️⃣ History DB 저장
		HistoryDTO historyDTO = new HistoryDTO();
		historyDTO.setResultUrl(resultUrl);
		historyDTO.setName(modelDTO.getModelName());
		historyDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
		historyDTO.setUpperName(fitRoomDTO.getUpperName());
		historyDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
		historyDTO.setLowerName(fitRoomDTO.getLowerName());
		hdao.insertHistory(historyDTO);

		// 6️⃣ Closet DB 저장
		ClosetDTO closetDTO = new ClosetDTO();
		closetDTO.setClothType(clothType);
		closetDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
		closetDTO.setUpperName(fitRoomDTO.getUpperName());
		closetDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
		closetDTO.setLowerName(fitRoomDTO.getLowerName());
		cdao.insertCloset(closetDTO);

		// 최종 합성 이미지 URL 반환
		return resultUrl;
	}


	//
	public String createTryOnTaskAndSave(MultipartFile modelImage,
			MultipartFile upperImage,
			MultipartFile lowerImage,
			String clothType) {

		// 1️⃣ Task 생성 (이미지 업로드는 나중에 결과가 나오면 수행)
		String taskId = createTryOnTask(modelImage, upperImage, lowerImage, clothType);

		// 2️⃣ FitRoomDTO에 Task ID만 저장 (DB 저장 최소화)
		FitRoomDTO fitRoomDTO = new FitRoomDTO();
		fitRoomDTO.setTaskId(taskId);
		fitRoomDTO.setClothType(clothType);
		fdao.insertFitRoom(fitRoomDTO);

		// 3️⃣ Task ID 반환
		return taskId;
	}

}