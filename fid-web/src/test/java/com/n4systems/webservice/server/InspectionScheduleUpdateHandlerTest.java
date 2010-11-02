package com.n4systems.webservice.server;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static com.n4systems.model.builders.InspectionScheduleBuilder.aScheduledInspectionSchedule;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.inspectionschedule.InspectionScheduleByGuidOrIdLoader;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;


public class InspectionScheduleUpdateHandlerTest {


	private InspectionScheduleSaver saver;
	private InspectionScheduleByGuidOrIdLoader inspectionScheduleByMobileGuidLoader;
	private EventSchedule eventSchedule;
	private InspectionScheduleServiceDTO inspectionScheduleServiceDTO;
	private Event event;
	
	@Before
	public void setup() {
		
		eventSchedule = aScheduledInspectionSchedule().build();
		
		event = anEvent().build();
	}

	@Test
	public void loading_inspection_schedule_by_shcedule_id() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());

		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void loading_inspection_schedule_by_shcedule_mobile_guid() throws Exception {
	
		buildInspectionScheduleServiceDTO(-1L, new Date());

		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(inspectionScheduleByMobileGuidLoader);
		
	}

	@Test
	public void if_inspection_schedule_not_found_then_skip_update() throws Exception {
	
		buildInspectionScheduleServiceDTO(-1L, new Date());
		
		createInspectionScheduleByMobileGuidLoaderMockReturningNull();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, null);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void is_inspection_schedule_next_date_updated_correctly() throws Exception {
	
		Calendar c1 = Calendar.getInstance(); 
		c1.set(2011, 11, 20, 0, 0, 0);
		
		buildInspectionScheduleServiceDTO(1L, c1.getTime());
	
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(saver);
		
		assertEquals(new PlainDate(c1.getTime()), eventSchedule.getNextDate());
		
	}
	
	
	@Test
	public void update_when_schedule_is_not_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
	
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		verify(saver);
		
	}
	
	@Test
	public void do_not_update_when_schedule_is_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		eventSchedule.completed(event);
		
		createInspectionScheduleByMobileGuidLoaderMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, null);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void remove_when_schedule_is_not_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverRemoveMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, saver);
		
		sut.removeInspectionSchedule(inspectionScheduleServiceDTO);
		verify(saver);
		
	}
	
	@Test
	public void do_not_remove_when_schedule_is_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		eventSchedule.completed(event);

		createInspectionScheduleByMobileGuidLoaderMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, null);
		
		sut.removeInspectionSchedule(inspectionScheduleServiceDTO);
		
	}

	private void createInspectionScheduleByMobileGuidLoaderMock() {
		inspectionScheduleByMobileGuidLoader = createMock(InspectionScheduleByGuidOrIdLoader.class);
		expect(inspectionScheduleByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid())).andReturn(inspectionScheduleByMobileGuidLoader);
		expect(inspectionScheduleByMobileGuidLoader.setId(inspectionScheduleServiceDTO.getId())).andReturn(inspectionScheduleByMobileGuidLoader);
		expect(inspectionScheduleByMobileGuidLoader.load()).andReturn(eventSchedule);
		replay(inspectionScheduleByMobileGuidLoader);
	}

	private void createInspectionScheduleByMobileGuidLoaderMockReturningNull() {
		inspectionScheduleByMobileGuidLoader = createMock(InspectionScheduleByGuidOrIdLoader.class);
		expect(inspectionScheduleByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid())).andReturn(inspectionScheduleByMobileGuidLoader);
		expect(inspectionScheduleByMobileGuidLoader.setId(inspectionScheduleServiceDTO.getId())).andReturn(inspectionScheduleByMobileGuidLoader);
		expect(inspectionScheduleByMobileGuidLoader.load()).andReturn(null);
		replay(inspectionScheduleByMobileGuidLoader);
	}
	
	private void createSaverMock() {
		saver = createMock(InspectionScheduleSaver.class);
		expect(saver.saveOrUpdate(eventSchedule)).andReturn(eventSchedule);
		replay(saver);
	}

	private void createSaverRemoveMock() throws Exception {
		saver = createMock(InspectionScheduleSaver.class);
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
