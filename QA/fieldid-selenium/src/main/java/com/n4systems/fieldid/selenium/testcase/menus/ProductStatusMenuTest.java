package com.n4systems.fieldid.selenium.testcase.menus;

import org.junit.Test;

import com.n4systems.fieldid.selenium.misc.Misc;
import com.n4systems.fieldid.selenium.testcase.LoggedInTestCase;

public class ProductStatusMenuTest extends LoggedInTestCase {

	public ProductStatusMenuTest() {
		super();
	}

	
	@Test
	public void should_be_able_to_go_back_to_the_list_of_product_status_from_the_view_all_link_on_the_menu() throws Exception {
		selenium.open("/fieldid/productStatusEdit.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		selenium.click("link=View All");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		assertEquals("Manage Product Statuses", selenium.getText("css=#contentTitle h1"));
	}
}
