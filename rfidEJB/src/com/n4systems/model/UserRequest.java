package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.parents.EntityWithTenant;


@Entity
@Table ( name= "userrequest" )
public class UserRequest extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	private String companyName;
	private String phoneNumber; 
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "r_useraccount")
    private UserBean userAccount;
    
    private String comment;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UserBean getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserBean userAccount) {
		this.userAccount = userAccount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
    
    
}
