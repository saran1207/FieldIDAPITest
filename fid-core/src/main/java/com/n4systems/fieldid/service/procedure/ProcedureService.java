package com.n4systems.fieldid.service.procedure;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureNotification;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.reporting.UpcomingScheduledLotoRecord;
import com.n4systems.util.persistence.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProcedureService extends FieldIdPersistenceService {

    @Autowired NotifyProcedureAssigneeService notifyProcedureAssigneeService;

    public boolean hasActiveProcedure(Asset asset) {
        return hasActiveProcedure(asset, null);
    }

    public boolean hasActiveProcedure(Asset asset, ProcedureDefinition procedureDefinition) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);

        if(procedureDefinition != null) {
            query.addSimpleWhere("type.familyId", procedureDefinition.getFamilyId());
        }

        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));
        return persistenceService.exists(query);
    }

    public Long createSchedule(Procedure openProcedure) {
        return persistenceService.save(openProcedure);
    }

    public Procedure updateSchedule(Procedure procedure) {
        if(procedure.isSendEmailOnUpdate() && procedure.isAssigneeOrDateChanged()) {
            if(!notifyProcedureAssigneeService.notificationExists(procedure)) {
                ProcedureNotification notification = new ProcedureNotification();
                notification.setProcedure(procedure);
                persistenceService.save(notification);
                procedure.setNotification(notification);
                System.out.println("It's updating the schedule, don't worry man...");
            }
        }

        //Should we update the asset while we're doing this...??
        return persistenceService.update(procedure);
    }

    public void deleteSchedule(Procedure procedure) {
        Preconditions.checkArgument(procedure.getWorkflowState().equals(ProcedureWorkflowState.OPEN));
        persistenceService.archive(procedure);
    }

    public Procedure findByMobileId(String mobileId, boolean withArchived) {
        QueryBuilder<Procedure> builder = createUserSecurityBuilder(Procedure.class, withArchived);
        builder.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
        return persistenceService.find(builder);
    }

    public Procedure getOpenProcedure(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);
        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));
        query.setLimit(1);
        return persistenceService.find(query);
    }

    public Boolean hasOpenProcedure(ProcedureDefinition procedureDefinition) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("type", procedureDefinition);
        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));
        query.setLimit(1);
        return persistenceService.exists(query);
    }

    public List<Procedure> getAllProcedures(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);
        return persistenceService.findAll(query);
    }

    public List<Procedure> getAllOpenProceduresByAssignee(User user) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);

        query.addSimpleWhere("workflowState", ProcedureWorkflowState.OPEN);

        if (user.getGroups().isEmpty()) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId()));
        } else {
            WhereParameterGroup group = new WhereParameterGroup();
            group.setChainOperator(WhereClause.ChainOp.AND);
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
            query.addWhere(group);
        }
        return persistenceService.findAll(query);
    }

    public Procedure getLockedProcedure(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("workflowState", ProcedureWorkflowState.LOCKED);
        query.setOrder("modified", false);
        query.setLimit(1);
        Procedure procedure = persistenceService.find(query);

        return procedure;
    }

    public Long getAllProceduresForAssetTypeCount(AssetType assetType) {
        return persistenceService.count(getProcedureForAssetTypeQueryBuilder(assetType));
    }


    public List<Procedure> getAllProceduresForAssetType(AssetType assetType) {
        return persistenceService.findAll(getProcedureForAssetTypeQueryBuilder(assetType));
    }

    private QueryBuilder<Procedure> getProcedureForAssetTypeQueryBuilder(AssetType assetType) {
        QueryBuilder<Procedure> query = new QueryBuilder<Procedure>(Procedure.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("asset.type.id", assetType.getId());
        return query;
    }

    public void archiveProceduresForAssetType(AssetType assetType) {
        List<Procedure> procedureList = getAllProceduresForAssetType(assetType);

        for (Procedure procedure : procedureList) {
            archiveProcedure(procedure);
        }
    }

    public void archiveProcedure(Procedure procedure) {
        procedure.archiveEntity();
        persistenceService.update(procedure);
    }

    @Transactional(readOnly = true)
    public List<UpcomingScheduledLotoRecord> getUpcomingScheduledLotos(Integer period) {

        QueryBuilder<UpcomingScheduledLotoRecord> builder = new QueryBuilder<UpcomingScheduledLotoRecord>(Procedure.class, securityContext.getUserSecurityFilter());

        builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledLotoRecord.class, "date(dueDate)", "COUNT(*)"));

        Date today = new PlainDate();
        Date endDate = DateUtils.addDays(today, period);

        builder.addWhere(whereFromTo(today, endDate, "dueDate"));
        builder.addSimpleWhere("workflowState", ProcedureWorkflowState.OPEN);

        builder.addGroupByClauses(Arrays.asList(new GroupByClause("date(dueDate)", true)));
        return persistenceService.findAll(builder);
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

    public List<Procedure> getLockedAssignedTo(BaseOrg org) {
        QueryBuilder<Procedure> query = createUserSecurityBuilder(Procedure.class);
        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.LOCKED));
        if(org != null) {
            query.applyFilter(new OwnerAndDownFilter(org));
        }

        List<Procedure> list = persistenceService.findAll(query);

        List<Procedure> filteredList = new ArrayList<Procedure>();

        for(Procedure l:list){
            if(l.getType() != null)
            {
                filteredList.add(l);
            }
        }

        return filteredList;
    }
}
