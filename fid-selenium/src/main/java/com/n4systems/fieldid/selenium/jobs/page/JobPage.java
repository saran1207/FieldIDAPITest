package com.n4systems.fieldid.selenium.jobs.page;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;

public class JobPage extends FieldIDPage {

	public JobPage(Selenium selenium) {
		super(selenium);
		if (!checkOnJobsListPage()) {
			fail("Expected to be on Job view page!");
		}
	}

	private boolean checkOnJobsListPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//h2[contains(text(),'Job Details')]");
	}

}
