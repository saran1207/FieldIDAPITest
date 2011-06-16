package com.n4systems.api.conversion.users;

import com.n4systems.api.conversion.AbstractViewToModelConverter;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.UserView;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.GlobalIdLoader;
import com.n4systems.security.UserType;

public class UserToModelConverter extends AbstractViewToModelConverter<User, UserView> {
	private final OrgByNameLoader orgLoader;

	public UserToModelConverter(GlobalIdLoader<User> externalIdLoader, OrgByNameLoader orgLoader) {
		super(externalIdLoader);
		this.orgLoader = orgLoader;
	}

	@Override
	public void copyProperties(UserView from, User to, boolean isEdit) throws ConversionException {
		BaseOrg baseOrg = orgLoader.setOrganizationName(from.getOrganization()).load();
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setGlobalId(from.getGlobalId());
		to.setInitials(from.getInitials());		
		to.setPermissions(from.getPermissions());
		to.setPosition(from.getPosition());
		to.setTenant(baseOrg.getTenant());
		to.setUserType(UserType.valueFromLabel(from.getAccountType()));
		to.setUserID(from.getUserID());
		to.setEmailAddress(from.getEmailAddress());
		to.setOwner(baseOrg);
		if (from.isAssignPassword()) { 
			to.assignPassword(from.getPassword());
		}
	}

	@Override
	protected User createModelBean() {
		return new User();
	}
	
}
