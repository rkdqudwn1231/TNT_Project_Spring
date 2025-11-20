package com.tnt.project.services;


import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

	@Value("${fitroom.api-key}")
	private String apiKey;

	// FitRoom API는 REST 기반이므로 POST, GET 요청을 처리

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${spring.cloud.gcp.bucket}")
	private String bucketName;

	private final FileService fileService;
	  @Autowired
	    public FitRoomService(FileService fileService) {
	        this.fileService = fileService;
	    }
	
	@Autowired
	private FitRoomDAO fdao;

	@Autowired
	private ClosetDAO cdao;

	@Autowired
	private ModelDAO mdao;

	@Autowired
	private HistoryDAO hdao;


	@Autowired
	private FileService Fserv;



	
	
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
	
	

	// Task 완료 후 signed URL에서 바로 이미지 다운로드 11.20
	public byte[] getResultBytes(String taskId) {
	    // Task 완료될 때까지 기다림
	    String signedUrl = waitForCompletion(taskId);
	    if (signedUrl == null) {
	        throw new RuntimeException("TryOn Task 완료 실패 또는 download_signed_url 없음");
	    }

	    // RestTemplate로 다운로드 시 헤더 추가
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("User-Agent", "Mozilla/5.0");
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

	    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

	    try {
	      ResponseEntity<byte[]> response = restTemplate.exchange(
	        	    URI.create(signedUrl),
	        	    HttpMethod.GET,
	        	    requestEntity,	
	        	    byte[].class
	        	);
	        return response.getBody();
	    } catch (Exception e) {
	        throw new RuntimeException("이미지 다운로드 실패", e);
	    }
	}

	// Task 완료 대기 + signed URL 반환
	public String waitForCompletion(String taskId) {
	    String url = "https://platform.fitroom.app/api/tryon/v2/tasks/" + taskId;
	    int maxWait = 120; // 최대 120초
	    int waited = 0;

	    while (waited < maxWait) {
	        ResponseEntity<Map> res = restTemplate.getForEntity(url, Map.class);
	        Map body = res.getBody();

	        if (body != null && "completed".equalsIgnoreCase((String) body.get("status"))) {
	            String signedUrl = (String) body.get("download_signed_url");
	            if (signedUrl != null) {
	                return signedUrl;
	            } else {
	                throw new RuntimeException("download_signed_url이 없습니다.");
	            }
	        }

	        try {
	            Thread.sleep(2000); // 2초 간격 폴링
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            throw new RuntimeException("Task 완료 대기 중 인터럽트 발생", e);
	        }
	        waited += 2;
	    }

	    throw new RuntimeException("Task 완료 대기 시간 초과");
	}



	

	// 대기시간
	
