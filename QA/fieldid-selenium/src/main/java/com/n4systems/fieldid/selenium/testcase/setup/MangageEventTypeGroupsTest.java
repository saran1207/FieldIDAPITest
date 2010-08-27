package com.n4systems.fieldid.selenium.testcase.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.datatypes.EventTypeGroup;
import com.n4systems.fieldid.selenium.pages.setup.MangageEventTypeGroupsPage;

public class MangageEventTypeGroupsTest extends FieldIDTestCase {
	
	private MangageEventTypeGroupsPage manageEventTypeGroupsPage;
	
	@Before
	public void setUp() {
		manageEventTypeGroupsPage = start().systemLogin().clickSetupLink().clickManageEventTypeGroups();		
	}

	@Test
	public void testViewAllEventTypeGroups() throws Exception {
		assertEquals("View All", manageEventTypeGroupsPage.getCurrentTab());
	}

	@Test
	public void testViewEventTypeGroup() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItem();
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
		assertTrue(manageEventTypeGroupsPage.checkPageHeaderText(eventName));
	}

	@Test
	public void testEditEventTypeGroupFromList() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItemEdit();
		assertEquals("Edit", manageEventTypeGroupsPage.getCurrentTab());
		assertTrue(manageEventTypeGroupsPage.checkPageHeaderText(eventName));
	}

	@Test
	public void testEditEventTypeGroupFromView() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItem();
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickEditFromViewTab();
		assertEquals("Edit", manageEventTypeGroupsPage.getCurrentTab());
		assertTrue(manageEventTypeGroupsPage.checkPageHeaderText(eventName));
	}
	
	@Test
	public void testEditEventTypeGroupFromTab() throws Exception {
		String eventName = manageEventTypeGroupsPage.clickFirstListItem();
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickEditTab();
		assertEquals("Edit", manageEventTypeGroupsPage.getCurrentTab());
		assertTrue(manageEventTypeGroupsPage.checkPageHeaderText(eventName));
	}

	@Test
	public void testAddAndCancelEventTypeGroup() throws Exception {
		manageEventTypeGroupsPage.clickAddTab();
		assertEquals("Add", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickCancelButton();
		assertEquals("View All", manageEventTypeGroupsPage.getCurrentTab());
	}
	
	@Test
	public void testAddEventTypeWithErrors() throws Exception {
		manageEventTypeGroupsPage.clickAddTab();
		assertEquals("Add", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickSaveButton();
		assertEquals(2, manageEventTypeGroupsPage.getFormErrorMessages().size());
	}
	
	@Test
	public void testAddAndDeleteEventTypeGroup() throws Exception {
		deleteEventTypeIfExists("Test");
		
		manageEventTypeGroupsPage.clickAddTab();
		assertEquals("Add", manageEventTypeGroupsPage.getCurrentTab());
		EventTypeGroup eventTypeGroup = new EventTypeGroup("Test", "Test", "Basic Visual Inspection ", "Full Observation Report");
		manageEventTypeGroupsPage.setEventTypeGroupFormFields(eventTypeGroup);
		manageEventTypeGroupsPage.clickSaveButton();
		
		assertEquals("View", manageEventTypeGroupsPage.getCurrentTab());
		manageEventTypeGroupsPage.clickViewAllTab();
		assertTrue(manageEventTypeGroupsPage.listItemExists("Test"));
		manageEventTypeGroupsPage.deleteListItem("Test");
		assertFalse(manageEventTypeGroupsPage.listItemExists("Test"));
	}

	private void deleteEventTypeIfExists(String eventName) {
		if(manageEventTypeGroupsPage.listItemExists(eventName)){
			manageEventTypeGroupsPage.deleteListItem(eventName);			
		}
	}
}
