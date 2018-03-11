package com.sjsu.backbenchers.vBless.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.backbenchers.vBless.entity.CampaignUser;
import com.sjsu.backbenchers.vBless.entity.CampaignUserRepository;
import com.sjsu.backbenchers.vBless.entity.FundDetailsRepository;

@RestController
@RequestMapping("/vBless/")
@CrossOrigin(origins = {"http://docker.for.mac.localhost:8080","http://localhost:8080"})
public class vBlessController {
	
	private static final Logger log = LoggerFactory.getLogger(CampaignRestController.class);
	
	@Autowired
	private CampaignUserRepository campaignUserRepository;
	
	@Autowired
	private FundDetailsRepository fundDetailsRepository;
	
	@RequestMapping("/user")
	public Principal user(Principal principal) {
	    return principal;
	}
	
	@RequestMapping("/test")
	public String test() {
		return "Hello World!!!";
	}
	
	@RequestMapping("/getCampaignUser/{userId:.+}")
	public CampaignUser getCampaignUser(@PathVariable("userId") String userId) {
		log.info("Inside getCampaignUser Method. Retrieving User Deatils for UserId: "+ userId );
		return campaignUserRepository.findByUserId(userId);
	}
	
	@RequestMapping("/updateCampaignUser")
	public CampaignUser updateCampaignUser(@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="firstname", required=true) String firstname,
			@RequestParam(value="lastname", required=true) String lastname,
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="paymentinfo", required=true) String paymentinfo
			) {
		log.info("Inside updateCampaignUser Method. Updating  User Details for User: "+ userId );
		CampaignUser campaignUser = new CampaignUser(userId, firstname, lastname, email, phone, paymentinfo);
		return campaignUserRepository.save(campaignUser);
	}
	
	@RequestMapping("/getFundRaised/{campaignId}")
	public Long getFundRaised(@PathVariable("campaignId") String campaignId) {
		return fundDetailsRepository.findTotalFundRaised(Long.parseLong(campaignId));
	}	

}
