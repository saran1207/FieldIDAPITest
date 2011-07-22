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
	private static final long serialVersionUID = 5061168697784244073L;
	
	private boolean secondaryOrgsEnabled;
	private int maxEmployeeUsers;
	private int maxLiteUsers;
	private int maxReadOnlyUsers;
	@Embedded
	private AccountPolicy accountPolicy;
	@Embedded
	private PasswordPolicy passwordPolicy;

	public boolean isSecondaryOrgsEnabled() {
		return secondaryOrgsEnabled;
	}

	public void setSecondaryOrgsEnabled(boolean secondaryOrgsEnabled) {
		this.secondaryOrgsEnabled = secondaryOrgsEnabled;
	}

	public int getMaxEmployeeUsers() {
		return maxEmployeeUsers;
	}

	public void setMaxEmployeeUsers(int maxEmployeeUsers) {
		this.maxEmployeeUsers = maxEmployeeUsers;
	}

	public int getMaxLiteUsers() {
		return maxLiteUsers;
	}

	public void setMaxLiteUsers(int maxLiteUsers) {
		this.maxLiteUsers = maxLiteUsers;
	}

	public int getMaxReadOnlyUsers() {
		return maxReadOnlyUsers;
	}

	public void setMaxReadOnlyUsers(int maxReadonlyUsers) {
		this.maxReadOnlyUsers = maxReadonlyUsers;
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

}
