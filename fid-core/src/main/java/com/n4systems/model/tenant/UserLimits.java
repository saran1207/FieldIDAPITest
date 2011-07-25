package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class UserLimits implements Serializable {
	private int maxEmployeeUsers;
	private int maxLiteUsers;
	private int maxReadOnlyUsers;
	
	public UserLimits() {
		this(1, 0, 0);
	}
	
	public UserLimits(int maxEmployeeUsers, int maxLiteUsers, int maxReadOnlyUsers) {
		this.maxEmployeeUsers = maxEmployeeUsers;
		this.maxLiteUsers = maxLiteUsers;
		this.maxReadOnlyUsers = maxReadOnlyUsers;
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
