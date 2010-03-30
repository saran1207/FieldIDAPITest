package com.n4systems.fieldid.testcase;

import junit.framework.TestCase;
import watij.runtime.ie.IE;

/**
 * Add the Assigned To field for Job Site customers.
 * 
 * The different extended features as well as some product type features will change how different pages are
 * validated. Here are the different test cases and why:
 * 
 * 1) Add Product page
 * 		a) JobSites, if enabled will add extra fields to the form and disable customer and division
 * 		b) Integration, if disabled will allow order field to appear on Add Products page
 * 2) Edit Product
 * 		a) JobSites, same deal as Add Product
 * 		b) Integration, same deal as Add Product
 * 		c) sub products, if a product is a sub product and attached to a master product you will not
 * 			be able to edit the customer/jobsite information
 * 
 * @author dgrainge
 *
 */
public class WEB_399 extends TestCase {

	IE ie = new IE();
	commonFieldIDMethods helper = new commonFieldIDMethods();
	boolean jobsite = true;
	boolean integration = true;
	boolean compliance = true;
	boolean subproduct = true;

	protected void setUp() throws Exception {
		super.setUp();
		helper.setBaseURL("https://localhost.localdomain/fieldid/");
		helper.setEndUser(false);
		helper.setPassword("Xk43g8!@");
		helper.setUserName("n4systems");
	}

	// no customer with this set up yet
	public void loginWithJobSitesWithIntegrationWithCompliance() throws Exception {
		helper.setTenant("key");	// with JobSites, with Integration
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.setupMasterAndSubProductTypes(ie, null);
	}
	
	public void loginWithJobSitesWithoutIntegrationWithoutCompliance() throws Exception {
		helper.setTenant("oceaneering");	// with JobSites, without Integration
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.setupMasterAndSubProductTypes(ie, null);
	}
	
	public void loginWithoutJobSitesWithIntegrationWithCompliance() throws Exception {
		helper.setTenant("n4systems");
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.setupMasterAndSubProductTypes(ie, null);
	}
	
	public void loginWithoutJobSitesWithoutIntegrationWithCompliance() throws Exception {
		helper.setTenant("navfac");
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.setupMasterAndSubProductTypes(ie, null);
	}
	
	public void loginWithoutJobSitesWithIntegrationWithoutCompliance() throws Exception {
		helper.setTenant("unirope");	// without JobSites, with Integration
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.setupMasterAndSubProductTypes(ie, null);
	}
	
	public void loginWithoutJobSitesWithoutIntegrationWithoutCompliance() throws Exception {
		helper.setTenant("swwr");	// without JobSites, without Integration
		helper.start(ie, helper.getLoginURL());
		helper.loginBrandedDefaultRegular(ie, false);
		helper.setupMasterAndSubProductTypes(ie, null);
	}
	
	// no customer with this set up yet
//	public void testAddproductWithJobSitesWithIntegration() throws Exception {
//		loginWithJobSitesWithIntegrationWithCompliance();
//		helper.gotoAddProduct(ie);
//		helper.validateAddProductPage(ie, jobsite, integration, subproduct);
//	helper.setupMasterAndSubProductTypes(ie, null);
//	}

	public void testAddproductWithJobSitesWithoutIntegration() throws Exception {
		loginWithJobSitesWithoutIntegrationWithoutCompliance();
		helper.gotoAddProduct(ie);
		helper.validateAddProductPage(ie, jobsite, !integration);
	}
	
	public void testAddproductWithoutJobSitesWithIntegration() throws Exception {
		loginWithoutJobSitesWithIntegrationWithoutCompliance();
		helper.gotoAddProduct(ie);
		helper.validateAddProductPage(ie, !jobsite, integration);
	}

	public void testAddproductWithoutJobSitesWithoutIntegration() throws Exception {
		loginWithoutJobSitesWithoutIntegrationWithoutCompliance();
		helper.gotoAddProduct(ie);
		helper.validateAddProductPage(ie, !jobsite, !integration);
	}	

