package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.ButtonGroupPage;

public class ButtonGroupTest extends PageNavigatingTestCase<ButtonGroupPage> {

    @Override
    protected ButtonGroupPage navigateToPage() {
        return startAsCompany("test1").systemLogin().clickSetupLink().clickManageEventTypes().clickButtonGroups();
    }

    @Test
	public void can_add_new_button_group() {
		addNewGroupAndButton("Test Group", "New Button");
		page.clickSave();
        List<String> buttonGroupNames = page.getButtonGroupNames();
        assertTrue("Should find newly added group in list", buttonGroupNames.contains("Test Group"));
	}

	@Test
	public void can_rename_button_group() {
		String groupName = "Test Group";
		int addedGroupNumber = addNewGroupAndButton(groupName, "New Button");
		page.renameGroup(addedGroupNumber, "New Test Group");
		page.clickSave();
        List<String> buttonGroupNames = page.getButtonGroupNames();
        assertTrue("Should find newly renamed group in list", buttonGroupNames.contains("New Test Group"));
        assertFalse("Should not find old named group in list", buttonGroupNames.contains("Test Group"));
	}

	@Test
	public void can_retire_a_button() {
        String buttonName = "New Button";
		int newGroupIndex = addNewGroupAndButton("New Group", buttonName);
		int newButtonIndex = page.addAButton(newGroupIndex, "Another new button");
		page.clickSave();

        assertTrue(page.getButtonNamesFromGroup(newGroupIndex).contains("Another new button"));
        page.retireButton(newGroupIndex, newButtonIndex);
		page.clickSave();
        assertFalse(page.getButtonNamesFromGroup(newGroupIndex).contains("Another new button"));
	}

	@Test
	public void can_change_button_indication() {
		int newGroupIndex = addNewGroupAndButton("New Group", "New Button");
		page.changeButtonIndicationToFail(newGroupIndex, 1);
		page.clickSave();
		assertEquals("Button indication wasn't changed", "Fail", page.getIndicationFor(newGroupIndex, 1));
	}

	@Test
	public void should_not_allow_blank_name() {
		int addedGroupIndex = addNewGroupAndButton("New Group", "New Button");
		page.renameGroup(addedGroupIndex, "");
		page.clickSave();

        assertTrue("Error message did not appear.", page.getFormErrorMessages().contains("Name is a required field."));
	}

	@Test
	public void should_not_allow_blank_button_label() {
		int addedGroupIndex = addNewGroupAndButton("New Group", "New Button");
		page.addAButton(addedGroupIndex, "");
		page.clickSave();

        List<String> formErrorMessages = page.getFormErrorMessages();
        assertTrue("Should have a form error message!", formErrorMessages.size() > 0);
        assertTrue("Should contain the correct error", formErrorMessages.contains("All buttons require a label."));
	}

	@Test
	public void should_not_allow_empty_group() {
		int newGroupIndex = addNewGroupAndButton("New Group", "New Button");
		page.clickSave();
		page.retireButton(newGroupIndex, 1);
		page.clickSave();
        assertTrue("Should contain the correct error", page.getFormErrorMessages().contains("There must be at least one button in the button group."));
	}

	private int addNewGroupAndButton(String groupName, String buttonName) {
		int newButtonGroupRow = page.addNewButtonGroup(groupName);
		page.addAButton(newButtonGroupRow, buttonName);
        return newButtonGroupRow;
	}
}
