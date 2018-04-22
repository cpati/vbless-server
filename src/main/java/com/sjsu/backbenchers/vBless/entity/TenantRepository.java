package com.sjsu.backbenchers.vBless.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TenantRepository  extends JpaRepository<Tenant, Long> {
	List<Tenant> findByTenantId(Long tenantId);
	
}