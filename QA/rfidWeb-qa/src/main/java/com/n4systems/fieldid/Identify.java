package com.n4systems.fieldid;

import static watij.finders.FinderFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.Owner;
import com.n4systems.fieldid.datatypes.Product;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import watij.runtime.ie.IEHtmlElement;
import junit.framework.TestCase;

public class Identify extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "identify.properties";
	FieldIDMisc misc;
	Home home;
	Finder identifyFinder;
	Finder addProductContentHeaderFinder;
	Finder identifyProductContentHeaderFinder;
	Finder addProductButtonFinder;
	Finder orderNumberTextFieldFinder;
	Finder loadOrderNumberButtonFinder;
	Finder orderDetailsHeaderFinder;
	Finder orderDetailsOrderNumberFieldFinder;
	Finder orderDetailsOrderDateFieldFinder;
	Finder orderDetailsDescriptionFieldFinder;
	Finder orderDetailsPurchaseOrderFieldFinder;
	Finder orderDetailsCustomerFieldFinder;
	Finder orderDetailsDivisionFieldFinder;
	Finder orderDetailsSalesAgentFieldFinder;
	Finder orderDetailsLineItemsHeaderFinder;
	Finder lineItemLinksFinder;
	Finder lineItemDescriptionsFinder;
	Finder lineItemQuantitiesFinder;
	Finder lineItemIdentifiedFinder;
	Finder orderNumberValueFinder;
	Finder orderDateValueFinder;
	Finder orderDescriptionValueFinder;
	Finder orderPurchaseOrderValueFinder;
	Finder orderCustomerValueFinder;
	Finder orderDivisionValueFinder;
	Finder orderSalesAgentValueFinder;
	Finder addProductSerialNumberFinder;
	Finder addProductGenerateFinder;
	Finder addProductRFIDNumberFinder;
	Finder addProductPurchaseOrderFinder;
	Finder addProductJobSiteFinder;			// if 'jobsite' and 'assigned to' exist
	Finder addProductAssignedToFinder;		// then 'customer' and 'division' will not
	Finder addProductCustomerFinder;
	Finder addProductDivisionFinder;
	Finder addProductLocationFinder;
	Finder addProductProductStatusFinder;
	Finder addProductReferenceNumberFinder;
	Finder addProductIdentifiedFinder;
	Finder addProductOrderNumberFinder;		// will not exist for customers with integration
	Finder addProductProductTypeFinder;
	Finder addProductCommentsFinder;
	Finder addProductCommentTemplatesFinder;
	Finder addProductRequiredInputFieldsFinder;	// LABELS; FOR attribute will hold ID for input fields required
	Finder addProductRequiredUOMFieldsFinder;	// Unit Of Measure inputs are slightly different xpath
	Finder orderResultsDivFinder;
	String orderResultsEmptyString;
	Finder addMultipleAssetsFinder;
	Finder addWithOrderFinder;
	Finder addFinder;
	Finder addMultipleAssetsPageContentHeaderFinder;
	Finder addMultipleAssetsStep1ContinueButtonFinder;
	Finder addMultipleAssetsStep1BodyFinder;
	Finder addMultipleAssetsCustomerFinder;
	Finder addMultipleAssetsDivisionFinder;
	Finder addMultipleAssetsJobSiteFinder;
	Finder addMultipleAssetsLocationFinder;
	Finder addMultipleAssetsProductStatusFinder;
	Finder addMultipleAssetsPurchaseOrderFinder;
	Finder addMultipleAssetsIdentifiedFinder;
	Finder addMultipleAssetsProductTypeFinder;
	Finder addMultipleAssetsCommentsFinder;
	Finder addMultipleAssetsCommentTemplatesFinder;
	private Finder backtoStep1LinkFinder;
	private Finder addMultipleAssetsQuantityFinder;
	private Finder addMultipleAssetsStep2ContinueButtonFinder;
	private Finder addMultipleAssetsStep2BodyFinder;
	private Finder backtoStep2LinkFinder;
	private Finder addMultipleAssetsStep3ContinueButtonFinder;
	private Finder backtoStep3LinkFinder;
	private Finder addMultipleAssetsStep3RangeFinder;
	private Finder addMultipleAssetsStep3PrefixFinder;
	private Finder addMultipleAssetsStep3StartFinder;
	private Finder addMultipleAssetsStep3SuffixFinder;
	private Finder addMultipleAssetsStep3BatchFinder;
	private Finder addMultipleAssetsStep3IdentifierFinder;
	private Finder addMultipleAssetsStep3ManualFinder;
	private Finder addMultipleAssetsStep3GenerateFinder;
	private Finder addMultipleAssetsSaveButtonFinder;
	private Finder addMultipleAssetsCancelButtonFinder;
	private Finder addProductSaveButtonFinder;
	private Finder addProductSaveAndInspectButtonFinder;
	private Finder addProductSaveAndPrintButtonFinder;
	private Finder addProductSaveAndScheduleButtonFinder;
	private Finder addProductResetFormButtonFinder;
	private Finder attachFileButtonFinder;
	private Finder addProductPublishFinder;
	
	public Identify(IE ie) {
		this.ie = ie;
		try {
			misc = new FieldIDMisc(ie);
			home = new Home(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			addProductPublishFinder = xpath(p.getProperty("addproductpublished"));
			identifyFinder = id(p.getProperty("link"));
			addProductContentHeaderFinder = xpath(p.getProperty("addproductcontentheader"));
			identifyProductContentHeaderFinder = xpath(p.getProperty("identifyproductcontentheader"));
			addProductButtonFinder = xpath(p.getProperty("addproductbutton"));
			orderNumberTextFieldFinder = id(p.getProperty("ordernumbertextfield"));
			loadOrderNumberButtonFinder = id(p.getProperty("loadordernumberbutton"));
			orderDetailsHeaderFinder = xpath(p.getProperty("orderdetailsheader"));
			orderDetailsOrderNumberFieldFinder = xpath(p.getProperty("orderdetailsordernumber"));
			orderDetailsOrderDateFieldFinder = xpath(p.getProperty("orderdetailsorderdate"));
			orderDetailsDescriptionFieldFinder = xpath(p.getProperty("orderdetailsdescription"));
			orderDetailsPurchaseOrderFieldFinder = xpath(p.getProperty("orderdetailspurchaseorder"));
			orderDetailsCustomerFieldFinder = xpath(p.getProperty("orderdetailscustomer"));
			orderDetailsDivisionFieldFinder = xpath(p.getProperty("orderdetailsdivision"));
			orderDetailsSalesAgentFieldFinder = xpath(p.getProperty("orderdetailssalesagent"));
			orderDetailsLineItemsHeaderFinder = xpath(p.getProperty("orderdetailslineitem"));
			lineItemLinksFinder = xpath(p.getProperty("lineitemlinks"));
			lineItemDescriptionsFinder = xpath(p.getProperty("lineitemdescriptions"));
			lineItemQuantitiesFinder = xpath(p.getProperty("lineitemquantities"));
			lineItemIdentifiedFinder = xpath(p.getProperty("lineitemsidentified"));
			orderNumberValueFinder = xpath(p.getProperty("ordernumbervalue"));
			orderDateValueFinder = xpath(p.getProperty("orderdatevalue"));
			orderDescriptionValueFinder = xpath(p.getProperty("orderdescriptionvalue"));
			orderPurchaseOrderValueFinder = xpath(p.getProperty("orderpurchaseordervalue"));
			orderCustomerValueFinder = xpath(p.getProperty("ordercustomervalue"));
			orderDivisionValueFinder = xpath(p.getProperty("orderdivisionvalue"));
			orderSalesAgentValueFinder = xpath(p.getProperty("ordersalesagentvalue"));
			addProductSerialNumberFinder = id(p.getProperty("serialnumber"));
			addProductGenerateFinder = text(p.getProperty("generate"));
			addProductRFIDNumberFinder = id(p.getProperty("rfidnumber"));
			addProductPurchaseOrderFinder = id(p.getProperty("purchaseorder"));
			addProductJobSiteFinder = id(p.getProperty("jobsite"));
			addProductAssignedToFinder = id(p.getProperty("assignedto"));
			addProductCustomerFinder = id(p.getProperty("customer"));
			addProductDivisionFinder = id(p.getProperty("division"));
			addProductLocationFinder = id(p.getProperty("location"));
			addProductProductStatusFinder = id(p.getProperty("productstatus"));
			addProductReferenceNumberFinder = id(p.getProperty("referencenumber"));
			addProductIdentifiedFinder = id(p.getProperty("identified"));
			addProductOrderNumberFinder = id(p.getProperty("ordernumber"));
			addProductProductTypeFinder = id(p.getProperty("producttype"));
			addProductCommentsFinder = id(p.getProperty("comments"));
			addProductCommentTemplatesFinder = xpath(p.getProperty("commenttemplate"));
			addProductRequiredInputFieldsFinder = xpath(p.getProperty("requiredinputfields"));
			addProductRequiredUOMFieldsFinder = xpath(p.getProperty("requireduomfields"));
			orderResultsDivFinder = xpath(p.getProperty("orderresultsdiv"));
			orderResultsEmptyString = p.getProperty("orderresultsemptystring");
			addMultipleAssetsFinder = xpath(p.getProperty("addmultipleassets"));
			addWithOrderFinder = xpath(p.getProperty("addwithorder"));
			addFinder = xpath(p.getProperty("add"));
			addMultipleAssetsPageContentHeaderFinder = xpath(p.getProperty("addmultipleassetspagecontentheader"));
			addMultipleAssetsStep1ContinueButtonFinder = id(p.getProperty("addmultiplestep1continuebutton"));
			addMultipleAssetsStep1BodyFinder = id(p.getProperty("addmultiplestep1body"));
			addMultipleAssetsCustomerFinder = id(p.getProperty("addmultiplecustomer"));
			addMultipleAssetsDivisionFinder = id(p.getProperty("addmultipledivision"));
			addMultipleAssetsJobSiteFinder = id(p.getProperty("addmultiplejobsite"));
			addMultipleAssetsLocationFinder = id(p.getProperty("addmultiplelocation"));
			addMultipleAssetsProductStatusFinder = id(p.getProperty("addmultipleproductstatus"));
			addMultipleAssetsPurchaseOrderFinder = id(p.getProperty("addmultiplepurchaseorder"));
			addMultipleAssetsIdentifiedFinder = id(p.getProperty("addmultipleidentified"));
			addMultipleAssetsProductTypeFinder = id(p.getProperty("addmultipleproducttype"));
			addMultipleAssetsCommentsFinder = id(p.getProperty("addmultiplecomments"));
			addMultipleAssetsCommentTemplatesFinder = xpath(p.getProperty("addmultiplecommenttemplates"));
			backtoStep1LinkFinder = xpath(p.getProperty("backtostep1"));
			addMultipleAssetsQuantityFinder = xpath(p.getProperty("addmultiplestep2quantity"));
			addMultipleAssetsStep2ContinueButtonFinder = xpath(p.getProperty("addmultiplestep2continuebutton"));
			addMultipleAssetsStep2BodyFinder = id(p.getProperty("addmultiplestep2body"));
			backtoStep2LinkFinder = xpath(p.getProperty("backtostep2"));
			addMultipleAssetsStep3ContinueButtonFinder = xpath(p.getProperty("addmultiplestep3continuebutton"));
			backtoStep3LinkFinder = xpath(p.getProperty("backtostep3"));
			addMultipleAssetsStep3RangeFinder = xpath(p.getProperty("step3rangebutton"));
			addMultipleAssetsStep3PrefixFinder = xpath(p.getProperty("step3prefix"));
			addMultipleAssetsStep3StartFinder = xpath(p.getProperty("step3start"));
			addMultipleAssetsStep3SuffixFinder = xpath(p.getProperty("step3suffix"));
			addMultipleAssetsStep3BatchFinder = xpath(p.getProperty("step3batchbutton"));
			addMultipleAssetsStep3IdentifierFinder = xpath(p.getProperty("step3identifier"));
			addMultipleAssetsStep3ManualFinder = xpath(p.getProperty("step3manualbutton"));
			addMultipleAssetsStep3GenerateFinder = xpath(p.getProperty("step3generatebutton"));
			addMultipleAssetsSaveButtonFinder = xpath(p.getProperty("step4saveandcreatebutton"));
			addMultipleAssetsCancelButtonFinder = xpath(p.getProperty("addmultipleassetscancelbutton"));
			addProductSaveButtonFinder = xpath(p.getProperty("addproductsavebutton"));
			addProductSaveAndInspectButtonFinder = xpath(p.getProperty("addproductsaveinspectbutton"));
			addProductSaveAndPrintButtonFinder = xpath(p.getProperty("addproductsaveprintbutton"));
			addProductSaveAndScheduleButtonFinder = xpath(p.getProperty("addproductsaveschedulebutton"));
			addProductResetFormButtonFinder = xpath(p.getProperty("addproductresetformbutton"));
			attachFileButtonFinder = xpath(p.getProperty("attachfilebutton"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	private void clickIdentifyIcon() throws Exception {
		Link identifyLink = ie.link(identifyFinder);
		assertTrue("Could not find the anchor to Identify page", identifyLink.exists());
		identifyLink.click();
		ie.waitUntilReady();
	}
	
	public void gotoAddMultipleAssets() throws Exception {
		Link addMultipleAssets = ie.link(addMultipleAssetsFinder);
		assertTrue("Could not find the link to Add Multiple Assets", addMultipleAssets.exists());
		addMultipleAssets.click();
		checkAddMultipleAssetsPageContentHeader();
	}

	private void checkAddMultipleAssetsPageContentHeader() throws Exception {
		HtmlElement addMultipleAssetsContentHeader = ie.htmlElement(addMultipleAssetsPageContentHeaderFinder);
		assertTrue("Could not find Add Multiple Assets page content header", addMultipleAssetsContentHeader.exists());
	}

	/**
	 * Add a new product. Works for integration and non-integration customers.
	 * If the customer has integration it will skip the load order page and go
	 * on to the Add Product page. This is going to the "Add" page.
	 * 
	 * @throws Exception
	 */
	public void gotoAddProduct() throws Exception {
		clickIdentifyIcon();
		Link add = getAddLink();
		if(add.exists()) {
			add.click();
		}
		checkIdentifyProductPageContentHeader();
	}
	
	public void gotoAdd() throws Exception {
		gotoAddProductFromIdentifyProduct();
	}
	
	private void gotoAddProductFromIdentifyProduct() throws Exception {
		Link addProduct = getAddLink();
		assertTrue("Could not find the Add Product Link on Identify Products page", addProduct.exists());
		addProduct.click();
		ie.waitUntilReady();
	}

	private Link getAddLink() throws Exception {
		Link addProduct = ie.link(addFinder);
		return addProduct;
	}
	
	public void checkAddProductPageContentHeader() throws Exception {
		HtmlElement addProductContentHeader = ie.htmlElement(addProductContentHeaderFinder);
		assertTrue("Could not find Add Product page content header '" + p.getProperty("addproductcontentheader") + "'", addProductContentHeader.exists());
	}
	
	/**
	 * Goes to the Identify Product Page. This page is only available for
	 * tenants with Integration. If you call this method on a tenant without
	 * Integration, the test case will fail. This is going to "Add with Order". 
	 * 
	 * @throws Exception
	 */
	public void gotoIdentifyProducts() throws Exception {
		clickIdentifyIcon();
		checkIdentifyProductPageContentHeader();
	}

	/**
	 * All the pages for Identify, which include 'Add Multiple Assets',
	 * 'Add with Order' and 'Add', should have the same page header.
	 * @throws Exception
	 */
	public void checkIdentifyProductPageContentHeader() throws Exception {
		HtmlElement identifyProductContentHeader = ie.htmlElement(identifyProductContentHeaderFinder);
		assertTrue("Could not find Identify Product page content header '" + p.getProperty("identifyproductcontentheader") + "'", identifyProductContentHeader.exists());
	}
	
	/**
	 * This method (1) assumes the tenant has Integration and (2) the order
	 * number is a valid number. If either item is false this method will
	 * fail the test case.
	 * 
	 * You must be on the Identify Products page before calling this method.
	 * 
	 * @param orderNumber
	 * @throws Exception
	 */
	public void gotoOrderNumber(String orderNumber) throws Exception {
		assertNotNull(orderNumber);
		TextField orderNumberTextField = ie.textField(orderNumberTextFieldFinder);
		assertTrue("Could not find the text field for Order Number on Identify Product page..", orderNumberTextField.exists());
		orderNumberTextField.set(orderNumber);
		Button loadOrderNumberButton = ie.button(loadOrderNumberButtonFinder);
		assertTrue("Could not find the Load button for Order Number on Identify Product page.", loadOrderNumberButton.exists());
		loadOrderNumberButton.click();
		ie.waitUntilReady();
		checkIdentifyProductPageContentHeader();
		checkIdentifyProductOrderDetails(orderNumber);
	}

	private void checkIdentifyProductOrderDetails(String order) throws Exception {
		assertNotNull(order);
		Div orderResultsDiv = ie.div(orderResultsDivFinder);
		assertTrue("Could not find the order result.", orderResultsDiv.exists());
		assertFalse(orderResultsEmptyString + ": '" + order + "'", orderResultsDiv.text().contains(orderResultsEmptyString));
		HtmlElement orderDetailsHeader = ie.htmlElement(orderDetailsHeaderFinder);
		assertTrue("Could not find the Order Details header", orderDetailsHeader.exists());
		Label orderNumber = ie.label(orderDetailsOrderNumberFieldFinder);
		assertTrue("Could not find the Order Number field in the Order Details table", orderNumber.exists());
		Label orderDate = ie.label(orderDetailsOrderDateFieldFinder);
		assertTrue("Could not find the Order Date field in the Order Details table", orderDate.exists());
		Label description = ie.label(orderDetailsDescriptionFieldFinder);
		assertTrue("Could not find the Description field in the Order Details table", description.exists());
		Label purchaseOrder = ie.label(orderDetailsPurchaseOrderFieldFinder);
		assertTrue("Could not find the Purchase Order field in the Order Details table", purchaseOrder.exists());
		Label customer = ie.label(orderDetailsCustomerFieldFinder);
		assertTrue("Could not find the Customer field in the Order Details table", customer.exists());
		Label division = ie.label(orderDetailsDivisionFieldFinder);
		assertTrue("Could not find the division field in the Order Details table", division.exists());
		HtmlElement lineItemsHeader = ie.htmlElement(orderDetailsLineItemsHeaderFinder);
		assertTrue("Could not find the Line Items header", lineItemsHeader.exists());
	}
	
	/**
	 * Clicks on the Identify link for a given line item. The line item
	 * number is the line you wish to identify a new asset. Line items
	 * start at 0. Assumes the input is greater than zero and less than
	 * or equal to the number of lines items in the table.
	 * 
	 * @param lineItem
	 * @throws Exception
	 */
	public void gotoAddProductOnOrderNumber(int lineItem) throws Exception {
		assertTrue("Line item must be a positive integer value", lineItem >= 0);
		Links lineItems = getLinksToLineItems();
		assertTrue("Found " + lineItems.length() + " items. You requested item " + lineItem + ".", lineItems.length() > lineItem);
		lineItems.get(lineItem).click();
	}
	
	private Links getLinksToLineItems() throws Exception {
		Links lineItems = ie.links(lineItemLinksFinder);
		assertTrue("Could not find any links to Line Items on current order.", lineItems.length() > 0);
		return lineItems;
	}
	
	/**
	 * Get the number of line items in an Order Details result.
	 * Assumes you have called gotoOrderNumber.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNumberOfLineItems() throws Exception {
		int result = -1;
		Links ls = getLinksToLineItems();
		result = ls.length();
		return result;
	}
	
	/**
	 * When you bring up an order, this will give you the description
	 * of a line item on the order.
	 * 
	 * @param lineItem
	 * @return
	 * @throws Exception
	 */
	public String getDescriptionOfLineItem(int lineItem) throws Exception {
		assertTrue("Line item must be a positive integer value", lineItem >= 0);
		String result = null;
		TableCells descriptions = ie.cells(lineItemDescriptionsFinder);
		assertTrue("Found " + descriptions.length() + " descriptions. You requested description for item " + lineItem + ".", descriptions.length() > lineItem);
		result = descriptions.get(lineItem).text().trim();
		return result;
	}
	
	/**
	 * When you bring up an order, this will give you the quantity
	 * of a line item on the order.
	 * 
	 * @param lineItem
	 * @return
	 * @throws Exception
	 */
	public int getQuantityOfLineItem(int lineItem) throws Exception {
		assertTrue("Line item must be a positive integer value", lineItem >= 0);
		int result = -1;
		TableCells quantities = ie.cells(lineItemQuantitiesFinder);
		assertTrue("Found " + quantities.length() + " quantity entries. You requested description for item " + lineItem + ".", quantities.length() > lineItem);
		result = Integer.parseInt(quantities.get(lineItem).text().trim());
		return result;
	}
	
	/**
	 * When you bring up an order, this will give you the number
	 * of items identified of a line item on the order.
	 * 
	 * @param lineItem
	 * @return
	 * @throws Exception
	 */
	public int getNumberIdentifiedOfLineItem(int lineItem) throws Exception {
		assertTrue("Line item must be a positive integer value", lineItem >= 0);
		int result = -1;
		TableCells identified = ie.cells(lineItemIdentifiedFinder);
		assertTrue("Found " + identified.length() + " quantity entries. You requested description for item " + lineItem + ".", identified.length() > lineItem);
		result = Integer.parseInt(identified.get(lineItem).text().trim());
		return result;
	}
	
	/**
	 * Gets the order number from the Identify Products page order.
	 * You need an order number to bring up the order, so that you
	 * can use this to get the order. In other words, this is really
	 * only useful for checking the number of the results page
	 * matches the order number used to being the page up.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderNumber() throws Exception {
		String result = null;
		Span orderNumber = ie.span(orderNumberValueFinder);
		assertTrue("Could not find the order number", orderNumber.exists());
		result = orderNumber.text().trim();
		return result;
	}
	
	/**
	 * Get the date from the Order Details.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderDate() throws Exception {
		String result = null;
		Span orderDate = ie.span(orderDateValueFinder);
		assertTrue("Could not find the order date", orderDate.exists());
		result = orderDate.text().trim();
		return result;
	}
	
	/**
	 * Get the description from the Order Details.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderDescription() throws Exception {
		String result = null;
		Span orderDescription = ie.span(orderDescriptionValueFinder);
		assertTrue("Could not find the order description", orderDescription.exists());
		result = orderDescription.text().trim();
		return result;
	}
	
	/**
	 * Get the purchase order number from the Order Details.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderPurchaseOrder() throws Exception {
		String result = null;
		Span orderPurchaseOrder = ie.span(orderPurchaseOrderValueFinder);
		assertTrue("Could not find the order purchase order", orderPurchaseOrder.exists());
		result = orderPurchaseOrder.text().trim();
		return result;
	}
	
	/**
	 * Get the customer from the Order Details.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderCustomer() throws Exception {
		String result = null;
		Span orderCustomer = ie.span(orderCustomerValueFinder);
		assertTrue("Could not find the order customer", orderCustomer.exists());
		result = orderCustomer.text().trim();
		return result;
	}
	
	/**
	 * Get the division from the Order Details.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderDivision() throws Exception {
		String result = null;
		Span orderDivision = ie.span(orderDivisionValueFinder);
		assertTrue("Could not find the order division", orderDivision.exists());
		result = orderDivision.text().trim();
		return result;
	}
	
	/**
	 * Get the sales agent from the Order Details.
	 * 
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	public String getOrderSalesAgent() throws Exception {
		String result = null;
		Span orderSalesAgent = ie.span(orderDivisionValueFinder);
		assertTrue("Could not find the order sales agent", orderSalesAgent.exists());
		result = orderSalesAgent.text().trim();
		return result;
	}
	
	/**
	 * Will set the information on the add product form. Need to call
	 * One of the addProduct*() methods to save the information.
	 * 
	 * It will scan the product type used for required fields. Any required
	 * fields it finds it will fill in random amounts. If generate is true,
	 * this will generate a serial number and update the Product p with the
	 * generated serial number.
	 * 
	 * Assumes you are already on the Add Product page.
	 * 
	 * The following rules must be obeyed.
	 * 
	 * 1) If the tenant has JobSites, customer and division must be null
	 * 2) If the tenant has Integration order number must be null
	 * 3) If the tenant does not have JobSites job sites and assigned to must be null
	 * 
	 * @param p - all the common fields
	 * @param generate - if set to true, generate a new serial number
	 * @throws Exception
	 */
	public String setProduct(Product p, boolean generate) throws Exception {
		assertNotNull(p);
		assertTrue("You either have to generate a serial number or provide one.", generate || (p.getSerialNumber() != null));
		
		FieldIDMisc.stopMonitor();	// turn the refresh monitor off while filling out the product form
		
		boolean jobsiteset = (p.getJobSite() != null);
		boolean assignedtoset = (p.getAssignedTo() != null);
		boolean customerset = (p.getCustomer() != null);
		boolean divisionset = (p.getDivision() != null);
		boolean ordernumberset = (p.getOrderNumber() != null);

		TextField serialNumber = ie.textField(addProductSerialNumberFinder);
		assertTrue("Could not find the text field for serial number", serialNumber.exists());
		Link generateSerialNumber = ie.link(addProductGenerateFinder);
		assertTrue("Could not find the link to generate a serial number", generateSerialNumber.exists());
		TextField rfidNumber = ie.textField(addProductRFIDNumberFinder);
		assertTrue("Could not find the text field for RFID number", rfidNumber.exists());
		TextField purchaseOrder = ie.textField(addProductPurchaseOrderFinder);
		assertTrue("Could not find the text field for purchase order", purchaseOrder.exists());
		SelectList jobSite = ie.selectList(addProductJobSiteFinder);
		if(jobsiteset) {
			assertTrue("Product has 'Job Site' defined but could not find a job site field", jobSite.exists());
		}
		SelectList assignedTo = ie.selectList(addProductAssignedToFinder);
		if(assignedtoset) {
			assertTrue("Product has 'Assigned To' defined but could not find an assigned to field", assignedTo.exists());
		}
		SelectList published = getPublishOverSafetyNetworkSelectList();
		if(p.getPublished()) {
			published.option(text("/Publish/")).select();
		} else {
			published.option(text("/Do Not Publish/")).select();
		}
		TextField location = ie.textField(addProductLocationFinder);
		assertTrue("Could not find the text field for location", location.exists());
		SelectList productStatus = ie.selectList(addProductProductStatusFinder);
		assertTrue("Could not find the select list for product status", productStatus.exists());
		TextField referenceNumber = ie.textField(addProductReferenceNumberFinder);
		assertTrue("Could not find the text field for reference number", referenceNumber.exists());
		TextField identified = ie.textField(addProductIdentifiedFinder);
		assertTrue("Could not find the text field for identified date", identified.exists());
		TextField orderNumber = ie.textField(addProductOrderNumberFinder);
		if(ordernumberset) {
			assertTrue("Product has 'Order Number' defined but could not find an order number field", orderNumber.exists());
		}
		SelectList productType = ie.selectList(addProductProductTypeFinder);
		assertTrue("Could not find the select list for product type", productType.exists());
		TextField comments = ie.textField(addProductCommentsFinder);
		assertTrue("Could not find the text field for comments", comments.exists());
		SelectList commentTemplates = ie.selectList(addProductCommentTemplatesFinder);
		assertTrue("Could not find the select list for comment templates", commentTemplates.exists());
		
		Button save = ie.button(addProductSaveButtonFinder);
		assertTrue("Could not find the 'Save' button", save.exists());
		Button saveInspect = ie.button(addProductSaveAndInspectButtonFinder);
		assertTrue("Could not find the 'Save And Inspect' button", saveInspect.exists());
		Button savePrint = ie.button(addProductSaveAndPrintButtonFinder);
		assertTrue("Could not find the 'Save And Print' button", savePrint.exists());
		Button saveSchedule = ie.button(addProductSaveAndScheduleButtonFinder);
		assertTrue("Could not find the 'Save and Schedule' button", saveSchedule.exists());
		
		if(p.getSerialNumber() != null) {
			serialNumber.set(p.getSerialNumber());
		}
		
		if(generate) {
			generateSerialNumber.click();
			misc.waitForJavascript();
			String newSerialNumber = serialNumber.value();
			assertNotNull("Could not find the value for the serial number field.", newSerialNumber);
			assertFalse("Generating a serial number appears to have failed.", newSerialNumber.equals(""));
			p.setSerialNumber(newSerialNumber);
		}

		if(p.getRFIDNumber() != null) {
			rfidNumber.set(p.getRFIDNumber());
		}
		
		if(p.getPurchaseOrder() != null) {
			purchaseOrder.set(p.getPurchaseOrder());
		}
		
		if(p.getJobSite() != null) {
			Option site = jobSite.option(text(p.getJobSite()));
			assertTrue("Could not find the job site '" + p.getJobSite() + "' on the list of job sites.", site.exists());
			site.select();
		}
		
		if(p.getAssignedTo() != null) {
			Option assign = assignedTo.option(text(p.getAssignedTo()));
			assertTrue("Could not find the user '" + p.getAssignedTo() + "' on the assigned to list.", assign.exists());
			assign.select();
		}

		if(p.getOwner() != null) {
			misc.gotoChooseOwner();
			misc.setOwner(p.getOwner());
			misc.selectOwner();
		}
		
		if(p.getLocation() != null) {
			location.set(p.getLocation());
		}
		
		if(p.getProductStatus() != null) {
			Option ps = productStatus.option(text(p.getProductStatus()));
			assertTrue("Could not find the product status '" + p.getProductStatus() + "' on the list of product status.", ps.exists());
			ps.select();
		}
		
		if(p.getReferenceNumber() != null) {
			referenceNumber.set(p.getReferenceNumber());
		}
		
		if(p.getIdentified() != null) {	// Must be in the format MM/DD/YY
			identified.set(p.getIdentified());
		}
		
		if(p.getOrderNumber() != null) {
			orderNumber.set(p.getOrderNumber());
		}
		
		if(p.getProductType() != null) {
			Option pt = productType.option(text(p.getProductType()));
			assertTrue("Could not find the product type '" + p.getProductType() + "' in the list of product types.", pt.exists());
			pt.select();
			productType.fireEvent("onchange");
			misc.waitForJavascript();
		}

		handleRequiredFieldsOnAddProduct(comments);

		if(p.getComments() != null) {
			comments.set(p.getComments());
		}
		
		FieldIDMisc.startMonitor();	// turn the monitor back on
		
		return p.getSerialNumber();
	}
	
	private SelectList getPublishOverSafetyNetworkSelectList() throws Exception {
		SelectList published = ie.selectList(addProductPublishFinder);
		assertTrue("Could not find the select list for Publish Over Safety Network", published.exists());
		return published;
	}

	public void addProductSave() throws Exception {
		Button submit = ie.button(addProductSaveButtonFinder);
		assertNotNull(submit);
		assertTrue("Could not find 'Save' button.", submit.exists());
		submit.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void addProductSaveAndInspect() throws Exception {
		Button submit = ie.button(addProductSaveAndInspectButtonFinder);
		assertNotNull(submit);
		assertTrue("Could not find 'Save and Inspect' button.", submit.exists());
		submit.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void addProductSaveAndPrint() throws Exception {
		Button submit = ie.button(addProductSaveAndPrintButtonFinder);
		assertNotNull(submit);
		assertTrue("Could not find 'Save And Print' button.", submit.exists());
		submit.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void addProductSaveAndSchedule() throws Exception {
		Button submit = ie.button(addProductSaveAndScheduleButtonFinder);
		assertNotNull(submit);
		assertTrue("Could not find 'Save And Schedule' button.", submit.exists());
		submit.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void addProductResetForm() throws Exception {
		Button reset = ie.button(addProductResetFormButtonFinder);
		assertNotNull(reset);
		assertTrue("Could not find 'Reset Form' button.", reset.exists());
		reset.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void handleRequiredFieldsOnAddProduct() throws Exception {
		TextField comments = ie.textField(addProductCommentsFinder);
		handleRequiredFieldsOnAddProduct(comments);
	}
	
	private void handleRequiredFieldsOnAddProduct(TextField comments) throws Exception {
		// handle text fields, select lists and combo boxes
		misc.waitForJavascript();	// seems to be a timing problem here so I'm adding a little more sleep
		Labels requiredFields = ie.labels(addProductRequiredInputFieldsFinder);
		int numberOfRequiredFields = requiredFields.length();
		for(int i = 0; i < numberOfRequiredFields; i++) {
			Label requiredField = requiredFields.get(i);
			String inputID;
			if(requiredField.exists()) {
				IEHtmlElement field = (IEHtmlElement) requiredField.htmlElement(xpath("../SPAN/*"));
				assertTrue("Could not find a required product type attribute input field", field.exists());
				inputID = field.id();
				String tag = field.tag();
				if(tag.equals("INPUT")) {			// text field
					TextField t = ie.textField(id(inputID));
					assertNotNull(t);
					assertTrue("Could not find a required product type attribute text field", t.exists());
					if(t.value().equals("")) {
						setTextFieldRandomly(t);
					}
				} else if(tag.equals("SELECT")) {	// combo box or select list
					SelectList sl = ie.selectList(id(inputID));
					assertNotNull(sl);
					assertTrue("Could not find a required product type attribute select list", sl.exists());
					setSelectListRandomly(sl);
					comments.focus();	// move focus away. helps if combo box
				} else {
					fail("Unexpected required input. Update automation.");
				}
			}
		}
		
		// handle the unit of measures
		requiredFields = ie.labels(addProductRequiredUOMFieldsFinder);
		numberOfRequiredFields = requiredFields.length();
		for(int i = 0; i < numberOfRequiredFields; i++) {
			Label requiredField = requiredFields.get(i);
			String inputID;
			if(requiredField.exists()) {
				IEHtmlElement field = (IEHtmlElement) requiredField.htmlElement(xpath("../SPAN/*"));
				assertTrue("Could not find a required product type attribute input field", field.exists());
				inputID = field.id();
				String tag = field.tag();
				if(tag.equals("INPUT")) {		// unit of measure
					if(field.value().equals("")) {
						setUnitOfMeasureRandomly(inputID);
						comments.focus();	// move focus away. helps
					}
				} else {
					fail("Unexpected required unit of measure input. Update automation.");
				}
			}
		}
	}
	
	private void setTextFieldRandomly(TextField textField) throws Exception {
		assertNotNull(textField);
		assertTrue("Could not find the text field.", textField.exists());
		textField.set(misc.getRandomString());
	}
	
	private void setSelectListRandomly(SelectList sl) throws Exception {
		assertNotNull(sl);
		assertTrue("Could not find the select list.", sl.exists());
		Options os = sl.options();
		assertNotNull("Could not find the options for a select list.", os);
		int i = misc.getRandomInteger(1, os.length()-1);
		os.get(i).select();
	}
	
	private void setUnitOfMeasureRandomly(String inputID) throws Exception {
		assertNotNull(inputID);
		assertFalse("The ID for the Unit of Measure is blank.", inputID.equals(""));
		Link selector = ie.link(id("unitOfMeasureSelector_" + inputID));
		assertTrue("Couldn't select unit of measure", selector.exists());
		selector.click();
		misc.waitForJavascript();
		// find and fill in unit of measure
		SelectList unitOfMeasure = ie.selectList(id("unitOfMeasureId_" + inputID));
		assertTrue("Could not find the dialog to enter Unit of Measure.", unitOfMeasure.exists());
		List<String> sel = unitOfMeasure.getSelectedItems();
		Option selected = unitOfMeasure.option(text(sel.get(0)));
		int value = Integer.parseInt(selected.value());
		String s;
		switch(value) {
			case 5:		// Feet
				s = Integer.toString(misc.getRandomInteger());
				TextField ft = ie.textField(id("5_" + inputID));	// includes inches as well
				ft.set(s);
			case 1:		// Inches
				s = Integer.toString(misc.getRandomInteger(1, 11));
				TextField in = ie.textField(id("1_" + inputID));
				in.set(s);
				break;
			case 6:		// Meters
				s = Integer.toString(misc.getRandomInteger());
				TextField m = ie.textField(id("6_" + inputID));		// includes centimeters as well
				m.set(s);
			case 2:		// Centimeters
				s = Integer.toString(misc.getRandomInteger(1, 99));		// 100 centimeters = 1 meter
				TextField cm = ie.textField(id("2_" + inputID));
				cm.set(s);
				break;
			case 3:		// Pounds
				s = Integer.toString(misc.getRandomInteger(1, 2239));	// 2240 pounds  = 1 ton
				TextField lb = ie.textField(id("3_" + inputID));
				lb.set(s);
				break;
			case 4:		// Kilograms
				s = Integer.toString(misc.getRandomInteger(1, 999));	// 1000 kilograms = 1 tonne
				TextField kg = ie.textField(id("4_" + inputID));
				kg.set(s);
				break;
			case 7:		// Tonne
				s = Integer.toString(misc.getRandomInteger());
				TextField tonne = ie.textField(id("7_" + inputID));
				tonne.set(s);
				break;
			case 8:		// Ton
				s = Integer.toString(misc.getRandomInteger());
				TextField ton = ie.textField(id("8_" + inputID));
				ton.set(s);
				break;
			case 9:		// kiloNewtons
				s = Integer.toString(misc.getRandomInteger());
				TextField kn = ie.textField(id("9_" + inputID));
				kn.set(s);
				break;
			case 10:	// Millimetres
				s = Integer.toString(misc.getRandomInteger(1,9));	// 10 millimetres = 1 centimeter
				TextField mm = ie.textField(id("10_" + inputID));
				mm.set(s);
				break;
			default:	// anything new
				fail("New unit of measure was added to the list. Update the automation.");
				break;
		}
		ie.button(id("unitOfMeasureForm_" + inputID + "_hbutton_submit")).click();
		misc.waitForJavascript();
	}

	public void validateAddProductPage(boolean jobsites, boolean integration) throws Exception {
		checkAddProductPageContentHeader();
		checkStaticFields(jobsites, integration);
		checkButtons();
		checkLinksOnAddProductPage(integration);	// new for 2009.5
	}

	private void checkLinksOnAddProductPage(boolean integration) throws Exception {
		Link generateSerialNumber = ie.link(addProductGenerateFinder);
		assertTrue("Could not find the link to generate a serial number", generateSerialNumber.exists());
		Link addMultipleAssets = ie.link(addMultipleAssetsFinder);
		assertTrue("Could not find the link to add multiple assets", addMultipleAssets.exists());
		Link addWithOrder = ie.link(addWithOrderFinder);
		if(integration) {
			assertTrue("Could not find the link to add with order", addWithOrder.exists());
		}
	}

	private void checkButtons() throws Exception {
		Button save = ie.button(addProductSaveButtonFinder);
		assertTrue("Could not find the 'Save' button", save.exists());
		Button saveInspect = ie.button(addProductSaveAndInspectButtonFinder);
		assertTrue("Could not find the 'Save And Inspect' button", saveInspect.exists());
		Button savePrint = ie.button(addProductSaveAndPrintButtonFinder);
		assertTrue("Could not find the 'Save And Print' button", savePrint.exists());
		Button saveSchedule = ie.button(addProductSaveAndScheduleButtonFinder);
		assertTrue("Could not find the 'Save and Schedule' button", saveSchedule.exists());
	}

	private void checkStaticFields(boolean jobsites, boolean integration) throws Exception {
		TextField serialNumber = ie.textField(addProductSerialNumberFinder);
		assertTrue("Could not find the text field for serial number", serialNumber.exists());
		TextField rfidNumber = ie.textField(addProductRFIDNumberFinder);
		assertTrue("Could not find the text field for RFID number", rfidNumber.exists());
		TextField referenceNumber = ie.textField(addProductReferenceNumberFinder);
		assertTrue("Could not find the text field for reference number", referenceNumber.exists());
		TextField purchaseOrder = ie.textField(addProductPurchaseOrderFinder);
		assertTrue("Could not find the text field for purchase order", purchaseOrder.exists());
		TextField location = ie.textField(addProductLocationFinder);
		assertTrue("Could not find the text field for location", location.exists());
		SelectList productStatus = ie.selectList(addProductProductStatusFinder);
		assertTrue("Could not find the select list for product status", productStatus.exists());
		TextField identified = ie.textField(addProductIdentifiedFinder);
		assertTrue("Could not find the text field for identified date", identified.exists());
		TextField orderNumber = ie.textField(addProductOrderNumberFinder);
		if(!integration) {
			assertTrue("Could not find an order number field", orderNumber.exists());
		}
		SelectList productType = ie.selectList(addProductProductTypeFinder);
		assertTrue("Could not find the select list for product type", productType.exists());
		TextField comments = ie.textField(addProductCommentsFinder);
		assertTrue("Could not find the text field for comments", comments.exists());
		SelectList commentTemplates = ie.selectList(addProductCommentTemplatesFinder);
		assertTrue("Could not find the select list for comment templates", commentTemplates.exists());
	}

	public void validateIdentifyProductsPage() throws Exception {
		TextField orderNumberTextField = ie.textField(orderNumberTextFieldFinder);
		assertTrue("Could not find the text field for Order Number on Identify Product page..", orderNumberTextField.exists());
		Button loadOrderNumberButton = ie.button(loadOrderNumberButtonFinder);
		assertTrue("Could not find the Load button for Order Number on Identify Product page.", loadOrderNumberButton.exists());
		Link addMultipleAssets = ie.link(addMultipleAssetsFinder);
		assertTrue("Could not find the link to add multiple assets", addMultipleAssets.exists());
		Link add = ie.link(addFinder);
		assertTrue("Could not find the link to add an asset", add.exists());
	}

	public void validateIdentifyProductsPageWithOrderDetails(String orderNumberExpected) throws Exception {
		validateIdentifyProductsPage();
		String orderNumber = getOrderNumber();
		assertEquals("The order number expected: '" + orderNumberExpected + "' does not match the order number in the details: '" + orderNumber + "'", getOrderNumber(), orderNumberExpected);
		int numLineItems = getNumberOfLineItems();
		if(numLineItems > 0) {	// calling these methods will check the fields exist
			getNumberIdentifiedOfLineItem(0);
			getOrderCustomer();
			getOrderDate();
			getOrderDescription();
			getOrderDivision();
			getOrderPurchaseOrder();
			// getOrderSalesAgent();
			getQuantityOfLineItem(0);
		}
	}

	public void gotoIdentify() throws Exception {
		clickIdentifyIcon();
		checkIdentifyProductPageContentHeader();
	}

	public void addMultipleAssetsContinueToStep2() throws Exception {
		Button cont = ie.button(addMultipleAssetsStep1ContinueButtonFinder);
		assertTrue("Could not find the Continue button on step 1 of Add Multiple Assets", cont.exists());
		cont.click();
		misc.waitForJavascript();
		checkAddMultipleAssetsStep1Hidden();
	}

	private void checkAddMultipleAssetsStep1Hidden() throws Exception {
		Div step1 = ie.div(addMultipleAssetsStep1BodyFinder);
		assertTrue("Could not find the body of step 1", step1.exists());
		String html = step1.html();
		int i =  html.indexOf(">");
		String tag = html.substring(0, i).toLowerCase();
		assertTrue("Step 1 is not hidden", tag.contains("display: none"));
	}

	public void addMultipleAssetsStep1(Product p) throws Exception {
		assertNotNull(p);
		assertNotNull(p.getIdentified());
		if(p.getCustomer() != null) {
			SelectList customer = ie.selectList(addMultipleAssetsCustomerFinder);
			assertTrue("Could not find the customer field on step 1 of add multiple assets", customer.exists());
			Option o = customer.option(text(p.getCustomer()));
			assertTrue("Could not find customer '" + p.getCustomer() + "'", o.exists());
			o.select();
			misc.waitForJavascript();
		}
		
		if(p.getDivision() != null) {
			SelectList division = ie.selectList(addMultipleAssetsDivisionFinder);
			assertTrue("Could not find the division field on step 1 of add multiple assets", division.exists());
			Option o = division.option(text(p.getDivision()));
			assertTrue("Could not find division '" + p.getDivision() + "'", o.exists());
			o.select();
		}
		
		if(p.getJobSite() != null) {
			SelectList jobsite = ie.selectList(addMultipleAssetsJobSiteFinder);
			assertTrue("Could not find the job site field on step 1 of add multiple assets", jobsite.exists());
			Option o = jobsite.option(text(p.getJobSite()));
			assertTrue("Could not find job site '" + p.getJobSite() + "'", o.exists());
			o.select();
		}
		
		TextField location = ie.textField(addMultipleAssetsLocationFinder);
		assertTrue("Could not find the Location field on step 1 of add multiple assets", location.exists());
		if(p.getLocation() != null) {
			location.set(p.getLocation());
		}
		
		SelectList productStatus = ie.selectList(addMultipleAssetsProductStatusFinder);
		assertTrue("Could not find the Product Status field on step 1 of add multiple assets", productStatus.exists());
		if(p.getProductStatus() != null) {
			Option o = productStatus.option(text(p.getProductStatus()));
			assertTrue("Could not find product status '" + p.getProductStatus() + "'", o.exists());
			o.select();
		}

		TextField purchaseOrder = ie.textField(addMultipleAssetsPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Order field on step 1 of add multiple assets", purchaseOrder.exists());
		if(p.getPurchaseOrder() != null) {
			purchaseOrder.set(p.getPurchaseOrder());
		}

		TextField identifed = ie.textField(addMultipleAssetsIdentifiedFinder);
		assertTrue("Could not find the Identified field on step 1 of add multiple assets", identifed.exists());
		if(p.getIdentified() != null) {
			identifed.set(p.getIdentified());
		}

		SelectList productType = ie.selectList(addMultipleAssetsProductTypeFinder);
		assertTrue("Could not find the Product Type field on step 1 of add multiple assets", productType.exists());
		if(p.getProductType() != null) {
			Option o = productType.option(text(p.getProductType()));
			assertTrue("Could not find product type '" + p.getProductType() + "'", o.exists());
			o.select();
		}

		TextField comments = ie.textField(addMultipleAssetsCommentsFinder);
		assertTrue("Could not find the Comments field on step 1 of add multiple assets", comments.exists());
		if(p.getComments() != null) {
			comments.set(p.getComments());
		}

		SelectList commentTemplates = ie.selectList(addMultipleAssetsCommentTemplatesFinder);
		assertTrue("Could not find the comment templates field on step 1 of add multiple assets", commentTemplates.exists());
		if(p.getCommentTemplate() != null) {
			Option o = commentTemplates.option(text(p.getCommentTemplate()));
			assertTrue("Could not find comment template '" + p.getCommentTemplate() + "'", o.exists());
			o.select();
		}
		
		handleRequiredFieldsOnAddProduct(comments);
	}
	
	public void validate(String orderNumber) throws Exception {
		String today = misc.getDateString();
		Product product = new Product(today);
		gotoIdentify();
		boolean integration = isIntegration();
		if(integration) {
			String s;
			gotoIdentifyProducts();
			gotoOrderNumber(orderNumber);
			s = getOrderNumber();
			s = getOrderPurchaseOrder();
			s = getOrderDate();
			s = getOrderCustomer();
			s = getOrderDescription();
			s = getOrderDivision();
			if(getNumberOfLineItems() > 0) {
				int n;
				s = getDescriptionOfLineItem(0);
				assertNotNull(s);	// should return something
				n = getQuantityOfLineItem(0);
				assertTrue(n >= 0);	// shouldn't be negative
				n = getNumberIdentifiedOfLineItem(0);
				assertTrue(n >= 0);	// shouldn't be negative
			}
			gotoAddProductOnOrderNumber(0);
			setProduct(product, true);
			addProductSave();
			gotoIdentify();
			gotoAdd();
		}

		gotoAddProduct();
		setProduct(product , true);
		gotoAddMultipleAssets();
		String quantity = "8";
		FieldIDMisc.stopMonitor();
		addMultipleAssetsStep1(product);
		addMultipleAssetsContinueToStep2();
		addMultipleAssetsBackToStep1();
		addMultipleAssetsContinueToStep2();
		addMultipleAssetsStep2(quantity);
		addMultipleAssetsContinueToStep3();
		addMultipleAssetsBackToStep2();
		addMultipleAssetsContinueToStep3();
		
		String prefix = "vali-";
		String start = "888";
		String suffix = "-date";
		String identifier = "batch-validate";
		addMultipleAssetsStep3Batch(identifier);
		addMultipleAssetsStep3Manual();
		addMultipleAssetsStep3Generate();
		addMultipleAssetsStep3Range(prefix, start, suffix);
		addMultipleAssetsContinueToStep4();
		addMultipleAssetsBackToStep3();
		addMultipleAssetsContinueToStep4();
		addMultipleAssetsSaveAndCreate();
		FieldIDMisc.startMonitor();
		addMultipleAssetsCancel();	// takes you to the Home page WEB-1024
	}
	
	public void gotoAddProductAttachFile() throws Exception {
		FieldIDMisc.stopMonitor();
		Button attachFile = ie.button(attachFileButtonFinder);
		assertTrue("Could not find the button to attach a file", attachFile.exists());
		attachFile.click();
		misc.waitForJavascript();
		fail("not implemented");
		FieldIDMisc.startMonitor();
	}

	public void addMultipleAssetsSaveAndCreate() throws Exception {
		Button save = ie.button(addMultipleAssetsSaveButtonFinder);
		assertTrue("Could not find the Save and Create button", save.exists());
		save.click();
		checkAddMultipleAssetsPageContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void addMultipleAssetsCancel() throws Exception {
		Button cancel = ie.button(addMultipleAssetsCancelButtonFinder);
		assertTrue("Could not find the Cancel Multi Add button", cancel.exists());
		cancel.click();
		home.checkHomePageContentHeader();
	}

	public void addMultipleAssetsStep3Generate() throws Exception {
		Radio r = ie.radio(addMultipleAssetsStep3GenerateFinder);
		assertTrue("Could not find the Non Serialized Assets radio button", r.exists());
		r.set();
	}

	public void addMultipleAssetsStep3Manual() throws Exception {
		Radio r = ie.radio(addMultipleAssetsStep3ManualFinder);
		assertTrue("Could not find the Batch radio button", r.exists());
		r.set();
	}

	public void addMultipleAssetsStep3Batch(String identifier) throws Exception {
		Radio r = ie.radio(addMultipleAssetsStep3BatchFinder);
		assertTrue("Could not find the Batch radio button", r.exists());
		TextField ident = ie.textField(addMultipleAssetsStep3IdentifierFinder);
		assertTrue("Could not find the Identifier text field", ident.exists());
		r.set();
		if(identifier != null) {
			ident.set(identifier);
		}
	}

	public void addMultipleAssetsContinueToStep4() throws Exception {
		Button cont = ie.button(addMultipleAssetsStep3ContinueButtonFinder);
		assertTrue("Could not find the Continue button on step 3 of Add Multiple Assets", cont.exists());
		cont.click();
		misc.waitForJavascript();
		checkAddMultipleAssetsStep3Hidden();
	}

	public void addMultipleAssetsBackToStep3() throws Exception {
		Link back = ie.link(backtoStep3LinkFinder);
		assertTrue("Could not find the link to go back to step 3", back.exists());
		back.click();
		checkAddMultipleAssetsPageContentHeader();
	}

	private void checkAddMultipleAssetsStep3Hidden() throws Exception {
		Div step3 = ie.div(addMultipleAssetsStep2BodyFinder);
		assertTrue("Could not find the body of step 3", step3.exists());
		String html = step3.html();
		int i =  html.indexOf(">");
		String tag = html.substring(0, i).toLowerCase();
		assertTrue("Step 3 is not hidden", tag.contains("display: none"));
	}

	public void addMultipleAssetsStep3Range(String prefix, String start, String suffix) throws Exception {
		Radio r = ie.radio(addMultipleAssetsStep3RangeFinder);
		assertTrue("Could not find the Range radio button", r.exists());
		TextField pre = ie.textField(addMultipleAssetsStep3PrefixFinder);
		assertTrue("Could not find the Prefix text field", pre.exists());
		TextField sta = ie.textField(addMultipleAssetsStep3StartFinder);
		assertTrue("Could not find the Start text field", sta.exists());
		TextField suf = ie.textField(addMultipleAssetsStep3SuffixFinder);
		assertTrue("Could not find the Prefix text field", suf.exists());
		r.set();
		if(prefix != null) {
			pre.set(prefix);
		}
		if(start != null) {
			sta.set(start);
		}
		if(suffix != null) {
			suf.set(suffix);
		}
	}

	public void addMultipleAssetsBackToStep2() throws Exception {
		Link back = ie.link(backtoStep2LinkFinder);
		assertTrue("Could not find the link to go back to step 2", back.exists());
		back.click();
		checkAddMultipleAssetsPageContentHeader();
	}

	public void addMultipleAssetsContinueToStep3() throws Exception {
		Button cont = ie.button(addMultipleAssetsStep2ContinueButtonFinder);
		assertTrue("Could not find the Continue button on step 2 of Add Multiple Assets", cont.exists());
		cont.click();
		misc.waitForJavascript();
		checkAddMultipleAssetsStep2Hidden();
	}

	private void checkAddMultipleAssetsStep2Hidden() throws Exception {
		Div step2 = ie.div(addMultipleAssetsStep2BodyFinder);
		assertTrue("Could not find the body of step 2", step2.exists());
		String html = step2.html();
		int i =  html.indexOf(">");
		String tag = html.substring(0, i).toLowerCase();
		assertTrue("Step 2 is not hidden", tag.contains("display: none"));
	}

	public void addMultipleAssetsStep2(String q) throws Exception {
		TextField quantity = ie.textField(addMultipleAssetsQuantityFinder);
		assertTrue("Could not find the Quantity field on step 2 of multi add", quantity.exists());
		quantity.set(q);
		checkAddMultipleAssetsPageContentHeader();
	}

	public void addMultipleAssetsBackToStep1() throws Exception {
		Link back = ie.link(backtoStep1LinkFinder);
		assertTrue("Could not find the link to go back to step 1", back.exists());
		back.click();
		checkAddMultipleAssetsPageContentHeader();
	}

	/**
	 * Checks to see if Identify starts with Add with Order.
	 * If it does then the tenant has integration and this
	 * returns true. It assumes you are already on the
	 * Assets (i.e. Identify) page.
	 * 
	 * @return
	 */
	public boolean isIntegration() throws Exception {
		boolean result = false;

		TextField orderNumber = ie.textField(orderNumberTextFieldFinder);
		result = orderNumber.exists();
		return result;
	}

	public List<String> getProductStatusFromAddAsset() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList ps = ie.selectList(addProductProductStatusFinder);
		assertTrue("Could not find the select list for Product Status", ps.exists());
		Options pss = ps.options();
		Iterator<Option> i = pss.iterator();
		while(i.hasNext()) {
			Option status = i.next();
			results.add(status.text().trim());
		}
		return results;
	}

	public boolean getPublishOverSafetyNetworkSetting() throws Exception {
		boolean result = false;
		SelectList published = getPublishOverSafetyNetworkSelectList();
		String s = published.getSelectedItems().get(0);
		result = !s.contains("Do Not Publish");
		return result;
	}
}
