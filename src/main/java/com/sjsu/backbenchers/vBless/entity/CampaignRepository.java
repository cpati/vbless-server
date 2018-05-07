package com.sjsu.backbenchers.vBless.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	List<Campaign> findByCampaignId(Long campaignId);
	
	 //To get all Campaigns of a tenant.
	List<Campaign> findByTenantId(Long tenantId);
	
	@Query("SELECT c from Campaign c where c.status =? and c.tenantId =?")
	List<Campaign> findActiveCampaigns(String status, long tenantId);
}