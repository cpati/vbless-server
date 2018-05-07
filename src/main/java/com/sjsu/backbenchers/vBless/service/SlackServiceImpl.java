package com.sjsu.backbenchers.vBless.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SlackServiceImpl implements SlackService{

	public void postMessage(String message) {	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("text", message);
		map.add("channel", "campaign");
		map.add("token", "xoxp-358937436453-359692941910-359697534374-b1a36297e8905c7fc2957f8b4f921321");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		
		String url = "https://slack.com/api/chat.postMessage";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
	}
	
//	public static void main(String args[]) {
//		new SlackServiceImpl().postMessage("Testing from java code");
//	}
}
