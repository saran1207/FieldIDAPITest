package com.n4systems.ws.model.setupdata;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.ws.model.WsModelConverter;

public class WsUserConverter extends WsModelConverter<User, WsUser> {

	public static final long NULL_ID = -1024L;
	
	@Override
	public WsUser fromModel(User model) {
		
		WsUser wsModel = new WsUser();		
		BitField permField = new BitField(model.getPermissions());
		
		wsModel.setId(model.getId());
		wsModel.setUserId(model.getUserID());
		wsModel.setHashPassword(model.getHashPassword());
		wsModel.setHashSecurityCardNumber(model.getHashSecurityCardNumber());
		wsModel.setAllowedToIdentify(permField.isSet(Permissions.Tag));
		wsModel.setAllowedToInspect(permField.isSet(Permissions.CreateEvent));
		wsModel.setAttachedToPrimaryOrg(model.getOwner().getInternalOrg().isPrimary());
		
		populateOwners(model.getOwner(), wsModel);
		
		return wsModel;
	}
	
	private void populateOwners(BaseOrg baseOrg, WsUser wsUser) {
		long customerId = NULL_ID;
		long orgId = NULL_ID;
		long divisionId = NULL_ID;

		if (baseOrg.isDivision()) {
			divisionId = baseOrg.getId();
			CustomerOrg customerOrg = baseOrg.getCustomerOrg();
			customerId = customerOrg.getId();
			orgId = retrieveOwnerId(customerOrg.getParent());
		} else if (baseOrg.isCustomer()) {
			customerId = baseOrg.getId();
			orgId = retrieveOwnerId(baseOrg.getParent());
		} else {
			orgId = retrieveOwnerId(baseOrg);
		}

		wsUser.setOwnerId(baseOrg.getId());
		wsUser.setOrgId(orgId);
		wsUser.setCustomerId(customerId);
		wsUser.setDivisionId(divisionId);
	}

	private long retrieveOwnerId(BaseOrg baseOrg) {
		return baseOrg.getId();
	}
}
