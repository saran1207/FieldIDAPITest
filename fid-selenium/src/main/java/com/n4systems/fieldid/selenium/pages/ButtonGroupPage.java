package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

public class ButtonGroupPage extends FieldIDPage {

	public ButtonGroupPage(Selenium selenium) {
		super(selenium);
		if (!checkOnButtonGroupsPage()) {
			fail("Expected to be on Button Groups Page!");
		}
	}

	public boolean checkOnButtonGroupsPage() {
		return selenium.isElementPresent("//p[@class='instructionsBox']");
	}

	public void addNewButtonGroup() {
		selenium.click("//button[@id='addGroup']");
		waitForAjax();
	}

	public void clickSave() {
		selenium.click("//button[@id='save']");
		selenium.getConfirmation();
		waitForAjax();
	}

	public void addAButton(int buttonIndex, String name) {
		selenium.click("//span[@id='buttonState__0_" + buttonIndex + "_add']/a");
		selenium.type("//input[@id='stateSet_0_form_states_" + buttonIndex + "__displayText']", name);
	}

	public void renameFirstGroup(String newName) {
		selenium.type("//input[@id='stateSet_0_form_name']", newName);
	}

	public void retireFirstButton() {
		selenium.click("//span[@id='buttonState__0_0_retire']/a");
	}

	public void changeButtonIndicationToFail(int index) {
		selenium.select("//select[@id='stateSet_0_form_states_" + index + "__statusName']", "Fail");
	}

}
