package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.safetynetwork.FindConnectionResultsPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.ConnectionProfilePage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkCatalogPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkSettingsPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkPage extends FieldIDPage {

    public SafetyNetworkPage(Selenium selenium) {
        super(selenium);
    }

    public FindConnectionResultsPage findConnections(String searchText) {
        selenium.type("//input[@id='companySearchBox']", searchText);
        selenium.click("//input[@id='searchForCompanyButton']");
        return new FindConnectionResultsPage(selenium);
    }
	
	public SafetyNetworkCatalogPage goToCatalog(){
		selenium.click("//*[@id='manageCatalog']");
		return new SafetyNetworkCatalogPage(selenium);
	}

	public SafetyNetworkSettingsPage goToSettings() {
		selenium.click("//a[@id='privacySettings']");
		return new SafetyNetworkSettingsPage(selenium);
	}
	
	public ConnectionProfilePage selectConnection(String id){
		selenium.click("//a[@href='/fieldid/publishedCatalog.action?uniqueID="+ id +"']");
		return new ConnectionProfilePage(selenium);
	}
}
