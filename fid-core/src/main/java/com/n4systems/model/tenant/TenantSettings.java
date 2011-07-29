package com.n4systems.model.tenant;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.security.PasswordPolicy;

@Entity
@Table(name = "tenant_settings")
public class TenantSettings extends EntityWithTenant {
	private boolean secondaryOrgsEnabled;
	
	@Embedded
	private UserLimits userLimits = new UserLimits();
	
	@Embedded
	private AccountPolicy accountPolicy = new AccountPolicy();
	
	@Embedded
	private PasswordPolicy passwordPolicy = new PasswordPolicy();
	
	private boolean gpsCapture;

	public boolean isSecondaryOrgsEnabled() {
		return secondaryOrgsEnabled;
	}

	public void setSecondaryOrgsEnabled(boolean secondaryOrgsEnabled) {
		this.secondaryOrgsEnabled = secondaryOrgsEnabled;
	}

	public UserLimits getUserLimits() {
		return userLimits;
	}

	public void setUserLimits(UserLimits userLimits) {
		this.userLimits = userLimits;
	}

	public void setAccountPolicy(AccountPolicy accountPolicy) {
		this.accountPolicy = accountPolicy;
	}

	public AccountPolicy getAccountPolicy() {
		return accountPolicy;
	}

	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public boolean isGpsCapture() {
		return gpsCapture;
	}

	public void setGpsCapture(boolean gpsCapture) {
		this.gpsCapture = gpsCapture;
	}

}
