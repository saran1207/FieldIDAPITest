package com.n4systems.model.tenant;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserLimits implements Serializable {

    private int maxEmployeeUsers;
	private int maxLiteUsers;
	private int maxReadOnlyUsers;
    private boolean unlimitedUsersEnabled = false;
    private int unlimitedUserEvents;

	
	public UserLimits() {
		this(1, 0, 0, false, 0);
	}

	public UserLimits(int maxEmployeeUsers, int maxLiteUsers, int maxReadOnlyUsers, boolean unlimitedUsersEnabled, int unlimitedUserEvents) {
		this.maxEmployeeUsers = maxEmployeeUsers;
		this.maxLiteUsers = maxLiteUsers;
		this.maxReadOnlyUsers = maxReadOnlyUsers;
        this.unlimitedUsersEnabled = unlimitedUsersEnabled;
        this.unlimitedUserEvents = unlimitedUserEvents;
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

    public boolean isUnlimitedUsersEnabled() {
        return unlimitedUsersEnabled;
    }

    public void setUnlimitedUsersEnabled(boolean unlimitedUsersEnabled) {
        this.unlimitedUsersEnabled = unlimitedUsersEnabled;
    }

    public int getUnlimitedUserEvents() {
        return unlimitedUserEvents;
    }

    public void setUnlimitedUserEvents(int unlimitedUserEvents) {
        this.unlimitedUserEvents = unlimitedUserEvents;
    }
}
