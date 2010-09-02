package com.n4systems.fieldid.selenium.pages.setup;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageCommentTemplatesPage extends FieldIDPage {
	
	public static final String FIELD_TEMPLATE_NAME = "commentTemplateEdit_name";
	public static final String FIELD_TEMPLATE_COMMENT = "commentTemplateEdit_comment";

	public ManageCommentTemplatesPage(Selenium selenium) {
		super(selenium);
	}
	
	public void clickAddTab() {
		clickNavOption("Add");
	}
	
	public void enterTemplateName(String name) {
		selenium.type("//form[@id='commentTemplateEdit']//input[@name='name']", name);
	}
	
	public void enterTemplateComment(String comment) {
		selenium.type("//form[@id='commentTemplateEdit']//textarea[@name='comment']", comment);
	}
	
	public String getTemplateName() {
		return selenium.getValue("//form[@id='commentTemplateEdit']//input[@name='name']");
	}
	
	public String getTemplateComment() {
		return selenium.getValue("//form[@id='commentTemplateEdit']//textarea[@name='comment']");
	}
	
	public void clickSaveButton() {
		selenium.click("//form[@id='commentTemplateEdit']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}
	
	public void clickCancel() {
		selenium.click("//form[@id='commentTemplateEdit']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}
	
	public List<String> getCommentTemplateNames() {
		List<String> templateNames = new ArrayList<String>();
		int numRows = selenium.getXpathCount("//div[@id='pageContent']//table[@class='list']//tr").intValue();
		for (int i = 3; i <= numRows; i++) {
			templateNames.add(selenium.getText("//div[@id='pageContent']//table[@class='list']//tr["+i+"]/td[1]"));
		}
		return templateNames;
	}

	public void removeTemplateNamed(String templateName) {
		selenium.chooseOkOnNextConfirmation();
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position() = 1 and text() = '"+templateName+"']/parent::tr/td[2]/a[.='Remove']");
		selenium.getConfirmation();
		waitForPageToLoad();
	}

	public void clickEditOnTemplate(String templateName) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position() = 1 and text() = '"+templateName+"']/parent::tr/td[2]/a[.='Edit']");
		waitForPageToLoad();
	}

}
