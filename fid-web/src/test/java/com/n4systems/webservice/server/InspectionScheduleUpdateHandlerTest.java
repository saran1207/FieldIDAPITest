package com.n4systems.webservice.server;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.EventScheduleBuilder.aScheduledEventSchedule;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.n4systems.model.eventschedule.EventScheduleByGuidOrIdLoader;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;


public class InspectionScheduleUpdateHandlerTest {


	private EventScheduleSaver saver;
	private EventScheduleByGuidOrIdLoader eventScheduleByMobileGuidLoader;
	private EventSchedule eventSchedule;
	private InspectionScheduleServiceDTO inspectionScheduleServiceDTO;
	private Event event;
	
	@Before
	public void setup() {
		
		eventSchedule = aScheduledEventSchedule().build();
		
		event = anEvent().build();
	}

	@Test
	public void loading_inspection_schedule_by_shcedule_id() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());

		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void loading_inspection_schedule_by_shcedule_mobile_guid() throws Exception {
	
		buildInspectionScheduleServiceDTO(-1L, new Date());

		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(eventScheduleByMobileGuidLoader);
		
	}

	@Test
	public void if_inspection_schedule_not_found_then_skip_update() throws Exception {
	
		buildInspectionScheduleServiceDTO(-1L, new Date());
		
		createInspectionScheduleByMobileGuidLoaderMockReturningNull();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, null);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void is_inspection_schedule_next_date_updated_correctly() throws Exception {
	
		Calendar c1 = Calendar.getInstance(); 
		c1.set(2011, 11, 20, 0, 0, 0);
		
		buildInspectionScheduleServiceDTO(1L, c1.getTime());
	
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(saver);
		
		assertEquals(new PlainDate(c1.getTime()), eventSchedule.getNextDate());
		
	}
	
	
	@Test
	public void update_when_schedule_is_not_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
	
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		verify(saver);
		
	}
	
	@Test
	public void do_not_update_when_schedule_is_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		eventSchedule.completed(event);
		
		createInspectionScheduleByMobileGuidLoaderMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, null);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void remove_when_schedule_is_not_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverRemoveMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, saver);
		
		sut.removeInspectionSchedule(inspectionScheduleServiceDTO);
		verify(saver);
		
	}
	
	@Test
	public void do_not_remove_when_schedule_is_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		eventSchedule.completed(event);

		createInspectionScheduleByMobileGuidLoaderMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(eventScheduleByMobileGuidLoader, null);
		
		sut.removeInspectionSchedule(inspectionScheduleServiceDTO);
		
	}

	private void createInspectionScheduleByMobileGuidLoaderMock() {
		eventScheduleByMobileGuidLoader = createMock(EventScheduleByGuidOrIdLoader.class);
		expect(eventScheduleByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid())).andReturn(eventScheduleByMobileGuidLoader);
		expect(eventScheduleByMobileGuidLoader.setId(inspectionScheduleServiceDTO.getId())).andReturn(eventScheduleByMobileGuidLoader);
		expect(eventScheduleByMobileGuidLoader.load()).andReturn(eventSchedule);
		replay(eventScheduleByMobileGuidLoader);
	}

	private void createInspectionScheduleByMobileGuidLoaderMockReturningNull() {
		eventScheduleByMobileGuidLoader = createMock(EventScheduleByGuidOrIdLoader.class);
		expect(eventScheduleByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid())).andReturn(eventScheduleByMobileGuidLoader);
		expect(eventScheduleByMobileGuidLoader.setId(inspectionScheduleServiceDTO.getId())).andReturn(eventScheduleByMobileGuidLoader);
		expect(eventScheduleByMobileGuidLoader.load()).andReturn(null);
		replay(eventScheduleByMobileGuidLoader);
	}
	
	private void createSaverMock() {
		saver = createMock(EventScheduleSaver.class);
		expect(saver.saveOrUpdate(eventSchedule)).andReturn(eventSchedule);
		replay(saver);
	}

	private void createSaverRemoveMock() throws Exception {
		saver = createMock(EventScheduleSaver.class);
		saver.remove(eventSchedule);
		replay(saver);
	}

	private void buildInspectionScheduleServiceDTO(long id, Date newNextDate) {
		
		String strNextDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(newNextDate);
		
		inspectionScheduleServiceDTO = new InspectionScheduleServiceDTO();
		inspectionScheduleServiceDTO.setId(id);
		inspectionScheduleServiceDTO.setMobileGuid("inspectionScheduleMobileGuid");
		inspectionScheduleServiceDTO.setNextDate(strNextDate);
		
	
	}
	
	
	

}
