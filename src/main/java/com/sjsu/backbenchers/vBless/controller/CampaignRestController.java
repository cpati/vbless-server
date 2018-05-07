package com.sjsu.backbenchers.vBless.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.sjsu.backbenchers.vBless.Constant;
import com.sjsu.backbenchers.vBless.entity.Campaign;
import com.sjsu.backbenchers.vBless.entity.CampaignRepository;
import com.sjsu.backbenchers.vBless.entity.FundDetails;
import com.sjsu.backbenchers.vBless.entity.FundDetailsRepository;
import com.sjsu.backbenchers.vBless.entity.UserRepository;
import com.sjsu.backbenchers.vBless.model.FileDetail;
import com.sjsu.backbenchers.vBless.service.CheckImageService;
import com.sjsu.backbenchers.vBless.service.FileUploadServiceImp;
import com.sjsu.backbenchers.vBless.service.SlackService;

@RestController
@RequestMapping("/{tenantId}/campaigns/")
@MultipartConfig(fileSizeThreshold = 20971520)
@CrossOrigin(origins = {"http://localhost:8080"})
@EnableOAuth2Sso
public class CampaignRestController {

	private static final Logger log = LoggerFactory.getLogger(CampaignRestController.class);
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FundDetailsRepository fundDetailsRepository;
	
	@Autowired
	private FileUploadServiceImp fileUploadServiceImp;

	@Autowired
	private CheckImageService checkImageService;
	
	@Autowired
	private SlackService slackService;
	
	
	/* Get all Active Campaigns */
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ResponseEntity<List<Campaign>> getCampaigns(@PathVariable long tenantId){
		log.debug("getCampaigns");
		System.out.println("chida getCampaigns");
		List<Campaign> campaigns=campaignRepository.findActiveCampaigns(Constant.ACTIVE,tenantId);
		for (Campaign campaign : campaigns) {
			if (campaign.getUserId() != null) {
				campaign.setUserId(userRepository.findByUserId(campaign.getUserId()).getUserId());
			}
			campaign.setImageBlob(fileUploadServiceImp.downloadFile(campaign.getCampaignImageUrl()));
		}
		return new ResponseEntity<List<Campaign>>(campaigns,org.springframework.http.HttpStatus.OK);
	}
	
//	/* Get all Campaigns */
//	@RequestMapping(value="/cam/{status}",method=RequestMethod.GET)
//	public ResponseEntity<List<Campaign>> getCampaigns(@PathVariable String status){
//		log.debug("getCampaigns");
//		System.out.println("chida getCampaigns");
//		List<Campaign> campaigns=campaignRepository.findActiveCampaigns(status);
//		for (Campaign campaign : campaigns) {
//			if (campaign.getUserId() != null) {
//				campaign.setUserId(userRepository.findByUserId(campaign.getUserId()).getUserId());
//			}
//		}
//		return new ResponseEntity<List<Campaign>>(campaigns,org.springframework.http.HttpStatus.OK);
//	}
	
	

	/* Get Campaign by Id */
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	public ResponseEntity<Campaign> getCampaignById(@PathVariable Long campaignId){
		log.debug("getCampaignById");
		Campaign campaign=campaignRepository.findByCampaignId(campaignId).get(0);
		if (campaign.getUserId() != null) {
			campaign.setUserId(userRepository.findByUserId(campaign.getUserId()).getUserId());
		}
		campaign.setImageBlob(fileUploadServiceImp.downloadFile(campaign.getCampaignImageUrl()));
		return new ResponseEntity<Campaign>(campaign,org.springframework.http.HttpStatus.OK);
	}
	 
