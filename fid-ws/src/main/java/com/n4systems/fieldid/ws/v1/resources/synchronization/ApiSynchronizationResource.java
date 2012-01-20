package com.n4systems.fieldid.ws.v1.resources.synchronization;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Path("/synchronize")
@Component
public class ApiSynchronizationResource extends FieldIdPersistenceService {

	@Autowired private OfflineProfileService offlineProfileService;
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiSynchronizationAsset> synchronize() {
		ListResponse<ApiSynchronizationAsset> response = new ListResponse<ApiSynchronizationAsset>();
		
		OfflineProfile profile = offlineProfileService.find(getCurrentUser());
		if (profile == null || profile.getAssets().size() + profile.getOrganizations().size() == 0) {
			return response;
		}
		
		if (!profile.getAssets().isEmpty()) {
			QueryBuilder<ApiSynchronizationAsset> builder = createBuilder();
			builder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileGUID", profile.getAssets()));
			response.getList().addAll(persistenceService.findAll(builder));
		}
		
		for (Long orgId: profile.getOrganizations()) {
			QueryBuilder<ApiSynchronizationAsset> builder = createBuilder();
			
			BaseOrg org = persistenceService.find(BaseOrg.class, orgId);
			if (org == null) {
				throw new NotFoundException("Organization", orgId);
			}
			
			builder.applyFilter(new OwnerAndDownFilter(org));
			response.getList().addAll(persistenceService.findAll(builder));
		}
		
		response.setTotal(response.getList().size());
		
		return response;
	}
	
	private QueryBuilder<ApiSynchronizationAsset> createBuilder() {
		QueryBuilder<ApiSynchronizationAsset> builder = new QueryBuilder<ApiSynchronizationAsset>(Asset.class, securityContext.getUserSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(ApiSynchronizationAsset.class, "mobileGUID", "modified"));
		return builder;
	}
}
