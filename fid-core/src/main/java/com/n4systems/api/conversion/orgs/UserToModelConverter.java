package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.AbstractViewToModelConverter;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.UserView;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public class UserToModelConverter extends AbstractViewToModelConverter<User, UserView> {
	private final OrgByNameLoader orgLoader;

	public UserToModelConverter(GlobalIdLoader<User> externalIdLoader, OrgByNameLoader orgLoader) {
		super(externalIdLoader);
		this.orgLoader = orgLoader;
	}

	@Override
	public void copyProperties(UserView from, User to, boolean isEdit) throws ConversionException {
		to.setEmailAddress(from.getContactEmail());
		to.setLastName(from.getLastName());
		to.setFirstName(from.getLastName());
		BaseOrg baseOrg = orgLoader.setOrganizationName(from.getOrganization()).load();
		to.setOwner(baseOrg);
	}

	@Override
	protected User createModelBean() {
		return new User();
	}
	
}
