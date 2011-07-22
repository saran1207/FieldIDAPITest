package com.n4systems.model.tenant;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "tenant_settings")
public class TenantSettings extends EntityWithTenant {
	private boolean secondaryOrgsEnabled;
	private int maxEmployeeUsers;
	private int maxLiteUsers;
	private int maxReadOnlyUsers;

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

}
