package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import org.junit.Test;

import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.inspectionschedule.NextInspectionScheduleLoader;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ReportMap;

public class InspectionScheduleMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		Event event = new Event();
		event.setAsset(AssetBuilder.anAsset().build());
		event.setType(InspectionTypeBuilder.anInspectionType().build());
		
		final EventSchedule schedule = new EventSchedule();
		schedule.setNextDate(new Date());
		
		DateTimeDefiner dateDefiner = new DateTimeDefiner("dd-MM-yyyy", TimeZone.getDefault());
		
		NextInspectionScheduleLoader loader = new NextInspectionScheduleLoader() {
			protected EventSchedule load(EntityManager em) {
				return schedule;
			}
		};
		
		InspectionScheduleMapBuilder builder = new InspectionScheduleMapBuilder(dateDefiner, loader);
		builder.addParams(reportMap, event, new DummyTransaction());
		
		assertEquals(schedule.getNextDate(), reportMap.get(ReportField.NEXT_DATE.getParamKey()));
		assertEquals(DateHelper.format(schedule.getNextDate(), dateDefiner), reportMap.get(ReportField.NEXT_DATE_STRING.getParamKey()));
	}
	
}
