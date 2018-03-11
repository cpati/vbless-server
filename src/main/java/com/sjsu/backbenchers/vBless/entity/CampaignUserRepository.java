package com.sjsu.backbenchers.vBless.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignUserRepository extends JpaRepository<CampaignUser, String>
{
	CampaignUser findByUserId(String userId);
}