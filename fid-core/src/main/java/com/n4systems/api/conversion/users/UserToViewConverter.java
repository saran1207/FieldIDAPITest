package com.n4systems.api.conversion.users;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.api.model.UserView;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class UserToViewConverter implements ModelToViewConverter<User, UserView> {
	
	public UserToViewConverter() {
		super();
	}
	
	@Override
	public UserView toView(User model) throws ConversionException {
		UserView view = new UserView();
	
		// TODO DD : ask what to do with sendWelcomeEmail???
		view.setSecurityRfidNumber(model.getHashSecurityCardNumber());
		view.setAccountType(model.getUserType().getLabel());
		view.setContactEmail(model.getEmailAddress());
		view.setOrganization(model.getOwner().getPrimaryOrg().getDisplayName());
		view.setDivision(model.getOwner().getDivisionOrg() == null ? "" : model.getOwner().getDivisionOrg().getDisplayName());
		view.setFirstName(model.getFirstName());
		view.setLastName(model.getLastName());
		view.setInitials(model.getInitials());
		view.setPosition(model.getPosition());
		view.setUserName(model.getUserID());
		view.setGlobalId(model.getGlobalId());
		view.setCountry(model.getOwner().getAddressInfo().getCountry());
		view.setTimeZone(model.getTimeZone().getDisplayName());
		// permissions...
		view.setIdentifyAssets(getPermissionYNString(model, Permissions.Tag));
		view.setManageSystemConfiguration(getPermissionYNString(model, Permissions.ManageSystemConfig));
		view.setManageSystemUsers(getPermissionYNString(model, Permissions.ManageSystemUsers));
		view.setManageJobSites(getPermissionYNString(model, Permissions.ManageEndUsers));
		view.setCreateEvents(getPermissionYNString(model, Permissions.CreateEvent));
		view.setEditEvents(getPermissionYNString(model, Permissions.EditEvent));
		view.setManageJobs(getPermissionYNString(model, Permissions.ManageJobs));
		view.setManageSafetyNetwork(getPermissionYNString(model, Permissions.ManageSafetyNetwork));
		view.setAccessWebStore(getPermissionYNString(model, Permissions.AccessWebStore));

		return view;
	}

	private String getPermissionYNString(User model, int tag) {
		return Permissions.hasOneOf(model, Permissions.Tag) ? "Y" : "N";	
	}

	protected void convertDirectFields(Event model, EventView view) {
		view.setComments(model.getComments());
		view.setDatePerformed(model.getDate());
		view.setLocation(model.getAdvancedLocation().getFreeformLocation());
		view.setPrintable(model.isPrintable());
	}

	
}
