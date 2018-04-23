package com.sjsu.backbenchers.vBless.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.sjsu.backbenchers.vBless.model.FileDetail;

import java.io.InputStream;
import java.util.List;

@Service
public interface FileUploadService {
	public InputStreamResource downloadFile(String keyName);
	public InputStreamResource downloadFile(String keyName,String from);
	public void uploadFile(String keyName, String uploadFilePath);
	public void uploadFile(String keyName,InputStream uploadFile);
	public void deleteFile(String keyName);
	public List<FileDetail> getFileNames();
	
}
