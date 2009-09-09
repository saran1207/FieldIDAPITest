package com.n4systems.services.limiters;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.ProductCountLoader;
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
	
	private Map<Long, ResourceLimit> diskSpace = new ConcurrentHashMap<Long, ResourceLimit>();
	private Map<Long, ResourceLimit> employeeUsers = new ConcurrentHashMap<Long, ResourceLimit>();
	private Map<Long, ResourceLimit> assets = new ConcurrentHashMap<Long, ResourceLimit>();
	
	private ProductCountLoader productCountLoader = new ProductCountLoader();
	private EmployeeUserCountLoader employeeCountLoader = new EmployeeUserCountLoader();
	
	private TenantLimitService() {}
	
	public ResourceLimit getDiskSpace(Long tenantId) {
		return diskSpace.get(tenantId);
	}
	
	public ResourceLimit getEmployeeUsers(Long tenantId) {
		// user counts are refreshed in real-time
		refreshEmployeeUserCount(tenantId);
		
		return employeeUsers.get(tenantId);
	}
	
	public ResourceLimit getAssets(Long tenantId) {
		// asset counts are refreshed in real-time
		refreshAssetCount(tenantId);
		
		return assets.get(tenantId);
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
		}
		
		return limits;
	}
	
	/**
	 * Updates all limits for all tenants
	 */
	public void updateAll() {
		logger.info("Reloading all limits");
		long startTime = System.currentTimeMillis();
		
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			for (PrimaryOrg primaryOrg: TenantCache.getInstance().findAllPrimaryOrgs()) {
				logger.info("Updating all limits for [" + primaryOrg.toString() + "]");
				updateDiskSpace(primaryOrg);
				updateEmployeeUsers(primaryOrg, transaction);
				updateAssets(primaryOrg, transaction);
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
	 * Updates the employee user limit for a single tenant
	 */
	private void updateEmployeeUsers(PrimaryOrg primaryOrg, Transaction transaction) {
		logger.trace("Updating employee limits for [" + primaryOrg.toString() + "]");
		employeeCountLoader.setTenantId(primaryOrg.getId());
		
		ResourceLimit limit = new AccountResourceLimit();
		limit.setUsed(employeeCountLoader.load(transaction));
		limit.setMaximum(primaryOrg.getLimits().getUsers());
		
		employeeUsers.put(primaryOrg.getId(), limit);
		logger.trace("Employee Limit [" + primaryOrg.toString() + "]: " + limit.toString());
	}
	
	/**
	 * Updates the disk space limit for a single tenant
	 */
	private void updateDiskSpace(PrimaryOrg primaryOrg) {
		logger.trace("Updating disk space limits for [" + primaryOrg.toString() + "]");
		TenantDiskUsageCalculator usageCalc = new TenantDiskUsageCalculator(primaryOrg.getTenant());
		
		ResourceLimit limit = new DiskResourceLimit();
		limit.setUsed(usageCalc.totalLimitingSize());
		limit.setMaximum(primaryOrg.getLimits().getDiskSpaceInBytes());
		
		diskSpace.put(primaryOrg.getId(), limit);
		logger.trace("Disk Limit [" + primaryOrg.toString() + "]: " + limit.toString());
	}
	
	/**
	 * Updates the asset limit for a single tenant
	 */
	private void updateAssets(PrimaryOrg primaryOrg, Transaction transaction) {
		logger.trace("Updating asset limits for [" + primaryOrg.toString() + "]");
		productCountLoader.setTenantId(primaryOrg.getId());
		
		ResourceLimit limit = new AssetResourceLimit();
		limit.setUsed(productCountLoader.load(transaction));
		limit.setMaximum(primaryOrg.getLimits().getAssets());
		
		assets.put(primaryOrg.getId(), limit);
		logger.trace("Asset Limit [" + primaryOrg.toString() + "]: " + limit.toString());
	}
}
