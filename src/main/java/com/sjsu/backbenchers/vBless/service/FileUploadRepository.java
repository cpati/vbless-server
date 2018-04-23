package com.sjsu.backbenchers.vBless.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sjsu.backbenchers.vBless.entity.FileUpload;


public interface FileUploadRepository extends JpaRepository<FileUpload,String> {
	List<FileUpload> findByFileName(String fileName);
}
