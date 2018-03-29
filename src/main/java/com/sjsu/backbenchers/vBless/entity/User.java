package com.sjsu.backbenchers.vBless.entity;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "user")
public class User {
	
	@javax.persistence.Id
	@GeneratedValue
	private Long userId;
	private Long tenantId;
	private String firstname;
	private String lastname;
	private String role;
	private String email;
	private String phone;
	private String paymentinfo;
	private String status;
	private Long createdBy;
	private Date CreatedAt;
	private Date lastUpdate;
	
 	@OneToMany
	@JoinColumn(name="userId",referencedColumnName="userId")
	private List<Campaign> campaigns;
	
	public User() {
		super();
	}
	
	public User(Long userId, Long tenantId, String firstname, String lastname, String role, String email, String phone,
			String paymentinfo, String status, Long createdBy, Date createdAt, Date lastUpdate) {
		super();
		this.userId = userId;
		this.tenantId = tenantId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.paymentinfo = paymentinfo;
		this.status = status;
		this.createdBy = createdBy;
		CreatedAt = createdAt;
		this.lastUpdate = lastUpdate;
	}

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPaymentinfo() {
		return paymentinfo;
	}
	public void setPaymentinfo(String paymentinfo) {
		this.paymentinfo = paymentinfo;
	}
	
	public String getFullName() {
		return this.getFirstname()+" "+this.getLastname();
	}
	
	
}