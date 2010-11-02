package com.n4systems.webservice.server.handlers;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.JobBuilder.*;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.inspection.EventByMobileGuidLoader;
import com.n4systems.model.inspectionschedule.EventScheduleSaver;
import org.junit.Test;

import com.n4systems.model.Project;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.server.InspectionNotFoundException;

public class CompletedScheduleCreatorTest {

	private final Event event = anEvent().build();
	private final Project job = aJob().build();
	
	@Test
	public void creates_schedule_with_loaded_inspection() {
		TestableScheduleSaver saver = new TestableScheduleSaver();
		
		CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, testableJobLoader());
		sut.create("SOME GUID", new Date(), 1L);
		
		assertEquals(event, saver.getSavedSchedule().getEvent());
	}
	
	@Test
	public void created_schedule_should_be_marked_complete() {
		TestableScheduleSaver saver = new TestableScheduleSaver();
		
		CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, testableJobLoader());
		sut.create("SOME GUID", new Date(), 1L);
		
		assertEquals(ScheduleStatus.COMPLETED, saver.getSavedSchedule().getStatus());		
	}
	
	@Test
	public void created_schedule_next_date_set_to_date_passed_in() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2010, 2, 18, 0, 0, 0);
		PlainDate someDate = new PlainDate(calendar.getTime());
		
		TestableScheduleSaver saver = new TestableScheduleSaver();
		
		CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, testableJobLoader());
		sut.create("SOME GUID", someDate, 1L);
		
		assertEquals(someDate, saver.getSavedSchedule().getNextDate());				
	}
	
	@Test
	public void schedule_should_be_assigned_to_job_passed_in() {
		TestableScheduleSaver saver = new TestableScheduleSaver();
		
		CompletedScheduleCreator sut = new CompletedScheduleCreator(testableInspectionLoader(), saver, testableJobLoader());
		sut.create("SOME GUID", new Date(), 1L);
		
		assertEquals(job, saver.getSavedSchedule().getProject());				
	}
	
	@Test(expected=InspectionNotFoundException.class)
	public void should_throw_exception_when_no_exception_found() {
		TestableScheduleSaver saver = new TestableScheduleSaver();
		
		CompletedScheduleCreator sut = new CompletedScheduleCreator(testableNoInspectionLoader(), saver, testableJobLoader());
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
	private EventSchedule savedSchedule;
	
	@Override
	public void save(EventSchedule schedule) {
		savedSchedule = schedule;
	}
	
	public EventSchedule getSavedSchedule() {
		return savedSchedule;
	}
}
