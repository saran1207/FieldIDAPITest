package com.n4systems.fieldid.permissions;

public interface UserSecurityGuard {

	//Inspection Permissions
	public boolean isAllowedTag();
	public boolean isAllowedManageSystemConfig();
	public boolean isAllowedManageSystemUsers();
	public boolean isAllowedManageEndUsers();
	public boolean isAllowedCreateEvent();
	public boolean isAllowedEditEvent();
	public boolean isAllowedManageJobs();
	public boolean isAllowedManageSafetyNetwork();
	public boolean isAllowedEditAssetDetails();

	//Loto Permissions
	public boolean isAllowedAuthorEditProcedure();
	public boolean isAllowedCertifyProcedure();
	public boolean isAllowedDeleteProcedure();
	public boolean isAllowedMaintainLotoSchedule();
	public boolean isAllowedPerformProcedure();
	public boolean isAllowedPrintProcedure();
	public boolean isAllowedProcedureAudit();
	public boolean isAllowedUnpublishProcedure();

}
