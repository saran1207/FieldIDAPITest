package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;
import static org.junit.Assert.fail;

public class SafetyNetworkCatalogPage extends FieldIDPage {

	public SafetyNetworkCatalogPage(Selenium selenium) {
		super(selenium);
	}

	public void checkAssetTypeCheckBox(String labelText) {
		if (!selenium.isElementPresent("//label[text()='" + labelText + "']/..//input[@type='checkbox']")) {
			fail("Could not find the label text '" + labelText + "' in the catalog page");
		}
		selenium.check("//label[text()='" + labelText + "']/..//input[@type='checkbox']");
	}

	public void unCheckAssetTypeCheckBox(String labelText) {
		selenium.uncheck("//label[text()='" + labelText + "']/..//input[@type='checkbox']");
	}

	public void submitForm() {
		selenium.click("//input[@id='publish']");
		waitForPageToLoad();
	}

	public boolean isChecked(String labelText) {
		return selenium.isChecked("//label[text()='" + labelText + "']/..//input[@type='checkbox']");
	}

}
