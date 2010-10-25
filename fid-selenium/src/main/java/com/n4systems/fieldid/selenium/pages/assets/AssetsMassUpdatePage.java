package com.n4systems.fieldid.selenium.pages.assets;

import com.n4systems.fieldid.selenium.pages.MassUpdatePage;
import com.thoughtworks.selenium.Selenium;

public class AssetsMassUpdatePage extends MassUpdatePage<AssetsSearchResultsPage> {

    public AssetsMassUpdatePage(Selenium selenium) {
        super(selenium, AssetsSearchResultsPage.class);
    }

    public void setAssetStatus(String newStatus) {
        selenium.select("//select[@id='massUpdateAssetsSave_assetStatus']", newStatus);
        selenium.fireEvent("//select[@id='massUpdateAssetsSave_assetStatus']", "change");
    }

    public void setPurchaseOrder(String newPurchaseOrder) {
        selenium.type("//input[@id='massUpdateAssetsSave_purchaseOrder']", newPurchaseOrder);
        selenium.fireEvent("//input[@id='massUpdateAssetsSave_purchaseOrder']", "change");
    }

}
