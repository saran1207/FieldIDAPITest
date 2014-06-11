package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class  EventScheduleService extends FieldIdPersistenceService {

    private static Logger logger = Logger.getLogger( EventScheduleService.class );

    @Autowired private NotifyEventAssigneeService notifyEventAssigneeService;

	@Transactional(readOnly = true)
	public ThingEvent getNextEventSchedule(Long assetId, Long eventTypeId) {
		QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class)
				.addOrder("dueDate")
				.addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", WorkflowState.OPEN))
				.addWhere(WhereClauseFactory.create("asset.id", assetId));

		if (eventTypeId != null) {
			query.addWhere(WhereClauseFactory.create("type.id", eventTypeId));
		}

		List<ThingEvent> schedules = persistenceService.findAll(query);
		return (schedules.isEmpty()) ? null : schedules.get(0);
	}
	
	@Transactional(readOnly = true)
	public ThingEvent findByMobileId(String mobileId) {
		return findByMobileId(mobileId, false);
	}

	@Transactional(readOnly = true)
	public ThingEvent findByMobileId(String mobileId, boolean withArchived) {
		QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class, withArchived);
		query.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
        ThingEvent eventSchedule = persistenceService.find(query);
		return eventSchedule;
	}	

    @Transactional
    public void create(AssetTypeSchedule schedule) {
        persistenceService.save(schedule);
    }

    @Transactional
    public List<ThingEvent> getAvailableSchedulesFor(Asset asset) {
        QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class);
        query.addSimpleWhere("asset", asset).addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
        query.addOrder("dueDate");

        return persistenceService.findAll(query);
    }

    @Transactional
    public List<PlaceEvent> getAvailableSchedulesFor(BaseOrg place) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("place", place).addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
        query.addOrder("dueDate");

        return persistenceService.findAll(query);
    }

    @Transactional
    public Event getNextAvailableSchedule(ThingEvent event) {
        QueryBuilder<ThingEvent> builder = createTenantSecurityBuilder(ThingEvent.class);
        builder.addSimpleWhere("recurringEvent", event.getRecurringEvent());
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);

        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", event.getId()));
        builder.addWhere(WhereClauseFactory.create("asset.id", event.getAsset().getId()));
        builder.addWhere(WhereClauseFactory.create("type.id", event.getType().getId()));
        builder.addOrder("dueDate");
        builder.setLimit(1);

        return persistenceService.find(builder);
    }

    @Transactional
    public PlaceEvent getNextAvailableSchedule(PlaceEvent event) {
        QueryBuilder<PlaceEvent> builder = createTenantSecurityBuilder(PlaceEvent.class);
        builder.addSimpleWhere("recurringEvent", event.getRecurringEvent());
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);

        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", event.getId()));
        builder.addWhere(WhereClauseFactory.create("place.id", event.getPlace().getId()));
        builder.addWhere(WhereClauseFactory.create("type.id", event.getType().getId()));
        builder.addOrder("dueDate");
        builder.setLimit(1);

        return persistenceService.find(builder);
    }

    @Transactional
    public Event updateSchedule(ThingEvent schedule) {
        ThingEvent updatedSchedule = persistenceService.update(schedule);
        updatedSchedule.getAsset().touch();
        persistenceService.update(updatedSchedule.getAsset());
        return updatedSchedule;
    }

    @Transactional
    @Deprecated //duplicate see PlaceEventScheduleService
    public Event updateSchedule(PlaceEvent schedule) {
        PlaceEvent updatedSchedule = persistenceService.update(schedule);
        return updatedSchedule;
    }

    @Transactional
    public Long createSchedule(ThingEvent openEvent) {
        openEvent.setOwner(openEvent.getAsset().getOwner());
        Long id = persistenceService.save(openEvent);
        //Update the asset to notify mobile of change
        openEvent.getAsset().touch();
        persistenceService.update(openEvent.getAsset());
        return id;
    }
}
