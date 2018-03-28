package com.sjsu.backbenchers.vBless.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Date;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.Table;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(appliesTo = "fund_details")
public class FundDetails {
	
	
	@javax.persistence.Id
	@GeneratedValue
	private Long fundPaymentId;
	@NotBlank
	private Long tenantId;
	private Long campaignId;
	private Long userId;
	private String amountPaid;
	private Date paidAt;
	private String status;
	@Column(length=1000)
	private String ppResponse;
	private Long createdBy;
	private Date createdAt;
	private Date lastUpdate;
	
		
	public FundDetails() {
		super();
	}

	
	public FundDetails(Long fundPaymentId, Long tenantId, Long campaignId, Long userId, String amountPaid,
			Date paidAt, String status, String ppResponse, Long createdBy, Date createdAt, Date lastUpdate) {
		super();
		this.fundPaymentId = fundPaymentId;
		this.tenantId = tenantId;
		this.campaignId = campaignId;
		this.userId = userId;
		this.amountPaid = amountPaid;
		this.paidAt = paidAt;
		this.status = status;
		this.ppResponse = ppResponse;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.lastUpdate = lastUpdate;
	}



	public Long getFundPaymentId() {
		return fundPaymentId;
	}

	public void setFundPaymentId(Long fundPaymentId) {
		this.fundPaymentId = fundPaymentId;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Date getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Date paidAt) {
		this.paidAt = paidAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPpResponse() {
		return ppResponse;
	}

	public void setPpResponse(String ppResponse) {
		this.ppResponse = ppResponse;
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

			
}
	
	