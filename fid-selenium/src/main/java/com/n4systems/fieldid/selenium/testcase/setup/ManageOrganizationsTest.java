package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageOrganizationsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;

public class ManageOrganizationsTest extends PageNavigatingTestCase<ManageOrganizationsPage> {

	private static final String SECONDARY_ORG1 = "SecondaryOrg1";
	private static final String SECONDARY_ORG2 = "SecondaryOrg2";
	private static String COMPANY = "test1";

    private static final String TEST_CUSTOMER_ORG1 = "CustomerOrg1";
	private static final String TEST_DIVISION_ORG = "DivisionOrg";
	private static final String ORG_USER = "User1";
	private static final String CUSTOMER_USER = "User2";
	private static final String DIVISION_USER = "User3";

	@Override
	public void setupScenario(Scenario scenario) {     
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(COMPANY);
		
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.ReadOnlyUser));

		SecondaryOrg secondaryOrg = (SecondaryOrg) scenario.aSecondaryOrg()
		  	    .withParent(primaryOrg)
		        .withName(SECONDARY_ORG1)
		        .build();
		       
        BaseOrg custOrg = scenario.aCustomerOrg()
								  .withParent(secondaryOrg)
				                  .withName(TEST_CUSTOMER_ORG1)
				                  .build();
        
        BaseOrg divisionOrg = scenario.aDivisionOrg()
        		.withParent(custOrg)
        		.withName(TEST_DIVISION_ORG)
        		.build();
        
        
        scenario.aUser()
                .withOwner(secondaryOrg)
                .withUserId(ORG_USER)
                .build();

        scenario.aUser()
                .withOwner(custOrg)
                .withUserId(CUSTOMER_USER)
                .build();

        scenario.aReadOnlyUser()
                .withOwner(divisionOrg)
                .withUserId(DIVISION_USER)
                .build();
     }

	
	@Override
	protected ManageOrganizationsPage navigateToPage() {
		return startAsCompany(COMPANY).systemLogin().clickSetupLink().clickManageOrganizations();
	}
	
	@Test
	public void add_organizational_unit_with_errors() throws Exception {
		page.clickAdd();
		page.clickSave();
		assertFalse(page.getFormErrorMessages().isEmpty());
	}

	@Test
	public void add_organizational_unit_sucessfully() throws Exception {
		page.clickAdd();
		page.enterOrganizationName(SECONDARY_ORG2);
		page.clickSave();
		assertTrue(page.getFormErrorMessages().isEmpty());
		assertTrue(page.getOrganizationNames().contains(SECONDARY_ORG2));
	}
	
	@Test
	public void archive_secondary_org_and_related_customers_divisions_and_users() throws Exception {
		page.clickArchiveOrganization(SECONDARY_ORG1);
		assertEquals(1, page.getCustomersToBeArchived());
		assertEquals(1, page.getDivisonsToBeArchived());
		assertEquals(3, page.getUsersToBeArchived());
		page.confirmArchive(true);
		assertTrue(page.getOrganizationNames().isEmpty());
	}
	
	@Test
	public void cancel_archive_secondary_org_and_related_customers_divisions_and_users() throws Exception {
		page.clickArchiveOrganization(SECONDARY_ORG1);
		assertEquals(1, page.getCustomersToBeArchived());
		assertEquals(1, page.getDivisonsToBeArchived());
		assertEquals(3, page.getUsersToBeArchived());
		page.confirmArchive(false);
		assertFalse(page.getOrganizationNames().isEmpty());
	}
	
	@Test
	public void edit_primary_org_with_errors() throws Exception {
		page.clickEditPrimaryOrganization();
		page.enterOrganizationName("");
		page.clickSave();
		assertFalse(page.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void edit_primary_org_succesfully() throws Exception {
		page.clickEditPrimaryOrganization();
		page.enterOrganizationName("NewName");
		page.clickSave();
		assertTrue(page.getFormErrorMessages().isEmpty());		
	}

	@Test
	public void edit_secondary_org_with_errors() throws Exception {
		page.clickEditOrganization(SECONDARY_ORG1);
		page.enterOrganizationName("");
		page.clickSave();
		assertFalse(page.getFormErrorMessages().isEmpty());		
	}
	
	@Test
	public void edit_secondary_org_succesfully() throws Exception {
		page.clickEditOrganization(SECONDARY_ORG1);
		page.enterOrganizationName(SECONDARY_ORG2);
		page.clickSave();
		assertTrue(page.getFormErrorMessages().isEmpty());		
	}
}
