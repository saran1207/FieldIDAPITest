package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.Login;

public class WEB_1430 extends FieldIDTestCase {

	Login login;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		login = new Login(selenium, misc);
		// TODO: SQL code to update all users to have the same, known password
	}

	@Test
	public void shouldBeAbleToLogIntoFieldIDAsAdminForTenantsWithDifferentTimeZones() throws Exception {
		// Assumes all users have had their password set to the same value
		String password = getStringProperty("password");
		int max = getIntegerProperty("numcompanies");

		try {
			for(int i = 0; i < max; i++) {
				String companyID = getStringProperty("company" + i);
				String username = getStringProperty("userid" + i);
				setCompany(companyID);
				loginAcceptingEULAIfNecessary(username, password);
				login.verifySignedIn();
				misc.gotoSignOut();
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void loginAcceptingEULAIfNecessary(String username, String password) {
		login.setUserName(username);
		login.setPassword(password);
		login.gotoSignIn();
		if(misc.isEULA()) {
			misc.scrollToBottomOfEULA();
			misc.gotoAcceptEULA();
		}
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
