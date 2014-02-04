package com.n4systems.fieldid.service.schedule;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.model.*;
import com.n4systems.model.builders.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.testutils.QueryTypeMatcher;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.joda.time.DateTimeConstants;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/** Debugging note :
 * because this code is testing an iterator for the most part, be aware of putting stuff like "foo.next()" in your debugger watches window.
 * it will screw up the order of the iterator and all subsequent assertions will fail.
 */
public class RecurringScheduleServiceTest extends FieldIdServiceTest {

    @TestTarget RecurringScheduleService recurringScheduleService;
    @TestMock EventScheduleService eventScheduleService;
    @TestMock PersistenceService persistenceService;

    private int minute;
    private int hour;
    private List<Asset> expectedAssetsByAssetType;

    @Before
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected LocalDateTime getTestTime() {
        return new LocalDateTime(april2_2011.toDate());
    }

    @Test
    public void test_daily() {
        Recurrence recurrence = new Recurrence(RecurrenceType.DAILY).withTime(new LocalTime(hour=5,minute=39));

        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        LocalDateTime expected = getTestTime().withHourOfDay(hour).withMinuteOfHour(minute);
        for (int i = 0; i < 1000; i++) {
            assertEquals(expected,times.next());
            expected = expected.plusDays(1);
        }
    }

    @Test
    public void test_monthly() {

        assertMonthly(RecurrenceType.MONTHLY_1ST);

        assertMonthly(RecurrenceType.MONTHLY_15TH);

        assertMonthly(RecurrenceType.MONTHLY_LAST);
    }

    @Test
    public void test_weekly() {

        List<Integer> days = Lists.newArrayList(
                DateTimeConstants.MONDAY,
                DateTimeConstants.TUESDAY,
                DateTimeConstants.WEDNESDAY,
                DateTimeConstants.THURSDAY,
                DateTimeConstants.FRIDAY,
                DateTimeConstants.SATURDAY,
                DateTimeConstants.SUNDAY);

        List<RecurrenceType> types = Lists.newArrayList(
                RecurrenceType.WEEKLY_MONDAY,
                RecurrenceType.WEEKLY_TUESDAY,
                RecurrenceType.WEEKLY_WEDNESDAY,
                RecurrenceType.WEEKLY_THURSDAY,
                RecurrenceType.WEEKLY_FRIDAY,
                RecurrenceType.WEEKLY_SATURDAY,
                RecurrenceType.WEEKLY_SUNDAY
        );

        for (int i=0; i<days.size(); i++) {
            assertWeekly(days.get(i), types.get(i));
        }

    }

    @Test
    public void test_annually() {
        LocalDateTime when = new LocalDateTime().withYear(2012).withMonthOfYear(DateTimeConstants.APRIL).withDayOfMonth(1).withHourOfDay(12).withMinuteOfHour(43);

        Recurrence recurrence = new Recurrence(RecurrenceType.ANNUALLY).withDayAndTime(when.toDate());
        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        LocalDateTime expected = when;
        for (int i = 0; i < 10; i++) {
            LocalDateTime actual = times.next();
            assertEquals(expected, actual);
            expected = expected.plusYears(1);
        }
    }

    @Test
    public void test_weekdays() {
        Recurrence recurrence = new Recurrence(RecurrenceType.WEEKDAYS).withTime(new LocalTime(hour = 7, minute = 27));
        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        LocalDateTime expected = new LocalDateTime(getTestTime().plusDays(1)).withHourOfDay(hour).withMinuteOfHour(minute);
        if (getTestTime().getDayOfWeek()>=DateTimeConstants.FRIDAY) {
            expected = expected.plusDays(DateTimeConstants.SUNDAY-getTestTime().getDayOfWeek());
        }
        for (int i = 0; i < 100; i++) {
            assertWeekdays(expected, times);
            expected = expected.plusWeeks(1);
        }
    }

    @Test(expected =  IllegalFieldValueException.class)
    public void test_bad_minutes() {
        new Recurrence(RecurrenceType.MONTHLY_15TH).withTime(new LocalTime(hour=0,minute=87));
    }

    @Test(expected =  IllegalFieldValueException.class)
    public void test_bad_hours() {
        new Recurrence(RecurrenceType.DAILY).withTime(new LocalTime(hour=27,minute=0));
    }

    @Test
    public void test_multipleTimes() {
        List<LocalTime> expected = Lists.newArrayList();

        Recurrence recurrence = new Recurrence(RecurrenceType.DAILY);
        for (RecurrenceTimeOfDay timeOfDay:RecurrenceTimeOfDay.values()) {
            recurrence.withTime(timeOfDay);
            expected.add(timeOfDay.asLocalTime());
        }

        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        assertMultipleTimes(expected, times);
    }

