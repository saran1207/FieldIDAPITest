package com.n4systems.fieldid.selenium.safetynetwork.page;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class SafetyNetworkPreAssignedAssetsListPage extends FieldIDPage {

	public SafetyNetworkPreAssignedAssetsListPage(Selenium selenium) {
		super(selenium);
	}

	public SafetyNetworkTraceabilityPage clickAsset(String serialNumber){
		selenium.click("//a[.='" + serialNumber + "']");
		return new SafetyNetworkTraceabilityPage(selenium);
	}
	
}
