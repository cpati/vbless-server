package com.sjsu.backbenchers.vBless.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.Table;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(appliesTo = "tenant")
public class Tenant{
	
	@javax.persistence.Id
	@GeneratedValue
	private Long tenantId;
	@NotBlank
	private String email;
	@NotBlank
	private String businessName;
	@NotBlank
	private String brandName;
	private String phone;
	private String paypalAccount;
	private String logoUrl;
	private String status;
	private Long createdBy;
	private Date createdAt;
	private Date lastUpdate;
	
	private String loginRedirectURL;
	
	public Tenant() {
		super();
		
	}
	
	public Tenant(Long tenantId, String email, String businessName, String brandName, String phone,
			String paypalAccount, String logoUrl, String status, Long createdBy, Date createdAt, Date lastUpdate) {
		super();
		this.tenantId = tenantId;
		this.email = email;
		this.businessName = businessName;
		this.brandName = brandName;
		this.phone = phone;
		this.paypalAccount = paypalAccount;
		this.logoUrl = logoUrl;
		this.status = status;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.lastUpdate = lastUpdate;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPaypalAccount() {
		return paypalAccount;
	}

	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	public String getLoginRedirectURL() {
		return loginRedirectURL;
	}

	public void setLoginRedirectURL(String loginRedirectURL) {
		this.loginRedirectURL = loginRedirectURL;
	}



}
