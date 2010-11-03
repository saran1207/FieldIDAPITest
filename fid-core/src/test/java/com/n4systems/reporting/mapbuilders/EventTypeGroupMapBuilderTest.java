package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import com.n4systems.model.EventTypeGroup;
import org.junit.Test;

import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;

public class EventTypeGroupMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		EventTypeGroupMapBuilder builder = new EventTypeGroupMapBuilder();
		
		EventTypeGroup group = new EventTypeGroup();
		group.setReportTitle(TestHelper.randomString());
		
		builder.addParams(reportMap, group, null);
		
		assertEquals(group.getReportTitle(), reportMap.get(ReportField.REPORT_TITLE.getParamKey()));
	}
	
}
