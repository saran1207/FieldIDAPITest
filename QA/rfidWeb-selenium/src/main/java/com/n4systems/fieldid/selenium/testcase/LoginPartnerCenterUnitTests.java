package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

public class LoginPartnerCenterUnitTests extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void goToLoginPage() throws Exception {
		loginPage.gotoLoginPage();
	}
	
	@Test
	public void goToRequestAnAccount() throws Exception {
		goToLoginPage();
		registerNewUser.gotoRequestAnAccount();
	}
	
	@Test
	public void returnToLoginPageFromRequestAnAccount() throws Exception {
		goToRequestAnAccount();
		loginPage.gotoReturnToSignInFromRegisterNewUserPage();
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		FieldIDTestCase.tearDownAfterClass();
	}
}
