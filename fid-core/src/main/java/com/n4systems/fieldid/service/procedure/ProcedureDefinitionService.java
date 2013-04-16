package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.util.persistence.MaxSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

import java.util.Arrays;
import java.util.List;

public class ProcedureDefinitionService extends FieldIdPersistenceService {

    public Boolean hasPublishedProcedureDefinition(Asset asset) {
        return persistenceService.exists(getPublishedProcedureDefinitionQuery(asset));
    }

    public ProcedureDefinition getPublishedProcedureDefinition(Asset asset) {
        return persistenceService.find(getPublishedProcedureDefinitionQuery(asset));
    }

    private QueryBuilder<ProcedureDefinition> getPublishedProcedureDefinitionQuery(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createTenantSecurityBuilder(ProcedureDefinition.class);

        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        return query;
    }

    public void saveProcedureDefinition(ProcedureDefinition procedureDefinition) {
        if (procedureDefinition.getRevisionNumber()==null) {
            procedureDefinition.setRevisionNumber(generateRevisionNumber(procedureDefinition.getAsset()));
        }
        if (getCurrentTenant().getSettings().getApprovalUserOrGroup() != null) {
            procedureDefinition.setPublishedState(PublishedState.WAITING_FOR_APPROVAL);
        } else {
            procedureDefinition.setPublishedState(PublishedState.PUBLISHED);
        }
        saveIsolationPoints(procedureDefinition.getIsolationPoints());
        persistenceService.saveOrUpdate(procedureDefinition);
    }

    private Long generateRevisionNumber(Asset asset) {
        QueryBuilder<Long> query = new QueryBuilder<Long>(ProcedureDefinition.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("asset",asset);
        query.setSelectArgument(new MaxSelect("revisionNumber"));
        Long biggestRevision = persistenceService.find(query);
        return biggestRevision==null ? 1 :  biggestRevision+1;
    }

    private void saveIsolationPoints(List<IsolationPoint> isolationPoints) {
        for (IsolationPoint isolationPoint:isolationPoints) {
            persistenceService.saveOrUpdate(isolationPoint);
        }
    }

    public List<ProcedureDefinition> getActiveProceduresForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        query.addWhere(WhereParameter.Comparator.IN, "publishedState", "publishedState", Arrays.asList(PublishedState.ACTIVE_STATES));

        return persistenceService.findAll(query);
    }

    public List<ProcedureDefinition> getPreviouslyPublishedProceduresForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        return persistenceService.findAll(query);
    }

    public void publishProcedureDefinition(ProcedureDefinition definition) {
        ProcedureDefinition previousDefinition = getPublishedProcedureDefinition(definition.getAsset());
        if (previousDefinition != null) {
            previousDefinition.setPublishedState(PublishedState.PREVIOUSLY_PUBLISHED);
        }
        definition.setPublishedState(PublishedState.PUBLISHED);
    }

}
