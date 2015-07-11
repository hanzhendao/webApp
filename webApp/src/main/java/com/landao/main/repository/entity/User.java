package com.landao.main.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 用户实体bean
 * @author snow
 *
 */
@Entity
@Table(name = "USER")
public class User {
	@Id
	@GenericGenerator(name = "userGenerator", strategy = "identity")
	@GeneratedValue(generator = "userGenerator")	
	@Column(name = "USERID", unique = true, nullable = false, precision = 20, scale = 0)
	private Long userId;
	@Column(name = "USERNAME")
	private String userName;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "USERTYPE")
	private String userType;
	@Column(name = "USERSTATUS")
	private String userStatus;
	@Column(name = "ONLINESTATUS")
	private String onlineStatus;
	@Column(name = "LASTLOGINTIME")
	private String lastLoginTime;
	@Column(name = "USERPORTRAIT")
	private String userPortrait;
	@Column(name = "CREATETIME")
	private String createTime;
	@Column(name = "SALT")
	private String salt;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getUserPortrait() {
		return userPortrait;
	}
	public void setUserPortrait(String userPortrait) {
		this.userPortrait = userPortrait;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
