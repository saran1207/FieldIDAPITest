package com.n4systems.fieldid.testcase;

import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_573 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	static boolean initialized = false;
	static String timestamp = null;
	static String productTypeGroupName = null;

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
	}
	
	public void testCreatingProductTypeGroup() throws Exception {
		String method = helper.getMethodName();
		Random r  = new Random();
		int n = Math.abs(r.nextInt());

		try {
			String tenant = "unirope";
			String user = "n4systems";
			String password = "Xk43g8!@";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			productTypeGroupName = "ptgroup-" + n;
			helper.addProductTypeGroups(ie, productTypeGroupName);
			helper.myWindowCapture(timestamp + method + "-After-Saving-Product-Type-Group.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testEditingProductTypeGroup() throws Exception {
		String method = helper.getMethodName();
		Random r  = new Random();
		int n = Math.abs(r.nextInt());

		try {
			String tenant = "unirope";
			String user = "n4systems";
			String password = "Xk43g8!@";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			String newProductTypeGroupName = "ptgroup-" + n;
			helper.editProductTypeGroup(ie, productTypeGroupName, newProductTypeGroupName);
			productTypeGroupName = newProductTypeGroupName;
			helper.myWindowCapture(timestamp + method + "-After-Editing-Product-Type-Group.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}	
	
	public void testDeletingProductTypeGroup() throws Exception {
		String method = helper.getMethodName();

		try {
			String tenant = "unirope";
			String user = "n4systems";
			String password = "Xk43g8!@";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			helper.deleteProductTypeGroup(ie, productTypeGroupName);
			helper.myWindowCapture(timestamp + method + "-After-Deleting-Product-Type-Group.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testAddingProductTypeToProductTypeGroup() throws Exception {
		String method = helper.getMethodName();
		Random r  = new Random();
		int n = Math.abs(r.nextInt());

		try {
			String tenant = "unirope";
			String user = "n4systems";
			String password = "Xk43g8!@";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			productTypeGroupName = "ptgroup-" + n;
			String productType = "pt" + n;
			helper.addProductTypeGroups(ie, productTypeGroupName);
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			helper.addProductTypeToProductTypeGroup(ie, productType, productTypeGroupName);
			helper.myWindowCapture(timestamp + method + "-Product-Type-Added-To-Product-Type-Group.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testDeletingTypeToProductTypeGroupWithProductType() throws Exception {
		String method = helper.getMethodName();
		Random r  = new Random();
		int n = Math.abs(r.nextInt());

		try {
			String tenant = "unirope";
			String user = "n4systems";
			String password = "Xk43g8!@";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			productTypeGroupName = "ptgroup-" + n;
			String productType = "pt" + n;
			helper.addProductTypeGroups(ie, productTypeGroupName);
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			helper.addProductTypeToProductTypeGroup(ie, productType, productTypeGroupName);
			helper.deleteProductTypeGroup(ie, productTypeGroupName);
			helper.myWindowCapture(timestamp + method + "-Product-Type-Group-Deleting.png", ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + method + "-Product-Type-Group-Deleted.png", ie);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testDeletingTypeToProductTypeGroupWithProduct() throws Exception {
		String method = helper.getMethodName();
		Random r  = new Random();
		int n = Math.abs(r.nextInt());

		try {
			String tenant = "unirope";
			String user = "n4systems";
			String password = "Xk43g8!@";

			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			helper.start(ie, helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			productTypeGroupName = "ptgroup-" + n;
			String productType = "pt" + n;
			helper.addProductTypeGroups(ie, productTypeGroupName);
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			helper.addProductTypeToProductTypeGroup(ie, productType, productTypeGroupName);
			String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
			helper.deleteProductTypeGroup(ie, productTypeGroupName);
			helper.myWindowCapture(timestamp + method + "-Product-Type-Group-Deleting.png", ie);
			ie.waitUntilReady();
			helper.myWindowCapture(timestamp + method + "-Product-Type-Group-Deleted.png", ie);
			String result = helper.getProduct(ie, null, serialNumber, null, null, null, null, null, null, null, null, null, null, null, null);
			if(!result.equals(serialNumber))
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
