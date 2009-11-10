package com.n4systems.fieldid.selenium.testcase;

import org.junit.*;

public class LoginCommonUnitTests extends FieldIDTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FieldIDTestCase.setUpBeforeClass();
		FieldIDTestCase.setEnvironmentVariables(null, 0, "*iehta", null, null, null);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void goToLoginPage() throws Exception {
		loginPage.gotoLoginPage();
	}
	
	@Test
	public void goToSignInWithSecurityRFIDNumber() throws Exception {
		// given we are on login page.
		goToLoginPage();
		
		loginPage.gotoSignInWithSecurityRFIDNumberLink();
		
		
	}
	
	@Test
	public void goToSignInWithUserName() throws Exception {
		goToSignInWithSecurityRFIDNumber();
		loginPage.gotoSignInWithUserNameLink();
	}
	
	@Test
	public void goToN4SystemsFromLoginPage() throws Exception {
		goToLoginPage();
		loginPage.gotoN4Systems();
	}
	
	@Ignore("Only used when testing production environment")
	@Test
	public void goToThawteSiteSealFromLoginPage() throws Exception {
		goToLoginPage();
		loginPage.gotoThawteCertificate();
	}
	
	@Test
	public void goToForgotPassword() throws Exception {
		goToLoginPage();
		forgotPasswordPage.gotoIForgotMyPassword();
	}
	
	@Test
	public void returnToLoginPageFromForgotPassword() throws Exception {
		goToForgotPassword();
		loginPage.gotoReturnToSignInFromForgotPassword();
	}
	
	@Test
	public void goToChooseACompany() throws Exception {
		goToLoginPage();
		chooseACompany.gotoNotTheCompanyIWant();
	}
	
	@Test
	public void returnToLoginPageFromChooseACompany() throws Exception {
		goToChooseACompany();
		chooseACompany.setCompanyID(tenant);
		loginPage.gotoReturnToSignInFromChooseACompany();
	}
	
	@Test
	public void loginIn() throws Exception {
		goToLoginPage();
		String username = "n4systems";
		String password = "makemore$";
		loginPage.setUserName(username);
		loginPage.setPassword(password);
		homePage.gotoSignInUserName();
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
