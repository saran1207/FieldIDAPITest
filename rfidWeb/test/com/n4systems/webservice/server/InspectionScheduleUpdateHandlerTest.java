package com.n4systems.webservice.server;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static com.n4systems.model.builders.InspectionScheduleBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.inspectionschedule.InspectionScheduleByMobileGuidLoader;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;


public class InspectionScheduleUpdateHandlerTest {

	private final class InspectionScheduleSaverExtension extends
			InspectionScheduleSaver {

		@Override
		public void remove(InspectionSchedule entity)
				throws EntityStillReferencedException {
		}
		
	}

	private InspectionScheduleSaver saver;
	private InspectionScheduleByMobileGuidLoader inspectionScheduleByMobileGuidLoader;
	private FilteredIdLoader<InspectionSchedule> filteredInspectionScheduleLoader;
	private InspectionSchedule inspectionSchedule;
	private InspectionScheduleServiceDTO inspectionScheduleServiceDTO;
	private Inspection inspection;
	
	@Before
	public void setup() {
		
		inspectionSchedule = aScheduledInspectionSchedule().build();
		
		inspection = anInspection().build();
	}

	@Test
	public void loading_inspection_schedule_by_shcedule_id() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		
		createFilteredInspectionScheduleLoaderMock();
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(filteredInspectionScheduleLoader);
		
	}
	
	@Test
	public void loading_inspection_schedule_by_shcedule_mobile_guid() throws Exception {
	
		buildInspectionScheduleServiceDTO(-1L, new Date());
		
		createFilteredInspectionScheduleLoaderMock();
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(inspectionScheduleByMobileGuidLoader);
		
	}

	@Test
	public void if_inspection_schedule_not_found_then_skip_update() throws Exception {
	
		buildInspectionScheduleServiceDTO(-1L, new Date());
		
		createInspectionScheduleByMobileGuidLoaderMockReturningNull();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, null);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void is_inspection_schedule_next_date_updated_correctly() throws Exception {
	
		Calendar c1 = Calendar.getInstance(); 
		c1.set(2011, 11, 20, 0, 0, 0);
		
		buildInspectionScheduleServiceDTO(1L, c1.getTime());
		
		createFilteredInspectionScheduleLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
		verify(saver);
		
		assertEquals(new PlainDate(c1.getTime()), inspectionSchedule.getNextDate());
		
	}
	
	
	@Test
	public void update_when_schedule_is_not_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		
		createFilteredInspectionScheduleLoaderMock();
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, saver);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		verify(saver);
		
	}
	
	@Test
	public void do_not_update_when_schedule_is_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		inspectionSchedule.completed(inspection);
		
		createFilteredInspectionScheduleLoaderMock();
		createInspectionScheduleByMobileGuidLoaderMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, null);
		
		sut.updateInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@Test
	public void remove_when_schedule_is_not_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		
		createFilteredInspectionScheduleLoaderMock();
		createInspectionScheduleByMobileGuidLoaderMock();
		createSaverRemoveMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, saver);
		
		sut.removeInspectionSchedule(inspectionScheduleServiceDTO);
		verify(saver);
		
	}
	
	@Test
	public void do_not_remove_when_schedule_is_completed() throws Exception {
	
		buildInspectionScheduleServiceDTO(1L, new Date());
		inspectionSchedule.completed(inspection);
		
		createFilteredInspectionScheduleLoaderMock();
		createInspectionScheduleByMobileGuidLoaderMock();
		
		InspectionScheduleUpdateHandler  sut = new InspectionScheduleUpdateHandler(inspectionScheduleByMobileGuidLoader, filteredInspectionScheduleLoader, null);
		
		sut.removeInspectionSchedule(inspectionScheduleServiceDTO);
		
	}
	
	@SuppressWarnings("unchecked")
	private void createFilteredInspectionScheduleLoaderMock() {
		filteredInspectionScheduleLoader = createMock(FilteredIdLoader.class);
		expect(filteredInspectionScheduleLoader.setId(inspectionScheduleServiceDTO.getId())).andReturn(filteredInspectionScheduleLoader);
		expect(filteredInspectionScheduleLoader.load()).andReturn(inspectionSchedule);
		replay(filteredInspectionScheduleLoader);
	}

	private void createInspectionScheduleByMobileGuidLoaderMock() {
		inspectionScheduleByMobileGuidLoader = createMock(InspectionScheduleByMobileGuidLoader.class);
		expect(inspectionScheduleByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid())).andReturn(inspectionScheduleByMobileGuidLoader);
		expect(inspectionScheduleByMobileGuidLoader.load()).andReturn(inspectionSchedule);
		replay(inspectionScheduleByMobileGuidLoader);
	}

	private void createInspectionScheduleByMobileGuidLoaderMockReturningNull() {
		inspectionScheduleByMobileGuidLoader = createMock(InspectionScheduleByMobileGuidLoader.class);
		expect(inspectionScheduleByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getMobileGuid())).andReturn(inspectionScheduleByMobileGuidLoader);
		expect(inspectionScheduleByMobileGuidLoader.load()).andReturn(null);
		replay(inspectionScheduleByMobileGuidLoader);
	}
	
	private void createSaverMock() {
		saver = createMock(InspectionScheduleSaver.class);
		expect(saver.saveOrUpdate(inspectionSchedule)).andReturn(inspectionSchedule);
		replay(saver);
	}

	private void createSaverRemoveMock() throws Exception {
		saver = createMock(InspectionScheduleSaver.class);
		saver.remove(inspectionSchedule);
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
