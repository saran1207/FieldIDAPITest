package com.n4systems.fieldid.selenium.testcase.nonloggedin;


import static com.n4systems.fieldid.selenium.asserts.FieldIdAssert.assertSystemLogoIsUsed;
import org.junit.Before;
import org.junit.Test;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.page.Choose;
import com.n4systems.fieldid.selenium.misc.Misc;

public class ArrivingAtChooseCompanyTest extends FieldIDTestCase {

	private Choose choosePage;
	
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		choosePage = new Choose(selenium, misc);
	}
	
	@Test
	public void should_not_show_warning_message_when_a_user_just_arrives_at_the_main_choose_company_page() throws Exception {
		gotoChooseCompanyPage();
		
		choosePage.verifyChooseCompany();
		assertTrue("No error messages should be present", !selenium.isVisible("css=#error"));
		
	}

	private void gotoChooseCompanyPage() {
		setCompany("www");
		selenium.open("/fieldid/chooseCompany.action");
	}
	
	
	@Test
	public void should_forward_to_choose_company_page_from_root_of_www_domain() throws Exception {
		String url = getFieldIDProtocol() + "://www." + getFieldIDDomain() + getFieldIDContextRoot();
		selenium.open(url);
		selenium.waitForPageToLoad(Misc.DEFAULT_TIMEOUT);
		
		choosePage.verifyChooseCompany();
		assertEquals("regexp:" + (url + "chooseCompany.action").replaceAll("\\/", "\\\\/").replaceAll("\\.", "\\\\."), selenium.getLocation());
		
		selenium.getLocation();
	}
	
	
	@Test
	public void should_show_warning_message_when_a_user_is_redirected_to_choose_company_page_when_they_enter_an_incorrect_tenant() throws Exception {
		setCompany("some-tenant-not-registered");

		
		assertTrue("error messages should be present", selenium.isVisible("css=#error"));
	}
	
	@Test
	public void should_display_the_system_logo_not_a_branded_logo() throws Exception {
		gotoChooseCompanyPage();
		
		assertSystemLogoIsUsed(selenium);
	}
}
