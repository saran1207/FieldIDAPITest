package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class CustomerConnectionProfilePage extends FieldIDPage {

	public CustomerConnectionProfilePage(Selenium selenium) {
		super(selenium);
	}

    public int getNumAssetsRegistered() {
        String numStr = selenium.getText("//div[contains(@class, 'registeredAssetsCount')]").trim();
        return Integer.parseInt(numStr);
    }

    public int getNumAssetsPreassigned() {
        String numStr = selenium.getText("//div[contains(@class, 'preassignedAssetsCount')]").trim();
        return Integer.parseInt(numStr);
    }
    
    public SafetyNetworkCatalogImportPage clickViewCatalog(){
    	selenium.click("//a[contains(text(), 'View Catalog')]");
    	return new SafetyNetworkCatalogImportPage(selenium);
    }
    
}
