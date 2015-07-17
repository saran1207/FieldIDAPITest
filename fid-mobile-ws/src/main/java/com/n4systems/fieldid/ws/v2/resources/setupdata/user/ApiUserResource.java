package com.n4systems.fieldid.ws.v2.resources.setupdata.user;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.fieldid.ws.v2.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Path("user")
public class ApiUserResource extends SetupDataResourceReadOnly<ApiUser, User> {

	@Autowired private OfflineProfileService offlineProfileService;
	@Autowired private ApiOfflineProfileResource apiOfflineProfileResource;
	
	public ApiUserResource() {
		super(User.class, true);
	}

	@Override
	protected void addTermsToQuery(QueryBuilder<?> query) {
		query.addWhere(WhereClauseFactory.create("registered", true));
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.PERSON));
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
		apiUser.setIdentifier(user.getIdentifier());
		apiUser.setGroupIds(convertGroups(user.getGroups()));
		
		OfflineProfile offlineProfile = offlineProfileService.find(user);
		if (offlineProfile != null) {
			apiUser.setOfflineProfile(apiOfflineProfileResource.convertEntityToApiModel(offlineProfile));
		}
		
		return apiUser;
	}

    private List<Long> convertGroups(Set<UserGroup> groups) {
        List<Long> groupIds = new ArrayList<>();
        for (UserGroup group : groups) {
            groupIds.add(group.getId());
        }
        return groupIds;
    }

}