    @Test
    public void test_multipleDaysAnnual() {
        Recurrence recurrence = new Recurrence(RecurrenceType.ANNUALLY);

        recurrence.withDayAndTime(DateTimeConstants.FEBRUARY, 29, 10, 15);  // note leap year implications!
        recurrence.withDayAndTime(DateTimeConstants.MARCH, 1, 18, 9);
        recurrence.withDayAndTime(DateTimeConstants.AUGUST, 31, 7, 1);
        recurrence.withDayAndTime(DateTimeConstants.DECEMBER, 15, 22, 59);

        List<LocalDateTime> expected = Lists.newArrayList(
                new LocalDateTime().withDate(2011,8,31).withTime(7, 1, 0, 0),
                new LocalDateTime().withDate(2011,12,15).withTime(22,59,0,0),

                new LocalDateTime().withDate(2012,2,29).withTime(10, 15, 0, 0),
                new LocalDateTime().withDate(2012,3,1).withTime(18, 9, 0, 0),
                new LocalDateTime().withDate(2012,8,31).withTime(7, 1, 0, 0),
                new LocalDateTime().withDate(2012,12,15).withTime(22,59,0,0),

                new LocalDateTime().withDate(2013,3,1).withTime(10, 15, 0, 0),
                new LocalDateTime().withDate(2013,3,1).withTime(18, 9, 0, 0)

                );

        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        assertMultipleDays(expected, times);
    }

    @Test
    public void test_leapYearAnnual() {
        Recurrence recurrence = new Recurrence(RecurrenceType.ANNUALLY).withDayAndTime(DateTimeConstants.FEBRUARY, 29, 0, 0);

        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        LocalDateTime expectedFeb = new LocalDateTime().withDate(2012, 2, 29);
        LocalDateTime expectedMarch = new LocalDateTime().withDate(2012, 3, 1);

        for (int year = getTestTime().getYear(); year<2050; year++) {
            LocalDateTime actual = times.next();
            LocalDateTime expect = (isLeapYear(actual.getYear()) ? expectedFeb : expectedMarch).withYear(actual.getYear());
            assertEquals(expect,actual);
        }
    }

    @Test
    public void test_scheduleAnEventFor() {
        Tenant tenant = TenantBuilder.aTenant().build();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().build();
        Asset asset = AssetBuilder.anAsset().forTenant(tenant).withOwner(owner).build();
        ThingEventType eventType = EventTypeBuilder.anEventType().withId(55L).build();
        Recurrence recurrence = RecurrenceBuilder.aRecurrence().withType(RecurrenceType.DAILY).withTimes(RecurrenceTimeOfDay.EIGHT_AM).build();
        LocalDateTime dueDate = new LocalDateTime().withDate(2012, 5, 13);
        RecurringAssetTypeEvent recurringAssetTypeEvent = RecurringAssetTypeEventBuilder.anAssetTypeEvent().
                withAssetType(asset.getType()).
                withEventType(eventType).
                withTenant(asset.getTenant()).
                withOwner(asset.getOwner()).
                withRecurrence(recurrence).build();

        expect(persistenceService.findAll(QueryTypeMatcher.eq(Asset.class))).andReturn(Lists.newArrayList(asset));
        expect(persistenceService.count(QueryTypeMatcher.eq(ThingEvent.class))).andReturn(0L);
        replay(persistenceService);

        expect(eventScheduleService.createSchedule(EventMatcher.eq(asset, dueDate.toDate(), owner, recurringAssetTypeEvent, tenant, eventType))).andReturn(33L);
        replay(eventScheduleService);

        recurringScheduleService.scheduleAnEventFor(recurringAssetTypeEvent, dueDate);

        verifyTestMocks();
    }

