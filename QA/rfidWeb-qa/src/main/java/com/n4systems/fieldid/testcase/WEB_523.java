package com.n4systems.fieldid.testcase;

import java.util.Random;

import junit.framework.TestCase;
import watij.elements.Span;
import watij.elements.Spans;
import watij.runtime.ie.IE;
import static watij.finders.SymbolFactory.*;

/**
 * HTML code in the project ID or title is not escaped.
 * 
 * @author dgrainge
 *
 */
public class WEB_523 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setUserName("n4systems");
		helper.setPassword("makemore$");
		helper.setTenant("hysafe");
		helper.start(ie, helper.getLoginURL());
		if(ie.link(text, "Logout").exists())	ie.link(text, "Logout").click();
		if(helper.isCurrentCompany(ie, helper.getTenant())) {
			helper.setTenantAtLoginPage(ie, helper.getTenant());
		}
		ie.maximize();
	}
	
	/**
	 * WEB-523: HTML code in the Project ID or Title fields is not escaped.
	 * This test case assumes the tenant has the extended feature of Project.
	 * It will attempt to put HTML code in the title field then do a screen
	 * capture. Someone will need to look at the screen capture to see if
	 * the HTML in the title has affected the formatting of the rest of the
	 * page.
	 * 
	 * @throws Exception
	 */
	public void testWeb523() throws Exception {
		Random n = new Random(System.currentTimeMillis());
		String spanID = "web-523-darrell";
		String title = "<span style='font-size:large' id='" + spanID + "'>Darrell";
		String projectID = title;
		String typeOfJob = "Asset";
		String customer = null;
		String division = null;
		String status = "on schedule";
		boolean open = true;
		String description = title; 
		String startDate = "01/26/10 12:00 am";
		String estimateDate = "02/02/10 12:00 am";
		String actualDate = null;
		String duration = "8 days";
		String poNumber = title;
		String workPerformed = title;
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);
		do {
			title += n.nextInt();	// use a random number, project id must be unique
			projectID = title;
		}
		while(helper.isJobPresent(ie, title));
		helper.addJob(ie, typeOfJob, projectID, title, customer, division, status, open, description, startDate, estimateDate, actualDate, duration, poNumber, workPerformed);
		
		// Scan the page to see if the SPAN id, from title, exists
		// if it does throw an exception
		Spans spans = ie.spans();
		for(int i = 0; i < spans.length(); i++) {
			Span s = spans.get(i);
			if(s != null && s.exists())
				if(s.id() != null)
						if(s.id().equals(spanID))
				assert(false);
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
