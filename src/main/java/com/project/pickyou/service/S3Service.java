package com.project.pickyou.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uplaodSign(MultipartFile file,String fullPath) throws IOException{
        String fileName = fullPath;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        amazonS3.putObject(bucket, fullPath, file.getInputStream(), metadata);
        return "fileName";
    }
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
    public void uploadPdfFile(File pdfFile, String folder) throws IOException {
        String fullPath = folder;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(pdfFile.length());
        metadata.setContentType("application/pdf");

        try (InputStream inputStream = new FileInputStream(pdfFile)) {
            amazonS3.putObject(bucket, fullPath, inputStream, metadata);
        }
    }

    public boolean fileExists(String filePath) {
        return amazonS3.doesObjectExist(bucket, filePath);
    }
    public URL generatePresignedUrl(String filePath) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, filePath)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url;
    }
    public String getPublicUrl(String filePath) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, filePath)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
   /* public void downloadFileToResponse(String s3FilePath, HttpServletResponse response, String fileName) throws IOException {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, s3FilePath));
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
        try (InputStream inputStream = s3Object.getObjectContent();
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
*/
    public void deleteFile(String filePath) {
        amazonS3.deleteObject(bucket, filePath);
    }
}
