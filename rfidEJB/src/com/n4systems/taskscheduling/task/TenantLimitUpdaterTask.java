package com.n4systems.taskscheduling.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.n4systems.model.tenant.AlertStatus;
import com.n4systems.model.tenant.AlertStatusSaver;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.services.limiters.ResourceLimit;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.taskscheduling.TaskExecutor;

public class TenantLimitUpdaterTask extends ScheduledTask {
	private static Logger logger = Logger.getLogger(TenantLimitUpdaterTask.class);
	
	private TenantLimitService limitService;
	private Map<Long, AlertStatus> tenantAlertStatusMap;
	
	public TenantLimitUpdaterTask() {
		super(2 * 60, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask() throws Exception {
		logger.info("Initializing TenantLimitService ... ");
		
		limitService = TenantLimitService.getInstance();
		
		// update the limit levels
		limitService.updateAll();
		
		checkLimitThresholds();
	}

	private void checkLimitThresholds() {
		loadTenantStatusMap();
		
		// notifications
		for (LimitType type: LimitType.values()) {
			if (!type.isAlertByEmail()) {
				continue;
			}
			
			Map<Long, ResourceLimit> limits = limitService.getLimitForType(type);
			
			ResourceLimit limit;
			for (Long tenantId: limits.keySet()) {
				limit = limits.get(tenantId);
				
				if (limit.isUnlimited()) {
					// if they're not limited, we can skip the checks
					continue;
				} 
				
				checkLimitThresholdForTenant(tenantId, type, limit);
			}
		}
	}

	/**
	 * Checks to see if a ResourceLimit for a given tenant has passed an alert threshold.  If it has
	 * it executes a LimitNotificationAlertTask, if it has not it resets the alert status
	 */
	private void checkLimitThresholdForTenant(Long tenantId, LimitType type, ResourceLimit limit) {
		AlertStatus alertStatus = tenantAlertStatusMap.get(tenantId);
		
		// check if we've passed the notification threshold
		if (type.isPassedThreshold(limit)) {
			int threshold = type.getLastPassedThreshold(limit);
			
			if (alertStatus == null || !alertStatus.isLevelAtThreshold(type, threshold)) {
				logger.info("Tenant [" + tenantId + "] has surpassed notification threshold [" + threshold + "%] for the limit type [" + type.name() + "]");
				// either there was no alert status, or our last alert was for a different level
				// in any case, we need to send out the notification
				LimitNotificationAlertTask limitAlertTask = new LimitNotificationAlertTask(tenantId, limit, type, alertStatus);
				TaskExecutor.getInstance().execute(limitAlertTask);
				
			} else {
				logger.debug("Notification has already been sent for Tenant [" + tenantId + "], type [" + type.name() + "], threshold [" + threshold + "%]");
			}
		} else {
			// the limit was nominal, check if we had previously alerted and clear that status
			ResetAlertStatus(type, alertStatus);
		}
	}

	/**
	 * Clears the alert status and updates the db
	 */
	private void ResetAlertStatus(LimitType type, AlertStatus alertStatus) {
		if (alertStatus != null && !alertStatus.isLevelAtNormal(type)) {
			logger.info("Clearing alert status for Tenant [" + alertStatus.getTenantId() + "], Type [" + type.name() + "]");
			
			alertStatus.clearStatus(type);
			
			AlertStatusSaver statusSaver = new AlertStatusSaver();
			statusSaver.update(alertStatus);
		}
	}

	/**
	 * Loads a map of tenant ids to AlertStatus's
	 */
	private Map<Long, AlertStatus> loadTenantStatusMap() {
		tenantAlertStatusMap = new HashMap<Long, AlertStatus>();
		
		AllEntityListLoader<AlertStatus> statusLoader = new AllEntityListLoader<AlertStatus>(AlertStatus.class);
		
		for (AlertStatus status: statusLoader.load()) {
			tenantAlertStatusMap.put(status.getTenantId(), status);
		}
		
		return tenantAlertStatusMap;
	}

}
