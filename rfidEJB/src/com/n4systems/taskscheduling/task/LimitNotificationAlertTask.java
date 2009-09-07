package com.n4systems.taskscheduling.task;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.AlertStatus;
import com.n4systems.model.tenant.AlertStatusSaver;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.services.limiters.ResourceLimit;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.TemplateMailMessage;

public class LimitNotificationAlertTask implements Runnable {
	private static Logger logger = Logger.getLogger(LimitNotificationAlertTask.class);
	
	private Long tenantId;
	private ResourceLimit limit;
	private LimitType type;
	private AlertStatus alertStatus;
	
	public LimitNotificationAlertTask(Long tenantId, ResourceLimit limit, LimitType type, AlertStatus alertStatus) {
		this.tenantId = tenantId;
		this.limit = limit;
		this.type = type;
		this.alertStatus = alertStatus;
	}

	public void run() {
		createAlertStatusIfNull();
		
		int threshold = type.getLastPassedThreshold(limit);
		
		// build up the alert message
		TemplateMailMessage alertMessage = new TemplateMailMessage(type.getEmailSubject(), type.getEmailTemplate());
		
		addNotificationAddresses(alertMessage);
		
		// add in our variables
		alertMessage.getTemplateMap().put("limit", limit);
		alertMessage.getTemplateMap().put("threshold", threshold);
		
		try {
			ServiceLocator.getMailManager().sendMessage(alertMessage);
		
			updateAlertStatus(threshold);
		} catch (MessagingException e) {
			logger.error("Could not send alert limit notification", e);
		} catch (RuntimeException e) {
			logger.error("Could not update alert status", e);
		}
	}

	private void createAlertStatusIfNull() {
		if (alertStatus == null) {
			// create a new alert status if one was not already provided
			alertStatus = new AlertStatus();
			alertStatus.setTenantId(tenantId);
		}
	}

	private void addNotificationAddresses(TemplateMailMessage alertMessage) {
		AdminUserListLoader userLoader = new AdminUserListLoader(new TenantOnlySecurityFilter(tenantId));
		for (UserBean user: userLoader.load()) {
			alertMessage.getToAddresses().add(user.getEmailAddress());
		}
	}
	
	private void updateAlertStatus(int threshold) {
		alertStatus.setLevel(type, threshold);
		
		AlertStatusSaver statusSaver = new AlertStatusSaver();
		statusSaver.update(alertStatus);
	}

}
