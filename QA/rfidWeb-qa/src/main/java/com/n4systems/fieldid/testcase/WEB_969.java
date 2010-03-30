package com.n4systems.fieldid.testcase;

import java.util.Iterator;
import java.util.List;

import watij.runtime.ie.IE;
import com.n4systems.fieldid.*;
import com.n4systems.fieldid.datatypes.*;
import junit.framework.TestCase;

public class WEB_969 extends TestCase {
	IE ie = new IE();
	FieldIDMisc misc = new FieldIDMisc(ie);
	Login login = new Login(ie);
	Home home = new Home(ie);
	Admin admin = new Admin(ie);
	Reporting reporting = new Reporting(ie);
	Assets assets = new Assets(ie);

	static String timestamp = null;
	static boolean once = true;
	String loginURL = "https://localhost.localdomain/fieldid/";
	String company = "hercules";
	String customer = "ABB Inc.";
	String userid = "n4systems";
	String password = "Xk43g8!@";

	protected void setUp() throws Exception {
		super.setUp();
		misc.start();
		login.gotoLoginPage(loginURL);
		if(once) {
			once = false;
			timestamp = misc.createTimestampDirectory();
			login.setCompany(company);
			login.setUserName(userid);
			login.setPassword(password);
			login.login();
		}
	}
	
	public void testReportingExportToExcelLessThan10000() throws Exception {
		String method = getName();

		try {
			reporting.gotoReporting();
			ReportSearchSelectColumns c = new ReportSearchSelectColumns();
			ReportingSearchCriteria r = new ReportingSearchCriteria();
			r.setCustomer(customer);	// limit to less than 10,000
			c.setAllOn();
			reporting.setReportingSearchCriteria(r);
			reporting.setReportSelectColumns(c);
			reporting.gotoReportSearchResults();
			reporting.exportToExcel();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testReportingExportToExcelMoreThan10000() throws Exception {
		String method = getName();

		try {
			reporting.gotoReporting();
			ReportSearchSelectColumns c = new ReportSearchSelectColumns();
			c.setAllOn();
			reporting.setReportSelectColumns(c);
			reporting.gotoReportSearchResults();
			reporting.exportToExcelWarningOver10000Reports();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAssetsExportToExcelLessThan10000() throws Exception {
		String method = getName();

		try {
			assets.gotoAssets();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			ProductSearchCriteria p = new ProductSearchCriteria();
			c.setAllOn();
			p.setCustomer(customer);
			assets.setProductSearchColumns(c);
			assets.setProductSearchCriteria(p);
			assets.gotoProductSearchResults();
			assets.exportToExcel();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testAssetsExportToExcelMoreThan10000() throws Exception {
		String method = getName();

		try {
			assets.gotoAssets();
			ProductSearchSelectColumns c = new ProductSearchSelectColumns();
			c.setAllOn();
			assets.setProductSearchColumns(c);
			assets.gotoProductSearchResults();
			assets.exportToExcelWarningOver10000Products();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + method + ".png");
			throw err;
		}
	}
	
	public void testReportingExportToExcelManyExportsPlusThisReport() throws Exception {
		String method = getName();

		try {
			reporting.gotoReporting();
			ReportSearchSelectColumns c = new ReportSearchSelectColumns();
			c.setAllOn();
			reporting.setReportSelectColumns(c);
			ReportingSearchCriteria r = new ReportingSearchCriteria();
			List<String> inspectionBooks = reporting.getInspectionBookOptions();
			Iterator<String> i = inspectionBooks.iterator();
			while(i.hasNext()) {
				String s = i.next();
				if(s.startsWith("TEST"))	// skip the TEST* inspection books
					continue;
				r.setInspectionBook(s);
				reporting.setReportingSearchCriteria(r);
				reporting.gotoReportSearchResults();
				
				long num = reporting.getTotalNumberOfInspections();
				if(num > 0 && num < 10000) {
					reporting.exportToExcel();
				}
				
				if(num > 0 && num < 1000) {
					reporting.printThisReport();
				}
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
		misc.myWindowCapture(timestamp + "/tearDown-" + getName() + ".png");
		login.close();
	}
}
