package com.sjsu.backbenchers.vBless.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.hibernate.annotations.Table;

import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(appliesTo = "campaign")
public class Campaign {

	@javax.persistence.Id
	@GeneratedValue
	private Long campaignId;
	
	private Long tenantId;
	
	private Long userId;
	

	private String status;
	private String campaignTitle;
	
	@Column(length=1500)
	private String campaignDescription;
	private String blurb;
	
	@JsonInclude()
	@Transient
	private byte[] imageBlob;
	
	
	private String campaignImageUrl;
	

	private String videoUrl;
	private String thumbnailUrl;
	private Long createdBy;
	private Date createDate;
	private Date lastUpdate;
	private String category;
	private String duration;
	private String goal;
	private String city;
	private String country;
	
	

	public Campaign() {
		super();
	}

	
	


	



	@Override
	public String toString() {
		return "Campaign [campaignId=" + campaignId + ", tenantId=" + tenantId + ", userId=" + userId + ", status="
				+ status + ", campaignTitle=" + campaignTitle + ", campaignDescription=" + campaignDescription
				+ ", blurb=" + blurb + ", campaignImageUrl=" + campaignImageUrl + ", videoUrl=" + videoUrl
				+ ", thumbnailUrl=" + thumbnailUrl + ", createdBy=" + createdBy + ", createDate=" + createDate
				+ ", lastUpdate=" + lastUpdate + ", category=" + category + ", duration=" + duration + ", goal=" + goal
				+ ", city=" + city + ", country=" + country + "]";
	}









	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public Long getTenantId() {
		return tenantId;
	}


	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getVideoUrl() {
		return videoUrl;
	}


	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}


	public Long getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	public String getCampaignTitle() {
		return campaignTitle;
	}

	public void setCampaignTitle(String campaignTitle) {
		this.campaignTitle = campaignTitle;
	}

	public String getCampaignDescription() {
		return campaignDescription;
	}

	public void setCampaignDescription(String campaignDescription) {
		this.campaignDescription = campaignDescription;
	}

	/**
	 * @return the blurb
	 */
	public String getBlurb() {
		return blurb;
	}

	/**
	 * @param blurb
	 *            the blurb to set
	 */
	public void setBlurb(String blurb) {
		this.blurb = blurb;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCampaignImageUrl() {
		return campaignImageUrl;
	}

	public void setCampaignImageUrl(String campaignImageUrl) {
		this.campaignImageUrl = campaignImageUrl;
	}

	
	public byte[] getImageBlob() {		
		return imageBlob;
	}

	public void setImageBlob(byte[] imageBlob) {
		this.imageBlob = imageBlob;
	}


}