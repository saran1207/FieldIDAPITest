package com.n4systems.fieldid.selenium.testcase;

import org.junit.After;
import org.junit.Before;
import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.login.page.Login;

public abstract class LoggedInTestCase extends FieldIDTestCase {
	public static final String SYSTEM_USER_NAME = "n4systems";
	public static final String SYSTEM_USER_PASSWORD = "Xk43g8!@";
	
	
	private Login loginPage;
	protected final String password;
	protected final String username;

	public LoggedInTestCase(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public LoggedInTestCase() {
		this(SYSTEM_USER_NAME, SYSTEM_USER_PASSWORD);
		
	}

	
	@Before
	public void signIn() throws Exception {
		loginPage = new Login(selenium, misc);
		loginPage.signIn(username, password);
	}

	@After
	public void signOut() throws Exception {
		loginPage.signOut();
	}

}