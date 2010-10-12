package com.n4systems.fieldid.selenium.pages.assets;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class AssetsMassUpdatePage extends FieldIDPage {

    public AssetsMassUpdatePage(Selenium selenium) {
        super(selenium);
    }

    public void setProductStatus(String newStatus) {
        selenium.select("//select[@id='massUpdateProductsSave_productStatus']", newStatus);
        selenium.fireEvent("//select[@id='massUpdateProductsSave_productStatus']", "change");
    }

    public void setPurchaseOrder(String newPurchaseOrder) {
        selenium.type("//input[@id='massUpdateProductsSave_purchaseOrder']", newPurchaseOrder);
        selenium.fireEvent("//input[@id='massUpdateProductsSave_purchaseOrder']", "change");
    }

    public AssetsSearchResultsPage clickSaveButton() {
        selenium.chooseOkOnNextConfirmation();
        selenium.click("//input[@type='submit' and @value='Save']");
        selenium.getConfirmation();
        return new AssetsSearchResultsPage(selenium); 
    }

}