	/*
	 * For Edit Product there are a number of different combinations
	 * and they all act slightly differently. If the tenant has Jobsites
	 * then the edit form will have an option to edit the job site and
	 * who the product is assigned to. If the tenant has Integration
	 * then the form will NOT have an Order Number field. The order
	 * number for customers with Integration comes from a databridge
	 * connection and not from the user.
	 * 
	 * Finally, the product can be a regular product, a subproduct or
	 * a master product. If it is a regular product or master product
	 * then we should be able to edit the customer and division (or
	 * job site and assigned to for customers with JobSites) but if
	 * the product is a subproduct (not just by definition but it is
	 * actually assigned to a master product) then we should NOT be
	 * able to edit the customer and division.
	 * 
	 * These tests will find a random product to use. For subproduct
	 * it, obviously, has to be a subproduct. If there is no product
	 * of type subproduct then this test will fail
	 * 
	 */
	public void testEditProductWithJobSitesWithIntegration() throws Exception {
		loginWithJobSitesWithIntegrationWithCompliance();
		String jsite = null;	// restrict customer to make it faster
		String serialNumber = "DL-4121";					// restrict serial number to make it faster
		String product = helper.getProduct(ie, null, serialNumber, null, null, null, null, jsite, null, null, null, null, null, null, null);
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, jobsite, integration, !subproduct);
		}
		else {
			throw new NoProductException();
		}
	}
	
	public void testEditProductWithJobSitesWithoutIntegration() throws Exception {
		loginWithJobSitesWithoutIntegrationWithoutCompliance();
		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "Save");
		String product = helper.getProduct(ie, null, serialNumber, null, null, null, null, null, null, null, null, null, null, null, null);
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, jobsite, !integration, !subproduct);
		}
		else {
			throw new NoProductException();
		}
	}
	
	public void testEditProductWithoutJobSitesWithIntegration() throws Exception {
		loginWithoutJobSitesWithIntegrationWithoutCompliance();
		String customer = "AVON ENGINEERING LTD.";	// restrict customer to make it faster
		String serialNumber = helper.addProduct(ie, null, true, null, null, customer, null, null, null, null, null, null, null, null, null, null, null, null, "Save");
		String product = helper.getProduct(ie, null, serialNumber, null, null, null, null, null, null, customer, null, null, null, null, null);
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, !jobsite, integration, !subproduct);
		}
		else {
			throw new NoProductException();
		}
	}
	
	public void testEditProductWithoutJobSitesWithoutIntegration() throws Exception {
		loginWithoutJobSitesWithoutIntegrationWithoutCompliance();
		String customer = "VISA/MASTERCARD";	// restrict customer to make it faster
		String serialNumber = helper.addProduct(ie, null, true, null, null, customer, null, null, null, null, null, null, null, null, null, null, null, null, "Save");
		String product = helper.getProduct(ie, null, serialNumber, null, null, null, null, null, null, customer, null, null, null, null, null);
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, !jobsite, !integration, !subproduct);
		}
		else {
			throw new NoProductException();
		}
	}
	
	public void testEditSubProductWithJobSitesWithIntegration() throws Exception {
		loginWithJobSitesWithIntegrationWithCompliance();
		String productType = "sub";				// need to add subproduct to this tenant
		String product = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, jobsite, integration, subproduct);
		}
		else {
			throw new NoSubProductException();
		}
	}
	
	public void testEditSubProductWithJobSitesWithoutIntegration() throws Exception {
		loginWithJobSitesWithoutIntegrationWithoutCompliance();
		String productType = "sub";				// need to add subproduct to this tenant
		String product = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, jobsite, !integration, subproduct);
		}
		else {
			throw new NoSubProductException();
		}
	}
	
	public void testEditSubProductWithoutJobSitesWithIntegration() throws Exception {
		loginWithoutJobSitesWithIntegrationWithoutCompliance();
		String productType = "sub";				// tenant has no sub products, need to add this.
		String product = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, !jobsite, integration, subproduct);
		}
		else {
			throw new NoSubProductException();
		}
	}
	
	public void testEditSubProductWithoutJobSitesWithoutIntegration() throws Exception {
		loginWithoutJobSitesWithoutIntegrationWithoutCompliance();
		String productType = "sub";				// tenant has no sub products, need to add this.
		String product = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, productType, null, null, null, "Save");
		if(product != null && !product.equals("")) {
			helper.gotoEditProduct(ie, product);
			helper.validateEditProductPage(ie, product, !jobsite, !integration, subproduct);
		}
		else {
			throw new NoSubProductException();
		}
	}
	
	// no tenants with this combination at the moment
//	public void testSubProductInfoJobSitesIntegrationCompliance() throws Exception {	// key
//		loginWithJobSitesWithIntegrationWithCompliance();
//		String prodType = "sub";
//		String product = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(product != null && !product.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, product);
//			helper.validateProductInfoPage(ie, product, jobsite, compliance, integration, productType);
//		}
//		else {
//			throw new NoSubProductException();
//		}
//	}

	// no tenants with this combination at the moment
//	public void testSubProductInfoJobSitesIntegration() throws Exception {
//		loginWithJobSitesWithIntegrationWithoutCompliance();
//		String prodType = "";
//		String serialNumber = helper.getSubProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, jobsite, !compliance, integration, productType);
//		}
//	else {
//		throw new NoSubProductException();
//	}
//	}

	// no tenants with this combination at the moment
