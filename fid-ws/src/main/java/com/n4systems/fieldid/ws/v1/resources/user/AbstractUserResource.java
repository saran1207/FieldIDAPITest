package com.n4systems.fieldid.ws.v1.resources.user;

import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.offlineprofile.ApiOfflineProfileResource;
import com.n4systems.fieldid.ws.v1.resources.tenant.ApiTenantResource;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.services.config.ConfigService;
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
	@Autowired protected ConfigService configService;

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

		//Adding new secondary, customer and division id's.
		if(user.getOwner().isSecondary()) {
			apiUser.setSecondaryId(user.getOwner().getSecondaryOrg().getID());
			apiUser.setCustomerId(null);
			apiUser.setDivisionId(null);
		} else if(user.getOwner().isCustomer()) {
			//check if it's a customer under Primary or Secondary
			if(user.getOwner().getSecondaryOrg() == null) {
				//it's under Primary
				apiUser.setSecondaryId(null);
			} else {
				//it's under a Secondary
				apiUser.setSecondaryId(user.getOwner().getSecondaryOrg().getID());
			}
			apiUser.setCustomerId(user.getOwner().getCustomerOrg().getID());
			apiUser.setDivisionId(null);
		} else if(user.getOwner().isDivision()) {
			//check if it's a customer under Primary or Secondary
			if(user.getOwner().getSecondaryOrg() == null) {
				//it's under Primary
				apiUser.setSecondaryId(null);
			} else {
				//it's under a Secondary
				apiUser.setSecondaryId(user.getOwner().getSecondaryOrg().getID());
			}
			apiUser.setCustomerId(user.getOwner().getCustomerOrg().getID());
			apiUser.setDivisionId(user.getOwner().getDivisionOrg().getID());
		} else {
			//It's Primary
			apiUser.setSecondaryId(null);
			apiUser.setCustomerId(null);
			apiUser.setDivisionId(null);
		}

		apiUser.setUserId(user.getUserID());
		apiUser.setName(user.getDisplayName());
		apiUser.setAuthKey(user.getAuthKey());
		apiUser.setUserType(user.getUserType().name());
		apiUser.setHashPassword(user.getHashPassword());
		apiUser.setHashSecurityCardNumber(user.getHashSecurityCardNumber());
		apiUser.setCreateEventEnabled(Permissions.hasOneOf(user, Permissions.CREATE_EVENT));
		apiUser.setEditEventEnabled(Permissions.hasOneOf(user, Permissions.EDIT_EVENT));
		apiUser.setIdentifyEnabled(Permissions.hasOneOf(user, Permissions.TAG));

		//Procedure Permissions
		apiUser.setAuthorEditProcedure(Permissions.hasOneOf(user, Permissions.AUTHOR_EDIT_PROCEDURE));
		apiUser.setCertifyProcedure(Permissions.hasOneOf(user, Permissions.CERTIFY_PROCEDURE));
		apiUser.setDeleteProcedure(Permissions.hasOneOf(user, Permissions.DELETE_PROCEDURE));
		apiUser.setMaintainLotoSchedule(Permissions.hasOneOf(user, Permissions.MAINTAIN_LOTO_SCHEDULE));
		apiUser.setPerformProcedure(Permissions.hasOneOf(user, Permissions.PERFORM_PROCEDURE));
		apiUser.setPrintProcedure(Permissions.hasOneOf(user, Permissions.PRINT_PROCEDURE));
		apiUser.setProcedureAudit(Permissions.hasOneOf(user, Permissions.PROCEDURE_AUDIT));
		apiUser.setUnpublishProcedure(Permissions.hasOneOf(user, Permissions.UNPUBLISH_PROCEDURE));

		apiUser.setTenant(apiTenantResource.convertEntityToApiModel(user.getOwner().getPrimaryOrg()));
		apiUser.setIdentifier(user.getIdentifier());
		apiUser.setGroupIds(convertGroups(user.getGroups()));
		
		OfflineProfile offlineProfile = offlineProfileService.find(user);
		if (offlineProfile != null) {
			apiUser.setOfflineProfile(apiOfflineProfileResource.convertEntityToApiModel(offlineProfile));
		}

		apiUser.setMobileAccessKeyId(configService.getConfig().getAws().getMobileAccessKeyId());
		apiUser.setMobileSecretAccessKey(configService.getConfig().getAws().getMobileSecretAccessKey());

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
