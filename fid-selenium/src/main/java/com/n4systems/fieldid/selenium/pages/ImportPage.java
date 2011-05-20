package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class ImportPage extends FieldIDPage {

	public ImportPage(Selenium selenium) {
		super(selenium);
		assertTitleAndTab("Setup", "Import");
	}

}
