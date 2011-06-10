package com.n4systems.fieldid.selenium.testcase.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.HomePage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ChangeUserPasswordTest extends PageNavigatingTestCase<ManageUsersPage> {
	
	private static final String TEST_USER1 = "test_user1";
	private static final String NEW_PASSWORD = "newpassword";
	private static String COMPANY = "test1";
	
	@Override
	public void setupScenario(Scenario scenario) {
				
		PrimaryOrg defaultPrimaryOrg = scenario.primaryOrgFor(COMPANY);
		
		defaultPrimaryOrg.setExtendedFeatures(setOf(ExtendedFeature.Projects));
		
		scenario.save(defaultPrimaryOrg);
	}
	
	@Override
	protected ManageUsersPage navigateToPage() {
		return startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageUsers();
	}

		
	@Test
	public void should_cancel_back_to_edit_page_of_user() throws Exception {
		
		String userName = page.clickFirstSearchResult();

		page.clickChangePasswordTab();
		page.clickCancelChangePassword();
		
		assertEquals("Edit", page.getCurrentTab());
		assertEquals(userName, page.getUserId());
	}
	
	@Test
	public void change_password_save_with_missing_password() throws Exception {
		page.clickUserID(TEST_USER1);
		page.clickChangePasswordTab();
		page.clickSave();
		
		assertFalse(page.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void change_password_save_with_missing_new_password() throws Exception {
		page.clickUserID(TEST_USER1);
		page.clickChangePasswordTab();
		page.confirmPassword(NEW_PASSWORD);
		page.clickSave();
		
		assertFalse(page.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void change_password_save_with_missing_confirm_password() throws Exception {
		page.clickUserID(TEST_USER1);
		page.clickChangePasswordTab();
		page.enterNewPassword(NEW_PASSWORD);
		page.clickSave();
		
		assertFalse(page.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void change_and_login_with_new_password() throws Exception {
		page.clickUserID(TEST_USER1);
		page.clickChangePasswordTab();
		page.enterNewPassword(NEW_PASSWORD);
		page.confirmPassword(NEW_PASSWORD);
		page.clickSave();
		
		HomePage homePage = page.clickSignOut().login(TEST_USER1, NEW_PASSWORD);
		assertTrue(homePage.getFormErrorMessages().isEmpty());
	}
}
