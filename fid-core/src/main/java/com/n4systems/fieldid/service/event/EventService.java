package com.n4systems.fieldid.service.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.util.ExistingEventTransientCriteriaResultPopulator;
import com.n4systems.fieldid.service.event.util.NewEventTransientCriteriaResultPopulator;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.date.DateService;
import com.n4systems.services.reporting.*;
import com.n4systems.util.DateHelper;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.Chartable;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.time.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class EventService extends FieldIdPersistenceService {

    @Autowired private ReportServiceHelper reportServiceHelper;
    @Autowired private AssetService assetService;
    @Autowired private DateService dateService;
    @Autowired private PriorityCodeService priorityCodeService;

    @Transactional(readOnly = true)
	public List<Event> getEventsByType(Long eventTypeId) {
		QueryBuilder<Event> builder = getEventsByTypeBuilder(eventTypeId);
        return persistenceService.findAll(builder);
	}

    @Transactional(readOnly = true)
    public List<Event> getEventsByType(Long eventTypeId, Date from, Date to) {
		QueryBuilder<Event> builder = getEventsByTypeBuilder(eventTypeId);
		builder.addWhere(Comparator.GE, "fromDate", "completedDate", from).addWhere(Comparator.LE, "toDate", "completedDate", to);
        builder.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
		builder.setOrder("completedDate", false);
		return persistenceService.findAll(builder);
	}
    
    private QueryBuilder<Event> getEventsByTypeBuilder(Long eventTypeId) {
    	checkArgument(eventTypeId!=null, "you must specify an event type id to get a list of events.");
    	QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);
    	builder.addSimpleWhere("type.id", eventTypeId);
    	builder.addOrder("completedDate");
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

		QueryBuilder<UpcomingScheduledEventsRecord> builder = new QueryBuilder<UpcomingScheduledEventsRecord>(Event.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(UpcomingScheduledEventsRecord.class, "date(dueDate)", "COUNT(*)"));
		
		Date today = new PlainDate();
		Date endDate = DateUtils.addDays(today, period);		
		
		builder.addWhere(whereFromTo(today, endDate, "dueDate"));
		builder.addSimpleWhere("workflowState", WorkflowState.OPEN);

		builder.applyFilter(new OwnerAndDownFilter(owner));
		builder.addGroupByClauses(Arrays.asList(new GroupByClause("date(dueDate)", true)));
		return persistenceService.findAll(builder);
	}

    private WhereClause<?> whereFromTo(Date fromDate, Date toDate, String property) {
        return whereFromTo(fromDate, toDate, property, null);
    }

    private WhereClause<?> whereFromTo(Date fromDate, Date toDate, String property, TimeZone timeZone) {
        if (timeZone!=null) {
            fromDate = DateHelper.convertToUTC(fromDate, timeZone);
            toDate = DateHelper.convertToUTC(toDate, timeZone);
        }

        if (fromDate!=null && toDate!=null) {
            WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
            filterGroup.addClause(WhereClauseFactory.create(Comparator.GE, "fromDate", property, fromDate, null, ChainOp.AND));
            filterGroup.addClause(WhereClauseFactory.create(Comparator.LT, "toDate", property, toDate, null, ChainOp.AND));
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

    // XXX : converting to UTC is probably the correct way.  other widgets might want to use this...
    private WhereClause<?> whereFromToForCompletedEvents(Date fromDate, Date toDate, String property, TimeZone timeZone) {
        if (timeZone!=null) {
            fromDate = DateHelper.convertToUTC(fromDate, timeZone);
            toDate = DateHelper.convertToUTC(toDate, timeZone);
        }

        if (fromDate!=null && toDate!=null) {
            WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
            filterGroup.addClause(WhereClauseFactory.create(Comparator.GE, "fromDate", property, fromDate, null, ChainOp.AND));
            filterGroup.addClause(WhereClauseFactory.create(Comparator.LT, "toDate", property, toDate, null, ChainOp.AND));
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
	public List<CompletedEventsReportRecord> getCompletedEvents(Date fromDate, Date toDate, BaseOrg org, EventResult eventResult, ChartGranularity granularity) {
        // UGGH : hack.   this is a small, focused approach to fixing yet another time zone bug.
        // this should be reverted when a complete, system wide approach to handling time zones is implemented.
        // see WEB-2836
        TimeZone timeZone = getCurrentUser().getTimeZone();

        QueryBuilder<CompletedEventsReportRecord> builder = new QueryBuilder<CompletedEventsReportRecord>(Event.class, securityContext.getUserSecurityFilter());
		
		NewObjectSelect select = new NewObjectSelect(CompletedEventsReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)");
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularityTimezoneAdjusted("completedDate", granularity, timeZone, fromDate));
		select.setConstructorArgs(args);
		builder.setSelectArgument(select);
		
		builder.addWhere(whereFromToForCompletedEvents(fromDate, toDate, "completedDate", timeZone));
        builder.addSimpleWhere("workflowState", WorkflowState.COMPLETED);

        Date sampleDate = fromDate;
		builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity, "completedDate", timeZone, sampleDate));
		builder.applyFilter(new OwnerAndDownFilter(org));
		if (eventResult != null) {
			builder.addSimpleWhere("eventResult", eventResult);
		}
		builder.addOrder("completedDate");
		
		return persistenceService.findAll(builder);	
	}

    @Transactional(readOnly = true)
	public EventKpiRecord getEventKpi(Date fromDate, Date toDate, BaseOrg owner) {
		EventKpiRecord eventKpiRecord = new EventKpiRecord();	
		eventKpiRecord.setCustomer(owner);
		
		QueryBuilder<EventScheduleStatusCount> builder1 = new QueryBuilder<EventScheduleStatusCount>(Event.class, securityContext.getUserSecurityFilter());
		builder1.setSelectArgument(new NewObjectSelect(EventScheduleStatusCount.class, "workflowState", "COUNT(*)"));
        builder1.applyFilter(new OwnerAndDownFilter(owner));

		builder1.addWhere(whereFromTo(fromDate, toDate, "dueDate"));
		builder1.addGroupBy("workflowState");

		List<EventScheduleStatusCount> statusCounts = persistenceService.findAll(builder1);
		
		for (EventScheduleStatusCount statusCount: statusCounts ) {
            if (statusCount.state.equals(WorkflowState.CLOSED))
                eventKpiRecord.setClosed(statusCount.count);
            if (statusCount.state.equals(WorkflowState.OPEN))
				eventKpiRecord.setIncomplete(statusCount.count);
		}

		QueryBuilder<CompletedResultRecord> builder2 = new QueryBuilder<CompletedResultRecord>(Event.class, securityContext.getUserSecurityFilter());
		builder2.applyFilter(new OwnerAndDownFilter(owner));
		builder2.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
		builder2.addWhere(whereFromTo(fromDate, toDate, "dueDate"));
        builder2.addGroupBy("eventResult");

        builder2.setSelectArgument(new NewObjectSelect(CompletedResultRecord.class, "eventResult", "COUNT(*)"));

        List<CompletedResultRecord> completed = persistenceService.findAll(builder2);
		
        eventKpiRecord.setCompleted(completed);

        return eventKpiRecord;
	}

    @Transactional(readOnly = true)
	public List<EventCompletenessReportRecord> getEventCompleteness(ChartGranularity granularity,Date fromDate, Date toDate, BaseOrg org) {

        // CAVEAT : currently there is a discrepancy across the app w.r.t. COMPLETED events.   some places (like dashboard) consider a CLOSED event completed,
        // while others (like reporting) consider CLOSED & COMPLETED as different.   for example, a dashboard widget might report that there are
        //  100 COMPLETED events but when you click thru to reporting, it will only show 95 because 5 of them are CLOSED.
        //
        // imo, CLOSED should be an custom extension or child of COMPLETED.  (and other extensions like COMPLETED_WAITING_FOR_SIGNOFF could be added)
        // and the code could then decide to include child nodes or not.  (i.e. do you want to see just COMPLETED or [COMPLETED/CLOSED/COMPLETED_WAITING_FOR_SIGNOFF] events)

		QueryBuilder<EventCompletenessReportRecord> builder = new QueryBuilder<EventCompletenessReportRecord>(Event.class, securityContext.getUserSecurityFilter());

        NewObjectSelect select = new NewObjectSelect(EventCompletenessReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)", QueryBuilder.defaultAlias+ ".workflowState");
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularity(granularity, "dueDate"));
		select.setConstructorArgs(args);
		builder.setSelectArgument(select);
		
		builder.addWhere(whereFromTo(fromDate,toDate,"dueDate"));
        Date sampleDate = fromDate;
        builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity,"dueDate", null, sampleDate));
        builder.addGroupBy("workflowState");
		builder.applyFilter(new OwnerAndDownFilter(org));

		builder.addOrder("dueDate");
		
		return persistenceService.findAll(builder);	
	}

    public Event createNewMasterEvent(Long assetId, Long eventTypeId) {
        Event masterEvent = createNewEvent(new Event(), assetId, eventTypeId);
        return masterEvent;
    }

    public Event createEventFromOpenEvent(Long openEventId) {
        Event event = persistenceService.find(Event.class, openEventId);
        return event;
    }

    public void populateNewEvent(Event masterEvent) {
        masterEvent.setEventForm(masterEvent.getType().getEventForm());
        masterEvent.setOwner(masterEvent.getAsset().getOwner());
        masterEvent.setDate(new Date());
        masterEvent.setPerformedBy(getCurrentUser());
        masterEvent.setPrintable(masterEvent.getEventType().isPrintable());
        masterEvent.setAdvancedLocation(masterEvent.getAsset().getAdvancedLocation());
        masterEvent.setPerformedBy(getCurrentUser());
        masterEvent.setProofTestInfo(new ProofTestInfo());
        masterEvent.setInitialResultBasedOnScoreOrOneClicksBeingAvailable();
        masterEvent.setAssetStatus(masterEvent.getAsset().getAssetStatus());
        new NewEventTransientCriteriaResultPopulator().populateTransientCriteriaResultsForNewEvent(masterEvent);
    }

    @Transactional
    private <T extends AbstractEvent> T createNewEvent(T event, Long assetId, Long eventTypeId) {
        EventType eventType = persistenceService.find(EventType.class, eventTypeId);
        Asset asset = persistenceService.find(Asset.class, assetId);

        event.setTenant(getCurrentTenant());
        event.setAsset(asset);
        event.setType(eventType);
        event.setEventForm(eventType.getEventForm());
        event.setAssetStatus(asset.getAssetStatus());

        return event;
    }

    public <T extends AbstractEvent> T lookupExistingEvent(Class<T> clazz, Long eventId) {
        T event = persistenceService.find(clazz, eventId);

        new ExistingEventTransientCriteriaResultPopulator().populateTransientCriteriaResultsForNewEvent(event);

        return event;
    }
    
    public List<Event> getEventsByNetworkId(Long networkId) {
        return getEventsByNetworkId(networkId, null, null, null);
    }

    public List<Event> getEventsByNetworkId(Long networkId, String order, Boolean ascending, List<WorkflowState> states) {

        QueryBuilder<Event> builder = createEventsByNetworkIdQuery(networkId, states);

        builder.addOrder("workflowState", false);
        boolean needsSortJoin = false;

        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("performedBy")) {
                    subOrder = subOrder.replaceAll("performedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    builder.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    builder.addOrder(subOrder, ascending);
                }
            }
        } else {
            builder.addOrder("completedDate", false);
        }

        if (needsSortJoin) {
            builder.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "performedBy", "sortJoin", true));
        }

        return persistenceService.findAll(builder);
    }

    public Long countEventsByNetworkId(Long networkId, List<WorkflowState> states) {
        QueryBuilder<Event> builder = createEventsByNetworkIdQuery(networkId, states);
        return persistenceService.count(builder);
    }

    private QueryBuilder<Event> createEventsByNetworkIdQuery(Long networkId, List<WorkflowState> states) {
        SecurityFilter filter = securityContext.getUserSecurityFilter();

        QueryBuilder<Tenant> connectedTenantsQuery = new QueryBuilder<Tenant>(TypedOrgConnection.class, filter);
        connectedTenantsQuery.setSimpleSelect("connectedOrg.tenant", true);

        SubSelectInClause insideSafetyNetworkSubClause = new SubSelectInClause("asset.owner.tenant", connectedTenantsQuery);


        QueryBuilder<Event> builder;
        if (getCurrentUser().getGroups().isEmpty()) {
            // Users without groups can pull in safety network events
            builder = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
            WhereParameterGroup wpg = new WhereParameterGroup();
            wpg.addClause(insideSafetyNetworkSubClause);
            wpg.addClause(WhereClauseFactory.create(Comparator.EQ, "asset.owner.tenant.id", filter.getTenantId(), ChainOp.OR));
            builder.addWhere(wpg);
        } else {
            // Users in groups can pull in only local events of their group. Group filtering is done in the user security filter.
            builder = createUserSecurityBuilder(Event.class);
        }

        builder.addWhere(WhereClauseFactory.create("asset.networkId", networkId));

        if(states!= null && !states.isEmpty()) {
            builder.addWhere(WhereClauseFactory.create(Comparator.IN, "workflowStatesList", "workflowState", states));
        }

        return builder;
    }

    public Event findByMobileId(String mobileId) {
    	return findByMobileId(mobileId, false);
    }
    
    public Event findByMobileId(String mobileId, boolean withArchived) {
    	QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class, withArchived);
    	builder.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
    	Event event = persistenceService.find(builder);
    	return event;
    }
    
    public List<Event> getLastEventOfEachType(Long assetId) {
		QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, securityContext.getUserSecurityFilter(), "i");
		builder.addWhere(WhereClauseFactory.create("asset.id", assetId));
        builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED));

		PassthruWhereClause latestClause = new PassthruWhereClause("latest_event");
		String maxDateSelect = String.format("SELECT MAX(iSub.completedDate) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.type.state = :iSubState AND iSub.asset.id = :iSubAssetId GROUP BY iSub.type", Event.class.getName());
		latestClause.setClause(String.format("i.completedDate IN (%s)", maxDateSelect));
		latestClause.getParams().put("iSubAssetId", assetId);
		latestClause.getParams().put("iSubState", EntityState.ACTIVE);
		builder.addWhere(latestClause);

        return persistenceService.findAll(builder);
	}

    public Event retireEvent(Event event) {
        event.retireEntity();
        event = persistenceService.update(event);
        event.setAsset(persistenceService.update(event.getAsset()));
        persistenceService.update(event);
        return event;
    }

    public ChartSeries<String> getUpcomingActions(Date from, Date to, BaseOrg owner, User assignee, EventType actionType) {
        Date now = dateService.nowUTC().toDate();
        /** if you specify a date range that is >=today there will, by definition be no overdue actions so skip this step. */
        List<? extends Chartable<String>> upcomingActions = Lists.newArrayList();
        if (to==null || !to.before(now)) {
            QueryBuilder<ActionsReportRecord> query = createActionsQuery(owner, assignee, actionType, ActionBar.UPCOMING);
            if (from==null || from.before(now)) {
                from = now;
            }
            query.addWhere(Comparator.GE, "from", "dueDate", from);
            query.addNullSafeWhere(Comparator.LT, "to", "dueDate", to);
            upcomingActions = persistenceService.findAll(query);
        }
        return new ChartSeries<String>(ActionBar.UPCOMING, "Upcoming", getPaddedResults(upcomingActions, ActionBar.UPCOMING));
    }

    public ChartSeries<String> getOverdueActions(Date from, Date to, BaseOrg owner, User assignee, EventType actionType) {
        Date now = dateService.nowUTC().toDate();
        List<? extends Chartable<String>> overdueActions = Lists.newArrayList();
        /** if you specify a date range that is >=today there will, by definition be no overdue actions so skip this step. */
        if (from==null || from.before(now)) {
            QueryBuilder<ActionsReportRecord> query = createActionsQuery(owner, assignee, actionType,ActionBar.OVERDUE);
            if (to==null || to.after(now)) {
                to = now;
            }
            query.addWhere(Comparator.LT, "to", "dueDate", to);
            query.addNullSafeWhere(Comparator.GE, "from", "dueDate", from);

            overdueActions = persistenceService.findAll(query);
        }

        return new ChartSeries<String>(ActionBar.OVERDUE, "Overdue ", getPaddedResults(overdueActions, ActionBar.OVERDUE));
    }

    private List<Chartable<String>> getPaddedResults(List<? extends Chartable<String>> resultActions, ActionBar barType) {
        List<PriorityCode> priorityCodes = priorityCodeService.getActivePriorityCodes();
        List<PriorityCode> differenceList = Lists.newArrayList(priorityCodes);
        for(PriorityCode priorityCode: priorityCodes) {
            for(Chartable<String> record: resultActions) {
                if (priorityCode.getName().equals(record.getX()))
                    differenceList.remove(priorityCode);
            }
        }
        List<Chartable<String>> paddedList = Lists.newArrayList();
        for(PriorityCode priorityCode: priorityCodes) {
            ActionsReportRecord paddingRecord = new ActionsReportRecord(priorityCode.getName(), 0L, barType.getDisplayName());
            paddedList.add(paddingRecord);
        }
        paddedList.addAll(resultActions);
        return paddedList;
    }

    private QueryBuilder<ActionsReportRecord> createActionsQuery(BaseOrg owner, User assignee, EventType actionType, ActionBar barType) {
        //Preconditions.checkArgument(actionType==null || actionType.getGroup().isAction(), "given event type [ " + actionType + " ] is not a valid 'Action'.");
        QueryBuilder<ActionsReportRecord> query = new QueryBuilder<ActionsReportRecord>(Event.class, securityContext.getUserSecurityFilter());
        NewObjectSelect select = new NewObjectSelect(ActionsReportRecord.class);
        select.setConstructorArgs(Lists.newArrayList("obj.priority.name", "COUNT(*)", "'"+barType.getDisplayName()+"'"));
        query.setSelectArgument(select);

        query.addNullSafeWhere(Comparator.EQ, "owner", "owner", owner);
        query.addNullSafeWhere(Comparator.EQ, "assignee", "assignee", assignee);
        query.addNullSafeWhere(Comparator.EQ, "type", "type", actionType);
        // TODO query.addSimpleWhere("type.group.action", Boolean.TRUE);
        query.addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
        query.addWhere(Comparator.NOTNULL, "triggeredEvent", "triggeredEvent", null);
        // make sure it's an action-able event type group.
        query.addSimpleWhere("type.group.action", Boolean.TRUE);
        query.addGroupBy("priority");
        return query;
    }

    public List<Event> getWork(DateRange dateRange, User user, BaseOrg org, AssetType assetType, EventType eventType, int limit) {
        QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);

        addToWorkQuery(builder, user, org, assetType, eventType, dateRange);
        builder.setLimit(limit + 1);

        return persistenceService.findAll(builder);
    }

    /**
     * @return map of date --> # of events on that date.   results will be sorted and padded so 0 entries will be populated.
     * CAVEAT : in almost all other code we use the standard definition of a week which is Monday-->Sunday.
     *  because the calendar widget which uses this service call is more "kitchen datebook" style and shows sunday as the first day of the week we can't use standard
     *  joda day of week calculations.  i.e. make sure you are clear when using .weekOfYear() or .dayOfWeek() etc...
     */
    public Map<LocalDate,Long> getMontlyWorkSummary(LocalDate dayInMonth, User user, BaseOrg org, AssetType assetType, EventType eventType) {
        QueryBuilder<WorkSummaryRecord> builder = new QueryBuilder<WorkSummaryRecord>(Event.class, securityContext.getUserSecurityFilter());

        // NOTE : From is defined as the Sunday of the first week including the first day of the month.
        //   there it will typically include the last few days of previous month.  the reverse applies for To.

        LocalDate from = DateUtil.getSundayOfWeek(dayInMonth);
        LocalDate to = DateUtil.getSundayAfterWeek(dayInMonth.plusMonths(1).withDayOfMonth(1));

        NewObjectSelect select = new NewObjectSelect(WorkSummaryRecord.class);
        select.setConstructorArgs(Lists.newArrayList("COUNT(*)","DATE(dueDate)"));
        builder.setSelectArgument(select);

        addToWorkQuery(builder, user, org, assetType, eventType, new DateRange(from,to));

        builder.addGroupByClauses(	Lists.newArrayList(new GroupByClause("DATE(dueDate)", true)) );

        List<WorkSummaryRecord> data = persistenceService.findAll(builder);

        Map<LocalDate,Long> result = Maps.newTreeMap();

        LocalDate date = from;
        while (date.isBefore(to)) {
            result.put(date, (long) 0);
            date = date.plusDays(1);
        }
        for (WorkSummaryRecord record:data) {
            result.put(new LocalDate(record.getDate()), record.getCount());
        }
        return result;
    }

    private void addToWorkQuery(QueryBuilder<?> builder, User user, BaseOrg org, AssetType assetType, EventType eventType, DateRange dateRange) {
        builder.addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);
        builder.addNullSafeWhere(Comparator.EQ, "asset_type", "asset.type", assetType);
        builder.addNullSafeWhere(Comparator.EQ, "type", "type", eventType);
        builder.addNullSafeWhere(Comparator.EQ, "assignee", "assignee", user);
        builder.addWhere(Comparator.GE, "from", "dueDate", dateRange.getFrom().toDate());
        builder.addWhere(Comparator.LT, "to", "dueDate", dateRange.getTo().toDate());
        if (org!=null) {
            builder.applyFilter(new OwnerAndDownFilter(org));
        }
        builder.addOrder("dueDate");
    }



    public enum ActionBar {
        OVERDUE("Overdue"),UPCOMING("Upcoming");
        private String displayName;

        ActionBar(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }

    public Event findPreviousEventOfSameType(Event event) {
        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, securityContext.getUserSecurityFilter(), "i");
        builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED));
        builder.addWhere(WhereClauseFactory.create("asset.id", event.getAsset().getId()));
        builder.addWhere(WhereClauseFactory.create("type.id", event.getType().getId()));

        PassthruWhereClause latestClause = new PassthruWhereClause("latest_event");
        String maxDateSelect = String.format("SELECT MAX(iSub.completedDate) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.asset.id = :iSubAssetId AND iSub.type.id = :iSubTypeId AND iSub.completedDate < :iSubCompletedDate", Event.class.getName());
        latestClause.setClause(String.format("i.completedDate = (%s)", maxDateSelect));
        latestClause.getParams().put("iSubAssetId", event.getAsset().getId());
        latestClause.getParams().put("iSubTypeId", event.getType().getId());
        latestClause.getParams().put("iSubCompletedDate", event.getCompletedDate());
        latestClause.getParams().put("iSubState", EntityState.ACTIVE);
        builder.addWhere(latestClause);
        builder.setLimit(1);

        return persistenceService.find(builder);
    }

    @Transactional
    public Event findNextOpenEventOfSameType(Event event) {
        return findNextEventOfSameType(event, true);
    }

    @Transactional
    public Event findNextOpenOrCompletedEventOfSameType(Event event) {
        return findNextEventOfSameType(event, false);
    }

    private Event findNextEventOfSameType(Event event, boolean openOnly) {
        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, securityContext.getUserSecurityFilter(), "i");

        if (openOnly) {
            builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.OPEN));
        }

        builder.addWhere(WhereClauseFactory.create("asset.id", event.getAsset().getId()));
        builder.addWhere(WhereClauseFactory.create("type.id", event.getType().getId()));

        PassthruWhereClause latestClause = new PassthruWhereClause("latest_event");
        String minDateSelect = String.format("SELECT MIN(iSub.dueDate) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.asset.id = :iSubAssetId AND iSub.type.id = :iSubTypeId AND iSub.dueDate > :iSubCompletedDate ", Event.class.getName());

        if (openOnly) {
            minDateSelect += " AND iSub.workflowState = :iSubEventWorkflowState";
            latestClause.getParams().put("iSubEventWorkflowState", WorkflowState.OPEN);
        }

        latestClause.setClause(String.format("i.dueDate = (%s)", minDateSelect));
        latestClause.getParams().put("iSubAssetId", event.getAsset().getId());
        latestClause.getParams().put("iSubTypeId", event.getType().getId());
        latestClause.getParams().put("iSubCompletedDate", event.getCompletedDate());
        latestClause.getParams().put("iSubState", EntityState.ACTIVE);
        builder.addWhere(latestClause);
        builder.setLimit(1);

        return persistenceService.find(builder);
    }

}



