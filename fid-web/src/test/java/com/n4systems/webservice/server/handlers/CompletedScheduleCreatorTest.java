package com.n4systems.webservice.server.handlers;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.Project;
import com.n4systems.model.event.EventByMobileGuidLoader;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.JobBuilder.aJob;
import static org.junit.Assert.assertEquals;

public class CompletedScheduleCreatorTest {

    private final Event event = anEvent().build();
    private final Project job = aJob().build();

    @Before
    public void setUp() {
        EventSchedule eventSchedule = new EventSchedule();
        eventSchedule.copyDataFrom(event);
        eventSchedule.completed(event);
    }

    @Test
    public void creates_schedule_with_loaded_inspection() {
        TestableScheduleSaver saver = new TestableScheduleSaver();
        TestableEventSaver eventSaver = new TestableEventSaver();

        CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, eventSaver, testableJobLoader());
        sut.create("SOME GUID", new Date(), 1L);

        assertEquals(event, saver.updatedSchedule.getEvent());
        assertEquals(event, eventSaver.updatedEvent);
    }

    @Test
    public void created_schedule_should_be_marked_complete() {
        TestableScheduleSaver saver = new TestableScheduleSaver();
        TestableEventSaver eventSaver = new TestableEventSaver();

        CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, eventSaver, testableJobLoader());
        sut.create("SOME GUID", new Date(), 1L);

        assertEquals(ScheduleStatus.COMPLETED, saver.updatedSchedule.getStatus());
    }

    @Test
    public void created_schedule_next_date_set_to_date_passed_in() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 2, 18, 0, 0, 0);
        PlainDate someDate = new PlainDate(calendar.getTime());

        TestableScheduleSaver saver = new TestableScheduleSaver();
        TestableEventSaver eventSaver = new TestableEventSaver();

        CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, eventSaver, testableJobLoader());
        sut.create("SOME GUID", someDate, 1L);

        assertEquals(someDate, saver.updatedSchedule.getNextDate());
        assertEquals(someDate, eventSaver.updatedEvent.getDueDate());
    }

    @Test
    public void schedule_should_be_assigned_to_job_passed_in() {
        TestableScheduleSaver scheduleSaver = new TestableScheduleSaver();
        TestableEventSaver eventSaver = new TestableEventSaver();

        CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), scheduleSaver, eventSaver, testableJobLoader());
        sut.create("SOME GUID", new Date(), 1L);

        assertEquals(job, scheduleSaver.updatedSchedule.getProject());
        assertEquals(job, eventSaver.updatedEvent.getProject());
    }

    @Test(expected=InspectionNotFoundException.class)
    public void should_throw_exception_when_no_exception_found() {
        TestableScheduleSaver scheduleSaver = new TestableScheduleSaver();
        TestableEventSaver eventSaver = new TestableEventSaver();

        CompletedScheduleCreator sut = new CompletedScheduleCreator(testableNoInspectionLoader(), scheduleSaver, eventSaver, testableJobLoader());
        sut.create("SOME GUID", new Date(), 1L);
    }

    private EventByMobileGuidLoader<Event> testableInspectionLoader() {
        EventByMobileGuidLoader<Event> loader = new EventByMobileGuidLoader<Event>(new TenantOnlySecurityFilter(1L), Event.class) {
            @Override
            public Event load() {
                return event;
            }

        };
        return loader;
    }

    private EventByMobileGuidLoader<Event> testableNoInspectionLoader() {
        EventByMobileGuidLoader<Event> loader = new EventByMobileGuidLoader<Event>(new TenantOnlySecurityFilter(1L), Event.class) {
            @Override
            public Event load() {
                return null;
            }

        };
        return loader;
    }

    private FilteredIdLoader<Project> testableJobLoader() {
        FilteredIdLoader<Project> loader = new FilteredIdLoader<Project>(new TenantOnlySecurityFilter(1L), Project.class) {
            @Override
            public Project load() {
                return job;
            }
        };
        return loader;
    }
}

class TestableScheduleSaver extends EventScheduleSaver {
    public EventSchedule savedSchedule;
    public EventSchedule updatedSchedule;

    @Override
    public void save(EventSchedule schedule) {
        savedSchedule = schedule;
    }

    @Override
    public EventSchedule update(EventSchedule schedule) {
        updatedSchedule = schedule;
        return schedule;
    }
}

class TestableEventSaver extends SimpleEventSaver {
    public Event savedEvent;
    public Event updatedEvent;
    @Override
    public void save(Event entity) {
        this.savedEvent = entity;
    }

    @Override
    public Event update(Event entity) {
        this.updatedEvent = entity;
        return entity;
    }
}
