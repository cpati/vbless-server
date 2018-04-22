package com.sjsu.backbenchers.vBless.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;
import org.hibernate.annotations.Table;
import org.hibernate.validator.constraints.NotBlank;



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
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "LONGBLOB")
	private byte[] imageBlob;
	private String videoUrl;
	private String thumbnailUrl;
	private Long createdBy;
	private Date createdAt;
	private Date lastUpdate;
	private String category;
	private String duration;
	private String goal;
	private String city;
	private String country;
	
	

	public Campaign() {
		super();
	}

	
	


	public Campaign(Long campaignId, Long tenantId, Long userId, String status, String campaignTitle,
			String campaignDescription, String blurb, byte[] imageBlob, String videoUrl, String thumbnailUrl,
			Long createdBy, Date createdAt, Date lastUpdate, String category, String duration, String goal, String city,
			String country) {
		super();
		this.campaignId = campaignId;
		this.tenantId = tenantId;
		this.userId = userId;
		this.status = status;
		this.campaignTitle = campaignTitle;
		this.campaignDescription = campaignDescription;
		this.blurb = blurb;
		this.imageBlob = imageBlob;
		this.videoUrl = videoUrl;
		this.thumbnailUrl = thumbnailUrl;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.lastUpdate = lastUpdate;
		this.category = category;
		this.duration = duration;
		this.goal = goal;
		this.city = city;
		this.country = country;
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



	public byte[] getImageBlob() {
		return imageBlob;
	}


	public void setImageBlob(byte[] imageBlob) {
		this.imageBlob = imageBlob;
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


	public Object getActive() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	

}