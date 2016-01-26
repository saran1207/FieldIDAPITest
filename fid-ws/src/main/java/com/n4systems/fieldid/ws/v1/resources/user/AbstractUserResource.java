package com.n4systems.fieldid.ws.v1.resources.user;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.ws.v1.resources.tenant.ApiTenantResource;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract class AbstractUserResource extends SetupDataResource<ApiUser, User> {

	@Autowired private OfflineProfileService offlineProfileService;
	@Autowired private ApiOfflineProfileResource apiOfflineProfileResource;
	@Autowired private ApiTenantResource apiTenantResource; 
	
	public AbstractUserResource() {
		super(User.class, true);
	}

	@Override
	protected QueryBuilder<User> createFindAllBuilder(Date after) {
		QueryBuilder<User> builder = createTenantSecurityBuilder(User.class, true);
		builder.addWhere(WhereClauseFactory.create("registered", true));
		if (after != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.GT, "modified", after));
		}
		builder.addOrder("id");
        //addUserTypeTermToQuery(builder);
		return builder;
	}
	
	@Override
	public ApiUser convertEntityToApiModel(User user) {
		ApiUser apiUser = new ApiUser();
		apiUser.setSid(user.getId());
		apiUser.setModified(user.getModified());
		apiUser.setActive(user.isActive());
		apiUser.setOwnerId(user.getOwner().getId());
		apiUser.setUserId(user.getUserID());
		apiUser.setName(user.getDisplayName());
		apiUser.setAuthKey(user.getAuthKey());
		apiUser.setUserType(user.getUserType().name());
		apiUser.setHashPassword(user.getHashPassword());
		apiUser.setHashSecurityCardNumber(user.getHashSecurityCardNumber());
		apiUser.setCreateEventEnabled(Permissions.hasOneOf(user, Permissions.CREATE_EVENT));
		apiUser.setEditEventEnabled(Permissions.hasOneOf(user, Permissions.EDIT_EVENT));
		apiUser.setIdentifyEnabled(Permissions.hasOneOf(user, Permissions.TAG));
		apiUser.setTenant(apiTenantResource.convertEntityToApiModel(user.getOwner().getPrimaryOrg()));
		apiUser.setIdentifier(user.getIdentifier());
		apiUser.setGroupIds(convertGroups(user.getGroups()));
		
		OfflineProfile offlineProfile = offlineProfileService.find(user);
		if (offlineProfile != null) {
			apiUser.setOfflineProfile(apiOfflineProfileResource.convertEntityToApiModel(offlineProfile));
		}
		
		return apiUser;
	}

    private List<Long> convertGroups(Set<UserGroup> groups) {
        List<Long> groupIds = new ArrayList<Long>();
        for (UserGroup group : groups) {
            groupIds.add(group.getId());
        }
        return groupIds;
    }

    protected abstract void addUserTypeTermToQuery(QueryBuilder<User> query);

}
