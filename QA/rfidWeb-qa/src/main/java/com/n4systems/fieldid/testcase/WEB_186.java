package com.n4systems.fieldid.testcase;

import java.util.Random;
import watij.runtime.ie.IE;

/**
 * Ability to delete and retire product types
 * 
 * @author Darrell
 *
 */
public class WEB_186 extends FieldIDTestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	Random n = new Random();
	final String tenant = "unirope";
	final String user = "n4systems";
	final String password = "Xk43g8!@";
	static boolean initialized = false;
	static String timestamp = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		// create a new directory for each test run but not for each test case
		if (!initialized) {
			initialized = true;
			timestamp = helper.createTimestampDirectory() + "/";
		}
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setTenant(tenant);
		helper.setUserName(user);
		helper.setPassword(password);
		helper.start(ie, helper.getLoginURL());	// login screen
		ie.maximize();					// maximize window for screen shots
		helper.loginBrandedRegular(ie, helper.getUserName(), helper.getPassword(), false);	// login
	}

	/**
	 * This will test that I can create then delete a product type.
	 * 
	 * @throws Exception
	 */
	public void testDeletingRegularProductType() throws Exception {
		String method = helper.getMethodName();
		try {
			// create a random product type
			String productType = helper.getMethodName() + n.nextInt();
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			// delete the new product type
			helper.deleteProductType(ie, productType);
			helper.gotoManageProductTypes(ie);
			if(helper.isProductType(ie, productType)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * This is like the testDeletingRegularProductType but we fully populate all
	 * the fields of the product type. 
	 * 
	 * @throws Exception
	 */
	public void testDeletingFullyDefinedproductType() throws Exception {
		String method = helper.getMethodName();
		try {
			int num = n.nextInt();
			String productType = helper.getMethodName() + num;
			String warnings = "warnings\n" + num;
			String instructions = "instructions\n" + num;
			String cautionURL = "http://www.google.ca/search?hl=en&q=caution%20" + num;
			boolean hasManufacturerCert = true;
			String manufacturerCertText = "Now is the time for all good men to come to the aid of their party\nmanufacturerCertText\n" + num;
			String productDescriptionTemplate = "productDescriptionTemplate-" + num;
			ProductTypeAttributes[] attributes = null;
			String[] attachments = null;
			String productImage = null;
			helper.addProductType(ie, productType, warnings, instructions, cautionURL, hasManufacturerCert, manufacturerCertText, productDescriptionTemplate, productImage, attributes, attachments);
			// delete the new product type
			helper.deleteProductType(ie, productType);
			helper.gotoManageProductTypes(ie);
			if(helper.isProductType(ie, productType)) {
				throw new TestCaseFailedException();
			}
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + method + ".png", ie);
			throw e;
		}
	}
	
	/**
	 * This tests deleting a product type when instances of the product
	 * have been identified. The product type will be "web-186-" plus the
	 * serial number. The deleteProductType will create a screen shot of
	 * the confirmation screen. From that you should be able to find the
	 * serial number and confirm the product was deleted.
	 * 
	 * @throws Exception
	 */
	public void testDeletingRegularProductTypeWithProducts() throws Exception {
		String productType = helper.getMethodName();;
		try {
			// create a random product type
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			// identify a product of that type
			helper.addProduct(ie, serialNumber, false, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
			// delete the new product type
			helper.deleteProductType(ie, productType);
			if(helper.isProductType(ie, productType)) {
				throw new TestCaseFailedException();
			}
			if(helper.isProduct(ie, serialNumber))
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This tests deleting a product type when instance of the product
	 * have been identified and inspected.
	 * 
	 * @throws Exception
	 */
	public void testDeletingRegularProductTypeWithInspections() throws Exception {
		String productType = helper.getMethodName();
		try {
			// create a random product type
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			String inspectionTypeName = "web-186-inspect_" + serialNumber;
			String group = null;
			boolean printable = true;
			boolean master = false;
			String[] supportedProofTestTypes = null;
			String[] attributes = null;
			helper.addInspectionType(ie, inspectionTypeName, group, printable, master, supportedProofTestTypes, attributes);
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			helper.addInspectionTypeToProductType(ie, inspectionTypeName, productType);
			helper.addProduct(ie, serialNumber, false, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save And Inspect");
			helper.addInspectionToProduct(ie, inspectionTypeName, serialNumber, false);
			helper.deleteProductType(ie, productType);
			if(helper.isProductType(ie, productType)) {
				throw new TestCaseFailedException();
			}
			if(helper.isProduct(ie, serialNumber))
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	public void testDeletingRegularProductTypeWithInspectionsScheduled() throws Exception {
		String productType = helper.getMethodName();
		try {
			// create a random product type
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			String inspectionTypeName = "web-186-inspect_" + serialNumber;
			String group = null;
			boolean printable = true;
			boolean master = false;
			String[] supportedProofTestTypes = null;
			String[] attributes = null;
			helper.addInspectionType(ie, inspectionTypeName, group, printable, master, supportedProofTestTypes, attributes);
			helper.addProductType(ie, productType, null, null, null, false, null, null, null, null, null);
			helper.addInspectionTypeToProductType(ie, inspectionTypeName, productType);
			String frequencyInDays = "365";
			boolean autoScheduled = true;
			helper.addInspectionScheduleToProductType(ie, inspectionTypeName, productType, frequencyInDays, autoScheduled);
			helper.addProduct(ie, serialNumber, false, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save And Inspect");
			helper.addInspectionToProduct(ie, inspectionTypeName, serialNumber, false);
			helper.deleteProductType(ie, productType);
			if(helper.isProductType(ie, productType)) {
				throw new TestCaseFailedException();
			}
			if(helper.isProduct(ie, serialNumber))
				throw new TestCaseFailedException();
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This will test that I can create then delete a product type.
	 * 
	 * @throws Exception
	 */
	public void testDeletingMasterProductType() throws Exception {
		String masterProductType = helper.getMethodName();
		try {
			String serialNumber = "" + n.nextInt();
			masterProductType = helper.getMethodName() + serialNumber;
			String subProductType = helper.getMethodName() + "sub" + serialNumber;
			helper.addProductType(ie, masterProductType, null, null, null, false, null, null, null, null, null);
			helper.addProductType(ie, subProductType, null, null, null, false, null, null, null, null, null);
			helper.AddProductTypeOneToBeSubProductOfProductTypeTwo(ie, subProductType, masterProductType);
			// TODO:
			// create a random master inspection type
			// add the master inspection type to the master product type
			// link the master and sub product types
			// delete the new master product type
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + masterProductType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This tests deleting a product type when instances of the product
	 * have been identified.
	 * 
	 * @throws Exception
	 */
	public void testDeletingMasterProductTypeWithProducts() throws Exception {
		String productType = helper.getMethodName();
		try {
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			// TODO:
			// create a random sub product type
			// create a random master product type
			// create a random master inspection type
			// add the master inspection type to the master product type
			// link the master and sub product types
			// identify a master product and add a sub product to it
			// delete the new master product type
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This tests deleting a product type when instance of the product
	 * have been identified and inspected.
	 * 
	 * @throws Exception
	 */
	public void testDeletingMasterProductTypeWithInspections() throws Exception {
		String productType = helper.getMethodName();
		try {
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			// TODO:
			// create a random sub product type
			// create a random master product type
			// create a random master inspection type
			// add the master inspection type to the master product type
			// link the master and sub product types
			// identify a master product and add a sub product to it
			// inspect the master product
			// delete the new master product type
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This will test that I can create then delete a product type.
	 * 
	 * @throws Exception
	 */
	public void testDeletingSubProductType() throws Exception {
		String productType = helper.getMethodName();
		try {
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			// TODO:
			// create a random sub product type
			// create a random master product type
			// create a random master inspection type
			// add the master inspection type to the master product type
			// link the master and sub product types
			// delete the new sub product type
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This tests deleting a product type when instances of the product
	 * have been identified.
	 * 
	 * @throws Exception
	 */
	public void testDeletingSubProductTypeWithProducts() throws Exception {
		String productType = helper.getMethodName();
		try {
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			// TODO:
			// create a random sub product type
			// create a random master product type
			// create a random master inspection type
			// add the master inspection type to the master product type
			// link the master and sub product types
			// identify a master product and add a sub product to it
			// delete the new sub product type
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	/**
	 * This tests deleting a product type when instance of the product
	 * have been identified and inspected.
	 * 
	 * @throws Exception
	 */
	public void testDeletingSubProductTypeWithInspections() throws Exception {
		String productType = helper.getMethodName();
		try {
			String serialNumber = "" + n.nextInt();
			productType = helper.getMethodName() + serialNumber;
			// TODO:
			// create a random sub product type
			// create a random master product type
			// create a random master inspection type
			// create a random standard inspection type
			// add the master inspection type to the master product type
			// add the standard inspection type to the sub product type
			// link the master and sub product types
			// identify a master product and add a sub product to it
			// inspect the sub product
			// delete the new sub product type
		} catch (Exception e) {
			helper.myWindowCapture(timestamp + "/FAILURE-" + productType + ".png", ie);
			throw e;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}

}
