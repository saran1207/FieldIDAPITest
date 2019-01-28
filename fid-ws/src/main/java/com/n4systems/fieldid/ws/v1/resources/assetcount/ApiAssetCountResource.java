package com.n4systems.fieldid.ws.v1.resources.assetcount;

import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.FieldIdPersistenceServiceWithEnhancedLogging;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.newrelic.api.agent.Trace;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("assetCount")
public class ApiAssetCountResource extends FieldIdPersistenceServiceWithEnhancedLogging {

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public ListResponse<ApiAssetCount> getAssetCounts(@QueryParam("orgId") List<Long> orgIds) {
        setEnhancedLoggingWithAppInfoParameters();
        List<ApiAssetCount> assetCounts = new ArrayList<ApiAssetCount>();

        BaseOrg currentUserOwner = getCurrentUser().getOwner();
        for (Long orgId: orgIds) {
            QueryBuilder<Long> builder = new QueryBuilder<Long>(Asset.class, securityContext.getUserSecurityFilter());
            builder.setCountSelect();

            BaseOrg org = null;

            org = persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, orgId);

            if (org == null) {
                throw new NotFoundException("Organization", orgId);
            }

            builder.applyFilter(new OwnerAndDownFilter(org));
            Long assets = persistenceService.find(builder);
            
            assetCounts.add(new ApiAssetCount(orgId, assets));
        }
        
        ListResponse<ApiAssetCount> response = new ListResponse<ApiAssetCount>(assetCounts, 0, 0, assetCounts.size());
        return response;
    }
    
}
