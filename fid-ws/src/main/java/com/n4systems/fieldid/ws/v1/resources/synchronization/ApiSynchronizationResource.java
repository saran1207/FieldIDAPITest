package com.n4systems.fieldid.ws.v1.resources.synchronization;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.n4systems.model.Event;
import com.n4systems.model.SubAsset;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
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
		OfflineProfile profile = offlineProfileService.find(getCurrentUser());
		if (profile == null || profile.getAssets().size() + profile.getOrganizations().size() == 0) {
			return new ListResponse<ApiSynchronizationAsset>();
		}
		
		Set<ApiSynchronizationAsset> assets = new HashSet<ApiSynchronizationAsset>();
		assets.addAll(getOfflineProfileAssets(profile));
		assets.addAll(getOfflineProfileOrgs(profile));
		assets.addAll(getAssignedOpenEventAssets(profile));
		assets.addAll(getLinkedAssets(assets));		
		
		ListResponse<ApiSynchronizationAsset> response = new ListResponse<ApiSynchronizationAsset>();
		response.getList().addAll(assets);
		response.setTotal(response.getList().size());
		return response;
	}
	
	private List<ApiSynchronizationAsset> getOfflineProfileAssets(OfflineProfile profile) {
		List<ApiSynchronizationAsset> assets = new ArrayList<ApiSynchronizationAsset>();
		if (!profile.getAssets().isEmpty()) {
			QueryBuilder<ApiSynchronizationAsset> builder = createBuilder();
			builder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileGUID", profile.getAssets()));
			assets.addAll(persistenceService.findAll(builder));
		}
		return assets;
	}
	
	private List<ApiSynchronizationAsset> getOfflineProfileOrgs(OfflineProfile profile) {
		List<ApiSynchronizationAsset> assets = new ArrayList<ApiSynchronizationAsset>();
		for (Long orgId: profile.getOrganizations()) {
			QueryBuilder<ApiSynchronizationAsset> builder = createBuilder();
			
			BaseOrg org = persistenceService.find(BaseOrg.class, orgId);
			if (org == null) {
				throw new NotFoundException("Organization", orgId);
			}
			
			builder.applyFilter(new OwnerAndDownFilter(org));
			assets.addAll(persistenceService.findAll(builder));
		}
		return assets;
	}
	
	private List<ApiSynchronizationAsset> getAssignedOpenEventAssets(OfflineProfile profile) {
		List<ApiSynchronizationAsset> assets = new ArrayList<ApiSynchronizationAsset>();		
		Date startDate = new Date();
		Date endDate = profile.getSyncEndDate(startDate);
		
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
		.addOrder("nextDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "eventState", Event.EventState.OPEN))
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", getCurrentUser().getId()))
		.addWhere(WhereClauseFactory.create(Comparator.GE, "nextDate", startDate));
		if(endDate != null) {
			query.addWhere(WhereClauseFactory.create(Comparator.LE, "nextDate", endDate));
		}
		
		List<Event> events = persistenceService.findAll(query);		
		for(Event event : events) {
			assets.add(convert(event.getAsset()));
		}
		return assets;
	}
	
	private List<ApiSynchronizationAsset> getLinkedAssets(Set<ApiSynchronizationAsset> assets) {
		if (assets.isEmpty()) {
			return new ArrayList<ApiSynchronizationAsset>();
		}
		
		List<String> assetIds = new ArrayList<String>();
		for(ApiSynchronizationAsset asset: assets) {
			assetIds.add(asset.getAssetId());
		}
		
		QueryBuilder<ApiSynchronizationAsset> builder = createSubAssetBuilder();
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "masterAsset.mobileGUID", assetIds));
		
		List<ApiSynchronizationAsset> linkedAssets = persistenceService.findAll(builder);
		return linkedAssets;
	}	
	
	private QueryBuilder<ApiSynchronizationAsset> createBuilder() {
		QueryBuilder<ApiSynchronizationAsset> builder = new QueryBuilder<ApiSynchronizationAsset>(Asset.class, securityContext.getUserSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(ApiSynchronizationAsset.class, "mobileGUID", "modified"));
		return builder;
	}
	
	private QueryBuilder<ApiSynchronizationAsset> createSubAssetBuilder() {
		QueryBuilder<ApiSynchronizationAsset> builder = new QueryBuilder<ApiSynchronizationAsset>(SubAsset.class, new OpenSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(ApiSynchronizationAsset.class, "asset.mobileGUID", "asset.modified"));
		return builder;
	}
	
	private ApiSynchronizationAsset convert(Asset asset) {
		ApiSynchronizationAsset apiAsset = new ApiSynchronizationAsset();
		apiAsset.setAssetId(asset.getMobileGUID());
		apiAsset.setModified(asset.getModified());
		return apiAsset;
	}
}
