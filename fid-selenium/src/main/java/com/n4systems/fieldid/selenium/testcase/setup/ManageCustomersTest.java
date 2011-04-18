package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageCustomersPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventBook;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;

public class ManageCustomersTest extends PageNavigatingTestCase<ManageCustomersPage> {
	
	private static final String TEST_USER_ID = "test123";
	private static String COMPANY = "test1";
    private static final String TEST_CUSTOMER_ORG_1 = "CustomerOrg1";
    private static final String TEST_CUSTOMER_ORG_2 = "CustomerOrg2";
    private static final String TEST_CUSTOMER_ORG_TO_MERGE_INTO = "CustomerOrg3";
	private static final String TEST_DIVISION_ORG_1 = "DivisionOrg1";
	private static final String TEST_DIVISION_ORG_2 = "DivisionOrg2";
	private static final String TEST_USER = "User";
	private static final String TRANSFERRED_USER_ID="TransUser";
	private static final String ASSET_TRANSFER_SERIAL="Asset To Transfer";
    
	@Override
	public void setupScenario(Scenario scenario) {     
		PrimaryOrg primaryOrg = scenario.primaryOrgFor(COMPANY);
		
		primaryOrg.setExtendedFeatures(setOf(ExtendedFeature.ReadOnlyUser, ExtendedFeature.Projects));
		
        scenario.aCustomerOrg()
        		.withParent(scenario.primaryOrgFor(COMPANY))
        		.withName(TEST_CUSTOMER_ORG_1)
        		.build();
        
        BaseOrg custOrg = scenario.aCustomerOrg()
								  .withParent(scenario.primaryOrgFor(COMPANY))
				                  .withName(TEST_CUSTOMER_ORG_2)
				                  .build();
       
        BaseOrg custOrgToMergeInto = scenario.aCustomerOrg()
        					.withParent(scenario.primaryOrgFor(COMPANY))
        					.withName(TEST_CUSTOMER_ORG_TO_MERGE_INTO)
        					.build();
        
        BaseOrg divisionOrg = scenario.aDivisionOrg()
        		.withParent(custOrg)
        		.withName(TEST_DIVISION_ORG_1)
        		.build();
        
        User transferredUser = scenario.aUser()
        								.withUserId(TRANSFERRED_USER_ID)
					        			.withOwner(custOrg)
					        			.build();
        
        EventBook eventBook = scenario.anEventBook()
        							.withOwner(custOrg)
        							.withName("Some Book")
        							.build();
        
        AssetType type = scenario.anAssetType()
        				.named("Gantry Crane - Cab Controlled")
        				.build();
        
        Project eventJob= scenario.aJob().
        					withProjectID("someid")
        					.withTitle("hurf")
        					.status("")
        					.withOwner(custOrg)
        					.build();
        
        AssetBuilder builder = scenario
        						.anAsset().withOwner(custOrg)
        						.ofType(type);
        
        Asset transferredAsset =  builder.withSerialNumber(ASSET_TRANSFER_SERIAL)
        								.withOwner(custOrg)
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
	public void filter_customer_by_name() throws Exception {
		page.filterByName(TEST_CUSTOMER_ORG_1);
		assertEquals(1, page.getNumberOfCustomersOnPage());		
		assertTrue(page.getOrgNames().contains(TEST_CUSTOMER_ORG_1));

		page.filterByName("test");
		assertEquals(0, page.getNumberOfCustomersOnPage());		
		assertFalse(page.getOrgNames().contains("test"));

	}

	@Test
	public void add_customer_test() throws Exception {
		page.clickAdd();
		page.enterCustomerID("Customer3");
		page.selectOrganizationalUnit(COMPANY);
		page.enterCustomerName("Customer3");
		page.clickSave();
		
		assertEquals("Data Saved.", page.getActionMessages().get(0));	
	}
	
	@Test
	public void add_division_to_customer_with_errors() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_1).clickDivisionsTab().clickAddDivision();
		page.clickSave();
		assertFalse(page.getFormErrorMessages().isEmpty());
	}

	@Test
	public void add_division_to_customer_sucessfully() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_1).clickDivisionsTab().clickAddDivision();
		page.enterDivisionId(TEST_DIVISION_ORG_2);
		page.enterDivisionName(TEST_DIVISION_ORG_2);
		page.clickSave();
		assertTrue(page.getFormErrorMessages().isEmpty());		
	}
	
	@Test
	public void archive_division_and_associated_users() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickDivisionsTab();
		page.archiveDivision(TEST_DIVISION_ORG_1);
		assertEquals(0, page.getNumberOfDivisionsOnPage());
		
	}
	
	@Test
	public void add_customer_user_sucessfully() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickUsersTab();
		assertEquals(2, page.getNumberOfUsersOnPage());
		page.clickAddUser();
		page.enterUserEmailAddress("test@test.com");
		page.enterUserFirstName("firstname");
		page.enterUserLastName("lastName");
		page.enterUserUserID("userid");
		page.enterAndConfirmUserPassword("password");
		page.clickSave();
		assertEquals(3, page.getNumberOfUsersOnPage());
	}

	@Test
	public void add_customer_user_with_errors() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickUsersTab();
		assertEquals(2, page.getNumberOfUsersOnPage());
		page.clickAddUser();
		page.clickSave();
		assertFalse(page.getFormErrorMessages().isEmpty());
	}
	
	@Test
	public void archiving_customer_should_remove_it_from_list() throws Exception {	
		page.filterByName(TEST_CUSTOMER_ORG_1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
		page.archiveCustomerNamed(TEST_CUSTOMER_ORG_1, true);
		
		page.filterByName(TEST_CUSTOMER_ORG_1);
		assertEquals(0, page.getNumberOfCustomersOnPage());
		
		page.clickViewArchivedTab();
		
		page.filterByName(TEST_CUSTOMER_ORG_1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
	}
	
	@Test
	public void cancelling_archiving_customer_should_not_archive_it() throws Exception {		
		page.filterByName(TEST_CUSTOMER_ORG_1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
		page.archiveCustomerNamed(TEST_CUSTOMER_ORG_1, false);
		
		page.filterByName(TEST_CUSTOMER_ORG_1);
		assertEquals(1, page.getNumberOfCustomersOnPage());
	}
	
	@Test
	public void unarchiving_a_customer_with_divisions_should_unarchive_divisions() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickDivisionsTab();
		assertTrue(page.getOrgNames().contains(TEST_DIVISION_ORG_1));
		
		page.clickViewAllTab();
		page.archiveCustomerNamed(TEST_CUSTOMER_ORG_2, true);
		page.clickViewArchivedTab();
		page.unarchiveCustomerNamed(TEST_CUSTOMER_ORG_2);
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickDivisionsTab();
		
		assertTrue(page.getOrgNames().contains(TEST_DIVISION_ORG_1));
	}
	
	@Test
	public void unarchiving_a_customer_with_divisions_should_remove_users() throws Exception {
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickUsersTab();
			
		assertTrue(page.getUserIds().contains(TEST_USER));
		
		page.clickViewAllTab().archiveCustomerNamed(TEST_CUSTOMER_ORG_2, true);
		page.clickViewArchivedTab().unarchiveCustomerNamed(TEST_CUSTOMER_ORG_2);
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickUsersTab();
		
		assertFalse(page.getUserIds().contains(TEST_USER_ID));
	}
	
	@Test
	public void merging_two_customers_transfers_assets_to_the_winner(){
		page.clickCustomer(TEST_CUSTOMER_ORG_1).clickViewTab().clickMerge().mergeWithCustomer(TEST_CUSTOMER_ORG_TO_MERGE_INTO);
		
		page.search(ASSET_TRANSFER_SERIAL);
		assertTrue("Ownership of the asset wasn't successfully changed!", selenium.isElementPresent("//span[contains(., "+TEST_CUSTOMER_ORG_TO_MERGE_INTO+")]"));
		
	}
	
	@Test
	public void merging_two_customers_archives_the_loser(){
		page.clickCustomer(TEST_CUSTOMER_ORG_1).clickViewTab().clickMerge().mergeWithCustomer(TEST_CUSTOMER_ORG_TO_MERGE_INTO);
		page.clickSetupLink().clickManageCustomers().clickViewArchivedTab();
		
		assertTrue("Loser wasn't successfully archived.",selenium.isElementPresent("//td[contains(.,"+TEST_CUSTOMER_ORG_1+")]"));
	}
	
	@Test
	public void merging_two_customers_transfers_users_to_the_winner(){
		page.clickCustomer(TEST_CUSTOMER_ORG_2).clickViewTab().clickMerge().mergeWithCustomer(TEST_CUSTOMER_ORG_TO_MERGE_INTO);
		page.clickSetupLink().clickManageCustomers().clickCustomer(TEST_CUSTOMER_ORG_TO_MERGE_INTO).clickUsersTab();
		assertTrue("Ownership of the User wasn't successfully changed", selenium.isElementPresent("//a[contains(.,'"+TRANSFERRED_USER_ID+"')]"));
	}
	
	@Test
	public void merging_two_customers_transfers_event_books_to_winner(){
		page.clickCustomer(TEST_CUSTOMER_ORG_1).clickViewTab().clickMerge().mergeWithCustomer(TEST_CUSTOMER_ORG_TO_MERGE_INTO);
		page.clickSetupLink().clickManageEventBooks();
		
		assertTrue("Event Book wasn't successfully copied over.",  selenium.isElementPresent("//td[contains(.,"+TEST_CUSTOMER_ORG_TO_MERGE_INTO+")]"));
	}
	
	@Test
	public void merging_two_customers_transfers_jobs_to_winner(){
		page.clickCustomer(TEST_CUSTOMER_ORG_1).clickViewTab().clickMerge().mergeWithCustomer(TEST_CUSTOMER_ORG_TO_MERGE_INTO);
		page.clickJobsLink();
		
		assertTrue("Job wasn't successfully copied over.",  selenium.isElementPresent("//td[contains(.,"+TEST_CUSTOMER_ORG_1+")]"));
	}
}
