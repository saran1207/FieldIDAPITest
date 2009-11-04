package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

public class TestStub extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void testLoginPageUnitTests() throws Exception {
		
		loginPage.gotoLoginPage();
		forgotPasswordPage.gotoIForgotMyPassword();
		loginPage.gotoReturnToSignInFromForgotPassword();
		forgotPasswordPage.gotoIForgotMyPassword();
		forgotPasswordPage.setUserName("darrell");
		
		Thread.sleep(1);
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
