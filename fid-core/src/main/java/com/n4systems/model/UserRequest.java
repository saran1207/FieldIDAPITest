package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;


@Entity
@Table ( name= "userrequest" )
public class UserRequest extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	private String companyName;
	private String phoneNumber; 
	private String city;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User userAccount;
    
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

	public User getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(User userAccount) {
		this.userAccount = userAccount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
    
    
}
