package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class EventScheduleService extends FieldIdPersistenceService {

    private static Logger logger = Logger.getLogger( EventScheduleService.class );

    @Autowired private NotifyEventAssigneeService notifyEventAssigneeService;

	@Transactional(readOnly = true)
	public Event getNextEventSchedule(Long assetId, Long eventTypeId) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
				.addOrder("dueDate")
				.addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", WorkflowState.OPEN))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		if (eventTypeId != null) {
			query.addWhere(WhereClauseFactory.create("type.id", eventTypeId));
		}

		List<Event> schedules = persistenceService.findAll(query);
		return (schedules.isEmpty()) ? null : schedules.get(0);
	}
	
	@Transactional(readOnly = true)
	public Event findByMobileId(String mobileId) {
		return findByMobileId(mobileId, false);
	}

	@Transactional(readOnly = true)
	public Event findByMobileId(String mobileId, boolean withArchived) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class, withArchived);
		query.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
        Event eventSchedule = persistenceService.find(query);
		return eventSchedule;
	}	

    @Transactional
    public void create(AssetTypeSchedule schedule) {
        persistenceService.save(schedule);
    }

    @Transactional
    public List<Event> getAvailableSchedulesFor(Asset asset) {
        QueryBuilder<Event> query = createUserSecurityBuilder(Event.class);
        query.addSimpleWhere("asset", asset).addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
        query.addOrder("dueDate");

        return persistenceService.findAll(query);
    }

    @Transactional
    public Event getNextAvailableSchedule(Event event) {

        QueryBuilder<Event> builder = createTenantSecurityBuilder(Event.class);
        builder.addSimpleWhere("recurringEvent", event.getRecurringEvent());
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);

        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", event.getId()));
        builder.addWhere(WhereClauseFactory.create("asset.id", event.getAsset().getId()));
        builder.addWhere(WhereClauseFactory.create("type.id", event.getType().getId()));
        builder.addOrder("dueDate");

//        PassthruWhereClause latestClause = new PassthruWhereClause("latest_event");
//        String minDateSelect = String.format("SELECT MIN(iSub.dueDate) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.asset.id = :iSubAssetId AND iSub.type.id = :iSubTypeId AND iSub.dueDate > :iSubCompletedDate ", Event.class.getName());
//         minDateSelect += " AND iSub.workflowState = :iSubEventWorkflowState";
//         latestClause.getParams().put("iSubEventWorkflowState", WorkflowState.OPEN);
//        latestClause.setClause(String.format("i.dueDate = (%s)", minDateSelect));
//        latestClause.getParams().put("iSubAssetId", event.getAsset().getId());
//        latestClause.getParams().put("iSubTypeId", event.getType().getId());
//        latestClause.getParams().put("iSubCompletedDate", event.getCompletedDate());
//        latestClause.getParams().put("iSubState", Archivable.EntityState.ACTIVE);
//        builder.addWhere(latestClause);
        builder.setLimit(1);

        return persistenceService.find(builder);
    }


    @Transactional
    public Event updateSchedule(Event schedule) {
        Event updatedSchedule = persistenceService.update(schedule);
        updatedSchedule.getAsset().touch();
        persistenceService.update(updatedSchedule.getAsset());
        return updatedSchedule;
    }

    @Transactional
    public Long createSchedule(Event openEvent) {
        openEvent.setOwner(openEvent.getAsset().getOwner());
        Long id = persistenceService.save(openEvent);
        //Update the asset to notify mobile of change
        openEvent.getAsset().touch();
        persistenceService.update(openEvent.getAsset());
        return id;
    }

}
