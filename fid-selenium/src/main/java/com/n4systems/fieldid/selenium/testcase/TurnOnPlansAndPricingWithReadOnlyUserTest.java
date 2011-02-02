package com.n4systems.fieldid.selenium.testcase;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminLoginPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgsListPage;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TurnOnPlansAndPricingWithReadOnlyUserTest extends FieldIDTestCase {

	@Test
	public void readOnlyUserOnEnablePlansAndPricingOffShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = true;
		boolean showPlansAndPricing = false;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
        LoginPage loginPage = startAsCompany("test1");
        assertTrue(loginPage.isRequestAnAccountAvailable());
	}
	
	@Test
	public void readOnlyUserOnEnablePlansAndPricingOnShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = true;
		boolean showPlansAndPricing = true;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		LoginPage loginPage = startAsCompany("test1");
        assertTrue(loginPage.isPlansAndPricingAvailable());
	}
	
	@Test
	public void readOnlyUserOffEnablePlansAndPricingOnShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = false;
		boolean showPlansAndPricing = true;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		LoginPage loginPage = startAsCompany("test1");
        assertTrue(loginPage.isPlansAndPricingAvailable());
	}
	
	@Test
	public void readOnlyUserOffEnablePlansAndPricingOffShowsRequestAnAccount() throws Exception {
		boolean readOnlyUser = false;
		boolean showPlansAndPricing = false;
		setTenantFeatures(readOnlyUser, showPlansAndPricing);
		LoginPage loginPage = startAsCompany("test1");
		assertTrue(loginPage.isPlansAndPricingAvailable());
	}
	
	private void setTenantFeatures(boolean readOnlyUser, boolean showPlansAndPricing) {
        AdminLoginPage adminLoginPage = startAdmin();
        AdminOrgsListPage login = adminLoginPage.login();
        AdminOrgPage orgPage = login.clickEditOrganization("test1");
        orgPage.enterReadOnlyUser(readOnlyUser);
        orgPage.enterShowPlansAndPricing(showPlansAndPricing);
        orgPage.submitOrganizationChanges();
	}

}
