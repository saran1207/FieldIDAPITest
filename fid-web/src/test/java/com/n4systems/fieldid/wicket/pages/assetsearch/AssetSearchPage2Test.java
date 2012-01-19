package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.n4systems.fieldid.wicket.FieldIdPageTest;
import com.n4systems.fieldid.wicket.FieldIdWicketTestRunner;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchPage2Test.AssetSearchHarness;


@RunWith(FieldIdWicketTestRunner.class)
public class AssetSearchPage2Test extends FieldIdPageTest<AssetSearchHarness, AssetSearchPage2> {
	
	@Test
	public void testRender() {
		expectingConfig();
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
