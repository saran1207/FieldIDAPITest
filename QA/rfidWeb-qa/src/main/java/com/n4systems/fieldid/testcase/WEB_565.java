package com.n4systems.fieldid.testcase;

import java.util.Random;

import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class WEB_565 extends TestCase {
	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	Random r = new Random();
	static boolean initialized = false;
	static String timestamp = null;
	final String manufacturer = "jergens";
	final String manufacturerName = "Jergens Inc.";
	final String tenant = "hysafe";
	final String tenantName = "Hy-Safe Technology";
	final String user = "n4systems";
	final String password = "makemore$";
	final String masterProductType = "master";
	final String subProductType = "sub";
	static String rfidNumber = "DEADBEEFCAFEF00D";
	static String rfidNumber2 = "CAFEF00DDEADBEEF";
	final int n = Math.abs(r.nextInt());
	final String masterInspectionType = "Master Visual " + n;;
	final String[] supportedProofTestTypes = { "ROBERTS", "NATIONALAUTOMATION", "CHANT", "WIROP", "OTHER" };
	final String inspectionTypeGroup = "Visual Inspection";
	String tenantMasterSerialNumber = null;
	String tenantSubSerialNumber = null;
	String masterSerialNumber = null;
	String subSerialNumber = null;

	/**
	 * There is a lot of set up for this testsuite.
	 * We need to add the tenant to the manufacturer's safety network.
	 * Next we want to create product types, inspection type, products
	 * and inspections from the manufacturer side. Finally, we want to
	 * add similar product types and inspection types on the tenant side.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.start(ie);
		ie.maximize();
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
			
			// get the tenant FIDAC
			loginTenant();
			String fidac = helper.getFIDAC(ie);
			helper.logout(ie);
			
			// Add the tenant to the manufacturer's safety network
			loginManufacturer();
			if(!helper.isTenantPartOfSafetyNetwork(ie, tenantName))
				helper.addTenantToSafetyNetwork(ie, fidac);
			
			// create product types, inspection types on manufacturer side
			if(!helper.isProductType(ie, masterProductType)) {
				helper.addProductType(ie, masterProductType, null, null, null, true, null, null, null, null, null);
			}
			if(!helper.isProductType(ie, subProductType)) {
				helper.addProductType(ie, subProductType, null, null, null, true, null, null, null, null, null);
			}
			if(!helper.isProductTypeOneASubProductOfProductTypeTwo(ie, subProductType, masterProductType))
				helper.AddProductTypeOneToBeSubProductOfProductTypeTwo(ie, subProductType, masterProductType);
			helper.addInspectionType(ie, masterInspectionType, inspectionTypeGroup, true, true, supportedProofTestTypes, null);
			helper.addInspectionTypeToProductType(ie, masterInspectionType, masterProductType);
			
			// create products, inspection on manufacturer side
			masterSerialNumber = helper.addProduct(ie, null, true, rfidNumber, "", "", null, null, null, "", "", "", null, null, masterProductType, null, null, null, "Save");
			subSerialNumber = helper.addProduct(ie, null, true, rfidNumber2, "", "", null, null, null, "", "", "", null, null, subProductType, null, null, null, "Save");
			helper.addSubProductToMasterProduct(ie, subSerialNumber, subProductType, masterSerialNumber, "sub");
			helper.addInspectionToProduct(ie, masterInspectionType, masterSerialNumber, true);
			helper.logout(ie);

			// create product types, inspection types on tenant side
			loginTenant();
			if(!helper.isProductType(ie, masterProductType)) {
				helper.addProductType(ie, masterProductType, null, null, null, true, null, null, null, null, null);
			}
			if(!helper.isProductType(ie, subProductType)) {
				helper.addProductType(ie, subProductType, null, null, null, true, null, null, null, null, null);
			}
			if(!helper.isProductTypeOneASubProductOfProductTypeTwo(ie, subProductType, masterProductType))
				helper.AddProductTypeOneToBeSubProductOfProductTypeTwo(ie, subProductType, masterProductType);
			helper.addInspectionType(ie, masterInspectionType, inspectionTypeGroup, true, true, supportedProofTestTypes, null);
			helper.addInspectionTypeToProductType(ie, masterInspectionType, masterProductType);
			helper.logout(ie);
		}
	}
	
	private void loginManufacturer() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(manufacturer);
			ie.goTo(helper.getLoginURL());
			helper.loginBrandedDefaultRegular(ie, false);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	private void loginTenant() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			ie.goTo(helper.getLoginURL());
			helper.loginBrandedDefaultRegular(ie, false);
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	public void testIdentifyingRFIDOnTenantSide() throws Exception {
		String method = helper.getMethodName();

		try {
			helper.setEndUser(false);
			helper.setUserName(user);
			helper.setPassword(password);
			helper.setTenant(tenant);
			ie.goTo(helper.getLoginURL());
			ie.maximize();
			helper.loginBrandedDefaultRegular(ie, false);
			tenantMasterSerialNumber = helper.addProduct(ie, null, true, rfidNumber, null, null, null, null, null, null, null, null, null, null, masterProductType, null, null, null, "Save");
//			helper.validateProductInfoPage(ie, tenantMasterSerialNumber, false, false, false, null);
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
