package com.n4systems.fieldid.selenium.testcase.menus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class AssetStatusMenuTest extends LoggedInTestCase {

	@Test
	public void should_be_able_to_go_back_to_the_list_of_asset_status_from_the_view_all_link_on_the_menu() throws Exception {
		selenium.open("/fieldid/assetStatusEdit.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		selenium.click("link=View All");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		assertEquals("Asset Statuses", selenium.getText("css=#contentTitle h1"));
	}

}
