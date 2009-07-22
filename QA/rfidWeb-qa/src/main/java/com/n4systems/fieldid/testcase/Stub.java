package com.n4systems.fieldid.testcase;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import watij.runtime.ie.IE;
import com.n4systems.fieldid.*;
import com.n4systems.fieldid.admin.*;
import com.n4systems.fieldid.datatypes.*;

//
// WEB_186 refactor
//
public class Stub extends FieldIDTestCase {

	String company = "cglift";
	String userid = "dg";
	String password = "makemore$";

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test() throws Exception {
		String method = getName();

		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			jobs.gotoJobs();
			for(int i = 0; i < 20; i++) {
				String jobTitle = "title-" + i;
				jobs.gotoJob(jobTitle);
				jobs.gotoAddResource(jobTitle);
				jobs.addEmployeeResource("Darrell Grainger");
				jobs.gotoViewAll();
			}
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
