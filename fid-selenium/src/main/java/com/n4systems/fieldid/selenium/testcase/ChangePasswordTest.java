package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageUsersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ChangePasswordTest extends FieldIDTestCase {
	
	private ManageUsersPage manageUsersPage;
	
	private static String COMPANY = "test1";
	
	@Override
	public void setupScenario(Scenario scenario) {
		
		Set<ExtendedFeature> extendedFeatures = new HashSet<ExtendedFeature>(
				Arrays.asList(ExtendedFeature.Projects, ExtendedFeature.ReadOnlyUser));
		
		PrimaryOrg defaultPrimaryOrg = scenario.primaryOrgFor(COMPANY);
		
		defaultPrimaryOrg.setExtendedFeatures(extendedFeatures);
		
		scenario.save(defaultPrimaryOrg);
	}
	
	@Before
	public void setUp() throws Exception {
		manageUsersPage = startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageUsers();
	}
		
	@Test
	public void should_cancel_back_to_edit_page_of_user() throws Exception {
		
		String userName = manageUsersPage.clickFirstSearchResult();

		manageUsersPage.clickChangePasswordTab();
		manageUsersPage.clickCancelChangePassword();
		
		assertEquals("Edit", manageUsersPage.getCurrentTab());
		assertEquals(userName, manageUsersPage.getUserId());
	}
}
