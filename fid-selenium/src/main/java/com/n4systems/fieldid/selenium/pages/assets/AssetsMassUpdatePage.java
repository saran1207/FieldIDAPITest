package com.n4systems.fieldid.selenium.pages.assets;

import com.n4systems.fieldid.selenium.pages.AssetsSearchPage;
import com.n4systems.fieldid.selenium.pages.PageFactory;
import com.n4systems.fieldid.selenium.pages.WicketFieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsMassUpdatePage extends WicketFieldIDPage {
	
    public AssetsMassUpdatePage(Selenium selenium) {
        super(selenium);
    }
    
    public void selectEdit() {
    	selenium.check("//input[@value='Edit']");
    	selenium.click("//input[@value='Next']"); 
    	waitForElementToBePresent("//form[@class='editForm']");
    }
    
    public void selectDelete() {
    	selenium.check("//input[@value='Delete']");
    	selenium.click("//input[@value='Next']");    	
    	waitForElementToBePresent("//div[@class='deleteDetails']");
    }
    
    public void saveEditDetails() {
    	selenium.click("//input[@value='Next']");
    	waitForElementToBePresent("//form[@class='confirmEditForm']");
    }
    
    public void saveEditDetailsWithError() {
    	selenium.click("//input[@value='Next']");    	
    	waitForElementToBePresent("//div[@class='formErrors']");
    }
    
    public void saveDeleteDetails() {
    	selenium.click("//input[@value='Next']");    	
    	waitForElementToBePresent("//form[@class='confirmDeleteForm']");
    }

    public void setAssetStatus(String newStatus) {
        selenium.select("//select[@name='assetStatus']", newStatus);
        selenium.fireEvent("//select[@name='assetStatus']", "change");
        selenium.waitForCondition("var value = selenium.isChecked('//input[@name=\\'assetStatusCheck\\']'); value == true", DEFAULT_TIMEOUT);
    }

    public void setPurchaseOrder(String newPurchaseOrder) {
        selenium.type("//input[@name='purchaseOrder']", newPurchaseOrder);
        selenium.fireEvent("//input[@name='purchaseOrder']", "change");
        selenium.waitForCondition("var value = selenium.isChecked('//input[@name=\\'purchaseOrderCheck\\']'); value == true", DEFAULT_TIMEOUT);
    }
    
    public AssetsSearchPage clickConfirmDelete() {
    	selenium.type("//input[@name='confirmationField']", "delete");
        selenium.fireEvent("//input[@name='confirmationField']", "keyup");
        waitForWicketAjax();
    	selenium.click("//input[@value='Delete']");
        return new AssetsSearchPage(selenium);
    }

    public AssetsSearchPage clickConfirmEdit() {
    	selenium.click("//input[@value='Perform Mass Update']");
    	return new AssetsSearchPage(selenium);
    }

	public void checkOwner() {
		selenium.check("//input[@name='ownerCheck']");
	}

	public void checkIdentified() {
		selenium.check("//input[@name='identifiedCheck']");
	}
}
