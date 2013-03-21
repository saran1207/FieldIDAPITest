package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ScheduleListRemovalService extends FieldIdPersistenceService {

    // CAVEAT : using IN on events table sucks on MySQL.  you may want to use a loop if you know your list size.
    //  also, be careful how many id's you have in your in clause.  there are limits to be aware of...  most likely 1024
    //    http://www.dbforums.com/mysql/1022217-max-no-values-where-clause.html

    public static final String ARCHIVE_IDS_QUERY = "UPDATE %s SET state = :archivedState, modified = :now  WHERE id IN (:ids)";
    public static final String DELETE_IDS_QUERY = "DELETE FROM %s WHERE id IN (:ids)";

    @Transactional
    public void deleteAssociatedEvents(AssetType assetType, EventType eventType) {
        List<Long> ids = eventIds(assetType, eventType);
        for(Long id: ids) {
            Event schedule = persistenceService.find(Event.class, id);
            schedule.setModified(new Date());
            schedule.archiveEntity();
            persistenceService.update(schedule);
        }
    }

    private List<Long> eventIds(AssetType assetType, EventType eventType) {
        QueryBuilder<Long> query = new QueryBuilder<Long>(Event.class, new TenantOnlySecurityFilter(assetType.getTenant()));
        query.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", Archivable.EntityState.ACTIVE).addSimpleWhere("workflowState", WorkflowState.OPEN).addSimpleWhere("type", eventType);

        if (assetType != null) {
            query.addSimpleWhere("asset.type", assetType);
        }

        return persistenceService.findAll(query);
    }

    private List<Long> scheduleIds(AssetType assetType, EventType eventType, Event.WorkflowStateGrouping workflowStates) {
        QueryBuilder<Long> schedulesToDelete = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
        schedulesToDelete.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", Archivable.EntityState.ACTIVE).addSimpleWhere("type", eventType);
        schedulesToDelete.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(workflowStates.getMembers()));
        schedulesToDelete.addWhere(WhereClauseFactory.createNotNull("dueDate"));

        if (assetType != null) {
            schedulesToDelete.addSimpleWhere("asset.type", assetType);
        }

        return persistenceService.findAll(schedulesToDelete);
    }

    @Transactional
	public ScheduleListRemovalSummary summary(AssetType assetType, EventType eventType, Event.WorkflowStateGrouping workflowStates) {
		return new ScheduleListRemovalSummary((long)scheduleIds(assetType,  eventType, workflowStates).size());
	}

}
