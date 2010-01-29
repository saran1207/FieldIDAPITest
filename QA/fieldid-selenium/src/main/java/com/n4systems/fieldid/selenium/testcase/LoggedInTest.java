package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.Login;

public abstract class LoggedInTest extends FieldIDTestCase {

	private Login loginPage;
	protected final String password;
	protected final String username;

	public LoggedInTest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		loginPage = new Login(selenium, misc);
		loginPage.loginAcceptingEULAIfNecessary(username, password);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		
	}

}