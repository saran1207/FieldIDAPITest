package com.n4systems.fieldid.testcase;

public class Stub extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test() throws Exception {
		String method = getName();

		try {
			login.setUserName(p.getProperty("userid"));
			login.setPassword(p.getProperty("password"));
			login.login();
			identify.gotoAddProduct();
			identify.gotoAddProductAttachFile();
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
