	package com.tnt.project.controllers;
	
	
	import java.util.HashMap;
	import java.util.Map;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	import org.springframework.web.multipart.MultipartFile;
	
	import com.tnt.project.services.FitRoomService;
	
	@RestController
	@RequestMapping("/fitroom")
	public class FitRoomController {
	
	
		@Autowired
		private FitRoomService fitRoomService;
		private final String BASE_URL = "https://platform.fitroom.app/api/tryon/v2";
		
		
	
		// taskId 돌리는 형식 이미지 그대로받아오기
		@PostMapping("/wear")
		public ResponseEntity<Map<String, Object>> createTryOn(
		        @RequestParam("model_image") MultipartFile modelImage,
		        @RequestParam(value = "cloth_image", required = false) MultipartFile upperImage,
		        @RequestParam(value = "lower_cloth_image", required = false) MultipartFile lowerImage,
		        @RequestParam("cloth_type") String clothType) {
	
		
		    String taskId = fitRoomService.createTryOnTask(modelImage, upperImage, lowerImage, clothType);
	
		
		    Map<String, Object> result = Map.of(
		        "status", "ok",
		        "taskId", taskId
		    );
	
		    return ResponseEntity.ok(result);
		}
	
		
		//기존 url 그대로 받아오는 wear 11.19
	//	@PostMapping("/wear")
	//	public ResponseEntity<Map<String, Object>> createTryOn(
	//	        @RequestParam("model_image") MultipartFile modelImage,
	//	        @RequestParam(value = "cloth_image", required = false) MultipartFile upperImage,
	//	        @RequestParam(value = "lower_cloth_image", required = false) MultipartFile lowerImage,
	//	        @RequestParam("cloth_type") String clothType) {
	//		
	//		boolean hdMode = false; // HD 모드 강제 OFF
	//
	//
	//	    String resultImageUrl = fitRoomService.createTryOnAndGetResult(
	//	        modelImage, upperImage, lowerImage, clothType);
	//
	//	
	//	    Map<String, Object> result = Map.of(
	//	        "status", "ok",
	//	        "imageUrl", resultImageUrl
	//	        
	//	    );
	//
	//	    return ResponseEntity.ok(result);
	//	}
	
		
	
		@PostMapping("/save")
		public ResponseEntity<?> saveTryOnResult(
				 @RequestParam("taskId") String taskId,
		        @RequestParam("cloth_type") String clothType,
		        @RequestParam("model_image") MultipartFile modelImage,
		        @RequestParam(value = "cloth_image", required = false) MultipartFile clothImage,
		        @RequestParam(value = "lower_cloth_image", required = false) MultipartFile lowerImage,
		        @RequestParam("memberId") String memberId,
		        @RequestParam("ClosetCategory") String ClosetCategory,
		        @RequestParam(value="lowerCategory", required=false) String lowerCategory,
		        @RequestParam("sex") String modelSex,
		        @RequestParam(value="upperClothColorR", required=false) Integer upperClothColorR,
		        @RequestParam(value="upperClothColorG", required=false) Integer upperClothColorG,
		        @RequestParam(value="upperClothColorB", required=false) Integer upperClothColorB,
		        @RequestParam(value="lowerClothColorR", required=false) Integer lowerClothColorR,
		        @RequestParam(value="lowerClothColorG", required=false) Integer lowerClothColorG,
		        @RequestParam(value="lowerClothColorB", required=false) Integer lowerClothColorB
		
		) {
		    fitRoomService.saveToDB(taskId, clothType, modelImage, clothImage, lowerImage , memberId,ClosetCategory,lowerCategory,modelSex
		    		,upperClothColorR,upperClothColorG,upperClothColorB
		    		,lowerClothColorR,lowerClothColorG,lowerClothColorB);
		    return ResponseEntity.ok("saved");
		}
	

		
		
		@GetMapping("/status")
		public ResponseEntity<Map<String, Object>> checkStatus(@RequestParam("taskId") String taskId) {
		    String imageUrl = fitRoomService.waitForCompletion(taskId); // Task 완료될 때까지 체크
		    Map<String, Object> result = new HashMap<>();
		    result.put("status", imageUrl != null ? "completed" : "pending");
		    result.put("imageUrl", imageUrl); // 완료되면 합성 이미지 URL 반환
		    return ResponseEntity.ok(result);
		}
		
		
		
	}
	
	
	
	
	
