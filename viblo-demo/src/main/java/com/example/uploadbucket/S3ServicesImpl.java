package com.example.uploadbucket;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Objects;

@Service
public class S3ServicesImpl implements S3Services {
    private final Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

    private final AmazonS3 s3client;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    public S3ServicesImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    @Override
    public byte[] downloadFile(String keyName) {
        try {
            S3Object s3object = s3client.getObject(bucketName, keyName);
            InputStream is = s3object.getObjectContent();
            return IOUtils.toByteArray(is);
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            writeLog(ase);
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
            throw ace;
        }

        return null;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = generateFileName(file);
        try {
            File convertedFile = convertMultiPartToFile(file);
            s3client.putObject(bucketName, fileName, convertedFile);
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            writeLog(ase);
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
            throw ace;
        }

        return "File uploaded : " + fileName;
    }

    @Override
    public String deleteFile(String fileName) {
        s3client.deleteObject(bucketName, fileName);
        return fileName + " removed!!";
    }


    private void writeLog(AmazonServiceException ase) {
        logger.info("Error Message:    " + ase.getMessage());
        logger.info("HTTP Status Code: " + ase.getStatusCode());
        logger.info("AWS Error Code:   " + ase.getErrorCode());
        logger.info("Error Type:       " + ase.getErrorType());
        logger.info("Request ID:       " + ase.getRequestId());
        throw ase;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return System.currentTimeMillis() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename())
                                                         .replace(" ", "_");
    }

}
