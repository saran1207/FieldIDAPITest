package com.n4systems.fieldid.selenium.testcase.userregistration;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.RegistrationRequestPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgPage;

public class UserRegistrationTest extends FieldIDTestCase {

	private static String USER_ID= "u_" + new Date().getTime();
	   
	@Before
	public void setUp() throws Exception {
		AdminOrgPage adminPage = startAdmin().login().filterByCompanyName("test1").clickEditOrganization("test1");
		adminPage.enterTenantLimits(-1, -1, -1);
	}
	
	@Test
	public void should_show_all_values_entered_on_the_registration_form_when_viewing_request_in_the_system(){

    	RegistrationRequestPage requestPage = startAsCompany("test1").clickRequestAnAccount(); 
    	LoginPage loginPage = requestPage.registerUser(USER_ID);
		
    	loginPage.login().clickSetupLink().clickRegistrationRequests().openRequestFromUser(USER_ID);
		
		assertTrue(selenium.isTextPresent(USER_ID));
		assertTrue(selenium.isTextPresent("dev@fieldid.com"));
		assertTrue(selenium.isTextPresent("Test"));
		assertTrue(selenium.isTextPresent("User"));
		assertTrue(selenium.isTextPresent("some position"));
		assertTrue(selenium.isTextPresent("Toronto"));
		assertTrue(selenium.isTextPresent("Canada:Ontario - Toronto"));
		assertTrue(selenium.isTextPresent("company_name"));
		assertTrue(selenium.isTextPresent("647-202-2789"));
		assertTrue(selenium.isTextPresent("This is a comment"));
	}


}
