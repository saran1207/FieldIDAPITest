package com.n4systems.fieldid.selenium.testcase;

import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.fieldid.selenium.PageNavigatingTestCase;
import com.n4systems.fieldid.selenium.pages.JobsListPage;

//Jobs page to be redone in the next iteration

@Ignore
public class JobsTest extends PageNavigatingTestCase<JobsListPage> {

	private static final String JOB_ID_1 = "17702";

	@Override
	protected JobsListPage navigateToPage() {
		return startAsCompany("test1").login().clickJobsLink();
	}

	@Ignore
	@Test
	public void test_search() {
		page.searchForJobById(JOB_ID_1);
	}

	@Ignore
	@Test
	public void test_add() {

	}

	@Ignore
	@Test
	public void test_delete() {

	}
}