    @Test
    public void test_scheduleAnEventFor_already_exist() {
        Tenant tenant = TenantBuilder.aTenant().build();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().build();
        Asset asset = AssetBuilder.anAsset().forTenant(tenant).withOwner(owner).build();
        ThingEventType eventType = EventTypeBuilder.anEventType().withId(55L).build();
        Recurrence recurrence = RecurrenceBuilder.aRecurrence().withType(RecurrenceType.DAILY).withTimes(RecurrenceTimeOfDay.EIGHT_AM).build();
        LocalDateTime dueDate = new LocalDateTime().withDate(2012, 5, 13);
        RecurringAssetTypeEvent recurringAssetTypeEvent = RecurringAssetTypeEventBuilder.anAssetTypeEvent().
                withAssetType(asset.getType()).
                withEventType(eventType).
                withTenant(asset.getTenant()).
                withOwner(asset.getOwner()).
                withRecurrence(recurrence).build();

        expect(persistenceService.findAll(QueryTypeMatcher.eq(Asset.class))).andReturn(Lists.newArrayList(asset));
        expect(persistenceService.count(QueryTypeMatcher.eq(ThingEvent.class))).andReturn(1L);  // means event already exists
        replay(persistenceService);

        // these will NOT be called because event already exists.

//        expect(eventScheduleService.createSchedule(EventMatcher.eq(asset, dueDate.toDate(), owner, recurringAssetTypeEvent, tenant, eventType))).andReturn(33L);
//        replay(eventScheduleService);

        recurringScheduleService.scheduleAnEventFor(recurringAssetTypeEvent, dueDate);

        verifyTestMocks();
    }

    @Test
    public void test_boundaries() {
        Recurrence recurrence = new Recurrence(RecurrenceType.DAILY).withTime(new LocalTime(hour = 7, minute = 27));
        Iterable<LocalDateTime> times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times, RecurringScheduleService.DAILY_EVENT_COUNT);

