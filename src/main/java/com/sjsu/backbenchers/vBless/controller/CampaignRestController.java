package com.sjsu.backbenchers.vBless.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.sjsu.backbenchers.vBless.entity.Campaign;
import com.sjsu.backbenchers.vBless.entity.CampaignRepository;
import com.sjsu.backbenchers.vBless.entity.CampaignUserRepository;
import com.sjsu.backbenchers.vBless.entity.FundDetails;
import com.sjsu.backbenchers.vBless.entity.FundDetailsRepository;

@RestController
@RequestMapping("/campaigns")
@MultipartConfig(fileSizeThreshold = 20971520)
@CrossOrigin(origins = {"http://docker.for.mac.localhost:8080","http://localhost:8080"})
public class CampaignRestController {

	private static final Logger log = LoggerFactory.getLogger(CampaignRestController.class);
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	@Autowired
	private CampaignUserRepository campaignUserRepository;

	@Autowired
	private FundDetailsRepository fundDetailsRepository;
	
	/* Get all Campaigns */
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ResponseEntity<List<Campaign>> getCampaigns(){
		log.debug("getCampaigns");
		System.out.println("chida getCampaigns");
		List<Campaign> campaigns=campaignRepository.findActiveCampaigns("N");
		for (Campaign campaign : campaigns) {
			if (campaign.getUserId() != null) {
				campaign.setUserId(campaignUserRepository.findByUserId(campaign.getUserId()).getFullName());
			}
		}
		return new ResponseEntity<List<Campaign>>(campaigns,org.springframework.http.HttpStatus.OK);
	}
	
	/* Get Campaign by Id */
	@RequestMapping(value="/{campaignId}",method=RequestMethod.GET)
	public ResponseEntity<Campaign> getCampaignById(@PathVariable Long campaignId){
		log.debug("getCampaignById");
		Campaign campaign=campaignRepository.findByCampaignId(campaignId).get(0);
		if (campaign.getUserId() != null) {
			campaign.setUserId(campaignUserRepository.findByUserId(campaign.getUserId()).getFullName());
		}
		return new ResponseEntity<Campaign>(campaign,org.springframework.http.HttpStatus.OK);
	}
	
	/* Create a campaign */
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign){
		log.debug("createCampaign");
		System.out.println("createCampaign");
		campaign.setActive("Y");
		Campaign newCampaign=campaignRepository.save(campaign);
		return new ResponseEntity<Campaign>(newCampaign,HttpStatus.CREATED);
	}
	
	/* Add Fund Details for a Campaign*/
	@RequestMapping(value="/addFundDetails",method=RequestMethod.POST)
	public ResponseEntity<FundDetails> addFundDetails(@RequestParam(value="amount_paid", required=true) String amountPaid,
			@RequestParam(value="campaign_id", required=true) Long campaignId,
			@RequestParam(value="user_id", required=false) String userId){
		log.debug("addFundDetails");
		FundDetails fd = new FundDetails();
		fd.setAmountPaid(amountPaid);
		fd.setCampaignId(campaignId);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
		String todayDate = dateFormat.format(new Date());		
		fd.setPaymentDate(todayDate);
		
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
			campaign.setImageBlob(IOUtils.toByteArray(fileInputStream));
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
		campaign.setActive("N");
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