// 11.19 백업	
//	public String waitForCompletion(String taskId) {
//		String url = "https://platform.fitroom.app/api/tryon/v2/tasks/" + taskId;
//		int maxWait = 60; // 최대 60초
//		int waited = 0;
//
//		while (waited < maxWait) {
//			ResponseEntity<Map> res = restTemplate.getForEntity(url, Map.class);
//			Map body = res.getBody();
//			if ("completed".equalsIgnoreCase((String) body.get("status"))) {
//				return (String) body.get("download_signed_url");
//			}
//			try {
//				Thread.sleep(2000);
//				waited += 2;
//			} catch (InterruptedException e) {
//				break;
//			}
//		}
//		return null;
//	}

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

	//테스트용
	public String createTryOnTask(MultipartFile modelImage, MultipartFile upperImage,
			MultipartFile lowerImage, String clothType) {

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
		Map<String, Object> bodyMap = response.getBody();

		if (bodyMap == null) {
			throw new RuntimeException("API Response is null");
		}

		System.out.println("API Response: " + bodyMap);

		String taskId = null;
		Object dataObj = bodyMap.get("data");
		if (dataObj instanceof Map) {
			Map<String, Object> dataMap = (Map<String, Object>) dataObj;
			taskId = (String) dataMap.get("task_id");
		}

		if (taskId == null) {
			taskId = (String) bodyMap.get("task_id");
		}

		if (taskId == null) {
			throw new RuntimeException("task_id not found in API response");
		}

		System.out.println("Task ID: " + taskId);



		// DB에 taskId 저장 (나중에 재요청 가능)
		FitRoomDTO fitRoomDTO = new FitRoomDTO();
		fitRoomDTO.setTaskId(taskId);
		fitRoomDTO.setClothType(clothType);
		fdao.insertFitRoom(fitRoomDTO);

		return taskId;
	}




	//	public String createTryOnTask(MultipartFile modelImage, MultipartFile upperImage,
	//			MultipartFile lowerImage, String clothType) {
	//
	//		System.out.println("Creating TryOn task with clothType=" + clothType + "중복1");
	//
	//		String url = "https://platform.fitroom.app/api/tryon/v2/tasks";
	//
	//		HttpHeaders headers = new HttpHeaders();
	//		headers.set("X-API-KEY", apiKey);
	//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	//
	//		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	//		body.add("model_image", toResource(modelImage));
	//		body.add("cloth_type", clothType);
	//
	//		if ("combo".equals(clothType)) {
	//			if (upperImage != null) body.add("cloth_image", toResource(upperImage));
	//			if (lowerImage != null) body.add("lower_cloth_image", toResource(lowerImage));
	//		} else {
	//			if (upperImage != null) body.add("cloth_image", toResource(upperImage));
	//		}
	//
	//		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
	//
	//		ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
	//
	//		System.out.println("FITROOM response = " + response.getBody());   // ★ 로그추가 ★
	//
	//		return (String) response.getBody().get("task_id");
	//	}



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



	// 이미지 GCP + DB 저장
	public void saveToDB(String taskId, String clothType, MultipartFile modelImage,
			MultipartFile clothImage, MultipartFile lowerImage, String memberId, 
			String closetCategory, String modelSex) {

		try {
			// ================== 1️⃣ 모델 이미지 GCP 업로드 ==================
			String modelUrl = null;
			if (modelImage != null) {
				modelUrl = Fserv.upload(
						modelImage.getBytes(),
						"models/" + System.currentTimeMillis() + "_" + modelImage.getOriginalFilename(),
						modelImage.getContentType()
						);
			}

			// ================== 2️⃣ 합성 결과 이미지 다운로드 후 GCP 업로드 ==================
			byte[] resultBytes = getResultBytes(taskId);
			String resultUrl = Fserv.upload(
					resultBytes,
					"tryon_results/" + System.currentTimeMillis() + "_result.png",
					"image/png"
					);

			// ================== 3️⃣ FitRoom 테이블 저장 ==================
			FitRoomDTO fitRoomDTO = new FitRoomDTO();
			fitRoomDTO.setClothType(clothType);
			fitRoomDTO.setMemberId(memberId);
			fitRoomDTO.setModelImageUrl(modelUrl);
			fitRoomDTO.setResultUrl(resultUrl);
			if (clothImage != null) {
				fitRoomDTO.setUpperImageUrl(Fserv.upload(
						clothImage.getBytes(),
						"closet/upper/" + System.currentTimeMillis() + "_" + clothImage.getOriginalFilename(),
						clothImage.getContentType()
						));
			}
			if (lowerImage != null) {
				fitRoomDTO.setLowerImageUrl(Fserv.upload(
						lowerImage.getBytes(),
						"closet/lower/" + System.currentTimeMillis() + "_" + lowerImage.getOriginalFilename(),
						lowerImage.getContentType()
						));
			}

			fdao.insertFitRoom(fitRoomDTO);

			// ================== 4️⃣ Model 테이블 저장 ==================
			if (modelImage != null) {
				ModelDTO modelDTO = new ModelDTO();
				modelDTO.setModelUrl(modelUrl);
				modelDTO.setModelName(modelImage.getOriginalFilename());
				modelDTO.setMemberId(memberId);
				modelDTO.setSex(modelSex);
				mdao.insertModel(modelDTO);
			}

			// ================== 5️⃣ History 테이블 저장 ==================
			HistoryDTO historyDTO = new HistoryDTO();
			historyDTO.setMemberId(memberId);
			historyDTO.setResultUrl(resultUrl);
			if (modelImage != null) historyDTO.setName(modelImage.getOriginalFilename());
			if (clothImage != null) historyDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
			if (clothImage != null) historyDTO.setUpperName(clothImage.getOriginalFilename());
			if (lowerImage != null) historyDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
			if (lowerImage != null) historyDTO.setLowerName(lowerImage.getOriginalFilename());
			hdao.insertHistory(historyDTO);

			// ================== 6️⃣ Closet 테이블 저장 ==================
			ClosetDTO closetDTO = new ClosetDTO();
			closetDTO.setMemberId(memberId);
			closetDTO.setClothType(clothType);
			closetDTO.setCategory(closetCategory);
			if (clothImage != null) closetDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
			if (clothImage != null) closetDTO.setUpperName(clothImage.getOriginalFilename());
			if (lowerImage != null) closetDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
			if (lowerImage != null) closetDTO.setLowerName(lowerImage.getOriginalFilename());
			cdao.insertCloset(closetDTO);

		} catch (IOException e) {
			throw new RuntimeException("이미지 처리 오류", e);
		}
	}


	// 11.19 쓰던 url 그대로받기 

	//	private String getDownloadSignedUrl(String taskId) {
	//		if (taskId == null) throw new RuntimeException("taskId is null");
	//
	//		String url = "https://platform.fitroom.app/api/tryon/v2/tasks/" + taskId;
	//		ResponseEntity<Map> res = restTemplate.getForEntity(url, Map.class);
	//		Map body = res.getBody();
	//
	//		if (body == null) throw new RuntimeException("API Response is null");
	//
	//		if ("completed".equalsIgnoreCase((String) body.get("status"))) {
	//			return (String) body.get("download_signed_url");
	//		}
	//
	//		throw new RuntimeException("Task not completed yet");
	//	}



	// 11.19 백업

	//	public void saveToDB(String imageUrl, String clothType, MultipartFile modelImage,
	//			MultipartFile clothImage, MultipartFile lowerImage, String memberId , 
	//			String ClosetCategory , String modelSex) {
	//
	//		try {
	//			// ================== 1️⃣ 모델 DB 저장 ==================
	//			ModelDTO modelDTO = new ModelDTO();
	//			if (modelImage != null) {
	//				modelDTO.setModelUrl(Base64.getEncoder().encodeToString(modelImage.getBytes()));
	//				modelDTO.setModelName(modelImage.getOriginalFilename());
	//				modelDTO.setMemberId(memberId);
	//				modelDTO.setSex(modelSex);
	//				mdao.insertModel(modelDTO);	
	//			}
	//
	//			// ================== 2️⃣ FitRoom 기록 DB 저장 ==================
	//			FitRoomDTO fitRoomDTO = new FitRoomDTO();
	//			fitRoomDTO.setClothType(clothType);
	//			fitRoomDTO.setResultUrl(imageUrl);
	//			fitRoomDTO.setMemberId(memberId);
	//
	//
	//			// DB에 저장하기 위해 이미지 파일을 byte => base64로 변환 => 이후 React에서 base64를 json으로 변환하기
	//			if (modelImage != null) {
	//				fitRoomDTO.setModelImageUrl(Base64.getEncoder().encodeToString(modelImage.getBytes()));
	//				fitRoomDTO.setModelName(modelImage.getOriginalFilename());
	//			}
	//			if (clothImage != null) {
	//				fitRoomDTO.setUpperImageUrl(Base64.getEncoder().encodeToString(clothImage.getBytes()));
	//				fitRoomDTO.setUpperName(clothImage.getOriginalFilename());
	//			}
	//			if (lowerImage != null) {
	//				fitRoomDTO.setLowerImageUrl(Base64.getEncoder().encodeToString(lowerImage.getBytes()));
	//				fitRoomDTO.setLowerName(lowerImage.getOriginalFilename());
	//			}
	//
	//			fdao.insertFitRoom(fitRoomDTO);
	//
	//			// ================== 3️⃣ History DB 저장 ==================
	//			HistoryDTO historyDTO = new HistoryDTO();
	//			historyDTO.setMemberId(memberId);
	//			historyDTO.setResultUrl(imageUrl);
	//			historyDTO.setName(modelDTO.getModelName());
	//			historyDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
	//			historyDTO.setUpperName(fitRoomDTO.getUpperName());
	//			historyDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
	//			historyDTO.setLowerName(fitRoomDTO.getLowerName());
	//
	//			hdao.insertHistory(historyDTO);
	//
	//			// ================== 4️⃣ Closet DB 저장 ==================
	//			ClosetDTO closetDTO = new ClosetDTO();
	//			closetDTO.setMemberId(memberId);
	//			closetDTO.setClothType(clothType);
	//			closetDTO.setCategory(ClosetCategory);
	//			closetDTO.setUpperImageUrl(fitRoomDTO.getUpperImageUrl());
	//			closetDTO.setUpperName(fitRoomDTO.getUpperName());
	//			closetDTO.setLowerImageUrl(fitRoomDTO.getLowerImageUrl());
	//			closetDTO.setLowerName(fitRoomDTO.getLowerName());
	//			cdao.insertCloset(closetDTO);
	//
	//		} catch (IOException e) {
	//			throw new RuntimeException("이미지 변환 오류", e);
	//		}
	//	}




}