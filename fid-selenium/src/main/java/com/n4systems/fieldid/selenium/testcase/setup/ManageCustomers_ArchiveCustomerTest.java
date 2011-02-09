package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;

public class ManageCustomers_ArchiveCustomerTest extends PageNavigatingTestCase<ManageCustomersPage> {
	
	private static final String TEST_USER_ID = "test123";
	private static String COMPANY = "test1";
    private static final String TEST_CUSTOMER_ORG1 = "CustomerOrg1";
    private static final String TEST_CUSTOMER_ORG2 = "CustomerOrg2";
	private static final String TEST_DIVISION_ORG = "DivisionOrg";
	private static final String TEST_USER = "User";
    
	@Override
	public void setupScenario(Scenario scenario) {     
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(COMPANY);
		
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.ReadOnlyUser));
		
        scenario.aCustomerOrg()
        		.withParent(scenario.primaryOrgFor(COMPANY))
        		.withName(TEST_CUSTOMER_ORG1)
        		.build();
        
        BaseOrg custOrg = scenario.aCustomerOrg()
								  .withParent(scenario.primaryOrgFor(COMPANY))
				                  .withName(TEST_CUSTOMER_ORG2)
				                  .build();
        
        BaseOrg divisionOrg = scenario.aDivisionOrg()
        		.withParent(custOrg)
        		.withName(TEST_DIVISION_ORG)
        		.build();
        
        scenario.aReadOnlyUser()
                .withOwner(divisionOrg)
                .withUserId(TEST_USER)
                .build();
     }

	@Override
	protected ManageCustomersPage navigateToPage() {
		return startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageCustomers();
	}

	
	@Test
	public void archiving_customer_should_remove_it_from_list() throws Exception {	
		page.filterByName(TEST_CUSTOMER_ORG1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
		page.archiveCustomerNamed(TEST_CUSTOMER_ORG1, true);
		
		page.filterByName(TEST_CUSTOMER_ORG1);
		assertEquals(0, page.getNumberOfCustomersOnPage());
		
		page.clickViewArchivedTab();
		
		page.filterByName(TEST_CUSTOMER_ORG1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
	}
	
	@Test
	public void cancelling_archiving_customer_should_not_archive_it() throws Exception {		
		page.filterByName(TEST_CUSTOMER_ORG1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
		page.archiveCustomerNamed(TEST_CUSTOMER_ORG1, false);
		
		page.filterByName(TEST_CUSTOMER_ORG1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
	}
	
	@Test
	public void unarchiving_a_customer_with_divisions_should_unarchive_divisions() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG2).clickDivisionsTab();
		assertTrue(page.getDivisionNames().contains(TEST_DIVISION_ORG));
		
		page.clickViewAllTab();
		page.archiveCustomerNamed(TEST_CUSTOMER_ORG2, true);
		page.clickViewArchivedTab();
		page.unarchiveCustomerNamed(TEST_CUSTOMER_ORG2);
		page.clickCustomer(TEST_CUSTOMER_ORG2).clickDivisionsTab();
		
		assertTrue(page.getDivisionNames().contains(TEST_DIVISION_ORG));
	}
	
	@Test
	public void unarchiving_a_customer_with_divisions_should_remove_users() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG2).clickUsersTab();
			
		assertTrue(page.getUserIds().contains(TEST_USER));
		
		page.clickViewAllTab().archiveCustomerNamed(TEST_CUSTOMER_ORG2, true);
		page.clickViewArchivedTab().unarchiveCustomerNamed(TEST_CUSTOMER_ORG2);
		page.clickCustomer(TEST_CUSTOMER_ORG2).clickUsersTab();
		
		assertFalse(page.getUserIds().contains(TEST_USER_ID));
	}

}
