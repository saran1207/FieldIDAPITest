package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminLoginPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgPage;
import com.n4systems.fieldid.selenium.pages.admin.AdminOrgsListPage;

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
		assertFalse(loginPage.isPlansAndPricingAvailable());
	}
	
	private void setTenantFeatures(boolean readOnlyUser, boolean showPlansAndPricing) {
        AdminLoginPage adminLoginPage = startAdmin();
        AdminOrgsListPage login = adminLoginPage.login();
        AdminOrgPage orgPage = login.filterByCompanyName("test1").clickEditOrganization("test1");
        orgPage.enterShowPlansAndPricing(showPlansAndPricing);
        if(readOnlyUser) {
        	orgPage.enterTenantLimits(-1, -1, -1);
        }
	}

}
