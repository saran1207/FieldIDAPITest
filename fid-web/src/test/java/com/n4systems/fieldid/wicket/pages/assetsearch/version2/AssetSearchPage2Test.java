package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.reporting.helpers.ColumnMappingBuilder;
import com.n4systems.fieldid.reporting.helpers.ColumnMappingGroupBuilder;
import com.n4systems.fieldid.reporting.helpers.ReportConfiguationBuilder;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.wicket.FieldIdPageTest;
import com.n4systems.fieldid.wicket.FieldIdWicketTestRunner;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AssetSearchPage2Test.AssetSearchHarness;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.security.SecurityFilter;


@RunWith(FieldIdWicketTestRunner.class)
public class AssetSearchPage2Test extends FieldIdPageTest<AssetSearchHarness, AssetSearchPage2> {
	
	private AssetColumnsService assetColumnService;


	@Override
	@Before
	public void setUp() throws Exception { 
		super.setUp();
		assetColumnService =  wire(AssetColumnsService.class);
	}
	
	@Test
	public void testRender() {
		ColumnMappingView cmv = ColumnMappingBuilder.aColumnMapping().build();
		ColumnMappingGroupView cmg = ColumnMappingGroupBuilder.aColumnMappingGroup().withMappings(cmv).build(); 
		ReportConfiguration reportConfiguration = ReportConfiguationBuilder.aReportConfiguration().withColumnGroups(Lists.newArrayList(cmg)).build();		
		
		expectingConfig();		
		expect(assetColumnService.getReportConfiguration(anyObject(SecurityFilter.class))).andReturn(reportConfiguration);
		replay(assetColumnService);
		
		renderFixture(this);
		verifyMocks();		
	}
	
	@Override
	public AssetSearchPage2 createFixture(String id) {
		return new AssetSearchPage2();
	}

	@Override
	protected AssetSearchHarness createHarness(String pathContext, IWicketTester wicketTester) {
		return new AssetSearchHarness(pathContext, wicketTester);
	}

	
	class AssetSearchHarness extends WicketHarness {

		public AssetSearchHarness(String pathContext, IWicketTester tester) {
			super(pathContext, tester);			
		} 
		
	}
	
}
