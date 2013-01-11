package com.n4systems.fieldid.selenium.testcase.setup;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageEventTypeGroupsPage;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Tenant;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManageEventTypeGroupsTest extends FieldIDTestCase {
	
	private ManageEventTypeGroupsPage manageEventTypeGroupsPage;
	
	@Before
	public void setUp() {
		manageEventTypeGroupsPage = startAsCompany("test1").systemLogin().clickSetupLink().clickManageEventTypeGroups();		
	}

	@Override
	public void setupScenario(Scenario scenario) {
        Tenant tenant = scenario.tenant("test1");
        
        scenario.anEventTypeGroup()
                .forTenant(tenant)
                .withName("EventTypeGroup1")
                .withReportTitle("EventTypeGroup1Report")
                .build();

	}

	@Test
	public void test_view_all_event_type_groups() throws Exception {
		assertEquals("View All", manageEventTypeGroupsPage.getCurrentTab());
	}

	@Test
	public void test_view_event_type_group() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItem();
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
	}

	@Test
	public void test_edit_event_type_group_from_list() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItemEdit();
		assertEquals("Edit", manageEventTypeGroupsPage.getCurrentTab());
	}

	@Test
	public void test_edit_event_type_group_from_view() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItem();
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickEditFromViewTab();
		assertEquals("Edit", manageEventTypeGroupsPage.getCurrentTab());
	}
	
	@Test
	public void test_edit_event_type_group_from_tab() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItem();
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickEditTab();
		assertEquals("Edit", manageEventTypeGroupsPage.getCurrentTab());
	}

	@Test
	public void test_add_and_cancel_event_type_group() throws Exception {
		manageEventTypeGroupsPage.clickAddTab();
		assertEquals("Add", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickCancelButton();
		assertEquals("View All", manageEventTypeGroupsPage.getCurrentTab());
	}
	
	@Test
	public void test_add_event_type_with_errors() throws Exception {
		manageEventTypeGroupsPage.clickAddTab();
		assertEquals("Add", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickSaveButton();
		assertEquals(2, manageEventTypeGroupsPage.getFormErrorMessages().size());
	}
	
	@Test
	public void test_add_event_type_group() throws Exception {
		manageEventTypeGroupsPage.clickAddTab();
		assertEquals("Add", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.setName("Test");
		manageEventTypeGroupsPage.setReportName("Test");
		manageEventTypeGroupsPage.setPdfReportStyle("Basic Inspection");
		manageEventTypeGroupsPage.setObservationGroupStyle("Full Observation Report");
		manageEventTypeGroupsPage.clickSaveButton();
		manageEventTypeGroupsPage.verifyEventTypeSaved();
	}	
	
	@Test
	public void test_archive_event_type_group() throws Exception {
		assertTrue(manageEventTypeGroupsPage.listItemExists("EventTypeGroup1"));
		manageEventTypeGroupsPage.archiveListItem("EventTypeGroup1", true);
        assertFalse(manageEventTypeGroupsPage.listItemExists("EventTypeGroup1"));
	}
}
