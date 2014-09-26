package com.n4systems.fieldid.service.schedule;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.PlaceEventScheduleService;
import com.n4systems.fieldid.service.event.ProcedureAuditScheduleService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.model.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

@Transactional
public class RecurringScheduleService extends FieldIdPersistenceService {

    public static final int DAILY_EVENT_COUNT = 14;
    public static final int WEEKLY_EVENT_COUNT = 6;
    public static final int BIWEEKLY_EVENT_COUNT = 8;
    public static final int MONTHLY_EVENT_COUNT = 2;
    public static final int ANNUAL_EVENT_COUNT = 2;
    public static final int ANNUAL_MONTH_THRESHOLD = 18;
    public static final int WEEKDAY_EVENT_COUNT = 10;
    public static final int WEEKLY_WEEKDAY_EVENT_COUNT = 2;

    @Autowired private EventScheduleService eventScheduleService;
    @Autowired private PlaceEventScheduleService placeEventScheduleService;
    @Autowired private ProcedureAuditScheduleService procedureAuditScheduleService;
    @Autowired private ProcedureService procedureService;
    @Autowired private AsyncService asyncService;

    private static final Logger logger= Logger.getLogger(RecurringScheduleService.class);


    /******* ThingEvents ********/

    public List<RecurringAssetTypeEvent> getRecurringAssetTypeEvents() {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("tenant.disabled", false);
        return persistenceService.findAll(query);
    }

    public List<RecurringAssetTypeEvent> getRecurringAssetTypeEvents(AssetType assetType) {
        QueryBuilder<RecurringAssetTypeEvent> query = createTenantSecurityBuilder(RecurringAssetTypeEvent.class);
        query.addSimpleWhere("assetType", assetType);
        return persistenceService.findAll(query);
    }

    public List<ThingEvent> getRecurringEventsForAsset(Asset asset, RecurringAssetTypeEvent event){
        QueryBuilder<ThingEvent> query = createTenantSecurityBuilder(ThingEvent.class);
        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("recurringEvent", event);
        return persistenceService.findAll(query);
    }

    public void verifyAssetOwnerRecurringSchedules(Asset asset) {
        for (RecurringAssetTypeEvent recurringEvent: getRecurringAssetTypeEvents(asset.getType())) {
            if(recurringEvent.getOwner() != null && !recurringEvent.getOwner().equals(asset.getOwner())) {
                for(Event event: getRecurringEventsForAsset(asset, recurringEvent)) {
                    event.archiveEntity();
                    persistenceService.update(event);
                }
            }
        }
    }

    public void scheduleAnEventFor(RecurringAssetTypeEvent event, LocalDateTime futureDate) {
        List<Asset> assetsToSchedule = getAssetsByAssetType(event);

        for (Asset asset : assetsToSchedule) {
            if(!checkIfScheduleExists(asset, event, futureDate)) {
                ThingEvent schedule = new ThingEvent();
                schedule.setAsset(asset);
                schedule.setType(event.getEventType());
                schedule.setDueDate(futureDate.toDate());
                schedule.setTenant(asset.getTenant());
                schedule.setRecurringEvent(event);
                schedule.setOwner(asset.getOwner());
                schedule.setEventResult(EventResult.VOID);
                eventScheduleService.createSchedule(schedule);
            }
        }
    }

