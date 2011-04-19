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

public class ManageAssetTypesPage extends FieldIDPage {

	public ManageAssetTypesPage(Selenium selenium) {
		super(selenium);
	}
	
	public ManageAssetTypesPage(Selenium selenium, boolean waitForPageToLoad) {
		super(selenium,waitForPageToLoad);
	}
	
	public List<String> getAssetTypes() {
		return getColumnFromTableStartingAtRow("//div[@id='pageContent']//table[@class='list']", 1, 2);
	}
	
	public ManageAssetTypesPage clickEditAssetType(String assetType) {
		selenium.click("//table[@class='list']//a[.='"+assetType+"']//..//..//a[.='Edit']");
		waitForPageToLoad();
		return this;
	}

	public ManageAssetTypesPage clickCopyAssetType(String assetType) {
		selenium.click("//table[@class='list']//a[.='"+assetType+"']//..//..//a[.='Copy']");
		waitForPageToLoad();
		return this;
	}
	
	public ManageAssetTypesPage clickAssetType(String assetType) {
		selenium.click("//div[@id='pageContent']//table[@class='list']//td[position()=1 and .='"+assetType+"']/a");
		waitForPageToLoad();
		return this;
	}

	public ManageAssetTypesPage clickEventTypeAssociationsTab() {
		clickNavOption("Event Type Associations");
		return this;
	}
	
	public ManageAssetTypesPage clickEventFrequenciesTab() {
		clickNavOption("Event Frequencies");
		return this;
	}
	
	public ManageAssetTypesPage clickSubComponentsTab() {
		clickNavOption("Sub-Components");
		return this;
	}
	
	public ManageAssetTypesPage clickEditTab() {
		clickNavOption("Edit");
		return this;
	}
	
	public ManageAssetTypesPage clickViewAllTab() {
		clickNavOption("View All");
		return this;
	}

	public void selectAssetTypeGroup(String groupName) {
		selenium.select("//form[@id='assetTypeUpdate']//select[@id='assetTypeUpdate_group']", groupName);
	}
	
	public String getEditAssetTypeGroup() {
		return selenium.getSelectedLabel("//form[@id='assetTypeUpdate']//select[@id='assetTypeUpdate_group']");
	}
	
