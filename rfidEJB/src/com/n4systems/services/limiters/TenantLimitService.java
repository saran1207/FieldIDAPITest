package com.n4systems.services.limiters;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.ProductCountLoader;
import com.n4systems.model.user.EmployeeUserCountLoader;
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
		for (PrimaryOrg primaryOrg: TenantCache.getInstance().findAllPrimaryOrgs()) {
			updateDiskSpace(primaryOrg);
			updateEmployeeUsers(primaryOrg);
			updateAssets(primaryOrg);
		}
		logger.info("All limits reloaded in " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	/**
	 * Updates the user count but does not reload the tenant limit.
	 */
	private void refreshEmployeeUserCount(Long tenantId) {
		EmployeeUserCountLoader loader = new EmployeeUserCountLoader();
		loader.setTenantId(tenantId);
		
		employeeUsers.get(tenantId).setUsed(loader.load());
	}
	
	/**
	 * Updates the asset count but does not reload the tenant limit.
	 */
	private void refreshAssetCount(Long tenantId) {
		ProductCountLoader loader = new ProductCountLoader();
		loader.setTenantId(tenantId);
		
		assets.get(tenantId).setUsed(loader.load());
	}
	
	/**
	 * Updates the employee user limit for a single tenant
	 */
	private void updateEmployeeUsers(PrimaryOrg primaryOrg) {
		logger.debug("Updating employee limits for [" + primaryOrg.toString() + "]");
		
		EmployeeUserCountLoader loader = new EmployeeUserCountLoader();
		loader.setTenantId(primaryOrg.getId());
		
		ResourceLimit limit = new AccountResourceLimit();
		limit.setUsed(loader.load());
		limit.setMaximum(primaryOrg.getLimits().getUsers());
		
		employeeUsers.put(primaryOrg.getId(), limit);
		logger.debug("Employee Limit [" + primaryOrg.toString() + "]: " + limit.toString());
	}
	
	/**
	 * Updates the disk space limit for a single tenant
	 */
	private void updateDiskSpace(PrimaryOrg primaryOrg) {
		logger.debug("Updating disk space limits for [" + primaryOrg.toString() + "]");
		TenantDiskUsageCalculator usageCalc = new TenantDiskUsageCalculator(primaryOrg.getTenant());
		
		ResourceLimit limit = new DiskResourceLimit();
		limit.setUsed(usageCalc.totalLimitingSize());
		limit.setMaximum(primaryOrg.getLimits().getDiskSpaceInBytes());
		
		diskSpace.put(primaryOrg.getId(), limit);
		logger.debug("Disk Limit [" + primaryOrg.toString() + "]: " + limit.toString());
	}
	
	/**
	 * Updates the asset limit for a single tenant
	 */
	private void updateAssets(PrimaryOrg primaryOrg) {
		logger.debug("Updating asset limits for [" + primaryOrg.toString() + "]");
		
		ProductCountLoader loader = new ProductCountLoader();
		loader.setTenantId(primaryOrg.getId());
		
		ResourceLimit limit = new AssetResourceLimit();
		limit.setUsed(loader.load());
		limit.setMaximum(primaryOrg.getLimits().getAssets());
		
		assets.put(primaryOrg.getId(), limit);
		logger.debug("Asset Limit [" + primaryOrg.toString() + "]: " + limit.toString());
	}
}
