package com.n4systems.fieldid.testcase;

import com.n4systems.fieldid.datatypes.Owner;

public class Stub extends FieldIDTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test() throws Exception {
		String method = getName();

		try {
			login.setCompany("unirope");
			login.setUserName("n4systems");
			login.setPassword("makemore$");
			login.login();
			identify.gotoAddProduct();
			misc.gotoChooseOwner();
			Owner o = new Owner("Unirope Ltd. Edmonton", "Testing", "Division #1");
			misc.setOwner(o);
			misc.selectOwner();
			Thread.sleep(1);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}

	public void test2() throws Exception {
		String method = getName();

		try {
			login.setCompany("unirope");
			login.setUserName("n4systems");
			login.setPassword("makemore$");
			login.login();
			admin.gotoAdministration();
			mos.gotoManageOrganizations();
			mos.gotoEditOrganization("validate-922548618355501174");
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
