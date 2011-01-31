package com.n4systems.fieldid.selenium.pages.setup;

import com.n4systems.fieldid.selenium.components.UnitOfMeasurePicker;
import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.fail;

public class ManageAssetCodeMappingsPage extends FieldIDPage {

    public ManageAssetCodeMappingsPage(Selenium selenium) {
        super(selenium);
        verifyManageAssetCodeMappingsPage();
    }

    private String manageAssetCodeMappingsPageHeaderLocator = "xpath=//DIV[@id='contentTitle']/H1[contains(text(),'Manage Asset Code Mappings')]";
    private String addAssetCodeMappingLinkLocator = "xpath=//DIV[@id='contentHeader']/UL[contains(@class,'options')]/LI[contains(@class,'add')]/A[contains(text(),'Add')]";
    private String assetCodeTextFieldLocator = "xpath=//INPUT[@id='assetCodeMappingEdit_assetCode']";
    private String referenceNumberTextFieldLocator = "xpath=//INPUT[@id='assetCodeMappingEdit_customerRefNumber']";
    private String assetTypeSelectListLocator = "xpath=//SELECT[@id='assetType']";
    private String saveButtonLocator = "//input[@type='submit' and @value='Save']";
    private String cancelButtonLocator = "xpath=//A[contains(text(),'Cancel')]";

    public void verifyManageAssetCodeMappingsPage() {
        checkForErrorMessages("verifyManageAssetCodeMappingsPage");
        if(!selenium.isElementPresent(manageAssetCodeMappingsPageHeaderLocator)) {
            fail("Could not find the header for 'Manage Asset Code Mappings'.");
        }
    }

    public void enterAssetCode(String assetCode) {
        selenium.type(assetCodeTextFieldLocator, assetCode);
    }

    public void enterReferenceNumber(String referenceNumber) {
        selenium.type(referenceNumberTextFieldLocator, referenceNumber);
    }

    public void selectAssetType(String assetType) {
        selenium.select(assetTypeSelectListLocator, assetType);
        waitForPageToLoad();
    }

    public ManageAssetCodeMappingsPage clickSaveAssetCodeMapping() {
        selenium.click(saveButtonLocator);
        return new ManageAssetCodeMappingsPage(selenium);
    }

    public void clickAddTab() {
        clickNavOption("Add");
    }

    public UnitOfMeasurePicker getUnitOfMeasurePickerForAttribute(String attributeName) {
        String elementId = selenium.getAttribute("//div[@infofieldname= '"+attributeName+"']//input[@type='text']/@id");
        return new UnitOfMeasurePicker(selenium, elementId);
    }

    public void enterAttributeValue(String attributeName, String value) {
        selenium.type("//div[@infofieldname='" + attributeName + "']//input[@type='text']", value);
    }

    public void selectAttributeValue(String attributeName, String value) {
        selenium.select("//div[@infofieldname='" + attributeName + "']//select", value);
    }
}
