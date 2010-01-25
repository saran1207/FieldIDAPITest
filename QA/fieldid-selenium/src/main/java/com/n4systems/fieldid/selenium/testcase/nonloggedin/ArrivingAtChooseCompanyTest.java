package com.n4systems.fieldid.selenium.testcase.nonloggedin;


import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.misc.Misc;

public class ArrivingAtChooseCompanyTest extends FieldIDTestCase {

	
	
	@Test
	public void should_not_show_warning_message_when_a_user_just_arrives_at_the_main_choose_company_page() throws Exception {
		setCompany("www");
		selenium.open("/fieldid/chooseCompany.action");
		
		assertTrue("must be on the choose company page", selenium.isTextPresent("Choose A Company"));
		assertTrue("No error messages should be present", !selenium.isVisible("css=#error"));
		selenium.getLocation();
	}
	
	
	@Test
	public void should_forward_to_choose_company_page_from_root_of_www_domain() throws Exception {
		String url = getFieldIDProtocol() + "://www." + getFieldIDDomain() + getFieldIDContextRoot();
		selenium.open(url);
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		assertTrue("must be on the choose company page", selenium.isTextPresent("Choose A Company"));
		assertEquals("regexp:" + (url + "chooseCompany.action").replaceAll("\\/", "\\\\/").replaceAll("\\.", "\\\\."), selenium.getLocation());
		
		selenium.getLocation();
	}
	
	
	@Test
	public void should_show_warning_message_when_a_user_is_redirected_to_choose_company_page_when_they_enter_an_incorrect_tenant() throws Exception {
		setCompany("some-tenant-not-registered");

		assertTrue("must be on the choose company page", selenium.isTextPresent("Choose A Company"));
		assertTrue("error messages should be present", selenium.isVisible("css=#error"));
	}
}
