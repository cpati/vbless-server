package com.sjsu.backbenchers.vBless.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

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

import com.sjsu.backbenchers.vBless.Constant;
import com.sjsu.backbenchers.vBless.entity.Campaign;
import com.sjsu.backbenchers.vBless.entity.FundDetailsRepository;
import com.sjsu.backbenchers.vBless.entity.Tenant;
import com.sjsu.backbenchers.vBless.entity.TenantRepository;
import com.sjsu.backbenchers.vBless.entity.User;
import com.sjsu.backbenchers.vBless.entity.UserRepository;
import com.sjsu.backbenchers.vBless.service.AWSRoute53Service;
import com.sjsu.backbenchers.vBless.service.FileUploadServiceImp;

@RestController
@RequestMapping("/vBless/")
@CrossOrigin(origins = {"http://docker.for.mac.localhost:8080","http://localhost:8080"})
public class vBlessController {
	
	private static final Logger log = LoggerFactory.getLogger(CampaignRestController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FundDetailsRepository fundDetailsRepository;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private AWSRoute53Service awsRoute53Service;
	
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
			@RequestParam(value="status", required=true) String status,
			@RequestParam(value="createdBy", required=true) Long createdBy,
			@RequestParam(value="createdAt", required=true) Date createdAt,
			@RequestParam(value="lastUpdate", required=true) Date lastUpdate
			) {
		log.info("Inside updateCampaignUser Method. Updating  User Details for User: "+ userId );
		User campaignUser = new User(userId,tenantId, firstname, lastname,role , email, phone, paymentinfo,status,createdBy,createdAt,lastUpdate);
		return userRepository.save(campaignUser);
	}
	
	@RequestMapping("/getFundRaised/{campaignId}")
	public Long getFundRaised(@PathVariable("campaignId") String campaignId) {
		return fundDetailsRepository.findTotalFundRaised(Long.parseLong(campaignId));
	}	
	
	@RequestMapping("/loggedInUser")
	public Principal getLoggedInUser(Principal principal) {
	    return principal;
	}
	

	/* Get all Active Campaigns */
	@RequestMapping(value="/getTenants",method=RequestMethod.GET)
	public ResponseEntity<List<Tenant>> getTenants(){
		log.debug("getTenants");
		System.out.println("chida getTenants");
		List<Tenant> tenants=tenantRepository.findAll();
		
		return new ResponseEntity<List<Tenant>>(tenants,org.springframework.http.HttpStatus.OK);
	} 
	
	/* Get Tenant by Id */
	@RequestMapping(value="/getTenant/{tenantId}",method=RequestMethod.GET)
	public ResponseEntity<Tenant> getTenantById(@PathVariable Long tenantId){
		log.debug("getTenantById");
		Tenant tenant=tenantRepository.findByTenantId(tenantId).get(0);
		
		return new ResponseEntity<Tenant>(tenant,org.springframework.http.HttpStatus.OK);
	}
	
	/* Create a tenant */
	@RequestMapping(value="/createTenant",method=RequestMethod.POST)
	public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant){  
		log.debug("createTenant");
		tenant.setStatus(Constant.ACTIVE);
		
		String logoURL = awsRoute53Service.createTenantDomain(tenant.getBrandName());
		tenant.setLogoUrl(logoURL);
		
		Tenant newTenant=tenantRepository.save(tenant);
		
		
		return new ResponseEntity<Tenant>(newTenant,HttpStatus.CREATED);
	}
}
