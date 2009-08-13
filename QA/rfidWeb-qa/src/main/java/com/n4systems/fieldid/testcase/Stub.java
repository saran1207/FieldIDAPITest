package com.n4systems.fieldid.testcase;

import java.util.List;

import com.n4systems.fieldid.datatypes.ProductSearchCriteria;
import com.n4systems.fieldid.datatypes.ProductSearchSelectColumns;

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
			String customer = "ALRO STEEL-AKRON";
//			assets.validate("Reel/ID", customer);
			assets.gotoAssets();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setOrderNumber(true);
			assets.setProductSearchColumns(c);
			ProductSearchCriteria prop = new ProductSearchCriteria();
			prop.setCustomer(customer);
			assets.setProductSearchCriteria(prop);
			assets.gotoProductSearchResults();
			List<String> orderNumbers = assets.getProductSearchResultsColumn("Order Number");
			System.out.println(orderNumbers);
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
