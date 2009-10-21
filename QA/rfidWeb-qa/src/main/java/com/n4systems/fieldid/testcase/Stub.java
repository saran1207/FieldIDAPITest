package com.n4systems.fieldid.testcase;

import com.n4systems.fieldid.datatypes.Owner;
import com.n4systems.fieldid.datatypes.Product;

public class Stub extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test() throws Exception {
		String method = getName();

		try {
			login.setCompany("crosby");
			login.setUserName("n4systems");
			login.setPassword("makemore$");
			login.login();
//			mysn.validateTenant("Field ID");
			Thread.sleep(1);
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
