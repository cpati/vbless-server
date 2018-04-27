package com.sjsu.backbenchers.vBless.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.AccessControlList;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.amazonaws.util.IOUtils;

import com.sjsu.backbenchers.vBless.controller.FileUploadService;


import org.slf4j.*;

@Service
@ComponentScan({"com.sjsu.backbenchers.vBless.service"})
public class FileUploadServiceImp implements FileUploadService {

	private Logger logger = LoggerFactory.getLogger(FileUploadServiceImp.class);
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	@Value("${cf.distribution}")
	private String cfDistribution;
	
	@Autowired
	private AmazonS3 s3client;
	
	
	@Override
	public byte[] downloadFile(String keyName) {
		// TODO Auto-generated method stub
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		
		try {
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//TODO : FIX CF code
//	public byte[] downloadFileFrom(String keyName, String from) {
//		// TODO Auto-generated method stub
//		byte file[]=null;
//		if (from=="S3") {
//			file=downloadFile(keyName);
//		} else if (from=="CF") {
//			URL url;
//			try {
//				url = new URL(cfDistribution+ "/"+keyName);
//				System.out.println("url:"+url);
//				InputStream inputStream=url.openStream();
//				file=new InputStreamResource(inputStream);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//			
//		}
//		System.out.println("file:"+file);
//		return file;
//	}

	
	@Override
	public void uploadFile(String keyName, InputStream uploadFile) {
		// TODO Auto-generated method stub
		System.out.println("inside uplad file...........");
		logger.info("uploadFile:" + bucketName + ":" + keyName + ":" + uploadFile);
		try {
			ByteArrayOutputStream tmp = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(uploadFile, tmp);
			byte[] bytes = tmp.toByteArray();
			Long contentLength = Long.valueOf(bytes.length);
			System.out.println("contentLength:"+contentLength);
			System.out.println(tmp);
			//System.out.println(uploadFile);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(contentLength);
			AccessControlList acl = new AccessControlList();
			acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
			//keyName = getKeyName(keyName);
			s3client.putObject(new PutObjectRequest(bucketName, keyName, new ByteArrayInputStream(bytes), metadata).withAccessControlList(acl));
			logger.info("keyName=="+keyName+"\nbucketName=="+bucketName);
			logger.info("===================== Upload File - Done! =====================");

		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	

}
