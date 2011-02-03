package com.n4systems.fieldid.selenium.testcase.menus;

import static org.junit.Assert.assertEquals;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import org.junit.Test;

public class AssetStatusMenuTest extends PageNavigatingTestCase<HomePage> {

    @Override
    protected HomePage navigateToPage() {
        return start().login();
    }

	@Test
	public void should_be_able_to_go_back_to_the_list_of_asset_status_from_the_view_all_link_on_the_menu() throws Exception {
		selenium.open("/fieldid/assetStatusEdit.action");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		
		selenium.click("link=View All");
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		
		assertEquals("Asset Statuses", selenium.getText("css=#contentTitle h1"));
	}

}
