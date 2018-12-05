package com.n4systems.api.conversion.users;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.api.model.UserView;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class UserToViewConverter implements ModelToViewConverter<User, UserView> {
	
	protected String YNStr = "N";

	public UserToViewConverter() {
		super();
	}

	public UserToViewConverter(String YNStr) {
		super();
		this.YNStr = YNStr;
	}

	@Override
	public UserView toView(User model) throws ConversionException {
		return toView(model, this.YNStr);
	}

	public UserView toView(User model, String YNStr) throws ConversionException {
		UserView view = new UserView();
	
		view.setSecurityRfidNumber(model.getHashSecurityCardNumber());
		view.setAccountType(model.getUserType().getLabel());
		view.setEmailAddress(model.getEmailAddress());
		view.setOrganization(model.getOwner().getSecondaryOrg() != null ?
				model.getOwner().getSecondaryOrg().getDisplayName() : model.getOwner().getPrimaryOrg().getDisplayName());
		view.setCustomer(model.getOwner().getCustomerOrg() == null ? "" : model.getOwner().getCustomerOrg().getName());
		view.setDivision(model.getOwner().getDivisionOrg() == null ? "" : model.getOwner().getDivisionOrg().getName());
		view.setFirstName(model.getFirstName());
		view.setLastName(model.getLastName());
		view.setInitials(model.getInitials());
        view.setIdentifier(model.getIdentifier());
		view.setPosition(model.getPosition());
		view.setUserID(model.getUserID());
		view.setGlobalId(model.getGlobalId());
		view.setUserGroup(model.getGroups().isEmpty() ? null : UserView.getUserGroupNames(model.getGroups()));
		// permissions...
		view.setIdentifyAssets("Y".equals(YNStr)?"Y":getPermissionYNString(model, Permissions.TAG));
		view.setManageSystemConfiguration(getPermissionYNString(model, Permissions.MANAGE_SYSTEM_CONFIG));
		view.setManageSystemUsers(getPermissionYNString(model, Permissions.MANAGE_SYSTEM_USERS));
		view.setManageJobSites(getPermissionYNString(model, Permissions.MANAGE_END_USERS));
		view.setCreateEvents("Y".equals(YNStr)?"Y":getPermissionYNString(model, Permissions.CREATE_EVENT));
		view.setEditEvents("Y".equals(YNStr)?"Y":getPermissionYNString(model, Permissions.EDIT_EVENT));
		view.setManageJobs(getPermissionYNString(model, Permissions.MANAGE_JOBS));
		view.setManageSafetyNetwork(getPermissionYNString(model, Permissions.MANAGE_SAFETY_NETWORK));
		view.setEditAssetDetails(getPermissionYNString(model, Permissions.EDIT_ASSET_DETAILS));
		view.setAuthorEditProcedure(getPermissionYNString(model, Permissions.AUTHOR_EDIT_PROCEDURE));
		view.setCertifyProcedure(getPermissionYNString(model, Permissions.CERTIFY_PROCEDURE));
		view.setDeleteProcedure(getPermissionYNString(model, Permissions.DELETE_PROCEDURE));
		view.setMaintainLotoSchedule(getPermissionYNString(model, Permissions.MAINTAIN_LOTO_SCHEDULE));
		view.setPerformProcedure(getPermissionYNString(model, Permissions.PERFORM_PROCEDURE));
		view.setPrintProcedure(getPermissionYNString(model, Permissions.PRINT_PROCEDURE));
		view.setProcedureAudit(getPermissionYNString(model, Permissions.PROCEDURE_AUDIT));
		view.setUnpublishProcedure(getPermissionYNString(model, Permissions.UNPUBLISH_PROCEDURE));
		// actions...
		view.setAssignPassword("Y".equals(YNStr)?"Y":"N");
		view.setSendWelcomeEmail("Y".equals(YNStr)?"Y":"N");

		return view;
	}

	private String getPermissionYNString(User model, int tag) {
		return Permissions.hasOneOf(model, tag) ? "Y" : "N";
	}

	public void setYNStr ( String YNStr) {
		this.YNStr = YNStr;
	}

	public String getYNStr () {
		return this.YNStr;
	}

	protected void convertDirectFields(Event model, EventView view) {
		view.setComments(model.getComments());
		view.setDatePerformed(model.getDate());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPrintable(model.isPrintable());
	}

	
}
