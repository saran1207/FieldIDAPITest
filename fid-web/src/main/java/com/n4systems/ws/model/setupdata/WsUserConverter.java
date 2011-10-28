package com.n4systems.ws.model.setupdata;

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
		wsModel.setOwnerId(model.getOwner().getId());
		wsModel.setFirstName(model.getFirstName());
		wsModel.setLastName(model.getLastName());
		wsModel.setDeleted(model.isArchived());
		
		return wsModel;
	}
}
