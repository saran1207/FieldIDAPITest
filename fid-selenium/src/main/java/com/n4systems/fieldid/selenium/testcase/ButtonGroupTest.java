package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.ButtonGroupPage;

public class ButtonGroupTest extends FieldIDTestCase {

	ButtonGroupPage buttonGroupPage;
	String BUTTON_NAME = "New Button";
	String NEW_NAME = "New Name";

	@Before
	public void setup() {
		buttonGroupPage = startAsCompany("test1").systemLogin().clickSetupLink().clickManageEventTypes().clickButtonGroups();
	}

	@Test
	public void can_add_new_button_group() {
		addNewGroup();
		buttonGroupPage.clickSave();
		assertTrue("Couldn't add new group", selenium.isElementPresent("//form[@id='stateSet_0_form']"));
	}

	@Test
	public void can_rename_button_group() {
		NEW_NAME = "New Group Name";
		addNewGroup();
		buttonGroupPage.renameFirstGroup(NEW_NAME);
		buttonGroupPage.clickSave();
		assertEquals(NEW_NAME, selenium.getValue("//input[@id='stateSet_0_form_name']"));
	}

	@Test
	public void can_retire_a_button() {
		NEW_NAME = "New Button";
		addNewGroup();
		buttonGroupPage.addAButton(1, NEW_NAME);
		buttonGroupPage.clickSave();
		buttonGroupPage.retireFirstButton();
		buttonGroupPage.clickSave();
		assertTrue("Button wasn't retired properly", selenium.isElementPresent("//li[@id='buttonState__0_0']/div[2][.='" + NEW_NAME + "']"));
	}

	@Test
	public void can_change_button_indication() {
		addNewGroup();
		buttonGroupPage.changeButtonIndicationToFail(0);
		buttonGroupPage.clickSave();
		assertTrue("Button indication wasn't changed", selenium.isElementPresent("//li[@id='buttonState__0_0']/div[3][contains(text(), 'Fail')]"));
	}

	@Test
	public void should_not_allow_blank_name() {
		addNewGroup();
		buttonGroupPage.renameFirstGroup("");
		buttonGroupPage.clickSave();
		assertTrue("Error message did not appear.", selenium.isElementPresent("//div[@id='formErrors']/div/ul/li[1]/span[contains(text(), 'Name is a required field.')]"));
	}

	@Test
	public void should_not_allow_blank_button_label() {
		addNewGroup();
		buttonGroupPage.addAButton(0, "");
		buttonGroupPage.clickSave();
		assertTrue("Error message did not appear.", selenium.isElementPresent("//div[@id='formErrors']/div/ul/li[1]/span[contains(text(), 'All buttons require a label.')]"));
	}

	@Test
	public void should_not_allow_empty_group() {
		addNewGroup();
		buttonGroupPage.clickSave();
		buttonGroupPage.retireFirstButton();
		buttonGroupPage.clickSave();
		assertTrue("Error message did not appear.", selenium.isElementPresent("//div[@id='formErrors']/div/ul/li[1]/span[contains(text(), 'There must be at least one button in the button group.')]"));
	}

	private void addNewGroup() {
		buttonGroupPage.addNewButtonGroup();
		buttonGroupPage.addAButton(0, BUTTON_NAME);
	}
}
