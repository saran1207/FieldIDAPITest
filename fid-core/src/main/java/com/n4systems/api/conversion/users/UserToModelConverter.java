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
		// FIXME DD : do i really need globalId & ExternalId???		
		to.setGlobalId(from.getGlobalId());
		to.setInitials(from.getInitials());
		// FIMXE DD : implement permission setting...
		//to.setPermissions();
		to.setPosition(from.getPosition());
		to.setTenant(baseOrg.getTenant());
		to.setUserType(UserType.valueFromLabel(from.getAccountType()));
		to.setUserID(from.getUserName());
		to.setEmailAddress(from.getContactEmail());
		to.setLastName(from.getLastName());
		to.setFirstName(from.getLastName());
		to.setOwner(baseOrg);
	}

	@Override
	protected User createModelBean() {
		return new User();
	}
	
}
