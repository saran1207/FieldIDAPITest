package com.n4systems.fieldid.testcase;

import com.n4systems.fieldid.datatypes.CustomerUser;
import com.n4systems.fieldid.datatypes.ProductAttribute;
import com.n4systems.fieldid.datatypes.ProductAttributeType;
import com.n4systems.fieldid.datatypes.ProductType;

public class Validate_2009_7_0 extends FieldIDTestCase {

	private String company;
	private String userid;
	private String password;

	protected void setUp() throws Exception {
		super.setUp();
		company = prop.getProperty("company");
		userid = prop.getProperty("userid");
		password = prop.getProperty("password");
	}

	public void testWeb1050() throws Exception {
		try {
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
			admin.gotoAdministration();
			mus.gotoManageUsers();
			mus.gotoAddCustomerUser();
			CustomerUser cu = new CustomerUser(null, null, null, null, null);
			cu = mus.getAddCustomerUser();
			String country = prop.getProperty("country");
			String timeZone = prop.getProperty("timezone");
			assertEquals(cu.getCountry(), country);
			assertEquals(cu.getTimeZone(), timeZone);
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
