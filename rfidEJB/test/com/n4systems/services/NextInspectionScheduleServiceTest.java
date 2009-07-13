package com.n4systems.services;

import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.ProductBuilder.aProduct;
import static com.n4systems.model.builders.InspectionScheduleBuilder.aScheduledInspectionSchedule;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.test.helpers.DateHelper;

public class NextInspectionScheduleServiceTest {

	private InspectionScheduleManager mockInspectionScheduleManager;
	
	@Before
	public void setUp() throws Exception {
		mockInspectionScheduleManager = createMock(InspectionScheduleManager.class);
	}
	
	@Test
	public void test_creates_schedule_when_one_doesnt_exist() {
		Product product = aProduct().build();
		InspectionType inspectionType = anInspectionType().build();
		Date nextDate = DateHelper.oneYearFromToday();
		InspectionSchedule schedule = aScheduledInspectionSchedule().product(product).inspectionType(inspectionType).nextDate(nextDate).build();
		List<InspectionSchedule> existingSchedules = new ArrayList<InspectionSchedule>();
		
		expect(mockInspectionScheduleManager.getAvailableSchedulesFor(product)).andReturn(existingSchedules);
		expect(mockInspectionScheduleManager.update((InspectionSchedule)anyObject())).andReturn(schedule);
		replay(mockInspectionScheduleManager);
		
		NextInspectionScheduleService scheduleService = new NextInspectionScheduleService(product, inspectionType, nextDate, mockInspectionScheduleManager);		
		InspectionSchedule returnedSchedule = scheduleService.createNextSchedule();
		
		verify(mockInspectionScheduleManager);
		assertEquals(schedule.getId(), returnedSchedule.getId());
	}
	
	@Test
	public void test_returns_existing_schedule_if_one_exists_on_same_day_ignoring_time() {
		Product product = aProduct().build();
		InspectionType inspectionType = anInspectionType().build();
		Date nextDate = DateHelper.oneYearFromToday();
		Date nextDateDifferentTime = new Date(nextDate.getTime() + 1);
		InspectionSchedule schedule = aScheduledInspectionSchedule().product(product).inspectionType(inspectionType).nextDate(nextDate).build();
		List<InspectionSchedule> existingSchedules = new ArrayList<InspectionSchedule>();
		existingSchedules.add(schedule);
				
		expect(mockInspectionScheduleManager.getAvailableSchedulesFor(product)).andReturn(existingSchedules);
		replay(mockInspectionScheduleManager);
		
		NextInspectionScheduleService scheduleService = new NextInspectionScheduleService(product, inspectionType, nextDateDifferentTime, mockInspectionScheduleManager);
		InspectionSchedule returnedSchedule = scheduleService.createNextSchedule();
		
		verify(mockInspectionScheduleManager);
		assertEquals(schedule.getId(), returnedSchedule.getId());		
	}
}
