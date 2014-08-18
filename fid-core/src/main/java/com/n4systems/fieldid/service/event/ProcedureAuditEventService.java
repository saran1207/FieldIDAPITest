package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.reporting.UpcomingScheduledProcedureAuditsRecord;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Date;
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

    public List<ProcedureAuditEvent> getAuditProceduresByAsset(Asset asset) {

        QueryBuilder<ProcedureAuditEvent> query = createUserSecurityBuilder(ProcedureAuditEvent.class);

        query.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        query.addSimpleWhere("procedureDefinition.asset", asset);
        query.addSimpleWhere("workflowState", WorkflowState.OPEN);

        return persistenceService.findAll(query);
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

    public List<ProcedureAuditEvent> getSelectedAuditProcedures(String procedureCode, Asset asset, boolean isAsset, Date fromDate, Date toDate, String order, boolean ascending, int first, int count, BaseOrg owner) {

        QueryBuilder<ProcedureAuditEvent> query = createUserSecurityBuilder(ProcedureAuditEvent.class);

        query.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        query.addSimpleWhere("procedureDefinition.asset", asset);
        query.addSimpleWhere("workflowState", WorkflowState.OPEN);
        query.addWhere(whereFromTo(fromDate, toDate, "dueDate"));

        query.applyFilter(new OwnerAndDownFilter(owner));

        if(!isAsset) {
            query.addSimpleWhere("procedureDefinition.procedureCode", procedureCode.trim());
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
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "procedureDefinition.developedBy", "sortJoin", true));
        }

        return persistenceService.findAll(query);
    }

    public List<ProcedureAuditEvent> getAllAuditProcedures(String sTerm, Date fromDate, Date toDate, String order, boolean ascending, int first, int count, BaseOrg owner) {

        QueryBuilder<ProcedureAuditEvent> query = createUserSecurityBuilder(ProcedureAuditEvent.class);

        query.addSimpleWhere("recurringEvent.type", RecurringLotoEvent.RecurringLotoEventType.AUDIT);
        query.addSimpleWhere("workflowState", WorkflowState.OPEN);
        query.addWhere(whereFromTo(fromDate, toDate, "dueDate"));

        query.applyFilter(new OwnerAndDownFilter(owner));

        if(!sTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureDefinition.procedureCode", sTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "procedureDefinition.equipmentNumber", sTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            query.addWhere(group);
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
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "procedureDefinition.developedBy", "sortJoin", true));
        }

        return persistenceService.findAll(query);
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

    public List<UpcomingScheduledProcedureAuditsRecord> getUpcomingScheduledProcedureAudits(Integer period, BaseOrg owner) {

        QueryBuilder<UpcomingScheduledProcedureAuditsRecord> builder = new QueryBuilder<UpcomingScheduledProcedureAuditsRecord>(ProcedureAuditEvent.class, securityContext.getUserSecurityFilter());

        builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledProcedureAuditsRecord.class, "date(dueDate)", "COUNT(*)"));

        Date today = new PlainDate();
        Date endDate = DateUtils.addDays(today, period);

        builder.addWhere(whereFromTo(today, endDate, "dueDate"));
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);

        builder.applyFilter(new OwnerAndDownFilter(owner));
        builder.addGroupByClauses(Arrays.asList(new GroupByClause("date(dueDate)", true)));
        return persistenceService.findAll(builder);
    };

    public Event retireEvent(ProcedureAuditEvent event) {
        event.retireEntity();
        event = persistenceService.update(event);
        return event;
    }

    private WhereClause<?> whereFromTo(Date fromDate, Date toDate, String property) {

        if (fromDate!=null && toDate!=null) {
            WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
            filterGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.GE, "fromDate", property, fromDate, null, WhereClause.ChainOp.AND));
            filterGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LT, "toDate", property, toDate, null, WhereClause.ChainOp.AND));
            return filterGroup;
        } else if (fromDate!=null) {
            return new WhereParameter<Date>(WhereParameter.Comparator.GE, property, fromDate);
        } else if (toDate!=null) {
            return new WhereParameter<Date>(WhereParameter.Comparator.LT, property, toDate);
        }
        // CAVEAT : we don't want results to include values with null dates. they are ignored.  (this makes sense for EventSchedules
        //   because null dates are used when representing AdHoc events).
        return new WhereParameter<Date>(WhereParameter.Comparator.NOTNULL, property);
    }

}
