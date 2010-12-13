package com.n4systems.fieldid.security;

import com.n4systems.services.limiters.TenantLimitService;

public class TenantLimitProxy {
	private final TenantLimitService limitService;
	private final Long tenantId;
	
	public TenantLimitProxy(Long tenantId) {
		this.limitService = TenantLimitService.getInstance();
		this.tenantId = tenantId;
	}
	
	public boolean isDiskSpaceMaxed() {
		return limitService.getDiskSpace(tenantId).isMaxed();
	}
	
	public Long getDiskSpaceUsed() {
		return limitService.getDiskSpace(tenantId).getUsed();
	}
	
	public Long getDiskSpaceMax() {
		return limitService.getDiskSpace(tenantId).getMaximum();
	}
	
	public boolean isDiskSpaceUnlimited() {
		return limitService.getDiskSpace(tenantId).isUnlimited();
	}
	
	public boolean isEmployeeUsersMaxed() {
		return limitService.getEmployeeUsers(tenantId).isMaxed();
	}
	
	public Long getLiteUsersUsed(){
		return limitService.getLiteUsers(tenantId).getUsed();
	}
	
	public boolean isLiteUsersMaxed(){
		return limitService.getLiteUsers(tenantId).isMaxed();
	}
	
	public Long getEmployeeUsersUsed() {
		return limitService.getEmployeeUsers(tenantId).getUsed();
	}
	
	public Long getEmployeeUsersMax() {
		return limitService.getEmployeeUsers(tenantId).getMaximum();
	}
	
	public Long getLiteUsersMax(){
		return limitService.getLiteUsers(tenantId).getMaximum();
	}
	
	public boolean isEmployeeUsersUnlimited() {
		return limitService.getEmployeeUsers(tenantId).isUnlimited();
	}
	
	public boolean isLiteUsersUnlimited() {
		return limitService.getLiteUsers(tenantId).isUnlimited();
	}

	public boolean isAssetsMaxed() {
		return limitService.getAssets(tenantId).isMaxed();
	}
	
	public Long getAssetsUsed() {
		return limitService.getAssets(tenantId).getUsed();
	}
	
	public Long getAssetsMax() {
		return limitService.getAssets(tenantId).getMaximum();
	}
	
	public boolean isAssetsUnlimited() {
		return limitService.getAssets(tenantId).isUnlimited();
	}
	
	public boolean isSecondaryOrgsMaxed() {
		return limitService.getSecondaryOrgs(tenantId).isMaxed();
	}
	
	public Long getSecondaryOrgsUsed() {
		return limitService.getSecondaryOrgs(tenantId).getUsed();
	}
	
	public Long getSecondaryOrgsMax() {
		return limitService.getSecondaryOrgs(tenantId).getMaximum();
	}
	
	public boolean isSecondaryOrgsUnlimited() {
		return limitService.getSecondaryOrgs(tenantId).isUnlimited();
	}
}
