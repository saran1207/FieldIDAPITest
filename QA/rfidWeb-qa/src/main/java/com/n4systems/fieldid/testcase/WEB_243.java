package com.n4systems.fieldid.testcase;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import watij.runtime.ie.IE;
import com.n4systems.fieldid.*;
import com.n4systems.fieldid.admin.*;
import com.n4systems.fieldid.datatypes.ProductSearchCriteria;
import com.n4systems.fieldid.datatypes.ProductSearchSelectColumns;
import com.n4systems.fieldid.datatypes.ReportSearchSelectColumns;
import com.n4systems.fieldid.datatypes.ReportingSearchCriteria;

public class WEB_243 extends TestCase {
	IE ie = new IE();
	FieldIDMisc helper = new FieldIDMisc(ie);
	Home home = new Home(ie);
	Login login = new Login(ie);
	Identify identify = new Identify(ie);
	Inspect inspect = new Inspect(ie);
	Assets assets = new Assets(ie);
	Reporting reporting = new Reporting(ie);
	Schedule schedule = new Schedule(ie);
	Jobs jobs = new Jobs(ie);
	Admin admin = new Admin(ie);
	Compliance compliance = new Compliance(ie);
	MyAccount myAccount = new MyAccount(ie);
	ManageProductTypes mpts = new ManageProductTypes(ie);
	commonFieldIDMethods common = new commonFieldIDMethods();
	ManageEventTypeGroups metgs = new ManageEventTypeGroups(ie);
	ManageInspectionTypes mits = new ManageInspectionTypes(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "unirope";
	String productType = "Wire Rope Sling";
	String userid = "n4systems";
	String password = "Xk43g8!@";

	protected void setUp() throws Exception {
		super.setUp();
		helper.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = helper.createTimestampDirectory() + "/";
		}
		login.setCompany(company);
		login.setUserName(userid);
		login.setPassword(password);
		login.login();
	}
	
	/**
	 * Setup: download the database from production. Install the software
	 * running on production to the test environment. Run this test case.
	 * It will generate a number of Excel spreadsheets with all the data
	 * for products. 
	 * @throws Exception
	 */
	public void testChangingMainTenantFromMontreal() throws Exception {
		String method = getName();

		try {
			assets.gotoAssets();
			List<String> productTypes = assets.getProductTypesOnSearchCriteria();
			assets.expandProductSearchSelectColumns();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			Iterator<String> i = productTypes.iterator();
			while(i.hasNext()) {
				String productType = i.next();
				ProductSearchCriteria p = new ProductSearchCriteria();
				p.setProductType(productType);
				assets.setProductSearchCriteria(p);
				assets.gotoProductSearchResults();
				long count = assets.getTotalNumberOfProducts();
				if(count > 0 && count < 1000) {
					System.out.printf("%5d: %s\n", count, productType);
					assets.exportToExcel();
					Thread.sleep(300000);	// Export To Excel is not thread safe
				}
			}
			reporting.gotoReporting();
			reporting.expandReportSelectColumns();
			ReportSearchSelectColumns rsc = new ReportSearchSelectColumns();
			rsc.setAllOn();
			reporting.setReportSelectColumns(rsc);
			Iterator<String> i2 = productTypes.iterator();
			while(i2.hasNext()) {
				String productType = i2.next();
				ReportingSearchCriteria r = new ReportingSearchCriteria();
				r.setProductType(productType);
				reporting.setReportingSearchCriteria(r);
				reporting.gotoReportSearchResults();
				long count = reporting.getTotalNumberOfInspections();
				if(count > 0 && count < 1000) {
					System.out.printf("%5d: %s\n", count, productType);
					reporting.exportToExcel();
					Thread.sleep(300000);	// Export To Excel is not thread safe
				}
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		helper.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
	}
}
