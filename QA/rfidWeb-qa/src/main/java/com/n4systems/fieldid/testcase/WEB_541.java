package com.n4systems.fieldid.testcase;

import java.util.Random;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * I cannot add sub-products to a master product.
 * Clicking the Save button appears to do nothing.
 * Exceptions thrown in the log.
 *  
 * @author Darrell
 *
 */
public class WEB_541 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	
	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setUserName("n4systems");
		helper.setPassword("makemore$");
		helper.setTenant("oceaneering");
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		ie.maximize();
	}
	
	public void testAddLookedUpSubProductsToMasterProduct() throws Exception {
		// See if 'sub' product type exists, create it if it does not.
		String subProductType = "sub";
		if(!helper.isProductType(ie, subProductType)) {
			helper.addProductType(ie, subProductType, null, null, null, false, null, null, null, null, null);
		}

		// See if 'master' product type exists, create it if it does not.
		String masterProductType = "master";
		if(!helper.isProductType(ie, masterProductType)) {
			helper.addProductType(ie, masterProductType, null, null, null, false, null, null, null, null, null);
		}
		
		// See if 'sub' product type is a sub-product of 'master', configure it if it is not.
		if(!helper.isProductTypeOneASubProductOfProductTypeTwo(ie, subProductType, masterProductType))
			helper.AddProductTypeOneToBeSubProductOfProductTypeTwo(ie, subProductType, masterProductType);

		// create an instance of 'sub' product
		Random n = new Random();
		String subSerialNumber = "test" + n.nextInt(10000);
		String identified = "02/05/09";
		subSerialNumber = helper.addProduct(ie, subSerialNumber, false, null, null, null, null, null, null, null, null, null, identified, null, subProductType, null, null, null, "Save");
		System.out.println(subSerialNumber);
		
		// create an instance of 'master' product
		String masterSerialNumber = "test" + n.nextInt(10000);
		masterSerialNumber = helper.addProduct(ie, masterSerialNumber, false, null, null, null, null, null, null, null, null, null, identified, null, masterProductType, null, null, null, "Save");
		System.out.println(masterSerialNumber);
		
		String label = subSerialNumber;
		helper.addSubProductToMasterProduct(ie, subSerialNumber, subProductType, masterSerialNumber, label);
		
		// TODO: validate the sub product / master product link
		if(!helper.isProductOneASubProductOfProductTwo(ie, subSerialNumber, masterSerialNumber)) {
			throw new TestCaseFailedException();
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}

}
