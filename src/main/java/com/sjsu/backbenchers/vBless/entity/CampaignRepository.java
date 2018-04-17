package com.sjsu.backbenchers.vBless.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	List<Campaign> findByCampaignId(Long campaignId);
	
	@Query("SELECT c from Campaign c where c.status =?")
	List<Campaign> findActiveCampaigns(String status);
}