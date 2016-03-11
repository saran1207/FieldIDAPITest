package com.n4systems.model.signup;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "signupreferrals")
public class SignupReferral extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("referralTenant.id", null, "referralUser.id", null);
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
	private User referralUser;
	
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

	public User getReferralUser() {
		return referralUser;
	}

	public void setReferralUser(User referralUser) {
		this.referralUser = referralUser;
	}
	
}
