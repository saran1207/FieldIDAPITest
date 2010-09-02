package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.home.page.Home;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.login.page.Login;
import com.n4systems.fieldid.selenium.misc.MiscDriver;

public class SessionBumpTest extends FieldIDTestCase {

	private FieldIdSelenium secondSession;
	
	@Before
	public void createSecondBrowserSession() throws Exception {
		secondSession = createOpenedWebBrowser();
	}
	
	@After
	public void shutdownSecondBrowserSession() throws Exception {
		shutDownSelenium(secondSession);
	}
	
	@Test
	public void should_warn_second_user_that_another_user_will_be_kicked_out_if_they_confirm() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		
		Login loginSession2 = new Login(secondSession, new MiscDriver(secondSession));
		
		loginSession1.signInAllTheWayToHome("aaitkens", "makemore$");
		
		loginSession2.doSignIn("aaitkens", "makemore$");
		loginSession2.assertOnConfirmSessionKick();
	}
	
	@Test
	public void should_kick_out_first_account_if_the_second_user_confirms_session_kick() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		
		Login loginSession2 = new Login(secondSession, new MiscDriver(secondSession));
		
		loginSession1.signInAllTheWayToHome("aaitkens", "makemore$");
		
		loginSession2.doSignIn("aaitkens", "makemore$");
		loginSession2.assertOnConfirmSessionKick();
		loginSession2.confirmKickingSession();
		loginSession2.verifySignedIn();
		
		misc.gotoHome();
		loginSession1.verifyLoginPageWithKickMessage();
	}
	
	@Ignore
	@Test
	public void should_warn_the_user_that_they_will_be_booted_next_release_on_every_request_after_it_happens() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		
		Login loginSession2 = new Login(secondSession, new MiscDriver(secondSession));
		
		loginSession1.signInAllTheWayToHome("sricci", "makemore$");
		
		loginSession2.signInAllTheWayToHome("sricci", "makemore$");
		
		selenium.open("/fieldid/home.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		secondSession.open("/fieldid/home.action");
		secondSession.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		selenium.open("/fieldid/search.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		selenium.open("/fieldid/report.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		assertTrue("The is no warning of session kick out ", selenium.isElementPresent("sessionKickNotice"));
	}
	
	@Test
	public void should_not_boot_out_multiple_system_account_sign_ins() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		Home home1 = new Home(selenium, misc);
		
		MiscDriver misc2 = new MiscDriver(secondSession);
		Login loginSession2 = new Login(secondSession, misc2);
		Home home2 = new Home(secondSession, misc2);
		
		loginSession1.signInWithSystemAccount();
		
		loginSession2.signInWithSystemAccount();
		
		selenium.open("/fieldid/home.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		secondSession.open("/fieldid/home.action");
		secondSession.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		home1.assertHomePage();
		home2.assertHomePage();
	}
	
	@Test
	@Ignore
	public void should_find_that_an_ajax_request_by_kicked_out_user_will_place_the_warning_on_the_page() throws Exception {
		Login loginSession1 = new Login(selenium, misc);
		Login loginSession2 = new Login(secondSession, new MiscDriver(secondSession));
		
		loginSession1.signInAllTheWayToHome("sricci", "makemore$");
		selenium.open("/fieldid/productAdd.action");
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
		
		loginSession2.signInAllTheWayToHome("sricci", "makemore$");
		
		selenium.click("css=.searchOwner");
		selenium.waitForAjax(MiscDriver.AJAX_TIMEOUT);
		
		assertTrue("The is no warning of session kick out ", selenium.isElementPresent("sessionKickNotice"));
	}
	
}
