package com.n4systems.fieldid.selenium.testcase.users;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgPage;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantLimit;

public class UserLimitTest extends FieldIDTestCase {
	
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
		adminPage.enterTenantLimits(new TenantLimit(-1L, -1L, 1L, -1L, 1L, -1L));

		manageUsersPage = startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageUsers();
	}
	
	@Test
	public void verify_user_limits_on_add() throws Exception {
		manageUsersPage.clickAddUserTab();
		assertFalse(manageUsersPage.canAddFullUser());
		assertFalse(manageUsersPage.canAddLiteUser());
	}
	
	@Test
	public void verify_lite_user_limits_on_full_user_upgrade() throws Exception {
		manageUsersPage.clickUserID(FULL_USER);
		manageUsersPage.clickChangeAccountType();
		assertFalse(manageUsersPage.canChangeToLiteUser());
	}
	
	@Test
	public void verify_full_user_limits_on_lite_user_upgrade() throws Exception {
		manageUsersPage.clickUserID(LITE_USER);
		manageUsersPage.clickChangeAccountType();
		assertFalse(manageUsersPage.canChangeToFullUser());
	}
}
