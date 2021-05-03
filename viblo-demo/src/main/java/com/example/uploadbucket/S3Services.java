package com.example.uploadbucket;

import org.springframework.web.multipart.MultipartFile;


public interface S3Services {
    byte[] downloadFile(String fileName);

    String uploadFile(MultipartFile multipartFile);

    String deleteFile(String fileName);
}
