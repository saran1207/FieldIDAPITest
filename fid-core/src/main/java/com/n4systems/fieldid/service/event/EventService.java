package com.n4systems.fieldid.service.event;

import static com.google.common.base.Preconditions.*;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.DateHelper;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.EventType;
import com.n4systems.model.Status;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.reporting.CompletedEventsReportRecord;
import com.n4systems.services.reporting.EventCompletenessReportRecord;
import com.n4systems.services.reporting.EventKpiRecord;
import com.n4systems.services.reporting.EventScheduleStatusCount;
import com.n4systems.services.reporting.UpcomingScheduledEventsRecord;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;

public class EventService extends FieldIdPersistenceService {
	
    @Autowired private ReportServiceHelper reportServiceHelper;

	@Transactional(readOnly = true)	
	public List<Event> getEventsByType(Long eventTypeId) {
		QueryBuilder<Event> builder = getEventsByTypeBuilder(eventTypeId);
        return persistenceService.findAll(builder);
	}


    @Transactional(readOnly = true)	
    public List<Event> getEventsByType(Long eventTypeId, Date from, Date to) {
		QueryBuilder<Event> builder = getEventsByTypeBuilder(eventTypeId);
		builder.addWhere(Comparator.GE, "fromDate", "date", from).addWhere(Comparator.LE, "toDate", "date", to);
		builder.setOrder("date", false);
		return persistenceService.findAll(builder);
	}
    
