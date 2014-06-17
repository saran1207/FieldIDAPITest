package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by rrana on 2014-06-12.
 */
public class ProcedureAuditEventService extends FieldIdPersistenceService {

    private static final Logger logger=Logger.getLogger(ProcedureAuditEventService.class);

    public Long getSelectedAuditCount(String procedureCode, Asset asset, boolean isAsset){

        QueryBuilder<Procedure> procedureCountQuery = createUserSecurityBuilder(Procedure.class);

        procedureCountQuery.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        procedureCountQuery.addSimpleWhere("procedureDefinition.asset", asset);
        procedureCountQuery.addSimpleWhere("workflowState", WorkflowState.OPEN);

        if(!isAsset) {
            procedureCountQuery.addSimpleWhere("procedureDefinition.procedureCode", procedureCode.trim());
        }

        return persistenceService.count(procedureCountQuery);
    }

    public Long getAllAuditCount(String searchTerm){

        QueryBuilder<ProcedureAuditEvent> procedureCountQuery = createUserSecurityBuilder(ProcedureAuditEvent.class);

        procedureCountQuery.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        procedureCountQuery.addSimpleWhere("workflowState", WorkflowState.OPEN);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureDefinition.procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "procedureDefinition.equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureCountQuery.addWhere(group);
        }

        return persistenceService.count(procedureCountQuery);
    }

    public List<ProcedureAuditEvent> getSelectedAuditProcedures(String procedureCode, Asset asset, boolean isAsset, String order, boolean ascending, int first, int count) {

        QueryBuilder<ProcedureAuditEvent> procedureCountQuery = createUserSecurityBuilder(ProcedureAuditEvent.class);

        procedureCountQuery.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        procedureCountQuery.addSimpleWhere("procedureDefinition.asset", asset);
        procedureCountQuery.addSimpleWhere("workflowState", WorkflowState.OPEN);

        if(!isAsset) {
            procedureCountQuery.addSimpleWhere("procedureDefinition.procedureCode", procedureCode.trim());
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("procedureDefinition.developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    procedureCountQuery.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    procedureCountQuery.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            procedureCountQuery.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "procedureDefinition.developedBy", "sortJoin", true));
        }

        return persistenceService.findAll(procedureCountQuery);
    }

    public List<ProcedureAuditEvent> getAllAuditProcedures(String sTerm, String order, boolean ascending, int first, int count) {

        QueryBuilder<ProcedureAuditEvent> procedureCountQuery = createUserSecurityBuilder(ProcedureAuditEvent.class);

        procedureCountQuery.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        procedureCountQuery.addSimpleWhere("workflowState", WorkflowState.OPEN);

        if(!sTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureDefinition.procedureCode", sTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "procedureDefinition.equipmentNumber", sTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureCountQuery.addWhere(group);
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("procedureDefinition.developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    procedureCountQuery.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    procedureCountQuery.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            procedureCountQuery.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "procedureDefinition.developedBy", "sortJoin", true));
        }

        return persistenceService.findAll(procedureCountQuery);
    }

    public List<ProcedureAuditEvent> getAllCompletedAuditsForAsset(Asset asset) {
        QueryBuilder<ProcedureAuditEvent> procedureCountQuery = createUserSecurityBuilder(ProcedureAuditEvent.class);

        procedureCountQuery.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        procedureCountQuery.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
        procedureCountQuery.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        procedureCountQuery.addSimpleWhere("procedureDefinition.asset", asset);
        procedureCountQuery.addOrder("completedDate", false);

        return persistenceService.findAll(procedureCountQuery);
    }

    public Event retireEvent(ProcedureAuditEvent event) {
        event.retireEntity();
        event = persistenceService.update(event);
        return event;
    }

}
