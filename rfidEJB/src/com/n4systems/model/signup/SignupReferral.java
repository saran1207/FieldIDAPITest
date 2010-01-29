package com.n4systems.model.signup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityDefiner;

@Entity
@Table(name = "signupreferrals")
public class SignupReferral extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("referralTenant.id", null, "referralUser.uniqueID", null);
	}
	
	@Column(name="signupdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date signupDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "referral_tenant_id", nullable = false)
	private Tenant referralTenant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "referred_tenant_id", nullable = false)
	private Tenant referredTenant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "referral_user_id", nullable = false)
	private UserBean referralUser;
	
	@Override
	protected void onCreate() {
		super.onCreate();
		if (signupDate == null) {
			signupDate = new Date();
		}
	}

	public SignupReferral() {}
	
	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public Tenant getReferralTenant() {
		return referralTenant;
	}

	public void setReferralTenant(Tenant referralTenant) {
		this.referralTenant = referralTenant;
	}

	public Tenant getReferredTenant() {
		return referredTenant;
	}

	public void setReferredTenant(Tenant referredTenant) {
		this.referredTenant = referredTenant;
	}

	public UserBean getReferralUser() {
		return referralUser;
	}

	public void setReferralUser(UserBean referralUser) {
		this.referralUser = referralUser;
	}
	
}