//	public void testSubProductInfoJobSitesCompliance() throws Exception {
//		loginWithJobSitesWithOutIntegrationWithCompliance();
//		String prodType = "";
//		String serialNumber = helper.getSubProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, jobsite, compliance, !integration, productType);
//		}
//	else {
//		throw new NoSubProductException();
//	}
//	}

//	public void testSubProductInfoJobSites() throws Exception {	// oceaneering
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		String prodType = "sub";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, jobsite, !compliance, !integration, productType);
//		}
//		else {
//			throw new NoSubProductException();
//		}
//	}
//
////	public void testSubProductInfoIntegrationCompliance() throws Exception {	// TODO no tenant with integration, compliance
////		loginWithoutJobSitesWithIntegrationWithCompliance();
////		String prodType = "sub";
////		String serialNumber = helper.getSubProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.sub;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, !jobsite, compliance, integration, productType);
////		}
////		else {
////			throw new NoSubProductException();
////		}
////	}
////
//	public void testSubProductInfoIntegration() throws Exception {	// unirope
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		String prodType = "sub";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, !compliance, integration, productType);
//		}
//		else {
//			throw new NoSubProductException();
//		}
//	}
//
//	public void testSubProductInfoCompliance() throws Exception {	// navfac
//		loginWithoutJobSitesWithoutIntegrationWithCompliance();
//		String prodType = "";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, compliance, !integration, productType);
//		}
//		else {
//			throw new NoSubProductException();
//		}
//	}
//
//	public void testSubProductInfo() throws Exception {	// swwr
//		loginWithoutJobSitesWithoutIntegrationWithoutCompliance();
//		String prodType = "sub";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.sub;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, !compliance, !integration, productType);
//		}
//		else {
//			throw new NoSubProductException();
//		}
//	}
//
//	// no tenant with this configuration
////	public void testMasterProductInfoJobSitesIntegrationCompliance() throws Exception {	// key
////		loginWithJobSitesWithIntegrationWithCompliance();
////		String prodType = "master";
////		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.master;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, jobsite, compliance, integration, productType);
////		}
////		else {
////			throw new NoMasterProductException();
////		}
////	}
//
//	// no tenants with this combination at the moment
////	public void testMasterProductInfoJobSitesIntegration() throws Exception {
////		loginWithJobSitesWithIntegrationWithoutCompliance();
////		String prodType = "";
////		String serialNumber = helper.getMasterProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.master;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, jobsite, !compliance, integration, productType);
////		}
////	else {
////		throw new NoMasterProductException();
////	}
////	}
//
//	// no tenants with this combination at the moment
////	public void testMasterProductInfoJobSitesCompliance() throws Exception {
////		loginWithJobSitesWithOutIntegrationWithCompliance();
////		String prodType = "";
////		String serialNumber = helper.getMasterProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.master;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, jobsite, compliance, !integration, productType);
////		}
////	else {
////		throw new NoMasterProductException();
////	}
////	}
//
//	public void testMasterProductInfoJobSites() throws Exception {	// oceaneering
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		String prodType = "master";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.master;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, jobsite, !compliance, !integration, productType);
//		}
//		else {
//			throw new NoMasterProductException();
//		}
//	}
//
////	public void testMasterProductInfoIntegrationCompliance() throws Exception {	// TODO no tenant with integration, compliance
////		loginWithoutJobSitesWithIntegrationWithCompliance();
////		String prodType = "master";
////		String serialNumber = helper.getMasterProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.master;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, !jobsite, compliance, integration, productType);
////		}
////		else {
////			throw new NoMasterProductException();
////		}
////	}
//
//	public void testMasterProductInfoIntegration() throws Exception {	// unirope
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		String prodType = "master";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.master;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, !compliance, integration, productType);
//		}
//		else {
//			throw new NoMasterProductException();
//		}
//	}
//
//	public void testMasterProductInfoCompliance() throws Exception {	// navfac
//		loginWithoutJobSitesWithoutIntegrationWithCompliance();
//		String prodType = "";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.master;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, compliance, !integration, productType);
//		}
//		else {
//			throw new NoMasterProductException();
//		}
//	}
//
//	public void testMasterProductInfo() throws Exception {	// swwr
//		loginWithoutJobSitesWithoutIntegrationWithoutCompliance();
//		String prodType = "master";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.master;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, !compliance, !integration, productType);
//		}
//		else {
//			throw new NoMasterProductException();
//		}
//	}
//
////	public void testProductInfoJobSitesIntegrationCompliance() throws Exception {	// key
////		loginWithJobSitesWithIntegrationWithCompliance();
////		String prodType = "";
////		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.regular;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, jobsite, compliance, integration, productType);
////		}
////		else {
////			throw new NoProductException();
////		}
////	}
//
//	// no tenants with this combination at the moment
////	public void testProductInfoJobSitesIntegration() throws Exception {
////		loginWithJobSitesWithIntegrationWithoutCompliance();
////		String prodType = "";
////		String serialNumber = helper.getProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.regular;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, jobsite, !compliance, integration, productType);
////		}
////	else {
////		throw new NoProductException();
////	}
////	}
//
//	// no tenants with this combination at the moment
////	public void testProductInfoJobSitesCompliance() throws Exception {
////		loginWithJobSitesWithOutIntegrationWithCompliance();
////		String prodType = "";
////		String serialNumber = helper.getProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.regular;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, jobsite, compliance, !integration, productType);
////		}
////	else {
////		throw new NoProductException();
////	}
////	}
//
//	public void testProductInfoJobSites() throws Exception {	// oceaneering
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		String prodType = "";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.regular;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, jobsite, !compliance, !integration, productType);
//		}
//		else {
//			throw new NoProductException();
//		}
//	}
//
////	public void testProductInfoIntegrationCompliance() throws Exception {	// TODO no tenant with integration, compliance
////		loginWithoutJobSitesWithIntegrationWithCompliance();
////		String prodType = "";
////		String serialNumber = helper.getProduct(ie, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null);
////		if(serialNumber != null && !serialNumber.equals("")) {
////			ProductType productType = ProductType.regular;
////			helper.gotoProductInfo(ie, serialNumber);
////			helper.validateProductInfoPage(ie, serialNumber, !jobsite, compliance, integration, productType);
////		}
////		else {
////			throw new NoProductException();
////		}
////	}
//
//	public void testProductInfoIntegration() throws Exception {	// unirope
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		String prodType = "";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.regular;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, !compliance, integration, productType);
//		}
//		else {
//			throw new NoProductException();
//		}
//	}
//
//	public void testProductInfoCompliance() throws Exception {	// navfac
//		loginWithoutJobSitesWithoutIntegrationWithCompliance();
//		String prodType = "";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.master;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, compliance, !integration, productType);
//		}
//		else {
//			throw new NoProductException();
//		}
//	}
//
//	public void testProductInfo() throws Exception {	// swwr
//		loginWithoutJobSitesWithoutIntegrationWithoutCompliance();
//		String prodType = "";
//		String serialNumber = helper.addProduct(ie, null, true, null, null, null, null, null, null, null, null, null, null, null, prodType, null, null, null, "Save");
//		if(serialNumber != null && !serialNumber.equals("")) {
//			ProductType productType = ProductType.regular;
//			helper.gotoProductInfo(ie, serialNumber);
//			helper.validateProductInfoPage(ie, serialNumber, !jobsite, !compliance, !integration, productType);
//		}
//		else {
//			throw new NoProductException();
//		}
//	}
//	
//	public void testSearchWithJobSites() throws Exception {
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		helper.gotoAssets(ie);
//		helper.validateAssets(ie, jobsite);
//	}
//	
//	public void testSearchWithoutJobSites() throws Exception {
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		helper.gotoAssets(ie);
//		helper.validateAssets(ie, !jobsite);
//	}
//	
//	public void testReportWithJobSites() throws Exception {
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		helper.gotoReporting(ie);
//		helper.validateReporting(ie, jobsite);
//	}
//	
//	public void testReportWithoutJobSites() throws Exception {
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		helper.gotoReporting(ie);
//		helper.validateReporting(ie, !jobsite);
//	}
//	
//	public void testScheduleWithJobSites() throws Exception {
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		helper.gotoSchedule(ie);
//		helper.validateSchedule(ie, jobsite);
//	}
//	
//	public void testScheduleWithoutJobSites() throws Exception {
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		helper.gotoSchedule(ie);
//		helper.validateSchedule(ie, !jobsite);
//	}
//	
//	public void testMassUpdateProductWithJobSites() throws Exception {
//		loginWithJobSitesWithoutIntegrationWithoutCompliance();
//		helper.gotoMassUpdateProducts(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
//		helper.validateProductMassUpdate(ie, jobsite);
//	}
//
//	public void testMassUpdateProductWithoutJobSites() throws Exception {
//		loginWithoutJobSitesWithIntegrationWithoutCompliance();
//		helper.gotoMassUpdateProducts(ie, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
//		helper.validateProductMassUpdate(ie, !jobsite);
//	}

	protected void tearDown() throws Exception {
		super.tearDown(); helper.stopMonitorStatus();
		ie.close();
	}
}
