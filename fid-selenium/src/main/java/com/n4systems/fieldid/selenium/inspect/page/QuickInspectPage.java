package com.n4systems.fieldid.selenium.inspect.page;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class QuickInspectPage extends FieldIDPage {

	public QuickInspectPage(Selenium selenium) {
		super(selenium);
		if (!selenium.isElementPresent("//")){
			
		}
	}

}
