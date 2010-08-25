package com.n4systems.fieldid.selenium.lib;

import org.junit.After;
import org.junit.Before;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.LoginPage;

public abstract class LoggedInTestCase extends FieldIDTestCase {

	public static final String SYSTEM_USER_NAME = "n4systems";
	public static final String SYSTEM_USER_PASSWORD = "Xk43g8!@";
	
	protected final String password;
	protected final String username;
	
	protected LoginPage loginPage;
	
	protected HomePage homePage;

	public LoggedInTestCase(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public LoggedInTestCase() {
		this(SYSTEM_USER_NAME, SYSTEM_USER_PASSWORD);
	}
	
	@Before
	public void signIn() throws Exception {
		loginPage = new LoginPage(selenium, false);
		homePage = loginPage.login(username, password);
	}

	@After
	public void signOut() throws Exception {
		loginPage.signOut();
	}

}