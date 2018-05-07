package com.sjsu.backbenchers.vBless.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.regions.*;


@Configuration
public class S3Configuration {
	private Logger logger = LoggerFactory.getLogger(S3Configuration.class);

	@Value("${aws.access_key_id}")
	private String awsAccessKeyId;
	

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;
	
	@Value("${s3.region}")
	private String region;

	@Bean
	public AmazonS3 s3client() {
		logger.info("awsAccessKeyId:    " + awsAccessKeyId);
		logger.info("awsSecretKey:    " + awsSecretKey);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
		/*AmazonS3 s3client =AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();*/
		AmazonS3 s3client =new AmazonS3Client(awsCreds);
		return s3client;
	}
	
	
	
	
}
