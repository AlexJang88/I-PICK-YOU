package com.project.pickyou.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fullPath = folder + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        System.out.println("-----------------------------fileName"+fileName);
        System.out.println("-----------------------------bucket"+bucket);

        amazonS3.putObject(bucket, fullPath, file.getInputStream(), metadata);

        return fileName; // S3에서 파일의 경로를 반환
    }

    public void deleteFile(String filePath) {
        amazonS3.deleteObject(bucket, filePath);
    }
}
