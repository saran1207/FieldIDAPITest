package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.util.persistence.QueryBuilder;

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

}
