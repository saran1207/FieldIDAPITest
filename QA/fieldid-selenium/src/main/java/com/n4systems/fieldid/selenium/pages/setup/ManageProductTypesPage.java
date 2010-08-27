package com.n4systems.fieldid.selenium.pages.setup;

import java.util.List;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class ManageProductTypesPage extends FieldIDPage {

	public ManageProductTypesPage(Selenium selenium) {
		super(selenium);
	}
	
	public List<String> getProductTypes() {
		return getColumnFromTableStartingAtRow("//div[@id='pageContent']//table[@class='list']", 1, 2);
	}
	
	public ManageProductTypesPage clickEditProductType(String productType) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position()=1 and .='"+productType+"']/../td[2]/a[.='Edit']");
		waitForPageToLoad();
		return this;
	}
	
	public ManageProductTypesPage clickEditTab() {
		clickNavOption("Edit");
		return this;
	}

	public void selectProductTypeGroup(String groupName) {
		selenium.select("//form[@id='productTypeUpdate']//select[@id='productTypeUpdate_group']", groupName);
	}
	
	public String getEditProductTypeGroup() {
		return selenium.getSelectedLabel("//form[@id='productTypeUpdate']//select[@id='productTypeUpdate_group']");
	}
	
	public void saveProductType() {
		selenium.click("//form[@id='productTypeUpdate']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}
	
}
