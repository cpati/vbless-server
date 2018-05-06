package com.sjsu.backbenchers.vBless.service;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.sjsu.backbenchers.vBless.entity.Campaign;
import com.sjsu.backbenchers.vBless.entity.CampaignRepository;
import com.sjsu.backbenchers.vBless.entity.FundDetails;
import com.sjsu.backbenchers.vBless.entity.FundDetailsRepository;
import com.sjsu.backbenchers.vBless.entity.User;
import com.sjsu.backbenchers.vBless.entity.UserRepository;

@Service
public class CheckImageService {
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
		
	@Value("${s3.bucket}")
	private String bucket;
	
	@Value("${aws.access_key_id}")
	private String awsAccessKeyId;
	

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	public boolean flagImage(Long campaignId, String photo){
		boolean suspendCampaign = false;
		
		try {
			// fetch campaign details
			Campaign campaign = campaignRepository.findByCampaignId(campaignId).get(0);
			
	        AWSCredentials credentials;
	        try {
	            credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
	        } catch(Exception e) {
	           throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
	            + "Please make sure that your credentials file is at the correct "
	            + "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
	        }
	        
	        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
	      	         .standard()
	      	         .withRegion(Regions.US_WEST_2)
	      	         .withCredentials(new AWSStaticCredentialsProvider(credentials))
	      	         .build();

	        DetectLabelsRequest request = new DetectLabelsRequest()
	      		  .withImage(new Image()
	      		  .withS3Object(new S3Object()
	      		  .withName(photo).withBucket(bucket)))
	      		  .withMaxLabels(10)
	      		  .withMinConfidence(75F);
	        
           DetectLabelsResult result = rekognitionClient.detectLabels(request);
           List <Label> labels = result.getLabels();

           System.out.println("Detected labels for " + photo);
           for (Label label: labels) {
        	  float conf = label.getConfidence();
        	  String lbl = label.getName();
        	  
              System.out.println(lbl + ": " + conf);
              if (lbl.contentEquals("Gun") && conf > 90.0) {
            	  suspendCampaign = true;  
              }
           }

		} catch(AmazonRekognitionException e) {
				log.error("AWS Rekognition " + e.getMessage());
				e.printStackTrace();
	           } 
		  catch (Exception ex) {
			System.out.println(ex);
		}
		return suspendCampaign;
	}

	
	

}