        recurrence = new Recurrence(RecurrenceType.DAILY).withTimes(Lists.newArrayList(RecurrenceTimeOfDay.EIGHT_AM, RecurrenceTimeOfDay.FOUR_PM));
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times, RecurringScheduleService.DAILY_EVENT_COUNT *2);

        recurrence = new Recurrence(RecurrenceType.MONTHLY_15TH).withTime(new LocalTime(hour = 7, minute = 27));
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times, RecurringScheduleService.MONTHLY_EVENT_COUNT);

        recurrence = new Recurrence(RecurrenceType.ANNUALLY).withDayAndTime(getTestTime().minusMonths(1).toDate());
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times,RecurringScheduleService.ANNUAL_EVENT_COUNT-1);

        recurrence = new Recurrence(RecurrenceType.ANNUALLY).withDayAndTime(getTestTime().plusMonths(7).toDate());
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times,RecurringScheduleService.ANNUAL_EVENT_COUNT-1);

        recurrence = new Recurrence(RecurrenceType.ANNUALLY).withDayAndTime(getTestTime().plusMonths(1).toDate());
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times,RecurringScheduleService.ANNUAL_EVENT_COUNT);

        recurrence = new Recurrence(RecurrenceType.ANNUALLY).withDayAndTime(getTestTime().plusMonths(RecurringScheduleService.ANNUAL_MONTH_THRESHOLD+1).toDate());
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times,RecurringScheduleService.ANNUAL_EVENT_COUNT-1);  //

        recurrence = new Recurrence(RecurrenceType.WEEKDAYS).withTime(new LocalTime(hour = 7, minute = 27));
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times, RecurringScheduleService.WEEKDAY_EVENT_COUNT);

        recurrence = new Recurrence(RecurrenceType.WEEKLY_MONDAY).withTime(new LocalTime(hour = 7, minute = 27));
        times = recurringScheduleService.getBoundedScheduledTimesIterator(recurrence);
        assertBoundedIterator(times, RecurringScheduleService.WEEKLY_EVENT_COUNT);
    }

    private boolean isLeapYear(int year) {
        return (year-2000)%4==0;
    }

    private void assertWeekdays(LocalDateTime expected, Iterator<LocalDateTime> times) {
        // test mon/tues/wed/thurs/fri.
        assertEquals(expected, times.next());

        expected = expected.plusDays(1);
        assertEquals(expected, times.next());

        expected = expected.plusDays(1);
        assertEquals(expected, times.next());

        expected = expected.plusDays(1);
        assertEquals(expected, times.next());

        expected = expected.plusDays(1);
        assertEquals(expected, times.next());

    }

    private void assertWeekly(int day, RecurrenceType type) {
        Recurrence recurrence = new Recurrence(type).withTime(new LocalTime(hour=7,minute=27));

        int testTimeDay = getTestTime().getDayOfWeek();
        int dayDelta = day - testTimeDay;
        if (dayDelta<0) {
            dayDelta = 7+dayDelta;
        }

        List<LocalDateTime> expected = Lists.newArrayList();
        LocalDateTime localDateTime = new LocalDateTime(getTestTime().toDate()).plusDays(dayDelta).withHourOfDay(hour).withMinuteOfHour(minute);
        for (int i = 0; i<100; i++) {
            expected.add(localDateTime);
            localDateTime = localDateTime.plusWeeks(1);
        }

        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        for (LocalDateTime expect:expected) {
            LocalDateTime actual = times.next();
            assertEquals(expect, actual);
        }
    }

    private LocalDateTime getExpectedWithDayOfMonth(RecurrenceType type, LocalDateTime expect) {
        if (type.equals(RecurrenceType.MONTHLY_LAST)) {
            return expect.withDayOfMonth(expect.dayOfMonth().getMaximumValue());
        } else if (type.equals(RecurrenceType.MONTHLY_1ST)) {
            return expect.withDayOfMonth(1);
        } else if (type.equals(RecurrenceType.MONTHLY_15TH)) {
            return expect.withDayOfMonth(15);
        }
        throw new IllegalStateException("huh? this is for MONTHLY_?? recurrence types only");
    }

    private void assertMonthly(RecurrenceType type) {
        Recurrence recurrence = new Recurrence(type).withTime(new LocalTime(hour = 15, minute = 53));

        List<LocalDateTime> expected = Lists.newArrayList();
        LocalDateTime time_midnight = new LocalDateTime(getTestTime().toDate());
        for (int i = 0; i<50; i++) {
            // monthly 1st delete 0 condition because it's before "now".
            LocalDateTime expect = getExpectedWithDayOfMonth(type, new LocalDateTime(time_midnight.withHourOfDay(hour).plusMonths(i).withMinuteOfHour(minute)));
            if (!expect.isBefore(getTestTime())) {
                expected.add(expect);
            }
        }

        Iterator<LocalDateTime> times = recurringScheduleService.getScheduledTimesIterator(recurrence).iterator();

        for (LocalDateTime expect:expected) {
            assertEquals(expect, times.next());
        }
    }

    private void assertMultipleDays(List<LocalDateTime> expected, Iterator<LocalDateTime> times) {
        Set<LocalDateTime> results = Sets.newHashSet();
        for (int i=0;i<expected.size();i++) {
            results.add(times.next());
        }
        for (LocalDateTime expect:expected) {
            assertTrue("expected time " + expect, results.contains(expect));
        }
    }

    private void assertMultipleTimes(List<LocalTime> expected, Iterator<LocalDateTime> times) {
        Set<LocalDateTime> results = Sets.newHashSet();
        for (int i=0;i<expected.size();i++) {
            results.add(times.next());
        }
        for (LocalTime time:expected) {
            LocalDateTime expect = getTestTime().withHourOfDay(time.getHourOfDay()).withMinuteOfHour(time.getMinuteOfHour());
            assertTrue(results.contains(expect));
        }
    }

    private void assertBoundedIterator(Iterable<LocalDateTime> times, int expectedSize) {
        List<LocalDateTime> actual = Lists.newArrayList();
        for (LocalDateTime time:times) {
            actual.add(time);
        }
        assertEquals(expectedSize, actual.size());
    }


    static protected class EventMatcher implements IArgumentMatcher {

        private Asset asset;
        private Date dueDate;
        private Tenant tenant;
        private RecurringAssetTypeEvent recurringAssetTypeEvent;
        private BaseOrg owner;
        private EventType eventType;

        public EventMatcher(Asset asset, Date dueDate, BaseOrg owner, RecurringAssetTypeEvent recurringAssetTypeEvent, Tenant tenant, EventType type) {
            this.asset = asset;
            this.dueDate = dueDate;
            this.owner = owner;
            this.recurringAssetTypeEvent = recurringAssetTypeEvent;
            this.tenant = tenant;
            this.eventType = type;
        }

        public static final ThingEvent eq(Asset asset, Date dueDate, BaseOrg owner, RecurringAssetTypeEvent recurringAssetTypeEvent, Tenant tenant, EventType eventType) {
            EasyMock.reportMatcher(new EventMatcher(asset, dueDate, owner, recurringAssetTypeEvent, tenant, eventType));
            return null;
        }

        @Override
        public String toString() {
            return "Event {" +
                    "asset=" + asset +
                    ", dueDate=" + dueDate +
                    ", tenant=" + tenant +
                    ", recurringAssetTypeEvent=" + recurringAssetTypeEvent +
                    ", owner=" + owner +
                    ", eventType=" + eventType +
                    '}';
        }

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append(toString());
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof Event) ) {
                return false;
            }
            ThingEvent actual = (ThingEvent) argument;
            return asset.equals(actual.getAsset()) &&
                    dueDate.equals(actual.getDueDate()) &&
                    tenant.equals(actual.getTenant()) &&
                    recurringAssetTypeEvent.equals(actual.getRecurringEvent()) &&
                    owner.equals(actual.getOwner()) &&
                    eventType.equals(actual.getEventType());
        }

    }


}
