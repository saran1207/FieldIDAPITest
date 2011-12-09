package com.n4systems.notifiers.notifications;

import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;

public class CustomerImportSuccessNotification extends ImportSuccessNotification {
	
	private Tenant tenant;
	private String label;
	
	public CustomerImportSuccessNotification(User notifyUser, Tenant tenant, String label) {
		super(notifyUser);
		this.tenant = tenant;
		this.label = label;
	}
	
	@Override
	public String notificationName() {
		return "customerImportSuccess";
	}
	
	@Override
	public String subject() {
		return "Import Completed: " + label + " Import for " + getPrimaryOrg(tenant.getId()).getName();
	}

	public String getLabel() {
		return label;
	}

}
