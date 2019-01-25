package com.n4systems.fieldid.ws.v1.resources.synchronization;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/synchronize")
@Component
public class ApiSynchronizationResource extends FieldIdPersistenceService {
    private static Logger logger = Logger.getLogger(ApiSynchronizationResource.class);

    @Autowired private OfflineProfileService offlineProfileService;
    @Autowired private ProcedureService procedureService;
    
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public ListResponse<ApiSynchronizationAsset> synchronize() {
        setNewRelicWithAppInfoParameters();
        OfflineProfile profile = offlineProfileService.find(getCurrentUser());

        Set<ApiSynchronizationAsset> assets = new HashSet<>();
        if (profile != null) {        
            assets.addAll(getOfflineProfileAssets(profile));
            assets.addAll(getOfflineProfileOrgs(profile));
        }
        assets.addAll(getAssignedOpenEventAssets(profile));
        assets.addAll(getAssignedOpenProcedureAssets(profile));
        assets.addAll(getLinkedAssets(assets));
        
        ListResponse<ApiSynchronizationAsset> response = new ListResponse<>();
        response.getList().addAll(assets);
        response.setTotal(response.getList().size());
        return response;
    }

    @PUT
    @Path("storePlatform")
    @Consumes(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional
    public void storePlatform() {
        setNewRelicWithAppInfoParameters();
        OfflineProfile profile = offlineProfileService.findOrCreate(getCurrentUser());

        profile.setCurrentPlatform(ThreadLocalInteractionContext.getInstance().getCurrentPlatform());
        profile.setCurrentPlatformType(ThreadLocalInteractionContext.getInstance().getCurrentPlatformType());

        offlineProfileService.update(profile);
    }

    private List<ApiSynchronizationAsset> getAssignedOpenProcedureAssets(OfflineProfile profile) {
        Date startDate = new LocalDate().toDate();
        Date endDate = profile == null
                ? getSyncEndDate(OfflineProfile.DEFAULT_SYNC_DURATION, startDate)
                : getSyncEndDate(profile.getSyncDuration(), startDate);

        List<ApiSynchronizationAsset> assets = new ArrayList<>();
        List<Procedure> openProcedures = procedureService.findAllOpenAssignedProcedures(startDate, endDate);
        for (Procedure openProcedure : openProcedures) {
            assets.add(convert(openProcedure.getAsset()));
        }
        return assets;
    }

    private List<ApiSynchronizationAsset> getOfflineProfileAssets(OfflineProfile profile) {
        List<ApiSynchronizationAsset> assets = new ArrayList<>();
        if (!profile.getAssets().isEmpty()) {
            QueryBuilder<ApiSynchronizationAsset> builder = createBuilder();
            builder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileGUID", profile.getAssets()));
            assets.addAll(persistenceService.findAll(builder));
        }
        return assets;
    }
    
    private List<ApiSynchronizationAsset> getOfflineProfileOrgs(OfflineProfile profile) {
        List<ApiSynchronizationAsset> assets = new ArrayList<>();
        for (Long orgId: profile.getOrganizations()) {
            QueryBuilder<ApiSynchronizationAsset> builder = createBuilder();
            
            BaseOrg org = persistenceService.find(BaseOrg.class, orgId);
            if (org == null) {
                logger.error("Organization (probably archived) not found for orgId: " + orgId);
            } else {            
                builder.applyFilter(new OwnerAndDownFilter(org));
                assets.addAll(persistenceService.findAll(builder));
            }
        }
        return assets;
    }
    
    private List<ApiSynchronizationAsset> getAssignedOpenEventAssets(OfflineProfile profile) {
        List<ApiSynchronizationAsset> assets = new ArrayList<>();
        Date startDate = new LocalDate().toDate();
        Date endDate = profile == null 
            ? getSyncEndDate(OfflineProfile.DEFAULT_SYNC_DURATION, startDate)
            : getSyncEndDate(profile.getSyncDuration(), startDate);
        User user = getCurrentUser();
        
        QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class)
        .addOrder("dueDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", WorkflowState.OPEN))
        .addWhere(WhereClauseFactory.create(Comparator.GE, "dueDate", startDate));
        
        if (user.getGroups().isEmpty()) {
            query.addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", user.getId()));
        } else {
            // WE need to do AND ( assignee.id = user.GetId() OR assignedGroup.id = user.getGroup().getId() )                
            WhereParameterGroup group = new WhereParameterGroup();
            group.setChainOperator(WhereClause.ChainOp.AND);
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
            query.addWhere(group);                
        }        
        
        if(endDate != null) {
            query.addWhere(WhereClauseFactory.create(Comparator.LE, "dueDate", endDate));
        }
        
        List<ThingEvent> events = persistenceService.findAll(query);
        for(ThingEvent event : events) {
            assets.add(convert(event.getAsset()));
        }
        return assets;
    }
    
    private List<ApiSynchronizationAsset> getLinkedAssets(Set<ApiSynchronizationAsset> assets) {
        if (assets.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> assetIds = new ArrayList<>();
        for(ApiSynchronizationAsset asset: assets) {
            assetIds.add(asset.getAssetId());
        }
        
        QueryBuilder<ApiSynchronizationAsset> builder = createSubAssetBuilder();
        builder.addWhere(WhereClauseFactory.create(Comparator.IN, "masterAsset.mobileGUID", assetIds));

        return persistenceService.findAll(builder);
    }    
    
    private QueryBuilder<ApiSynchronizationAsset> createBuilder() {
        QueryBuilder<ApiSynchronizationAsset> builder = new QueryBuilder<>(Asset.class, securityContext.getUserSecurityFilter());
        builder.setSelectArgument(new NewObjectSelect(ApiSynchronizationAsset.class, "mobileGUID", "modified"));
        return builder;
    }
    
    private QueryBuilder<ApiSynchronizationAsset> createSubAssetBuilder() {
        QueryBuilder<ApiSynchronizationAsset> builder = new QueryBuilder<>(SubAsset.class, new OpenSecurityFilter());
        builder.setSelectArgument(new NewObjectSelect(ApiSynchronizationAsset.class, "asset.mobileGUID", "asset.modified"));
        return builder;
    }
    
    private ApiSynchronizationAsset convert(Asset asset) {
        ApiSynchronizationAsset apiAsset = new ApiSynchronizationAsset();
        apiAsset.setAssetId(asset.getMobileGUID());
        apiAsset.setModified(asset.getModified());
        return apiAsset;
    }
    
    public static Date getSyncEndDate(SyncDuration syncDuration, Date startDate) {
        DateTime dateTime = new DateTime(startDate);
        
        switch(syncDuration) {
            case WEEK: return dateTime.plusWeeks(1).toDate();
            case MONTH: return dateTime.plusMonths(1).toDate();
            case SIX_MONTHS: return dateTime.plusMonths(6).toDate();
            case YEAR: return dateTime.plusYears(1).toDate();
        }
        
        return null;
    }
}