	/* Create a campaign */
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ResponseEntity<Campaign> createCampaign(@PathVariable long tenantId, @RequestBody Campaign campaign){  
		log.debug("createCampaign");
		campaign.setStatus(Constant.ACTIVE);
		campaign.setTenantId(tenantId);
		Campaign newCampaign=campaignRepository.save(campaign);
		return new ResponseEntity<Campaign>(newCampaign,HttpStatus.CREATED);
	}
	
	
	/* Add Fund Details for a Campaign*/
	@RequestMapping(value="/addFundDetails",method=RequestMethod.POST)
	public ResponseEntity<FundDetails> addFundDetails(@RequestParam(value="amount_paid", required=true) String amountPaid,
			@RequestParam(value="campaign_id", required=true) Long campaignId,
			@RequestParam(value="user_id", required=false) Long userId){
		log.debug("addFundDetails");
		FundDetails fd = new FundDetails();
		fd.setAmountPaid(amountPaid);
		fd.setCampaignId(campaignId);
		
		//SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		//String todayDate = dateFormat.format(new Date());
		java.util.Date todaydate= new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(todaydate.getTime());
		fd.setPaidAt(sqlDate);
		
		fd.setUserId(userId);
		
		fundDetailsRepository.save(fd);
		return new ResponseEntity<FundDetails>(fd,HttpStatus.OK);
	}
	 
	/* Upload File to a campaign */
	@RequestMapping(value="/uploadfile/{campaignId}",method=RequestMethod.POST)
	public ResponseEntity<Campaign> uploadFile(@PathVariable Long campaignId,@RequestParam("fileUpload") MultipartFile fileUpload){
		log.debug("uploadFile");
		FileInputStream fileInputStream=null;
		Campaign campaign=null;
		try {
			fileInputStream =  (FileInputStream) fileUpload.getInputStream();
		
			campaign=campaignRepository.findByCampaignId(campaignId).get(0);
			
			//TODO : Code will fail if image doesn't have an extension
			String photoName = campaignId+ "." + fileUpload.getOriginalFilename();
			campaign.setCampaignImageUrl(campaignId+ "." + fileUpload.getOriginalFilename());
			campaign.setImageBlob(fileUpload.getBytes());
			fileUploadServiceImp.uploadFile(campaign.getCampaignImageUrl(), fileInputStream);

			//Check if the image has any prohibited elements using AWS Image Rekognition
			boolean flagImage = checkImageService.flagImage(campaignId, photoName);
			if (flagImage) {
				campaign.setStatus(Constant.INAPPROPRIATE);
				slackService.postMessage("Campaign " + campaign.getCampaignTitle() + " created with Inappropriate content is deactivated. Please login to activate.");
			}
			campaignRepository.save(campaign);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Campaign>(campaign,HttpStatus.CREATED);
	}
	
	/* Update a campaign */
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public ResponseEntity<Campaign> updateCampaign(@RequestBody Campaign campaign){
		log.debug("updateCampaign");
		Campaign updatedCampaign=campaignRepository.save(campaign);
		return new ResponseEntity<Campaign>(updatedCampaign,HttpStatus.OK);
	}
	
	/* Update a campaign Status */
	@RequestMapping(value="/updateCampaignStatus",method=RequestMethod.POST)
	public ResponseEntity<Campaign> updateCampaignStatus(@RequestParam Long campaignId){
		log.debug("updateCampaignStatus.... " + campaignId);
		Campaign campaign = campaignRepository.findOne(campaignId);
		campaign.setStatus(Constant.SUSPENDED);
		Campaign updatedCampaign=campaignRepository.save(campaign);
		return new ResponseEntity<Campaign>(updatedCampaign,HttpStatus.OK);
	}
	
	/* Delete a campaign */
	@RequestMapping(value="/{campaignId}",method=RequestMethod.DELETE)
	public ResponseEntity<Campaign> deleteCampaign(@PathVariable Long campaignId){
		log.debug("deleteCampaign");
		Campaign campaign=campaignRepository.findByCampaignId(campaignId).get(0);
		campaignRepository.delete(campaign);
		return new ResponseEntity<Campaign>(campaign,HttpStatus.OK);
	}
}
