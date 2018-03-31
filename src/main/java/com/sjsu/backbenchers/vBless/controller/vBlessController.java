package com.sjsu.backbenchers.vBless.controller;

import java.security.Principal;
import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.backbenchers.vBless.entity.FundDetailsRepository;
import com.sjsu.backbenchers.vBless.entity.User;
import com.sjsu.backbenchers.vBless.entity.UserRepository;

@RestController
@RequestMapping("/vBless/")
@CrossOrigin(origins = {"http://docker.for.mac.localhost:8080","http://localhost:8080"})
public class vBlessController {
	
	private static final Logger log = LoggerFactory.getLogger(CampaignRestController.class);
	
	@Autowired
	private UserRepository userRepository;
	
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
	public User getCampaignUser(@PathVariable("userId") Long userId) {
		log.info("Inside getCampaignUser Method. Retrieving User Deatils for UserId: "+ userId );
		return userRepository.findByUserId(userId);
	}
	
	@RequestMapping("/updateCampaignUser")
	public User updateCampaignUser(@RequestParam(value="userId", required=true) Long userId,
			@RequestParam(value="firstname", required=true) String firstname,
			@RequestParam(value="lastname", required=true) String lastname,
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="paymentinfo", required=true) String paymentinfo,
			@RequestParam(value="tenantId", required=true) Long tenantId,
			@RequestParam(value="role", required=true) String role,
			@RequestParam(value="role", required=true) String status,
			@RequestParam(value="role", required=true) Long createdBy,
			@RequestParam(value="role", required=true) Date createdAt,
			@RequestParam(value="role", required=true) Date lastUpdate
			) {
		log.info("Inside updateCampaignUser Method. Updating  User Details for User: "+ userId );
		User campaignUser = new User(userId,tenantId, firstname, lastname,role , email, phone, paymentinfo,status,createdBy,createdAt,lastUpdate);
		return userRepository.save(campaignUser);
	}
	
	@RequestMapping("/getFundRaised/{campaignId}")
	public Long getFundRaised(@PathVariable("campaignId") String campaignId) {
		return fundDetailsRepository.findTotalFundRaised(Long.parseLong(campaignId));
	}	

}
