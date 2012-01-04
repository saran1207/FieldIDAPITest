package com.n4systems.fieldid.ws.v1.resources.assetcount;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.persistence.QueryBuilder;

@Component
@Path("assetCount")
public class ApiAssetCountResource extends FieldIdPersistenceService {

	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAssetCount> getAssetCounts(@QueryParam("orgId") Long[] orgIds) {
		List<ApiAssetCount> apiAssetCount = new ArrayList<ApiAssetCount>();
		for (Long orgId: orgIds) {
			QueryBuilder<Long> builder = new QueryBuilder<Long>(Asset.class, securityContext.getUserSecurityFilter());
			builder.setCountSelect();
			
			BaseOrg org = persistenceService.find(BaseOrg.class, orgId);
			if (org == null) {
				throw new NotFoundException("Organization", orgId);
			}
			
			builder.applyFilter(new OwnerAndDownFilter(org));
			Long assets = persistenceService.find(builder);
			
			apiAssetCount.add(new ApiAssetCount(orgId, assets));
		}
		
		ListResponse<ApiAssetCount> response = new ListResponse<ApiAssetCount>();
		return response;
	}
	
}
