package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.inspectionschedule.NextInspectionScheduleLoader;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ReportMap;

public class InspectionScheduleMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		Inspection inspection = new Inspection();
		inspection.setProduct(ProductBuilder.aProduct().build());
		inspection.setType(InspectionTypeBuilder.anInspectionType().build());
		
		final InspectionSchedule schedule = new InspectionSchedule();
		schedule.setNextDate(new Date());
		
		DateTimeDefiner dateDefiner = new DateTimeDefiner("dd-MM-yyyy", TimeZone.getDefault());
		
		NextInspectionScheduleLoader loader = new NextInspectionScheduleLoader() {
			protected InspectionSchedule load(EntityManager em) {
				return schedule;
			}
		};
		
		InspectionScheduleMapBuilder builder = new InspectionScheduleMapBuilder(dateDefiner, loader);
		builder.addParams(reportMap, inspection, new DummyTransaction());
		
		assertEquals(schedule.getNextDate(), reportMap.get(ReportField.NEXT_DATE.getParamKey()));
		assertEquals(DateHelper.format(schedule.getNextDate(), dateDefiner), reportMap.get(ReportField.NEXT_DATE_STRING.getParamKey()));
	}
	
}
