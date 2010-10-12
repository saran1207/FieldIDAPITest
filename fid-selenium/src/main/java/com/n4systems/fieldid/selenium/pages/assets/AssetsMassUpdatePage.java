package com.n4systems.fieldid.selenium.pages.assets;

import com.n4systems.fieldid.selenium.pages.MassUpdatePage;
import com.thoughtworks.selenium.Selenium;

public class AssetsMassUpdatePage extends MassUpdatePage<AssetsSearchResultsPage> {

    public AssetsMassUpdatePage(Selenium selenium) {
        super(selenium, AssetsSearchResultsPage.class);
    }

    public void setProductStatus(String newStatus) {
        selenium.select("//select[@id='massUpdateProductsSave_productStatus']", newStatus);
        selenium.fireEvent("//select[@id='massUpdateProductsSave_productStatus']", "change");
    }

    public void setPurchaseOrder(String newPurchaseOrder) {
        selenium.type("//input[@id='massUpdateProductsSave_purchaseOrder']", newPurchaseOrder);
        selenium.fireEvent("//input[@id='massUpdateProductsSave_purchaseOrder']", "change");
    }

}
