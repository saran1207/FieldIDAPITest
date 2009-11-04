package com.n4systems.model;

import static com.n4systems.model.builders.InspectionScheduleBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import static com.n4systems.test.helpers.Asserts.*;

public class InspectionScheduleTest {

	
	private InspectionSchedule schedule;
	private Inspection inspection;

	@Before public void setUp() throws Exception {
		Product product = aProduct().build();
		inspection = new Inspection();
		inspection.setId(1L);
		inspection.setProduct(product);
		
		schedule = aScheduledInspectionSchedule().product(product).build(); 
	}

	@After public void tearDown() throws Exception {
	}

	
	@Test public void test_completed_inspection() {
		schedule.completed(inspection);
		assertEquals(inspection, schedule.getInspection());
		assertInRange(new Date(), schedule.getCompletedDate(), 1000L);
	}

	@Test public void test_remove_inspection() {
		test_completed_inspection();
		
		schedule.removeInspection();
		assertNull(schedule.getCompletedDate());
		assertNull(schedule.getInspection());
		assertEquals(ScheduleStatus.SCHEDULED, schedule.getStatus());
		
	}
}