	public void clickSaveAssetType() {
		selenium.click("//form[@id='assetTypeUpdate']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}
	
	public void clickDeleteAssetType() {
		selenium.click("//form[@id='assetTypeUpdate']//div[@class='formAction']//a[contains(.,'Delete')]");
		waitForPageToLoad();
		selenium.click("//input[@type='submit' and @value='Delete']");
		waitForPageToLoad();
	}

	public ManageAssetTypesPage clickAddTab() {
		clickNavOption("Add");
		return this;
	}

	public void selectAllEventTypes(){
		selenium.click("//tr[1]/th[1]/a[1]");
		waitForAjax();
	}
	
	public void enterName(String name) {
		selenium.type("//form[@id='assetTypeUpdate']//input[@name='name']", name);
	}
	
	public void enterWarnings(String warnings) {
		selenium.type("//form[@id='assetTypeUpdate']//textarea[@name='warnings']", warnings);
	}
	
	public void enterInstructions(String instructions) {
		selenium.type("//form[@id='assetTypeUpdate']//textarea[@name='instructions']", instructions);
	}
	
	public void enterCautionsUrl(String cautionsUrl) {
		selenium.type("//form[@id='assetTypeUpdate']//input[@name='cautionsUrl']", cautionsUrl);
	}
	
	public void checkHasManufacturerCertificate() {
		selenium.check("//form[@id='assetTypeUpdate']//input[@name='hasManufacturerCertificate']");
	}
	
	public void enterManufacturerCertificateText(String text) {
		selenium.type("//form[@id='assetTypeUpdate']//textarea[@name='manufacturerCertificateText']", text);
	}
	
	public void enterAssetDescriptionTemplate(String template) {
		selenium.type("//form[@id='assetTypeUpdate']//input[@name='descriptionTemplate']", template);
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
	
	public String getViewAssetDescriptionTemplate() {
		return selenium.getText("//div[@class='viewSection']//label[.='Asset Description Template']/../span");
	}
	
	public String getEditName() {
		return selenium.getValue("//form[@id='assetTypeUpdate']//input[@name='name']");
	}
	
	public String getEditWarnings() {
		return selenium.getValue("//form[@id='assetTypeUpdate']//textarea[@name='warnings']");
	}
	
	public String getEditInstructions() {
		return selenium.getValue("//form[@id='assetTypeUpdate']//textarea[@name='instructions']");
	}
	
	public String getEditCautionsUrl() {
		return selenium.getValue("//form[@id='assetTypeUpdate']//input[@name='cautionsUrl']");
	}
	
	public boolean getEditHasManufacturerCertificate() {
		return selenium.isChecked("//form[@id='assetTypeUpdate']//input[@name='hasManufacturerCertificate']");
	}
	
	public String getEditManufacturerCertificateText() {
		return selenium.getValue("//form[@id='assetTypeUpdate']//textarea[@name='manufacturerCertificateText']");
	}
	
	public String getEditAssetDescriptionTemplate() {
		return selenium.getValue("//form[@id='assetTypeUpdate']//input[@name='descriptionTemplate']");
	}

	public List<String> getEventTypes() {
		return getColumnFromTableStartingAtRow("//form[@id='assetTypeEventTypesSave']//table[@class='list']", 2, 2);
	}

	public void selectEventType(String eventType) {
		selenium.check("//form[@id='assetTypeEventTypesSave']//table[@class='list']//td[position() = 2 and contains(.,'"+eventType+"')]//parent::tr/td[1]/input[@type='checkbox']");
	}

	public void saveEventTypes() {
		selenium.click("//form[@id='assetTypeEventTypesSave']//input[@type='submit' and @value='Save']");
		waitForPageToLoad();
	}
	
	public void scheduleEventFrequencyForType(String eventType, int everyNDays) {
		selenium.click("//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]//a[.='Edit']");
		String frequencyInputXPath = "//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]//input[@type='text' and contains(@id, 'frequency')]";
		waitForElementToBePresent(frequencyInputXPath);
		selenium.type(frequencyInputXPath, everyNDays+"");
		selenium.click("//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]//a[.='Save']");
		waitForAjax();
	}
	
	public void removeEventFrequencyForType(String eventType, boolean confirm) {
		confirmNextDialog(confirm);
		selenium.click("//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]//a[.='Remove']");
        waitForAjax();
		selenium.getConfirmation();
	}
		
	public boolean isEventFrequencyScheduledForType(String eventType) {
		return !selenium.isElementPresent("//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[position() = 2 and contains(., 'Not Scheduled')]");
	}
	
	public int getScheduledFrequencyForEventType(String eventType) {
		Pattern frequencyPattern = Pattern.compile("Schedule a .* every (\\d+) Days");
		String freqStr = selenium.getText("//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]//span[@class='frequency']");
		Matcher m = frequencyPattern.matcher(freqStr);
		if (!m.matches()) fail("Bad frequency pattern, could not parse: " + freqStr);
		return Integer.parseInt(m.group(1).trim());
	}

	public void addOverrideForOwner(String eventType, Owner owner, int frequency) {
		String cellXpath = "//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]";
		expandOverridesSectionIfNecessary(eventType);
		
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

	private String expandOverridesSectionIfNecessary(String eventType) {
		String cellXpath = "//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]";
		if (selenium.isElementPresent(cellXpath + "//a[starts-with(@id, 'overrideExpand')]//img[@alt='[+]']")) {
			selenium.click(cellXpath + "//a[starts-with(@id, 'overrideExpand')]//img[@alt='[+]']");
		}
		waitForElementToBePresent(cellXpath + "//a[.='Add override']");
		return cellXpath;
	}
	
	public List<EventFrequencyOverride> getEventFrequencyOverrides(String eventType) {
		expandOverridesSectionIfNecessary(eventType);
		
		List<EventFrequencyOverride> overrides = new ArrayList<EventFrequencyOverride>();
		
		String overrideXpath = "//table[@id='eventListTable']//td[position()=1 and contains(.,'"+eventType+"')]//parent::tr/td[2]"
			+"//div[contains(@class, 'customerOverride')]";
		int countOverrides = selenium.getXpathCount(overrideXpath).intValue();
		for (int i = 1; i <= countOverrides; i++) {
			EventFrequencyOverride override = new EventFrequencyOverride();
			override.customer = selenium.getText("xpath=("+overrideXpath+")["+i+"]/span[@class='customer']/b").trim();
			override.frequency = Integer.parseInt(selenium.getText("xpath=("+overrideXpath+")["+i+"]/span[@class='frequency']/b[2]").trim());
			overrides.add(override);
		}
		return overrides;
	}
	
	public static class EventFrequencyOverride {
		public String customer;
		public int frequency;
	}

	public List<String> getSubComponents() {
		List<String> subComponents = new ArrayList<String>();
		
		int numSubComponents = selenium.getXpathCount("//ul[@id='subAssets']/li").intValue();
		for (int i = 1; i <= numSubComponents; i++) {
			String subComponent = selenium.getText("//ul[@id='subAssets']/li["+i+"]/span[starts-with(@id, 'assetName_')]");
			subComponents.add(subComponent);
		}
		
		return subComponents;
	}
	
	public void addSubComponent(String subcomponentName) {
		selenium.select("//select[@id='addSubAsset']", subcomponentName);
		selenium.click("//button[@id='addSubAssetButton']");
	}
	
	public void saveSubComponents() {
		selenium.click("//input[@id='assetTypeConfigurationUpdate_label_save']");
		waitForPageToLoad();
	}

	public void removeSubComponent(String componentName) {
		selenium.click("//ul[@id='subAssets']//span[.='"+componentName+"']/../a[.='Remove']");
	}

    public void retireAttributes(String... attrNames) {
        for (String attrName : attrNames) {
            clickAttributeLink(attrName, "Retire");
        }
    }

    public void deleteAttributes(String... attrNames) {
        for (String attrName : attrNames) {
            clickAttributeLink(attrName, "Delete");
        }
    }

    private void clickAttributeLink(String attributeName, String linkText) {
        selenium.click("//div[@id='infoFields']//input[@class = 'name' and @value= '"+attributeName+"']/../..//div[contains(@class,'linkCol')]//a[.='"+linkText+"']");
    }
    
	public ManageAssetTypesPage clickSave(){
		selenium.click("//input[@name='hbutton.save']");
		throwExceptionOnFormError(true);
		return new ManageAssetTypesPage(selenium, false);
	}

	public void enterNameFilter(String name) {
		selenium.type("//input[@id='nameFilter']", name);
	}

	public void clickFilter() {
		selenium.click("//input[@name='hbutton.filter']");
		waitForPageToLoad();
	}

	public void selectFilterGroup(String group) {
		selenium.select("//select[@id='groupFilter']", group);
	}

	public void clickClearFilter() {
		selenium.click("//a[contains(.,'Clear')]");
		waitForPageToLoad();
	}

}
