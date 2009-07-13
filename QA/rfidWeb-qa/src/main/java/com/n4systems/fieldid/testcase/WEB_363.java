package com.n4systems.fieldid.testcase;

import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

/**
 * Improve Inspector drop down box so it only shows the smallest number of inspectors.
 * For create inspection this is the list of inspectors currently in the system but
 * for edit, you need to keep everyone. The list need to contain people who HAD
 * create inspection but there is no way to know who HAD create inspection so the
 * list is everyone. Note: if you delete a user, they appear on the list of inspectors
 * but not in the administration section.
 * 
 * @author Darrell
 *
 */
public class WEB_363 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	String[] products = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setPassword("makemore$");
		helper.setTenant("cglift");
		helper.setUserName("n4systems");
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);
		products = helper.getProductsFirstPage(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	/**
	 * Only people with Create or Edit Inspection permission should be on the list of Inspectors
	 * when creating a new inspection. If the two lists do not match it will print the
	 * two lists so it can be validated by hand.
	 * 
	 * @throws Exception
	 */
	public void testWeb363CreateInspection() throws Exception {
		// NOTE: inspecting an existing product or add product, save and inspect
		// brings you to the same page so there is no need to test both scenarios.
		String nameFilter = null;
		String userType = null;
		boolean clear = true;
		Random r = new Random();
		int i = r.nextInt(products.length);
		String[] user = helper.getListOfUserNamesWithInspectPermission(ie, nameFilter, userType, clear);
		String[] inspectionType = helper.getInspectionTypesForProduct(ie, products[i]);
		helper.gotoInspectionTypeOnProduct(ie, products[i], inspectionType[0]);
		// validate the list on the page with 'user'
		helper.validateInspectorsOnCreateInspection(ie, user);
	}
	
	/**
	 * If WEB-551 gets implemented, this test is no longer valid.
	 * 
	 * In Reporting you want to look up inspections from the past. In the past
	 * anyone could have had create or edit inspection permission. Therefore
	 * the Inspector list should include all users. From the web I can find
	 * all ACTIVE users. I cannot find deleted users. This test case will
	 * fail if there are deleted users. Therefore, the validate method will
	 * print a list of users who are on the list but not in the system.
	 * You will need to confirm with the database that the users showing up
	 * in the list are validate for the tenant but not ACTIVE.
	 * 
	 * @throws Exception
	 */
	public void donottestWeb363Reporting() throws Exception {
		String nameFilter = null;
		String userType = null;
		boolean clear = true;
		String[] user = helper.getListOfUserNames(ie, nameFilter, userType, clear);
		helper.gotoReporting(ie);
		helper.validateInspectorsOnReporting(ie, user);
	}

	/**
	 * If WEB-551 gets implemented, this test is no longer valid. Similar
	 * scenario to Reporting. If this fails, you will need to hand validate
	 * the list of users.
	 * 
	 * @throws Exception
	 */
	public void donottestWeb363EditInspection() throws Exception {
		String nameFilter = null;
		String userType = null;
		boolean clear = true;
		Random r = new Random();
		int i = r.nextInt(products.length);
		String[] user = helper.getListOfUserNames(ie, nameFilter, userType, clear);
		helper.gotoInspectionGroupsForProduct(ie, products[i]);
		String[] inspectionIds = helper.getInspectionIdsFromInspectionGroupsForProducts(ie);
		helper.gotoEditInspectionFromInspectionGroupsForProducts(ie, inspectionIds[0]);
		helper.validateInspectorsOnEditInspection(ie, user);
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}

}
