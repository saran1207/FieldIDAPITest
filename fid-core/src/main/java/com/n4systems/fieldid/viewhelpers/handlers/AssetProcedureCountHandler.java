package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

import java.util.Arrays;

/**
 * Created by rrana on 2015-10-13.
 */
public class AssetProcedureCountHandler extends WebOutputHandler {

    private PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

    public AssetProcedureCountHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return getAssetProcedureCount(value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return getAssetProcedureCount(value);
    }

    private String getAssetProcedureCount(Object value) {
        Asset asset = (Asset) value;

        QueryBuilder<ProcedureDefinition> builder = new QueryBuilder<>(ProcedureDefinition.class, new OpenSecurityFilter());
        builder.addSimpleWhere("asset.id", asset.getId());
        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        builder.addWhere(WhereParameter.Comparator.IN, "publishedState", "publishedState", Arrays.asList(com.n4systems.model.procedure.PublishedState.ACTIVE_STATES));

        Long count = persistenceManager.findCount(builder);
        return Long.toString(count);
    }
}