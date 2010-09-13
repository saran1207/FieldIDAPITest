package com.n4systems.fieldid.selenium.pages;

import com.n4systems.fieldid.selenium.pages.safetynetwork.FindConnectionResultsPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.SafetyNetworkImportPage;
import com.n4systems.fieldid.selenium.safetynetwork.page.VendorConnectionProfilePage;
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

	public SafetyNetworkCatalogPage goToCatalog() {
		selenium.click("//*[@id='manageCatalog']");
		return new SafetyNetworkCatalogPage(selenium);
	}

	public SafetyNetworkSettingsPage clickSettings() {
		selenium.click("//a[@id='privacySettings']");
		return new SafetyNetworkSettingsPage(selenium);
	}

	public VendorConnectionProfilePage selectVendorConnection(String connectionName) {
		selenium.click("//ul[@id='safetyNetworkVendorList']/li[1]/a[.='" + connectionName + "']");
		return new VendorConnectionProfilePage(selenium);
	}

	public SafetyNetworkImportPage selectCustomerConnection(String connectionName) {
		selenium.click("//ul[@id='safetyNetworkCustomerList']/li/a[.='" + connectionName + "']");
		return new SafetyNetworkImportPage(selenium);
	}

}
