package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.TimeZone;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.inspectionschedule.NextEventScheduleLoader;
import org.junit.Test;

import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ReportMap;

public class EventScheduleMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		Event event = new Event();
		event.setAsset(AssetBuilder.anAsset().build());
		event.setType(EventTypeBuilder.anEventType().build());
		
		final EventSchedule schedule = new EventSchedule();
		schedule.setNextDate(new Date());
		
		DateTimeDefiner dateDefiner = new DateTimeDefiner("dd-MM-yyyy", TimeZone.getDefault());
		
		NextEventScheduleLoader loader = new NextEventScheduleLoader() {
			protected EventSchedule load(EntityManager em) {
				return schedule;
			}
		};
		
		EventScheduleMapBuilder builder = new EventScheduleMapBuilder(dateDefiner, loader);
		builder.addParams(reportMap, event, new DummyTransaction());
		
		assertEquals(schedule.getNextDate(), reportMap.get(ReportField.NEXT_DATE.getParamKey()));
		assertEquals(DateHelper.format(schedule.getNextDate(), dateDefiner), reportMap.get(ReportField.NEXT_DATE_STRING.getParamKey()));
	}
	
}
