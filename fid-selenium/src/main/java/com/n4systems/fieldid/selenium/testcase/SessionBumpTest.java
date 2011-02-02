package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SessionBumpPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SessionBumpTest extends FieldIDTestCase {

    private static final String TEST_USER = "testuser";
    private static final String TEST_PASSWORD = "testpass";

	private FieldIdSelenium secondSession;

    @Override
    public void setupScenario(Scenario scenario) {
        scenario.aUser().withUserId(TEST_USER).withPassword(TEST_PASSWORD).build();
    }

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
        LoginPage loginPage1 = startAsCompany("test1");
        LoginPage loginPage2 = startAsCompany("test1", secondSession);

        loginPage1.login(TEST_USER, TEST_PASSWORD);
        loginPage2.signInToSessionBump(TEST_USER, TEST_PASSWORD);
	}
	
	@Test
	public void should_kick_out_first_account_if_the_second_user_confirms_session_kick() throws Exception {
        LoginPage loginPage1 = startAsCompany("test1");
        LoginPage loginPage2 = startAsCompany("test1", secondSession);

        HomePage homePage1 = loginPage1.login(TEST_USER, TEST_PASSWORD);
        SessionBumpPage sessionBumpPage = loginPage2.signInToSessionBump(TEST_USER, TEST_PASSWORD);
        sessionBumpPage.confirmKickingSession();

        loginPage1 = homePage1.clickHomeExpectingSessionBoot();
        loginPage1.verifySessionKickMessageDisplayed();
	}
	
	@Test
	public void should_not_boot_out_multiple_system_account_sign_ins() throws Exception {
        LoginPage loginPage1 = startAsCompany("test1");
        LoginPage loginPage2 = startAsCompany("test1", secondSession);

        HomePage home1 = loginPage1.login().clickHomeLink();
        HomePage home2 = loginPage2.login().clickHomeLink();

        home1.clickSetupLink();
        home2.clickSetupLink();
	}

}
