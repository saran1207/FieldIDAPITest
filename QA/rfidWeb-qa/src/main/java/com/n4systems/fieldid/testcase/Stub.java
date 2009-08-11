package com.n4systems.fieldid.testcase;

import com.n4systems.fieldid.datatypes.Inspection;
import com.n4systems.fieldid.datatypes.Job;
import com.n4systems.fieldid.datatypes.ScheduleSearchCriteria;

public class Stub extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test() throws Exception {
		String method = getName();

		try {
			login.setCompany("cglift");
			login.setUserName("n4systems");
			login.setPassword("makemore$");
			login.login();
			assets.validate("Reel/ID");
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
