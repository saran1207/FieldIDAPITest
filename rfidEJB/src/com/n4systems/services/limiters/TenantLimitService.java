package com.n4systems.services.limiters;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrgCountLoader;
import com.n4systems.model.product.ProductLimitCountLoader;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.user.EmployeeUserCountLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.TenantCache;
import com.n4systems.usage.TenantDiskUsageCalculator;

public class TenantLimitService implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TenantLimitService.class);

	private static final TenantLimitService self = new TenantLimitService();
	
	public static TenantLimitService getInstance() {
		return self;
	}
	
	private final Map<Long, ResourceLimit> diskSpace = new ConcurrentHashMap<Long, ResourceLimit>();
	private final Map<Long, ResourceLimit> employeeUsers = new ConcurrentHashMap<Long, ResourceLimit>();
	private final Map<Long, ResourceLimit> assets = new ConcurrentHashMap<Long, ResourceLimit>();
	private final Map<Long, ResourceLimit> secondaryOrgs = new ConcurrentHashMap<Long, ResourceLimit>();
	
	private final ProductLimitCountLoader productCountLoader = new ProductLimitCountLoader();
	private final EmployeeUserCountLoader employeeCountLoader = new EmployeeUserCountLoader();
	private final SecondaryOrgCountLoader secondaryOrgCountLoader = new SecondaryOrgCountLoader();
	
	private final LimitUpdater[] limitUpdaters = {
			new LimitUpdater(new TenantDiskUsageCalculator(), diskSpace),
			new LimitUpdater(productCountLoader, assets),
			new LimitUpdater(employeeCountLoader, employeeUsers),
			new LimitUpdater(secondaryOrgCountLoader, secondaryOrgs),
	};
	
	private TenantLimitService() {}
	
	public ResourceLimit getDiskSpace(Long tenantId) {
		ResourceLimit limit = diskSpace.get(tenantId);
		return limit;
	}
	
	public ResourceLimit getEmployeeUsers(Long tenantId) {
		// user counts are refreshed in real-time
		refreshEmployeeUserCount(tenantId);
		
		ResourceLimit limit = employeeUsers.get(tenantId);
		return limit;
	}
	
	public ResourceLimit getAssets(Long tenantId) {
		// asset counts are refreshed in real-time
		refreshAssetCount(tenantId);
		
		ResourceLimit limit = assets.get(tenantId);
		return limit;
	}
	
	public ResourceLimit getSecondaryOrgs(Long tenantId) {
		// secondaryorg counts are refreshed in real-time
		refreshSecondaryOrgCount(tenantId);
		
		ResourceLimit limit = secondaryOrgs.get(tenantId);
		return limit;
	}
	
	public Map<Long, ResourceLimit> getLimitForType(LimitType type) {
		Map<Long, ResourceLimit> limits = null;
		
		switch (type) {
			case DISK_SPACE:
				limits = diskSpace;
				break;
			case EMPLOYEE_USERS:
				limits = employeeUsers;
				break;
			case ASSETS:
				limits = assets;
				break;
			case SECONDARY_ORGS:
				limits = secondaryOrgs;
				break;
		}
		
		return limits;
	}
	
	/**
	 * Updates all limits for all tenants
	 */
	public void updateAll() {
		long startTime = System.currentTimeMillis();
		
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			// for each tenant, update all the limits.
			for (PrimaryOrg primaryOrg: TenantCache.getInstance().findAllPrimaryOrgs()) {
				logger.debug("Updating all limits for [" + primaryOrg.toString() + "]");
				for (LimitUpdater updater: limitUpdaters) {
					updater.updateLimitMap(primaryOrg, transaction);
				}
			}

			PersistenceManager.finishTransaction(transaction);
		} catch(Exception e) {
			PersistenceManager.rollbackTransaction(transaction);
			logger.error("Failed updating limits", e);
		}
		
		logger.info("All limits reloaded in " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	/**
	 * Updates the user count but does not reload the tenant limit.
	 */
	private void refreshEmployeeUserCount(Long tenantId) {
		employeeCountLoader.setTenantId(tenantId);
		
		employeeUsers.get(tenantId).setUsed(employeeCountLoader.load());
	}
	
	/**
	 * Updates the asset count but does not reload the tenant limit.
	 */
	private void refreshAssetCount(Long tenantId) {
		productCountLoader.setTenantId(tenantId);
		
		assets.get(tenantId).setUsed(productCountLoader.load());
	}
	
	/**
	 * Updates the org count but does not reload the tenant limit.
	 */
	private void refreshSecondaryOrgCount(Long tenantId) {
		secondaryOrgCountLoader.setTenantId(tenantId);
		
		secondaryOrgs.get(tenantId).setUsed(secondaryOrgCountLoader.load());
	}
	
	private class LimitUpdater {
		private final LimitLoader limitLoader;
		private final Map<Long, ResourceLimit> primaryOrgLimitMap;
		
		public LimitUpdater(LimitLoader limitLoader, Map<Long, ResourceLimit> primaryOrgLimitMap) {
			this.limitLoader = limitLoader;
			this.primaryOrgLimitMap = primaryOrgLimitMap;
		}
		
		public void updateLimitMap(PrimaryOrg primaryOrg, Transaction transaction) {
			logger.trace(String.format("Updating %s limits for [%s]", limitLoader.getType().name(), primaryOrg.toString()));
			
			limitLoader.setTenant(primaryOrg.getTenant());
			ResourceLimit limit = createLimit(primaryOrg.getLimits(), limitLoader, transaction);
			
			primaryOrgLimitMap.put(primaryOrg.getTenant().getId(), limit);
			logger.trace(String.format("%s [%s]: %s", limitLoader.getType().name(), primaryOrg.toString(), limit.toString()));
		}
		
		private ResourceLimit createLimit(TenantLimit tenantLimit, LimitLoader loader, Transaction transaction) {
			ResourceLimit limit = new DiskResourceLimit();
			limit.setUsed(limitLoader.getLimit(transaction));
			limit.setMaximum(tenantLimit.getLimitForType(limitLoader.getType()));
			
			return limit;
		}
	}
}
