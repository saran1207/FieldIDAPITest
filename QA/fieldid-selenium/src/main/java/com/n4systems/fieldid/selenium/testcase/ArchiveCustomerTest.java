package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;

public class ArchiveCustomerTest extends FieldIDTestCase {
	
	private static final String TEST_USER_ID = "test123";
	
	@Test
	public void archiving_customer_should_remove_it_from_list() throws Exception {
		ManageCustomersPage manageCustomersPage = startAsCompany("aacm").systemLogin().clickSetupLink().clickManageCustomers();
		
		//TODO: Remove when data setup is added
		unarchiveCustomerIfNecessary("AACM-Below the hook products", manageCustomersPage);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfCustomersOnPage());
		manageCustomersPage.archiveCustomerNamed("AACM-Below the hook products", true);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(0, manageCustomersPage.getNumberOfCustomersOnPage());
		
		manageCustomersPage.clickViewArchivedTab();
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfCustomersOnPage());
		
		manageCustomersPage.unarchiveCustomerNamed("AACM-Below the hook products");
	}
	
	@Test
	public void cancelling_archiving_customer_should_not_archive_it() throws Exception {
		ManageCustomersPage manageCustomersPage = startAsCompany("aacm").systemLogin().clickSetupLink().clickManageCustomers();
		
		//TODO: Remove when data setup is added
		unarchiveCustomerIfNecessary("AACM-Below the hook products", manageCustomersPage);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfCustomersOnPage());
		manageCustomersPage.archiveCustomerNamed("AACM-Below the hook products", false);
		
		manageCustomersPage.filterByName("Below the hook");
		assertEquals(1, manageCustomersPage.getNumberOfCustomersOnPage());
	}
	
	@Test
	public void unarchiving_a_customer_with_divisions_should_unarchive_divisions() throws Exception {
		ManageCustomersPage manageCustomersPage = startAsCompany("aacm").systemLogin().clickSetupLink().clickManageCustomers();
		manageCustomersPage.clickCustomer("AACM-TEST").clickDivisionsTab();
		List<String> divisionNames = manageCustomersPage.getDivisionNames();
		assertEquals(2, divisionNames.size());
		divisionNames.containsAll(Arrays.asList("TestDivision", "TestDivision2"));
		
		manageCustomersPage.clickViewAllTab();
		manageCustomersPage.archiveCustomerNamed("AACM-TEST", true);
		manageCustomersPage.clickViewArchivedTab();
		manageCustomersPage.unarchiveCustomerNamed("AACM-TEST");
		manageCustomersPage.clickCustomer("AACM-TEST").clickDivisionsTab();
		
		divisionNames = manageCustomersPage.getDivisionNames();
		assertEquals(2, divisionNames.size());
		divisionNames.containsAll(Arrays.asList("TestDivision", "TestDivision2"));
	}
	
	@Test
	public void unarchiving_a_customer_with_divisions_should_remove_users() throws Exception {
		ManageCustomersPage manageCustomersPage = startAsCompany("aacm").systemLogin().clickSetupLink().clickManageCustomers();
		manageCustomersPage.clickCustomer("AACM-TEST").clickUsersTab();
		
		removeUserIfNecessary(manageCustomersPage, TEST_USER_ID);
		
		manageCustomersPage.clickAddUser();
		
		manageCustomersPage.enterUserFirstName("Testy");
		manageCustomersPage.enterUserLastName("McTest");
		manageCustomersPage.enterUserEmailAddress("at@dot.com");
		manageCustomersPage.enterAndConfirmUserPassword("makemore$");
		manageCustomersPage.enterUserUserID(TEST_USER_ID);
		manageCustomersPage.clickSaveUser();
		
		manageCustomersPage.clickViewAllTab().archiveCustomerNamed("AACM-TEST", true);
		manageCustomersPage.clickViewArchivedTab().unarchiveCustomerNamed("AACM-TEST");
		manageCustomersPage.clickCustomer("AACM-TEST").clickUsersTab();
		
		assertFalse(manageCustomersPage.getUserIds().contains(TEST_USER_ID));
	}

	private void removeUserIfNecessary(ManageCustomersPage manageCustomersPage, String userId) {
		if (manageCustomersPage.getUserIds().contains(userId)) {
			manageCustomersPage.clickDeleteUser(userId);
		}
		
		assertFalse(manageCustomersPage.getUserIds().contains(userId));
	}

	private void unarchiveCustomerIfNecessary(String customerName, ManageCustomersPage manageCustomersPage) {
		manageCustomersPage.clickViewArchivedTab();
		manageCustomersPage.filterByName(customerName);
		if(manageCustomersPage.getNumberOfCustomersOnPage() == 1) {
			manageCustomersPage.unarchiveCustomerNamed(customerName);
		} else {
			manageCustomersPage.clickViewAllTab();
		}
	}
	
}
