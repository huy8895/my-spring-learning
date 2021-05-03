package com.example.uploadbucket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class S3Controller {

    @Autowired
    S3Services s3Services;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMultipartFile(
            @RequestParam(value = "file") MultipartFile file) {
        return ResponseEntity.ok(s3Services.uploadFile(file));
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        byte[] downloadInputStream = s3Services.downloadFile(fileName);
        return ResponseEntity.ok()
                             .contentLength(downloadInputStream.length)
                             .contentType(contentType(fileName))
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                             .body(downloadInputStream);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return ResponseEntity.ok(s3Services.deleteFile(fileName));
    }

    private MediaType contentType(String fileName) {
        String[] arr = fileName.split("\\.");
        String type = arr[arr.length - 1]; // get suffix after last dot (.)
        switch (type) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
