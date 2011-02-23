package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

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

	public int addNewButtonGroup(String groupName) {
		selenium.click("//button[@id='addGroup']");
        waitForAjax();
        int newButtonGroupRow = getNumButtonGroups();
        selenium.type("xpath=(//table[@id='buttonGroups']//tr)[" + newButtonGroupRow + "]//input[@name='name']", groupName);
        return newButtonGroupRow;
	}

	public void clickSave() {
		selenium.click("//button[@id='save']");
		selenium.getConfirmation();
		waitForAjax();
	}

	public int addAButton(int rowIndex, String name) {
        int newButtonIndex = getNumButtonsInRow(rowIndex) + 1;
        selenium.click("xpath=(//table[@id='buttonGroups']//tr)[" + rowIndex + "]//ul[@class='buttonGroup']//li[" + (newButtonIndex + 1) + "]//a[.='Add']");
        selenium.type("xpath=(//table[@id='buttonGroups']//tr)[" + rowIndex + "]//ul[@class='buttonGroup']//li[" + (newButtonIndex + 1) + "]//input[contains(@id,'_displayText')]", name);
        return newButtonIndex;
	}

	public void renameGroup(int groupIndex, String newName) {
        selenium.type("xpath=(//table[@id='buttonGroups']//tr)[" + groupIndex + "]//input[@name='name']", newName);
	}

	public void retireButton(int groupIndex, int buttonIndex) {
        selenium.click("xpath=(//table[@id='buttonGroups']//tr)[" + groupIndex + "]//ul[@class='buttonGroup']//li[" + (buttonIndex + 1) + "]//a[.='Retire']");
	}

	public void changeButtonIndicationToFail(int groupIndex, int buttonIndex) {
        selenium.select("xpath=(//table[@id='buttonGroups']//tr)[" + groupIndex + "]//ul[@class='buttonGroup']//li[" + (buttonIndex + 1) + "]//select", "Fail");
	}

    public String getIndicationFor(int groupIndex, int buttonIndex) {
        return selenium.getText("xpath=(//table[@id='buttonGroups']//tr)[" + groupIndex + "]//ul[@class='buttonGroup']//li[" + (buttonIndex + 1) + "]//div[3]").trim();
    }

    private int getNumButtonGroups() {
        return selenium.getXpathCount("//table[@id='buttonGroups']//tr[starts-with(@id,'stateRow')]").intValue();
    }

    private int getNumButtonsInRow(int rowNum) {
        int numListElements = selenium.getXpathCount("(//table[@id='buttonGroups']/tbody/tr)["+rowNum+"]//ul[@class='buttonGroup']//li").intValue();
        int numAddLinks = selenium.getXpathCount("(//table[@id='buttonGroups']/tbody/tr)["+rowNum+"]//ul[@class='buttonGroup']//li//span[not(contains(@style,'none'))]//a[.='Add']").intValue();
        System.out.println("List elements: " + numListElements + " add linx: " + numAddLinks);
        // The number of available slots is the number of add links.
        return (numListElements - numAddLinks) - 1;
    }

    public List<String> getButtonGroupNames() {
        List<String> buttonGroupNames = new ArrayList<String>();
        int numButtonGroups = getNumButtonGroups();
        for (int i = 1; i <= numButtonGroups; i++) {
            buttonGroupNames.add(selenium.getAttribute("xpath=(//table[@id='buttonGroups']//tr)[" + i + "]//input[@name='name']/@value"));
        }
        return buttonGroupNames;
    }

    public List<String> getButtonNamesFromGroup(int groupIndex) {
        ArrayList<String> buttonNames = new ArrayList<String>();

        int numButtons = getNumButtonsInRow(groupIndex);
        for (int i = 1; i <= numButtons; i++) {
            if (selenium.isVisible("xpath=(//table[@id='buttonGroups']//tr)[" + i + "]//ul[@class='buttonGroup']//li["+(i+1)+"]/div[2]")) {
                buttonNames.add(selenium.getText("xpath=(//table[@id='buttonGroups']//tr)[" + i + "]//ul[@class='buttonGroup']//li["+(i+1)+"]/div[2]").trim());
            }
        }

        return buttonNames;
    }

}
