package com.n4systems.fieldid.wicket.pages.widgets;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;


public class OrgDateRangeSubtitleHelperTest {

	private OrgDateRangeSubtitleHelper fixture;
	
	@Before
	public void setUp() { 
		fixture = new OrgDateRangeSubtitleHelper();
	}
	
	@Test 
	public void testgetSubtitleModel_all_date_ranges() { 
		BaseOrg org = OrgBuilder.aCustomerOrg().build();
		SubTitleModelInfo result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.FOREVER);
		assertEquals("dateRange.forever.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());

		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.FOREVER);
		assertEquals("dateRange.forever.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.LAST_MONTH);
		assertEquals("dateRange.month.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.LAST_MONTH);
		assertEquals("dateRange.month.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.THIS_MONTH);
		assertEquals("dateRange.month.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.THIS_MONTH);
		assertEquals("dateRange.month.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		

		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.LAST_WEEK);
		assertEquals("dateRange.week.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.LAST_WEEK);
		assertEquals("dateRange.week.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.THIS_WEEK);
		assertEquals("dateRange.week.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.THIS_WEEK);
		assertEquals("dateRange.week.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		


		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.LAST_YEAR);
		assertEquals("dateRange.year.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.LAST_YEAR);
		assertEquals("dateRange.year.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.THIS_YEAR);
		assertEquals("dateRange.year.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.THIS_YEAR);
		assertEquals("dateRange.year.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		

		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.LAST_QUARTER);
		assertEquals("dateRange.quarter.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.LAST_QUARTER);
		assertEquals("dateRange.quarter.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", org, ChartDateRange.THIS_QUARTER);
		assertEquals("dateRange.quarter.org.subTitle", result.getKey());
		assertEquals(3, result.getModels().size());
		
		result = fixture.getSubTitleModel("bogusModel", null, ChartDateRange.THIS_QUARTER);
		assertEquals("dateRange.quarter.subTitle", result.getKey());
		assertEquals(2, result.getModels().size());
		
	}
}
