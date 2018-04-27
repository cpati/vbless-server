package com.sjsu.backbenchers.vBless.controller;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public interface FileUploadService {
	public byte[] downloadFile(String keyName);
	
	public void uploadFile(String keyName,InputStream uploadFile);
	
	
}
