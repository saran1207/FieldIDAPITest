package com.n4systems.fieldid.ws.v1.resources.user;

import java.util.Date;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.ws.v1.resources.tenant.ApiTenantResource;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Component
@Path("user")
public class ApiUserResource extends SetupDataResource<ApiUser, User> {

	@Autowired private OfflineProfileService offlineProfileService;
	@Autowired private ApiOfflineProfileResource apiOfflineProfileResource;
	@Autowired private ApiTenantResource apiTenantResource; 
	
	public ApiUserResource() {
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
		apiUser.setCreateEventEnabled(Permissions.hasOneOf(user, Permissions.CreateEvent));
		apiUser.setEditEventEnabled(Permissions.hasOneOf(user, Permissions.EditEvent));
		apiUser.setIdentifyEnabled(Permissions.hasOneOf(user, Permissions.Tag));
		apiUser.setTenant(apiTenantResource.convertEntityToApiModel(user.getOwner().getPrimaryOrg()));
		apiUser.setOfflineProfile(apiOfflineProfileResource.convertEntityToApiModel(offlineProfileService.find(user)));
		
		return apiUser;
	}

}
