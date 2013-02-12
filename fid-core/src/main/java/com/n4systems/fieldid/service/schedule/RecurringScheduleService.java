package com.n4systems.fieldid.service.schedule;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Transactional
public class RecurringScheduleService extends FieldIdPersistenceService {

    public static final int DAILY_EVENT_COUNT = 14;
    public static final int WEEKLY_EVENT_COUNT = 6;
    public static final int MONTHLY_EVENT_COUNT = 2;
    public static final int ANNUAL_EVENT_COUNT = 2;
    public static final int ANNUAL_MONTH_THRESHOLD = 18;
    public static final int WEEKDAY_EVENT_COUNT = 10;
    public static final int WEEKLY_WEEKDAY_EVENT_COUNT = 2;

    @Autowired private EventScheduleService eventScheduleService;

    public List<RecurringAssetTypeEvent> getRecurringAssetTypeEvents() {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new OpenSecurityFilter());
        return persistenceService.findAll(query);
    }

    public List<RecurringAssetTypeEvent> getRecurringAssetTypeEvents(AssetType assetType) {
        QueryBuilder<RecurringAssetTypeEvent> query = createTenantSecurityBuilder(RecurringAssetTypeEvent.class);
        query.addSimpleWhere("assetType", assetType);
        return persistenceService.findAll(query);
    }

    public List<Event> getRecurringEventsForAsset(Asset asset, RecurringAssetTypeEvent event){
        QueryBuilder<Event> query = createTenantSecurityBuilder(Event.class);
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
                Event schedule = new Event();
                schedule.setAsset(asset);
                schedule.setType(event.getEventType());
                schedule.setDueDate(futureDate.toDate());
                schedule.setTenant(asset.getTenant());
                schedule.setRecurringEvent(event);
                schedule.setOwner(asset.getOwner());
                eventScheduleService.createSchedule(schedule);
            }
        }
    }

    /*package protected for test reasons*/
    boolean checkIfScheduleExists(Asset asset, RecurringAssetTypeEvent event, LocalDateTime futureDate) {
        QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
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
            query.addWhere(WhereClauseFactory.create("owner", event.getOwner()));
        }
        return persistenceService.findAll(query);
    }

    public Iterable<LocalDateTime> getScheduledTimesIterator(Recurrence recurrence) {
        return new ScheduleTimeIterator(recurrence);
    }

    public Iterable<LocalDateTime> getBoundedScheduledTimesIterator(Recurrence recurrence) {
        return new BoundedScheduleTimeIterator(recurrence);
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
                case ANNUALLY:
                    return Months.monthsBetween(start,localDate).getMonths() >= ANNUAL_MONTH_THRESHOLD;
                default:
                    throw new UnsupportedOperationException("type " + recurrence.getType() + " not supported.");
            }
        }

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
