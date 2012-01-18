package com.n4systems.fieldid.wicket.pages.widgets;

import static org.junit.Assert.*;

import com.n4systems.util.chart.FloatingDateRange;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;


public class OrgDateRangeSubtitleHelperTest {

	private OrgDateRangeSubtitleHelper fixture;
	
	@Before
	public void setUp() { 
		fixture = new OrgDateRangeSubtitleHelper();
	}
	
	@Test 
	public void testgetSubtitleModel_all_date_ranges() { 
		BaseOrg org = OrgBuilder.aCustomerOrg().build();
		SubTitleModelInfo result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.FOREVER);
		assertEquals("dateRange.forever.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());

		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.FOREVER);
		assertEquals("dateRange.forever.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.LAST_MONTH);
		assertEquals("dateRange.month.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.LAST_MONTH);
		assertEquals("dateRange.month.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.THIS_MONTH);
		assertEquals("dateRange.month.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.THIS_MONTH);
		assertEquals("dateRange.month.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		

		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.LAST_WEEK);
		assertEquals("dateRange.week.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.LAST_WEEK);
		assertEquals("dateRange.week.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.THIS_WEEK);
		assertEquals("dateRange.week.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.THIS_WEEK);
		assertEquals("dateRange.week.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		


		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.LAST_YEAR);
		assertEquals("dateRange.year.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.LAST_YEAR);
		assertEquals("dateRange.year.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.THIS_YEAR);
		assertEquals("dateRange.year.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.THIS_YEAR);
		assertEquals("dateRange.year.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		

		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.LAST_QUARTER);
		assertEquals("dateRange.quarter.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.LAST_QUARTER);
		assertEquals("dateRange.quarter.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, FloatingDateRange.THIS_QUARTER);
		assertEquals("dateRange.quarter.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, FloatingDateRange.THIS_QUARTER);
		assertEquals("dateRange.quarter.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
	}
}
