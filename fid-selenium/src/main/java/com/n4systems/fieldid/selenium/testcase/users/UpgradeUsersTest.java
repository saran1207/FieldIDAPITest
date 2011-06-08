package com.n4systems.fieldid.selenium.testcase.users;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantLimit;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class UpgradeUsersTest extends FieldIDTestCase {
	
	private static String COMPANY = "test1";
	private static String FULL_USER = "test_user1";
	private static String READ_ONLY_USER = "aReadOnlyUser";
	private static String LITE_USER = "aLiteUser";
	private ManageUsersPage manageUsersPage;

	@Override
	public void setupScenario(Scenario scenario) {
		PrimaryOrg defaultPrimaryOrg = scenario.primaryOrgFor(COMPANY);
		
		defaultPrimaryOrg.setExtendedFeatures(setOf(ExtendedFeature.Projects, ExtendedFeature.ReadOnlyUser));
				
		scenario.updatePrimaryOrg(defaultPrimaryOrg);		
		
		scenario.aReadOnlyUser()
		        .withUserId(READ_ONLY_USER)
		        .withPassword(READ_ONLY_USER)
		        .build();
		
		scenario.aLiteUser()
			.withUserId(LITE_USER)
			.withPassword(LITE_USER)
			.build();		
	}	
	
	@Before
	public void setUp() throws Exception {
		AdminOrgPage adminPage = startAdmin().login().filterByCompanyName(COMPANY).clickEditOrganization(COMPANY);
		adminPage.enterTenantLimits(new TenantLimit(-1L, -1L, -1L, -1L, -1L, -1L));
		manageUsersPage = startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageUsers();
	}
		
	@Test
	public void upgrade_read_only_user_to_lite() throws Exception {
		manageUsersPage.clickUserID(READ_ONLY_USER);
		manageUsersPage.clickChangeAccountType();
		assertTrue(manageUsersPage.canChangeToLiteUser());
		manageUsersPage.clickChangeToLiteUser();
		manageUsersPage.clickSave();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_LITE, READ_ONLY_USER);
	}

	@Test
	public void upgrade_read_only_user_to_full() throws Exception {
		manageUsersPage.clickUserID(READ_ONLY_USER);
		manageUsersPage.clickChangeAccountType();
		assertTrue(manageUsersPage.canChangeToFullUser());
		manageUsersPage.clickChangeToFullUser();
		manageUsersPage.clickSave();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_FULL, READ_ONLY_USER);
	}
	
	@Test
	public void upgrade_lite_user_to_full() throws Exception {
		manageUsersPage.clickUserID(LITE_USER);
		manageUsersPage.clickChangeAccountType();
		assertTrue(manageUsersPage.canChangeToFullUser());
		manageUsersPage.clickChangeToFullUser();
		manageUsersPage.clickSave();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_FULL, LITE_USER);
	}
	
	@Test
	public void downgrade_lite_user_to_readonly() throws Exception {
		manageUsersPage.clickUserID(LITE_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToReadOnlyUser();
		manageUsersPage.clickSave();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_READONLY, LITE_USER);
	}
	
	@Test
	public void downgrade_full_user_to_readonly() throws Exception {
		manageUsersPage.clickUserID(FULL_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToReadOnlyUser();
		manageUsersPage.clickSave();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_READONLY, FULL_USER);
	}
	
	@Test
	public void downgrade_full_user_lite() throws Exception {
		manageUsersPage.clickUserID(FULL_USER);
		manageUsersPage.clickChangeAccountType();
		assertTrue(manageUsersPage.canChangeToLiteUser());
		manageUsersPage.clickChangeToLiteUser();
		manageUsersPage.clickSave();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_LITE, FULL_USER);
	}
	
	private void verifyUserTypeChange(String userType, String userid) {
		manageUsersPage.clickViewAllTab();
		manageUsersPage.selectSearchUserType(userType);
		manageUsersPage.clickSearchButton();
		List<String> users = manageUsersPage.getListOfUserIDsOnCurrentPage();
		assertTrue(users.size() > 0);
		assertTrue(users.contains(userid));
	}

}
