package com.n4systems.model.tenant;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserLimits implements Serializable {

    private int maxEmployeeUsers;
	private int maxLiteUsers;
	private int maxReadOnlyUsers;
    private boolean usageBasedUsersEnabled = false;
    private int usageBasedUserEvents;

	
	public UserLimits() {
		this(1, 0, 0, false, 0);
	}

	public UserLimits(int maxEmployeeUsers, int maxLiteUsers, int maxReadOnlyUsers, boolean usageBasedUsersEnabled, int usageBasedUserEvents) {
		this.maxEmployeeUsers = maxEmployeeUsers;
		this.maxLiteUsers = maxLiteUsers;
		this.maxReadOnlyUsers = maxReadOnlyUsers;
        this.usageBasedUsersEnabled = usageBasedUsersEnabled;
        this.usageBasedUserEvents = usageBasedUserEvents;
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

    public boolean isUsageBasedUsersEnabled() {
        return usageBasedUsersEnabled;
    }

    public void setUsageBasedUsersEnabled(boolean usageBasedUsersEnabled) {
        this.usageBasedUsersEnabled = usageBasedUsersEnabled;
    }

    public int getUsageBasedUserEvents() {
        return usageBasedUserEvents;
    }

    public void setUsageBasedUserEvents(int usageBasedUserEvents) {
        this.usageBasedUserEvents = usageBasedUserEvents;
    }
}
