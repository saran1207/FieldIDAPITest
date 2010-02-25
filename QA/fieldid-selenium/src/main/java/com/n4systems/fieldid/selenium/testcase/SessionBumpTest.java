package com.n4systems.fieldid.selenium.testcase;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.Misc;


@BrowserResitriction(browsers={"*chrome","*firefox"})
public class SessionBumpTest extends FieldIDTestCase {

	
	private FieldIdSelenium secondSession;
	
	
	@Before
	public void createSecondBrowserSession() throws Exception {
		secondSession = createWebBrowser();
	}
	
	@After
	public void shutdownSecondBrowserSession() throws Exception {
		shutDownSelenium(secondSession);
	}
	
	
	@Test
	public void should_warn_the_user_that_they_will_be_booted_next_release() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		
		Login loginSession2 = new Login(secondSession, new Misc(secondSession, Logger.getRootLogger()));
		
		loginSession1.loginAcceptingEULAIfNecessary("sricci", "makemore$");
		
		loginSession2.loginAcceptingEULAIfNecessary("sricci", "makemore$");
		
		selenium.open("/fieldid/home.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		secondSession.open("/fieldid/home.action");
		secondSession.waitForPageToLoad(Misc.defaultTimeout);
		
		assertTrue("The is no warning of session kick out ", selenium.isElementPresent("sessionKickNotice"));
		assertFalse("The a session kick out warning is being shown to the wrong user. ", secondSession.isElementPresent("sessionKickNotice"));
	}
	
	
	
	@Test
	public void should_warn_the_user_that_they_will_be_booted_next_release_on_every_request_after_it_happens() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		
		Login loginSession2 = new Login(secondSession, new Misc(secondSession, Logger.getRootLogger()));
		
		loginSession1.loginAcceptingEULAIfNecessary("sricci", "makemore$");
		
		loginSession2.loginAcceptingEULAIfNecessary("sricci", "makemore$");
		
		selenium.open("/fieldid/home.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		secondSession.open("/fieldid/home.action");
		secondSession.waitForPageToLoad(Misc.defaultTimeout);
		
		selenium.open("/fieldid/search.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		selenium.open("/fieldid/report.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		assertTrue("The is no warning of session kick out ", selenium.isElementPresent("sessionKickNotice"));
	}
	
	
	@Test
	public void should_not_have_give_warnings_to_system_account_sign_ins() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		
		Login loginSession2 = new Login(secondSession, new Misc(secondSession, Logger.getRootLogger()));
		
		loginSession1.loginAcceptingEULAIfNecessary("n4systems", "makemore$");
		
		loginSession2.loginAcceptingEULAIfNecessary("n4systems", "makemore$");
		
		selenium.open("/fieldid/home.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		secondSession.open("/fieldid/home.action");
		secondSession.waitForPageToLoad(Misc.defaultTimeout);
		
		assertFalse("The a session kick out warning is being shown a system user.", selenium.isElementPresent("sessionKickNotice"));
		assertFalse("The a session kick out warning is being shown a system user.", secondSession.isElementPresent("sessionKickNotice"));
	}
	
	@Test
	public void should_find_that_an_ajax_request_by_kicked_out_user_will_place_the_warning_on_the_page() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		Login loginSession2 = new Login(secondSession, new Misc(secondSession, Logger.getRootLogger()));
		
		loginSession1.loginAcceptingEULAIfNecessary("sricci", "makemore$");
		selenium.open("/fieldid/productAdd.action");
		selenium.waitForPageToLoad(Misc.defaultTimeout);
		
		
		loginSession2.loginAcceptingEULAIfNecessary("sricci", "makemore$");
		
		selenium.click("css=.searchOwner");
		selenium.waitForAjax(Misc.AJAX_TIMEOUT);
		
		assertTrue("The is no warning of session kick out ", selenium.isElementPresent("sessionKickNotice"));
	
	}
	
	
	
	
	
	
	
}
