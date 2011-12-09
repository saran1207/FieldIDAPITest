package com.n4systems.notifiers.notifications;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;

public class CustomerImportFailureNotification extends ImportFailureNotification {

	private BaseEntity tenant;
	private String label;
	
	public CustomerImportFailureNotification(User notifiyUser, Tenant tenant, String label) {
		super(notifiyUser);
		this.tenant = tenant;
		this.label = label;
	}

	@Override
	public String notificationName() {
		return "customerImportFailed";
	}
	
	@Override
	public String subject() {
		return "Import Failed: " + label + " Import for " + getPrimaryOrg(tenant.getId()).getName();
	}

	public String getLabel() {
		return label;
	}


}
