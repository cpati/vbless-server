package com.sjsu.backbenchers.vBless.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sjsu.bb.vBless.util.Utility;
import com.sjsu.backbenchers.vBless.controller.FileUploadService;
import com.sjsu.backbenchers.vBless.entity.FileUpload;
import com.sjsu.backbenchers.vBless.model.FileDetail;

import org.slf4j.*;

@Service
@ComponentScan({"com.sjsu.backbenchers.vBless.service"})
public class FileUploadServiceImp implements FileUploadService {

	private Logger logger = LoggerFactory.getLogger(FileUploadServiceImp.class);
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	@Autowired
	private FileUploadRepository fileUploadRepository;
	
	@Autowired
	private AmazonS3 s3client;
	
	
	@Override
	public InputStreamResource downloadFile(String keyName) {
		// TODO Auto-generated method stub
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		return new InputStreamResource(inputStream);	}

	@Override
	public InputStreamResource downloadFile(String keyName, String from) {
		// TODO Auto-generated method stub
		InputStreamResource file=null;
		if (from=="S3") {
			file=downloadFile(keyName);
		} else if (from=="CF") {
			URL url;
			try {
				url = new URL("http://d30lkyn29uha7q.cloudfront.net/"+keyName);
				System.out.println("url:"+url);
				InputStream inputStream=url.openStream();
				file=new InputStreamResource(inputStream);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		System.out.println("file:"+file);
		return file;
	}

	@Override
	public void uploadFile(String keyName, String uploadFilePath) {
		try {

			File file = new File(uploadFilePath);
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
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
		}

	}

	@Override
	public void deleteFile(String keyName) {
		// TODO Auto-generated method stub
		logger.info("deleteFile:" + keyName + ":" + bucketName);
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
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
		}

	}


	
	
	

	

	@Override
	public List<FileDetail> getFileNames() {
		// TODO Auto-generated method stub
		List<FileDetail> files = new ArrayList<>();

		try {
			System.out.println("Listing objects");
			final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
			ListObjectsV2Result result;
			String owner = "Chidananda Pati";
			String lastModified = null;
			do {
				result = s3client.listObjectsV2(req);

				for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {

					try {
						lastModified = Utility.dateFormatter(objectSummary.getLastModified());
					} catch (NullPointerException e) {
						lastModified = Utility.dateFormatter(new Date());
					}		
					List<FileUpload> fileUpload=fileUploadRepository.findByFileName(objectSummary.getKey());
					if (fileUpload.size() >0) {
						System.out.println("fileUpload:"+fileUpload.get(0));
						System.out.println(
								" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
						files.add(new FileDetail(objectSummary.getKey(),fileUpload.get(0).getFileDesc(),
												fileUpload.get(0).getFirstName()+" "+fileUpload.get(0).getLastName(),
												fileUpload.get(0).getUpdateDate()));
					}
				}
				System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
				req.setContinuationToken(result.getNextContinuationToken());
			} while (result.isTruncated() == true);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, " + "which means your request made it "
					+ "to Amazon S3, but was rejected with an error response " + "for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, " + "which means the client encountered "
					+ "an internal error while trying to communicate" + " with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

		return files;
	}

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

	public String getKeyName(String keyName) {
		List<FileDetail> list = getFileNames();
		for(FileDetail filedetial :list) {
			
		}
		return keyName;
	}

	

}
