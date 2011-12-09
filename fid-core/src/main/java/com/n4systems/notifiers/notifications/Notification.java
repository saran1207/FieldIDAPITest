package com.n4systems.notifiers.notifications;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.services.TenantFinder;

public abstract class Notification {

	protected static final String SPACE = " ";
	
	private User to;
	

	public abstract String notificationName();

	public abstract String subject();

	public User getTo() {
		return to;
	}

	public void notifiyUser(User to) {
		this.to = to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (to.hashCode()) + subject().hashCode() + notificationName().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Notification)) {
			return false;
		}
		Notification other = (Notification) obj;
		return equals(other);
	}

	private boolean equals(Notification other) {
		if (!to.equals(other.to) 
				|| !subject().equals(other.subject()) 
				|| !notificationName().equals(other.notificationName())) {
			return false;
		}
		return true;
	}

	protected PrimaryOrg getPrimaryOrg(long tenantId) {
		return TenantFinder.getInstance().findPrimaryOrg(tenantId);
	}
	
}
