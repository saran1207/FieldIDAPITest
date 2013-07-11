package com.n4systems.api.conversion.users;

import com.n4systems.api.conversion.AbstractViewToModelConverter;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.user.UserGroupForNameLoader;
import com.n4systems.persistence.loaders.GlobalIdLoader;
import com.n4systems.security.UserType;

import java.util.Date;

public class UserToModelConverter extends AbstractViewToModelConverter<User, UserView> {
	private final OrgByNameLoader orgLoader;
    private final UserGroupForNameLoader userGroupForNameLoader;

	public UserToModelConverter(GlobalIdLoader<User> externalIdLoader, OrgByNameLoader orgLoader, UserGroupForNameLoader userGroupForNameLoader) {
		super(externalIdLoader);
		this.orgLoader = orgLoader;
        this.userGroupForNameLoader = userGroupForNameLoader;
    }

	@Override
	public void copyProperties(UserView from, User to, boolean isEdit) throws ConversionException {
		BaseOrg baseOrg = orgLoader.setOrganizationName(from.getOrganization()).
							setCustomerName(from.getCustomer()).
							setDivision(from.getDivision()).
							load();
        if (from.getUserGroup() != null) {
            String[] groupNames = from.getUserGroup().split(",");
            for (String groupName : groupNames) {
                UserGroup userGroup = userGroupForNameLoader.setName(groupName).load();
                to.getGroups().add(userGroup);
            }
        }
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
		to.setRegistered(true);
		to.setCreated(new Date());
        to.setIdentifier(from.getIdentifier());
		
		if(from.getSecurityRfidNumber()!=null) { 
			to.assignSecruityCardNumber(from.getSecurityRfidNumber());
		}
		if (YNField.isYes(from.getAssignPassword())) { 
			to.assignPassword(from.getPassword());
		}
        if (YNField.isYes(from.getArchive())) {
            to.archiveUser();
        }
	}

	@Override
	protected User createModelBean() {
		return new User();
	}
	
}
