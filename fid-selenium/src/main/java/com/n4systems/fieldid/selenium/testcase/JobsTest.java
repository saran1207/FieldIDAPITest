package com.n4systems.fieldid.selenium.testcase;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.JobsListPage;

public class JobsTest extends FieldIDTestCase {

	   private static final String JOB_ID_1= "17702";
	   private static final String JOB_ID_1_TITLE_LOCATION= "//h1[contains(text(),'Job - KEMLITE')]";
	
	@Test
	public void test_search() {
		JobsListPage jobsListPage = startAsCompany("sievert").login().clickJobsLink();
		jobsListPage.searchForJobById(JOB_ID_1);
		assertTrue("Could not open the job page", selenium.isElementPresent(JOB_ID_1_TITLE_LOCATION));
	}
	
	@Test
	public void test_add(){
		
	}
	
	@Test
	public void test_delete(){
		
	}
	
}
