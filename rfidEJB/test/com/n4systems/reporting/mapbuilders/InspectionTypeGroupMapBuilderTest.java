package com.n4systems.reporting.mapbuilders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ReportMap;

public class InspectionTypeGroupMapBuilderTest {

	@Test
	public void testSetAllFields() {
		ReportMap<Object> reportMap = new ReportMap<Object>();
		
		InspectionTypeGroupMapBuilder builder = new InspectionTypeGroupMapBuilder();
		
		InspectionTypeGroup group = new InspectionTypeGroup();
		group.setReportTitle(TestHelper.randomString());
		
		builder.addParams(reportMap, group, null);
		
		assertEquals(group.getReportTitle(), reportMap.get(ReportField.REPORT_TITLE.getParamKey()));
	}
	
}
