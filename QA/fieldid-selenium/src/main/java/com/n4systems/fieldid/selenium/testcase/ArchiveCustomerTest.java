package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;

public class ArchiveCustomerTest extends FieldIDTestCase {
	
	@Test
	public void testArchiveCustomerSuccess() throws Exception {
		ManageCustomersPage manageCustomersPage = startAsCompany("aacm").systemLogin().clickSetupLink().clickManageCustomers();
		
		//TODO: Remove when data setup is added
		unarchiveCustomerIfNecessary("AACM-Below the hook products", manageCustomersPage);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfResultsOnPage());
		manageCustomersPage.archiveCustomerNamed("AACM-Below the hook products", true);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(0, manageCustomersPage.getNumberOfResultsOnPage());
		
		manageCustomersPage.clickViewArchived();
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfResultsOnPage());
		
		manageCustomersPage.unarchiveCustomerNamed("AACM-Below the hook products");
	}
	
	@Test
	public void testArchiveCustomerCancelled() throws Exception {
		ManageCustomersPage manageCustomersPage = startAsCompany("aacm").systemLogin().clickSetupLink().clickManageCustomers();
		
		//TODO: Remove when data setup is added
		unarchiveCustomerIfNecessary("AACM-Below the hook products", manageCustomersPage);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfResultsOnPage());
		manageCustomersPage.archiveCustomerNamed("AACM-Below the hook products", false);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfResultsOnPage());
	}

	private void unarchiveCustomerIfNecessary(String customerName, ManageCustomersPage manageCustomersPage) {
		manageCustomersPage.clickViewArchived();
		manageCustomersPage.filterByName(customerName);
		if(manageCustomersPage.getNumberOfResultsOnPage() == 1) {
			manageCustomersPage.unarchiveCustomerNamed(customerName);
		} else {
			manageCustomersPage.clickViewAll();
		}
	}
	
}