    /*package protected for test reasons*/
    boolean checkIfScheduleExists(Asset asset, RecurringAssetTypeEvent event, LocalDateTime futureDate) {
        QueryBuilder<ThingEvent> query = new QueryBuilder<ThingEvent>(ThingEvent.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("asset", asset));
        query.addWhere(WhereClauseFactory.create("recurringEvent", event));

        //A simple equals does not work due to comparison problems comparing Timestamps and Date see java.sql.Timestamp
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "dueDate", futureDate.minusMillis(1).toDate()));
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "to", "dueDate", futureDate.toDate()));

        return persistenceService.count(query) > 0;
    }

    /*package protected for testing reasons*/
    List<Asset> getAssetsByAssetType(RecurringAssetTypeEvent event) {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("type", event.getAssetType()));
        if(event.getOwner() != null) {
            if(event.getOwnerAndDown())
                query.applyFilter(new OwnerAndDownFilter(event.getOwner() ));
            else {
                query.addWhere(WhereClauseFactory.create("owner", event.getOwner()));
            }
        }
        return persistenceService.findAll(query);
    }

    /******* PlaceEvents ********/

    /*package protected for test reasons*/
    boolean checkIfScheduleExists(BaseOrg place, RecurringPlaceEvent event, LocalDateTime futureDate) {
        QueryBuilder<PlaceEvent> query = new QueryBuilder<PlaceEvent>(PlaceEvent.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("place", place));
        query.addWhere(WhereClauseFactory.create("recurringEvent", event));

        //A simple equals does not work due to comparison problems comparing Timestamps and Date see java.sql.Timestamp
        // .: we use a range.
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "dueDate", futureDate.minusMillis(1).toDate()));
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "to", "dueDate", futureDate.toDate()));

        return persistenceService.count(query) > 0;
    }

    public void scheduleAPlaceEventFor(RecurringPlaceEvent recurringEvent, LocalDateTime futureDate) {
        BaseOrg place = recurringEvent.getPlace();

        if(!checkIfScheduleExists(place, recurringEvent, futureDate)) {
            PlaceEvent schedule = new PlaceEvent();
            schedule.setPlace(place);
            schedule.setType(recurringEvent.getEventType());
            schedule.setDueDate(futureDate.toDate());
            schedule.setTenant(place.getTenant());
            schedule.setRecurringEvent(recurringEvent);
            schedule.setEventResult(EventResult.VOID);
            placeEventScheduleService.createSchedule(schedule);
        }
    }

    public List<RecurringPlaceEvent> getAllRecurringPlaceEvents() {
        QueryBuilder<RecurringPlaceEvent> query = new QueryBuilder<RecurringPlaceEvent>(RecurringPlaceEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("tenant.disabled", false);
        return persistenceService.findAll(query);
    }

    public List<RecurringPlaceEvent> getRecurringPlaceEvents(BaseOrg org) {
        QueryBuilder<RecurringPlaceEvent> query = createTenantSecurityBuilder(RecurringPlaceEvent.class);
        query.addSimpleWhere("owner", org);
        return persistenceService.findAll(query);
    }

    public Long countRecurringPlaceEvents(BaseOrg org) {
        QueryBuilder<RecurringPlaceEvent> query = createTenantSecurityBuilder(RecurringPlaceEvent.class);
        query.addSimpleWhere("owner", org);
        return persistenceService.count(query);
    }

    public void purgeRecurringEvent(RecurringPlaceEvent recurringEvent) {
        removeScheduledEvents(recurringEvent);
        removeRecurringEvent(recurringEvent);
    }

    private void removeScheduledEvents(RecurringPlaceEvent recurringEvent){
        QueryBuilder<PlaceEvent> query = createTenantSecurityBuilder(PlaceEvent.class);

        query.addSimpleWhere("workflowState", WorkflowState.OPEN);
        query.addSimpleWhere("recurringEvent", recurringEvent);

        List<PlaceEvent> events = persistenceService.findAll(query);
        for (PlaceEvent event:events) {
            logger.debug("removing scheduled event for place: " + event.getPlace().getDisplayName() + " on " + event.getDueDate());
            persistenceService.delete(event);
        }
    }

    public void removeRecurringEvent(RecurringPlaceEvent recurringEvent) {
        recurringEvent.archiveEntity();
        persistenceService.update(recurringEvent);
    }

    public void addRecurringEvent(BaseOrg org, final RecurringPlaceEvent recurringEvent) {
        persistenceService.save(recurringEvent.getRecurrence());
        persistenceService.save(recurringEvent);

        AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                scheduleInitialEvents(recurringEvent);
                return null;
            }
        });
        asyncService.run(task);
    }

    private void scheduleInitialEvents(RecurringPlaceEvent recurringPlaceEvent) {
        for (LocalDateTime dateTime: getBoundedScheduledTimesIterator(recurringPlaceEvent.getRecurrence())) {
            scheduleAPlaceEventFor(recurringPlaceEvent, dateTime);
        }
    }


    /******* Lockouts / Procedure Audits ********/

    public void scheduleALotoEventFor(RecurringLotoEvent recurringEvent, LocalDateTime futureDate) {

        if(!checkIfLotoScheduleExists(recurringEvent, futureDate)) {
            Procedure procedure = new Procedure();
            procedure.setType(recurringEvent.getProcedureDefinition());
            procedure.setAsset(recurringEvent.getProcedureDefinition().getAsset());
            procedure.setTenant(recurringEvent.getProcedureDefinition().getTenant());
            procedure.setWorkflowState(ProcedureWorkflowState.OPEN);
            procedure.setRecurringEvent(recurringEvent);
            procedure.setDueDate(futureDate.toDate());
            procedure.setAssignedUserOrGroup(recurringEvent.getAssignedUserOrGroup());
            procedureService.createSchedule(procedure);
        }
    }

    private boolean checkIfLotoScheduleExists(RecurringLotoEvent recurringEvent, LocalDateTime futureDate) {
        QueryBuilder<Procedure> query = new QueryBuilder<Procedure>(Procedure.class, new OpenSecurityFilter());
        query.addSimpleWhere("type", recurringEvent.getProcedureDefinition());
        query.addSimpleWhere("recurringEvent", recurringEvent);

        //A simple equals does not work due to comparison problems comparing Timestamps and Date see java.sql.Timestamp
        // .: we use a range.
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "dueDate", futureDate.minusMillis(1).toDate()));
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "to", "dueDate", futureDate.toDate()));

        return persistenceService.count(query) > 0;
    }

    public void scheduleAnAuditEventFor(RecurringLotoEvent recurringEvent, LocalDateTime futureDate) {

        if(!checkIfAuditScheduleExists(recurringEvent, futureDate)) {
            ProcedureAuditEvent auditEvent = new ProcedureAuditEvent();
            auditEvent.setTarget(recurringEvent.getProcedureDefinition());
            auditEvent.setType(recurringEvent.getAuditEventType());
            auditEvent.setRecurringEvent(recurringEvent);
            auditEvent.setTenant(recurringEvent.getProcedureDefinition().getTenant());
            auditEvent.setWorkflowState(WorkflowState.OPEN);
            auditEvent.setDueDate(futureDate.toDate());
            auditEvent.setAssignedUserOrGroup(recurringEvent.getAssignedUserOrGroup());
            auditEvent.setEventResult(EventResult.VOID);
            procedureAuditScheduleService.createSchedule(auditEvent);
        }
    }


    private boolean checkIfAuditScheduleExists(RecurringLotoEvent recurringEvent, LocalDateTime futureDate) {
        QueryBuilder<ProcedureAuditEvent> query = new QueryBuilder<ProcedureAuditEvent>(ProcedureAuditEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("recurringEvent.auditEventType", recurringEvent.getAuditEventType());
        query.addSimpleWhere("procedureDefinition", recurringEvent.getProcedureDefinition());
        query.addSimpleWhere("recurringEvent", recurringEvent);

        //A simple equals does not work due to comparison problems comparing Timestamps and Date see java.sql.Timestamp
        // .: we use a range.
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "from", "dueDate", futureDate.minusMillis(1).toDate()));
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "to", "dueDate", futureDate.toDate()));

        return persistenceService.findAll(query).size() > 0;
    }

    public List<RecurringLotoEvent> getAllRecurringLotoEvents() {
        QueryBuilder<RecurringLotoEvent> query = new QueryBuilder<RecurringLotoEvent>(RecurringLotoEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("tenant.disabled", false);
        return persistenceService.findAll(query);
    }

    public List<RecurringLotoEvent> getRecurringLotoEvents(Asset asset) {
        return persistenceService.findAll(getRecurringLotoEventQuery(asset));
    }

    public Long countRecurringLotoEvents(Asset asset) {
         return persistenceService.count(getRecurringLotoEventQuery(asset));
    }

    private QueryBuilder<RecurringLotoEvent> getRecurringLotoEventQuery(Asset asset) {
        QueryBuilder<RecurringLotoEvent> query = createTenantSecurityBuilder(RecurringLotoEvent.class);
        query.addSimpleWhere("procedureDefinition.asset", asset);
        return query;
    }

    public void purgeRecurringEvent(RecurringLotoEvent recurringEvent) {
        if(recurringEvent.isRecurringLockout()) {
            removeScheduledLotos(recurringEvent);
        } else {
            removeScheduledAudits(recurringEvent);
        }
        removeRecurringEvent(recurringEvent);
    }

    private void removeScheduledLotos(RecurringLotoEvent recurringEvent){
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);

        query.addSimpleWhere("workflowState", ProcedureWorkflowState.OPEN);
        query.addSimpleWhere("recurringEvent", recurringEvent);

        List<Procedure> events = persistenceService.findAll(query);
        for (Procedure event:events) {
            logger.debug("removing scheduled LOTO for asset: " + event.getAsset().getIdentifier() + " on " + event.getDueDate());
            persistenceService.delete(event);
        }
    }

    private void removeScheduledAudits(RecurringLotoEvent recurringEvent){
        QueryBuilder<ProcedureAuditEvent> query = createTenantSecurityBuilder(ProcedureAuditEvent.class);
        query.addSimpleWhere("workflowState", WorkflowState.OPEN);
        query.addSimpleWhere("recurringEvent", recurringEvent);

        List<ProcedureAuditEvent> events = persistenceService.findAll(query);
        for (ProcedureAuditEvent event:events) {
            logger.debug("removing scheduled Procedure Audits for Procedure Definition: " + event.getProcedureDefinition().getProcedureCode() + " on " + event.getDueDate());
            persistenceService.delete(event);
        }
    }

    public void removeRecurringEvent(RecurringLotoEvent recurringEvent) {
        recurringEvent.archiveEntity();
        persistenceService.update(recurringEvent);
    }

    public void addRecurringEvent(final RecurringLotoEvent recurringEvent) {
        persistenceService.save(recurringEvent.getRecurrence());
        persistenceService.save(recurringEvent);

        AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                scheduleInitialEvents(recurringEvent);
                return null;
            }
        });
        asyncService.run(task);
    }

    private void scheduleInitialEvents(RecurringLotoEvent recurringEvent) {
        if(recurringEvent.isRecurringLockout()) {
            for (LocalDateTime dateTime: getBoundedScheduledTimesIterator(recurringEvent.getRecurrence())) {
                scheduleALotoEventFor(recurringEvent, dateTime);
            }
        } else {
            LocalDateTime dateTime = getNextProcedureAuditDate(recurringEvent);
            scheduleAnAuditEventFor(recurringEvent, dateTime);
        }
    }

    public LocalDateTime getNextProcedureAuditDate(RecurringLotoEvent recurringEvent) {
        return new ScheduleTimeIterator(recurringEvent.getRecurrence()).next();
    }

    class BoundedScheduleTimeIterator implements Iterable<LocalDateTime>, Iterator<LocalDateTime> {

        private ScheduleTimeIterator iterable;
        private Recurrence recurrence;
        private LocalDate start = LocalDate.now();
        private LocalDateTime peek;

        public BoundedScheduleTimeIterator(Recurrence recurrence) {
            this.recurrence = recurrence;
            this.iterable = new ScheduleTimeIterator(recurrence);
        }

        @Override public Iterator<LocalDateTime> iterator() {
            return this;
        }

        @Override public boolean hasNext() {
            peek = iterable.next();
            return !hasIteratorEnded();
        }

        @Override public LocalDateTime next() {
            return peek;
        }

        @Override public void remove() {
            iterable.remove();
        }

        private boolean hasIteratorEnded() {
            LocalDate localDate = peek.toLocalDate();
            switch (recurrence.getType()) {
                case DAILY:
                    return Days.daysBetween(start,localDate).getDays() >= DAILY_EVENT_COUNT;
                case WEEKLY_MONDAY:
                case WEEKLY_TUESDAY:
                case WEEKLY_WEDNESDAY:
                case WEEKLY_THURSDAY:
                case WEEKLY_FRIDAY:
                case WEEKLY_SATURDAY:
                case WEEKLY_SUNDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= WEEKLY_EVENT_COUNT;
                case WEEKDAYS:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= WEEKLY_WEEKDAY_EVENT_COUNT;
                case MONTHLY_1ST:
                case MONTHLY_15TH:
                case MONTHLY_LAST:
                    return Months.monthsBetween(start,localDate).getMonths() >= MONTHLY_EVENT_COUNT;
                case BIWEEKLY_MONDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case BIWEEKLY_TUESDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case BIWEEKLY_WEDNESDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case BIWEEKLY_THURSDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case BIWEEKLY_FRIDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case BIWEEKLY_SATURDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case BIWEEKLY_SUNDAY:
                    return Weeks.weeksBetween(start, localDate).getWeeks() >= BIWEEKLY_EVENT_COUNT;
                case ANNUALLY:
                    return Months.monthsBetween(start,localDate).getMonths() >= ANNUAL_MONTH_THRESHOLD;
                default:
                    throw new UnsupportedOperationException("type " + recurrence.getType() + " not supported.");
            }
        }

    }

    public Iterable<LocalDateTime> getScheduledTimesIterator(Recurrence recurrence) {
        return new ScheduleTimeIterator(recurrence);
    }

    public Iterable<LocalDateTime> getBoundedScheduledTimesIterator(Recurrence recurrence) {
        return new BoundedScheduleTimeIterator(recurrence);
    }

    class ScheduleTimeIterator implements Iterable<LocalDateTime>, Iterator<LocalDateTime> {

        private Recurrence recurrence;
        private LocalDate fromDate;
        private Iterator<LocalDateTime> times = null;

        public ScheduleTimeIterator(Recurrence recurrence) {
            this.recurrence = recurrence;
            fromDate = initializeStartDate();
        }

        private LocalDate initializeStartDate() {
            return recurrence.getType().previous();
        }

        @Override public boolean hasNext() {
            return true;
        }

        @Override public void remove() {
            throw new UnsupportedOperationException("can't remove scheduled times");
        }

        @Override
        public LocalDateTime next() {
            if (times!=null && times.hasNext()) {
                while (times.hasNext()) {
                    LocalDateTime next = times.next();
                    if (!next.isBefore(LocalDateTime.now())) {
                        return next;
                    }
                }
            }
            fromDate = recurrence.getType().getNext(fromDate);
            times = getTimesForDay(fromDate).iterator();
            return next();
        }

        public List<LocalDateTime> getTimesForDay(LocalDate nextDate) {
            LocalDateTime base = new LocalDateTime(nextDate.toDate());
            List<LocalDateTime> result = Lists.newArrayList();
            // only add times after today!
            for (RecurrenceTime time:recurrence.getTimes()) {
                result.add(base.plus(time.toPeriod()));
            }
            return result;
        }

        @Override
        public Iterator<LocalDateTime> iterator() {
            return this;
        }
    }

}
