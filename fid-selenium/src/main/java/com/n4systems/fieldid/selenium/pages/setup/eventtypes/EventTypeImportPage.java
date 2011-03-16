package com.n4systems.fieldid.selenium.pages.setup.eventtypes;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class EventTypeImportPage extends FieldIDPage {

	public EventTypeImportPage(Selenium selenium) {
		super(selenium);
		assertTitleAndTab("Manage Event Type", "Import");
	}

}
