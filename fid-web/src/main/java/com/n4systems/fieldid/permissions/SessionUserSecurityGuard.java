package com.n4systems.fieldid.permissions;


import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class SessionUserSecurityGuard implements UserSecurityGuard {

	private final int permissions;
	
	public SessionUserSecurityGuard(User user) {
		permissions = user.getPermissions();
	}
	
	public boolean isAllowedManageSafetyNetwork() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_SAFETY_NETWORK);
	}

	public boolean isAllowedCreateEvent() {
		return Permissions.hasOneOf(permissions, Permissions.CREATE_EVENT);
	}

	public boolean isAllowedEditEvent() {
		return Permissions.hasOneOf(permissions, Permissions.EDIT_EVENT);
	}

	public boolean isAllowedManageEndUsers() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_END_USERS);
	}

	public boolean isAllowedManageJobs() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_JOBS);
	}

	public boolean isAllowedManageSystemConfig() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_SYSTEM_CONFIG);
	}

	public boolean isAllowedTag() {
		return Permissions.hasOneOf(permissions, Permissions.TAG);
	}

	public boolean isAllowedManageSystemUsers() {
		return Permissions.hasOneOf(permissions, Permissions.MANAGE_SYSTEM_USERS);
	}

	@Override
	public boolean isAllowedAuthorEditProcedure() {
		return Permissions.hasOneOf(permissions, Permissions.AUTHOR_EDIT_PROCEDURE);
	}

	@Override
	public boolean isAllowedCertifyProcedure() {
		return Permissions.hasOneOf(permissions, Permissions.CERTIFY_PROCEDURE);
	}

	@Override
	public boolean isAllowedDeleteProcedure() {
		return Permissions.hasOneOf(permissions, Permissions.DELETE_PROCEDURE);
	}

	@Override
	public boolean isAllowedMaintainLotoSchedule() {
		return Permissions.hasOneOf(permissions, Permissions.MAINTAIN_LOTO_SCHEDULE);
	}

	@Override
	public boolean isAllowedPerformProcedure() {
		return Permissions.hasOneOf(permissions, Permissions.PERFORM_PROCEDURE);
	}

	@Override
	public boolean isAllowedPrintProcedure() {
		return Permissions.hasOneOf(permissions, Permissions.PRINT_PROCEDURE);
	}

	@Override
	public boolean isAllowedProcedureAudit() {
		return Permissions.hasOneOf(permissions, Permissions.PROCEDURE_AUDIT);
	}

	@Override
	public boolean isAllowedUnpublishProcedure() {
		return Permissions.hasOneOf(permissions, Permissions.UNPUBLISH_PROCEDURE);
	}

}
