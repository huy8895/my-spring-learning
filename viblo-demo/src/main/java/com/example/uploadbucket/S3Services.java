package com.example.uploadbucket;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

public interface S3Services {
    ByteArrayOutputStream downloadFile(String keyName);

    void uploadFile(String keyName, MultipartFile file);
}
