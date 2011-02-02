package com.n4systems.fieldid.selenium.testcase.users;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class UpgradeUsersTest extends FieldIDTestCase {
	
	private static String COMPANY = "test1";
	private static String FULL_USER = "test_user1";
	private static String READ_ONLY_USER = "aReadOnlyUser";
	private static String LITE_USER = "aLiteUser";
	private ManageUsersPage manageUsersPage;

	@Override
	public void setupScenario(Scenario scenario) {
		
		Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>(
				Arrays.asList(ExtendedFeature.Projects, ExtendedFeature.ReadOnlyUser));
		
		PrimaryOrg defaultPrimaryOrg = scenario.primaryOrgFor(COMPANY);
		
		defaultPrimaryOrg.setExtendedFeatures(extendedFeatures);
		
		defaultPrimaryOrg.getLimits().setLiteUsersUnlimited();
		
		scenario.save(defaultPrimaryOrg);
		
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
		manageUsersPage = startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageUsers();
	}
	
	@Test
	public void upgrade_read_only_user_to_lite() throws Exception {
		manageUsersPage.clickUserID(READ_ONLY_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToLiteUser();
		manageUsersPage.clickSaveUser();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_LITE, READ_ONLY_USER);
	}

	@Test
	public void upgrade_read_only_user_to_full() throws Exception {
		manageUsersPage.clickUserID(READ_ONLY_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToFullUser();
		manageUsersPage.clickSaveUser();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_FULL, READ_ONLY_USER);
	}
	
	@Test
	public void upgrade_lite_user_to_full() throws Exception {
		manageUsersPage.clickUserID(LITE_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToFullUser();
		manageUsersPage.clickSaveUser();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_FULL, LITE_USER);
	}
	
	@Test
	public void downgrade_lite_user_to_readonly() throws Exception {
		manageUsersPage.clickUserID(LITE_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToReadOnlyUser();
		manageUsersPage.clickSaveUser();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_READONLY, LITE_USER);
	}
	
	@Test
	public void downgrade_full_user_to_readonly() throws Exception {
		manageUsersPage.clickUserID(FULL_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToReadOnlyUser();
		manageUsersPage.clickSaveUser();
		
		verifyUserTypeChange(ManageUsersPage.USER_TYPE_READONLY, FULL_USER);
	}
	
	@Test
	public void downgrade_full_user_lite() throws Exception {
		manageUsersPage.clickUserID(FULL_USER);
		manageUsersPage.clickChangeAccountType();
		manageUsersPage.clickChangeToLiteUser();
		manageUsersPage.clickSaveUser();
		
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
