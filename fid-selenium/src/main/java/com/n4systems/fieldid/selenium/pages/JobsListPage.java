package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import com.n4systems.fieldid.selenium.jobs.page.JobPage;
import com.thoughtworks.selenium.Selenium;

public class JobsListPage extends FieldIDPage {

	public JobsListPage(Selenium selenium) {
		super(selenium);
		if (!checkOnJobsListPage()) {
			fail("Expected to be on Jobs List page!");
		}
	}

	public boolean checkOnJobsListPage() {
		checkForErrorMessages(null);
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'Jobs')]");
	}

	public JobPage searchForJobById(String jobID) {
		selenium.type("//input[@id='jobSearchForm_searchID']", jobID);
		selenium.click("//input[@id='jobSearchForm_hbutton_search']");
		return new JobPage(selenium);
	}

	public JobPage selectJob(String title) {
		selenium.click("//a[.='" + title + "']");
		return new JobPage(selenium);
	}

}
