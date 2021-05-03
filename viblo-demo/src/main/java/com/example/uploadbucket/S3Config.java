package com.example.uploadbucket;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class S3Config {

    @Value("${amazonProperties.region}")
    private String region;

    @Value("${amazonProperties.accessKey}")
    private String awsId;

    @Value("${amazonProperties.secretKey}")
    private String awsKey;


    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
    }


    @Bean
    public AmazonS3 s3client() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                                 .withRegion(Regions.fromName(region))
                                                 .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                                                 .build();

        return s3Client;
    }
}

