package com.tnt.project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
@Service
public class FileService {

    private final Storage storage;
    private final String bucketName;

    public FileService(
            @Value("${spring.cloud.gcp.bucket}") String bucketName,
            @Value("${spring.cloud.gcp.credentials.location}") Resource gcpResource
    ) throws Exception {
        if (!gcpResource.exists()) {
            throw new IllegalStateException("GCP 서비스 계정 파일을 찾을 수 없습니다: " + gcpResource.getFilename());
        }
        this.storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(gcpResource.getInputStream()))
                .build()
                .getService();
        this.bucketName = bucketName;
    }

    
    public String upload(byte[] data, String fileName, String contentType) {
        var blobId = com.google.cloud.storage.BlobId.of(bucketName, fileName);
        var blobInfo = com.google.cloud.storage.BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, data);
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
    
    
    
    // 다운로드 메서드 추가
    public byte[] download(String fileName) {
        var blob = storage.get(bucketName, fileName);
        if (blob == null) {
            throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);
        }
        return blob.getContent();
    }
}
