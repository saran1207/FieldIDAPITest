package com.n4systems.fieldid.selenium.pages.setup;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.datatypes.Owner;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
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

	public ManageProductTypesPage clickCopyProductType(String productType) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position()=1 and .='"+productType+"']/../td[2]/a[.='Copy']");
		waitForPageToLoad();
		return this;
	}
	
	public ManageProductTypesPage clickProductType(String productType) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position()=1 and .='"+productType+"']/a");
		waitForPageToLoad();
		return this;
	}

	public ManageProductTypesPage clickInspectionTypesTab() {
		clickNavOption("Inspection Types");
		return this;
	}
	
	public ManageProductTypesPage clickInspectionFrequenciesTab() {
		clickNavOption("Inspection Frequencies");
		return this;
	}
	
	public ManageProductTypesPage clickSubComponentsTab() {
		clickNavOption("Sub-Components");
		return this;
	}
	
	public ManageProductTypesPage clickEditTab() {
		clickNavOption("Edit");
		return this;
	}
	
	public ManageProductTypesPage clickViewAllTab() {
		clickNavOption("View All");
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
	
	public void deleteProductType() {
		selenium.click("//form[@id='productTypeUpdate']//input[@type='button' and @value='Delete']");
		waitForPageToLoad();
		selenium.click("//input[@type='submit' and @value='Delete']");
		waitForPageToLoad();
	}

	public ManageProductTypesPage clickAddTab() {
		clickNavOption("Add");
		return this;
	}

	public void enterName(String name) {
		selenium.type("//form[@id='productTypeUpdate']//input[@name='name']", name);
	}
	
	public void enterWarnings(String warnings) {
		selenium.type("//form[@id='productTypeUpdate']//textarea[@name='warnings']", warnings);
	}
	
	public void enterInstructions(String instructions) {
		selenium.type("//form[@id='productTypeUpdate']//textarea[@name='instructions']", instructions);
	}
	
	public void enterCautionsUrl(String cautionsUrl) {
		selenium.type("//form[@id='productTypeUpdate']//input[@name='cautionsUrl']", cautionsUrl);
	}
	
	public void checkHasManufacturerCertificate() {
		selenium.check("//form[@id='productTypeUpdate']//input[@name='hasManufacturerCertificate']");
	}
	
	public void enterManufacturerCertificateText(String text) {
		selenium.type("//form[@id='productTypeUpdate']//textarea[@name='manufacturerCertificateText']", text);
	}
	
	public void enterProductDescriptionTemplate(String template) {
		selenium.type("//form[@id='productTypeUpdate']//input[@name='descriptionTemplate']", template);
	}

	public String getViewName() {
		return selenium.getText("//div[@class='viewSection']//label[.='Name']/../span");
	}
	
	public String getViewWarnings() {
		return selenium.getText("//div[@class='viewSection']//label[.='Warnings']/../span");
	}

	public String getViewInstructions() {
		return selenium.getText("//div[@class='viewSection']//label[.='Instructions']/../span");
	}

	public String getViewHasManufacturerCertificate() {
		return selenium.getText("//div[@class='viewSection']//label[.='Has Manufacturer Certificate']/../span");
	}

	public String getViewManufacturerCertificateText() {
		return selenium.getText("//div[@class='viewSection']//label[.='Manufacturer Certificate Text']/../span");
	}
	
	public String getViewProductDescriptionTemplate() {
		return selenium.getText("//div[@class='viewSection']//label[.='Product Description Template']/../span");
	}
	
	public String getEditName() {
		return selenium.getValue("//form[@id='productTypeUpdate']//input[@name='name']");
	}
	
	public String getEditWarnings() {
		return selenium.getValue("//form[@id='productTypeUpdate']//textarea[@name='warnings']");
	}
	
	public String getEditInstructions() {
		return selenium.getValue("//form[@id='productTypeUpdate']//textarea[@name='instructions']");
	}
	
	public String getEditCautionsUrl() {
		return selenium.getValue("//form[@id='productTypeUpdate']//input[@name='cautionsUrl']");
	}
	
	public boolean getEditHasManufacturerCertificate() {
		return selenium.isChecked("//form[@id='productTypeUpdate']//input[@name='hasManufacturerCertificate']");
	}
	
	public String getEditManufacturerCertificateText() {
		return selenium.getValue("//form[@id='productTypeUpdate']//textarea[@name='manufacturerCertificateText']");
	}
	
	public String getEditProductDescriptionTemplate() {
		return selenium.getValue("//form[@id='productTypeUpdate']//input[@name='descriptionTemplate']");
	}

	public List<String> getInspectionTypes() {
		return getColumnFromTableStartingAtRow("//form[@id='productTypeEventTypesSave']//table[@class='list']", 2, 2);
	}

	public void selectInspectionType(String inspectionType) {
		selenium.check("//form[@id='productTypeEventTypesSave']//table[@class='list']//td[position() = 2 and contains(.,'"+inspectionType+"')]//parent::tr/td[1]/input[@type='checkbox']");
	}

	public void saveInspectionTypes() {
		selenium.click("//form[@id='productTypeEventTypesSave']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}
	
	public void scheduleInspectionFrequencyForType(String inspectionType, int everyNDays) {
		selenium.click("//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]//a[.='Edit']");
		String frequencyInputXPath = "//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]//input[@type='text' and contains(@id, 'frequency')]";
		waitForElementToBePresent(frequencyInputXPath);
		selenium.type(frequencyInputXPath, everyNDays+"");
		selenium.click("//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]//a[.='Save']");
		waitForAjax();
	}
	
	public void removeInspectionFrequencyForType(String inspectionType) {
		selenium.click("//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]//a[.='Remove']");
		waitForAjax();
	}
	
	public boolean isInspectionFrequencyScheduledForType(String inspectionType) {
		return !selenium.isElementPresent("//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[position() = 2 and contains(., 'Not Scheduled')]");
	}
	
	public int getScheduledFrequencyForInspectionType(String inspectionType) {
		Pattern frequencyPattern = Pattern.compile("Schedule a .* every (\\d+) Days");
		String freqStr = selenium.getText("//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]//span[@class='frequency']");
		Matcher m = frequencyPattern.matcher(freqStr);
		if (!m.matches()) fail("Bad frequency pattern, could not parse: " + freqStr);
		return Integer.parseInt(m.group(1).trim());
	}

	public void addOverrideForOwner(String inspectionType, Owner owner, int frequency) {
		String cellXpath = "//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]"; 
		expandOverridesSectionIfNecessary(inspectionType);
		
		selenium.click(cellXpath + "//a[.='Add override']");
		new ConditionWaiter(new Predicate() {
			@Override
			public boolean evaluate() {
				return selenium.isVisible("//form[@id='orgForm']");
			}
		}).run("Organization form did not appear!");
		
		OrgPicker orgPicker = new OrgPicker(selenium);
		orgPicker.clickChooseOwner();
		orgPicker.setOwner(owner);
		orgPicker.clickSelectOwner();
		
		selenium.type("//input[@id='orgForm_frequency']", frequency+"");
		selenium.click("//span[@class='orgPickerFormActions']//a[.=' Save']");
		waitForPageToLoad();
	}

	private String expandOverridesSectionIfNecessary(String inspectionType) {
		String cellXpath = "//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]";
		if (selenium.isElementPresent(cellXpath + "//a[starts-with(@id, 'overrideExpand')]//img[@alt='[+]']")) {
			selenium.click(cellXpath + "//a[starts-with(@id, 'overrideExpand')]//img[@alt='[+]']");
		}
		waitForElementToBePresent(cellXpath + "//a[.='Add override']");
		return cellXpath;
	}
	
	public List<InspectionFrequencyOverride> getInspectionFrequencyOverrides(String inspectionType) {
		expandOverridesSectionIfNecessary(inspectionType);
		
		List<InspectionFrequencyOverride> overrides = new ArrayList<InspectionFrequencyOverride>();
		
		String overrideXpath = "//table[@id='inspectionListTable']//td[position()=1 and contains(.,'"+inspectionType+"')]//parent::tr/td[2]"
			+"//div[contains(@class, 'customerOverride')]";
		int countOverrides = selenium.getXpathCount(overrideXpath).intValue();
		for (int i = 1; i <= countOverrides; i++) {
			InspectionFrequencyOverride override = new InspectionFrequencyOverride();
			override.customer = selenium.getText("xpath=("+overrideXpath+")["+i+"]/span[@class='customer']/b").trim();
			override.frequency = Integer.parseInt(selenium.getText("xpath=("+overrideXpath+")["+i+"]/span[@class='frequency']/b[2]").trim());
			overrides.add(override);
		}
		return overrides;
	}
	
	public static class InspectionFrequencyOverride {
		public String customer;
		public int frequency;
	}

	public List<String> getSubComponents() {
		List<String> subComponents = new ArrayList<String>();
		
		int numSubComponents = selenium.getXpathCount("//ul[@id='subProducts']/li").intValue();
		for (int i = 1; i <= numSubComponents; i++) {
			String subComponent = selenium.getText("//ul[@id='subProducts']/li["+i+"]/span[starts-with(@id, 'productName_')]");
			subComponents.add(subComponent);
		}
		
		return subComponents;
	}
	
	public void addSubComponent(String subcomponentName) {
		selenium.select("//select[@id='addSubProduct']", subcomponentName);
		selenium.click("//button[@id='addSubProductButton']");
	}
	
	public void saveSubComponents() {
		selenium.click("//input[@id='productTypeConfigurationUpdate_label_save']");
		waitForPageToLoad();
	}

	public void removeSubComponent(String componentName) {
		selenium.click("//ul[@id='subProducts']//span[.='"+componentName+"']/../a[.='Remove']");
	}

}
