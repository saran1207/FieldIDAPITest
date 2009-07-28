package com.n4systems.services.limiters;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.n4systems.model.TenantOrganization;
import com.n4systems.model.product.ProductCountLoader;
import com.n4systems.model.user.EmployeeUserCountLoader;
import com.n4systems.persistence.loaders.AllEntityListLoader;
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
		AllEntityListLoader<TenantOrganization> loader = new AllEntityListLoader<TenantOrganization>(TenantOrganization.class);
	
		for (TenantOrganization tenant: loader.load()) {
			updateDiskSpace(tenant);
			updateEmployeeUsers(tenant);
			updateAssets(tenant);
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
	private void updateEmployeeUsers(TenantOrganization tenant) {
		logger.debug("Updating employee limits for [" + tenant.getName() + "]");
		
		EmployeeUserCountLoader loader = new EmployeeUserCountLoader();
		loader.setTenantId(tenant.getId());
		
		ResourceLimit limit = new AccountResourceLimit();
		limit.setUsed(loader.load());
		limit.setMaximum(tenant.getLimits().getUsers());
		
		employeeUsers.put(tenant.getId(), limit);
		logger.debug("Employee Limit [" + tenant.getName() + "]: " + limit.toString());
	}
	
	/**
	 * Updates the disk space limit for a single tenant
	 */
	private void updateDiskSpace(TenantOrganization tenant) {
		logger.debug("Updating disk space limits for [" + tenant.getName() + "]");
		TenantDiskUsageCalculator usageCalc = new TenantDiskUsageCalculator(tenant);
		
		ResourceLimit limit = new DiskResourceLimit();
		limit.setUsed(usageCalc.totalLimitingSize());
		limit.setMaximum(tenant.getLimits().getDiskSpace());
		
		diskSpace.put(tenant.getId(), limit);
		logger.debug("Disk Limit [" + tenant.getName() + "]: " + limit.toString());
	}
	
	/**
	 * Updates the asset limit for a single tenant
	 */
	private void updateAssets(TenantOrganization tenant) {
		logger.debug("Updating asset limits for [" + tenant.getName() + "]");
		
		ProductCountLoader loader = new ProductCountLoader();
		loader.setTenantId(tenant.getId());
		
		ResourceLimit limit = new AssetResourceLimit();
		limit.setUsed(loader.load());
		limit.setMaximum(tenant.getLimits().getAssets());
		
		assets.put(tenant.getId(), limit);
		logger.debug("Asset Limit [" + tenant.getName() + "]: " + limit.toString());
	}
}