    private QueryBuilder<Event> getEventsByTypeBuilder(Long eventTypeId) {
    	checkArgument(eventTypeId!=null, "you must specify an event type id to get a list of events.");
    	QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);   
    	builder.addSimpleWhere("type.id", eventTypeId);
    	builder.addOrder("date");
    	return builder;
    }
    
    @Transactional(readOnly = true)	
    public List<EventType> getEventTypes() {
        QueryBuilder<EventType> builder = createUserSecurityBuilder(EventType.class);        
        builder.addOrder("name");
        return persistenceService.findAll(builder);
    }    
    
    @Transactional(readOnly = true)
    public Event getEventFromSafetyNetwork(Long eventId) {
		Event event = persistenceService.findNonSecure(Event.class, eventId);
		
		if (event == null) {
			return null;
		}
		
		// If the event is visible to the current user, we don't want to security enhance it
		if (getCurrentUser().getOwner().canAccess(event.getOwner())) {
			return event;
		}
		
		// Access is allowed to this event if we have an asset that is linked to its asset
		QueryBuilder<?> assetExistsQuery = createUserSecurityBuilder(Asset.class)
				.addWhere(WhereClauseFactory.create("networkId", event.getAsset().getNetworkId()));

		if (!persistenceService.exists(assetExistsQuery)) {
			throw new SecurityException("Network event failed security check");
		}

		Event enhancedEvent = EntitySecurityEnhancer.enhance(event, securityContext.getUserSecurityFilter());
		return enhancedEvent;
    }
    
    @Transactional(readOnly = true)	
	public List<UpcomingScheduledEventsRecord> getUpcomingScheduledEvents(Integer period, BaseOrg owner) {

		QueryBuilder<UpcomingScheduledEventsRecord> builder = new QueryBuilder<UpcomingScheduledEventsRecord>(EventSchedule.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledEventsRecord.class, "nextDate", "COUNT(*)"));
		
		Date today = new PlainDate();
		Date endDate = DateUtils.addDays(today, period);		
		
		builder.addWhere(whereFromTo(today, endDate, "nextDate"));
		builder.addSimpleWhere("status", ScheduleStatus.SCHEDULED);

		builder.applyFilter(new OwnerAndDownFilter(owner));
		builder.addGroupBy("nextDate");
		return persistenceService.findAll(builder);		
	}

    private WhereClause<?> whereFromTo(Date fromDate, Date toDate, String property) {
        return whereFromTo(fromDate, toDate, property, null);
    }
    
    private WhereClause<?> whereFromTo(Date fromDate, Date toDate, String property, TimeZone timeZone) {
        if (timeZone!=null) { 
            fromDate = DateHelper.convertToUserTimeZone(fromDate, timeZone);
            toDate = DateHelper.convertToUserTimeZone(toDate, timeZone);
        }
        
    	if (fromDate!=null && toDate!=null) {
    		WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
    		filterGroup.addClause(WhereClauseFactory.create(Comparator.GE, "fromDate", property, fromDate, null, ChainOp.AND));
    		filterGroup.addClause(WhereClauseFactory.create(Comparator.LE, "toDate", property, toDate, null, ChainOp.AND));
    		return filterGroup;
    	} else if (fromDate!=null) {
    		return new WhereParameter<Date>(Comparator.GE, property, fromDate);
    	} else if (toDate!=null) {
    		return new WhereParameter<Date>(Comparator.LT, property, toDate);
    	}
        // CAVEAT : we don't want results to include values with null dates. they are ignored.  (this makes sense for EventSchedules
        //   because null dates are used when representing AdHoc events).
        return new WhereParameter<Date>(Comparator.NOTNULL, property);
	}
        
	@Transactional(readOnly = true)
	public List<CompletedEventsReportRecord> getCompletedEvents(Date fromDate, Date toDate, BaseOrg org, Status status, ChartGranularity granularity) {
        // UGGH : hack.   this is a small, focused approach to fixing yet another time zone bug.
        // this should be reverted when a complete, system wide approach to handling time zones is implemented.
        // see WEB-2836
        TimeZone timeZone = getCurrentUser().getTimeZone();

        QueryBuilder<CompletedEventsReportRecord> builder = new QueryBuilder<CompletedEventsReportRecord>(Event.class, securityContext.getUserSecurityFilter());
		
		NewObjectSelect select = new NewObjectSelect(CompletedEventsReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)");
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularity("date", granularity, timeZone));
		select.setConstructorArgs(args);
		builder.setSelectArgument(select);
		
		builder.addWhere(whereFromTo(fromDate, toDate, "date", timeZone));
		builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity,"date"));		
		builder.applyFilter(new OwnerAndDownFilter(org));
		if (status!=null) { 
			builder.addSimpleWhere("status", status);
		}
		builder.addOrder("date");
		
		return persistenceService.findAll(builder);	
	}

    @Transactional(readOnly = true)
	public EventKpiRecord getEventKpi(Date fromDate, Date toDate, BaseOrg owner) {
		EventKpiRecord eventKpiRecord = new EventKpiRecord();	
		eventKpiRecord.setCustomer(owner);
		
		QueryBuilder<EventScheduleStatusCount> builder1 = new QueryBuilder<EventScheduleStatusCount>(EventSchedule.class, securityContext.getUserSecurityFilter());
		builder1.setSelectArgument(new NewObjectSelect(EventScheduleStatusCount.class, "status", "COUNT(*)"));
        builder1.applyFilter(new OwnerAndDownFilter(owner));

		builder1.addWhere(whereFromTo(fromDate, toDate, "nextDate"));
        
		builder1.addGroupBy("status");
		List<EventScheduleStatusCount> statusCounts = persistenceService.findAll(builder1);
		
		for (EventScheduleStatusCount statusCount: statusCounts ) {
			if(statusCount.status.equals(ScheduleStatus.COMPLETED))
				eventKpiRecord.setCompleted(statusCount.count);
			if(statusCount.status.equals(ScheduleStatus.IN_PROGRESS))
				eventKpiRecord.setInProgress(statusCount.count);
			if(statusCount.status.equals(ScheduleStatus.SCHEDULED))
				eventKpiRecord.setScheduled(statusCount.count);			
		}

		QueryBuilder<EventSchedule> builder2 = new QueryBuilder<EventSchedule>(EventSchedule.class, securityContext.getUserSecurityFilter());
		builder2.applyFilter(new OwnerAndDownFilter(owner));
		builder2.addSimpleWhere("status", ScheduleStatus.COMPLETED);
		builder2.addSimpleWhere("event.status", Status.FAIL);
		builder2.addWhere(whereFromTo(fromDate, toDate, "nextDate"));
		
		Long failedCount = persistenceService.count(builder2);
		
		eventKpiRecord.setFailed(failedCount);
		
		return eventKpiRecord;
	}

    @Transactional(readOnly = true)
	public List<EventCompletenessReportRecord> getEventCompleteness(ChartGranularity granularity, Date fromDate, Date toDate, BaseOrg org) {
		return getEventCompleteness(null, granularity, fromDate, toDate, org); 
	}

    @Transactional(readOnly = true)
	public List<EventCompletenessReportRecord> getEventCompleteness(ScheduleStatus status, ChartGranularity granularity, 
			Date fromDate, Date toDate, BaseOrg org) {
		QueryBuilder<EventCompletenessReportRecord> builder = new QueryBuilder<EventCompletenessReportRecord>(EventSchedule.class, securityContext.getUserSecurityFilter());
		
		NewObjectSelect select = new NewObjectSelect(EventCompletenessReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)");
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularity("nextDate", granularity, null));
		select.setConstructorArgs(args);
		builder.setSelectArgument(select);
		
		builder.addWhere(whereFromTo(fromDate,toDate,"nextDate"));
		builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity,"nextDate"));		
		builder.applyFilter(new OwnerAndDownFilter(org));
		if (status!=null) { 
			builder.addSimpleWhere("status", status);
		}
		builder.addOrder("nextDate");
		
		return persistenceService.findAll(builder);	
	}

    public Event findEventByScheduleId(Long scheduleId){
        EventSchedule eventSchedule = persistenceService.find(EventSchedule.class, scheduleId);
        return persistenceService.find(Event.class, eventSchedule.getEvent().getId());
    }
    
}
