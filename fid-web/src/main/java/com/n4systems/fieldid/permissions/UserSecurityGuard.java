package com.n4systems.fieldid.permissions;

public interface UserSecurityGuard {

	public boolean isAllowedTag();
	public boolean isAllowedManageSystemConfig();
	public boolean isAllowedManageEndUsers();
	public boolean isAllowedCreateInspection();
	public boolean isAllowedEditInspection();
	public boolean isAllowedManageJobs();
	public boolean isAllowedManageSafetyNetwork();
	public boolean isAllowedManageSystemUsers();
	public boolean isAllowedAccessWebStore();
}
