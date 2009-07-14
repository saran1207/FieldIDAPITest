package com.n4systems.fieldid;

import static watij.finders.FinderFactory.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.MassUpdateForm;
import com.n4systems.fieldid.datatypes.Product;
import com.n4systems.fieldid.datatypes.ProductSearchCriteria;
import com.n4systems.fieldid.datatypes.ProductSearchSelectColumns;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Assets extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	FieldIDMisc misc;
	String propertyFile = "assets.properties";
	Finder assetsFinder;
	Finder assetsContentHeaderFinder;
	Finder productInformationContentHeaderFinder;
	Finder viewAllInspectionsFinder;
	Finder inspectionForFinder;
	Finder productSearchRFIDNumberFinder;
	Finder productSearchSerialNumberFinder;
	Finder productSearchOrderNumberFinder;
	Finder productSearchPurchaseOrderFinder;
	Finder productSearchCustomerFinder;
	Finder productSearchDivisionFinder;
	Finder productSearchJobSiteFinder;
	Finder productSearchAssignedToFinder;
	Finder productSearchLocationFinder;
	Finder productSearchSalesAgentFinder;
	Finder productSearchReferenceNumberFinder;
	Finder productSearchProductStatusFinder;
	Finder productSearchProductTypeFinder;
	Finder productSearchFromDateFinder;
	Finder productSearchToDateFinder;
	Finder productSearchRunButtonFinder;
	Finder productSearchClearFormButtonFinder;
	Finder productSearchExpandSearchCriteriaFinder;
	Finder productSearchExpandSelectColumnFinder;
	Finder selectColumnSerialNumberFinder;
	Finder selectColumnRFIDNumberFinder;
	Finder selectColumnCustomerNameFinder;
	Finder selectColumnDivisionFinder;
	Finder selectColumnOrganizationFinder;
	Finder selectColumnReferenceNumberFinder;
	Finder selectColumnProductTypeFinder;
	Finder selectColumnProductStatusFinder;
	Finder selectColumnLastInspectionDateFinder;
	Finder selectColumnDateIdentifiedFinder;
	Finder selectColumnIdentifiedByFinder;
	Finder selectColumnDescriptionFinder;
	Finder selectColumnLocationFinder;
	Finder selectColumnOrderDescriptionFinder;
	Finder selectColumnOrderNumberFinder;
	Finder selectColumnPurchaseOrderFinder;
	Finder selectColumnFormFinder;
	Finder searchCriteriaFormFinder;
	Finder productSearchResultsContentHeaderFinder;
	Finder productAttributesSelectColumnsFinder;
	Finder searchHasNoResultsFinder;
	Finder manageInspectionLinkFinder;
	Finder inspectionGroupsContentHeaderFinder;
	Finder totalProductsSpanFinder;
	Finder printAllManufacturerCertificatesFinder;
	Finder printAllManufacturerCertificatesWarningFinder;
	Finder printAllManufacturerCertificatesWarningToolTipFinder;
	Finder exportToExcelFinder;
	Finder exportToExcelWarningToolTipFinder;
	Finder exportToExcelWarningFinder;
	Finder massUpdateLinkFinder;
	Finder massUpdateContentHeaderFinder;
	Finder massUpdateWarningFinder;
	Finder massUpdateWarningToolTipFinder;
	Finder noResultsReturnedFinder;
	Finder massUpdateCustomerNameFinder;
	Finder massUpdateDivisionFinder;
	Finder massUpdateCustomerDivisionSelectFinder;
	Finder massUpdateJobSiteFinder;
	Finder massUpdateJobSiteSelectFinder;
	Finder massUpdateAssignedToFinder;
	Finder massUpdateAssignedToSelectFinder;
	Finder massUpdateProductStatusFinder;
	Finder massUpdateProductStatusSelectFinder;
	Finder massUpdatePurchaseOrderFinder;
	Finder massUpdatePurchaseOrderSelectFinder;
	Finder massUpdateLocationFinder;
	Finder massUpdateLocationSelectFinder;
	Finder massUpdateIdentifiedFinder;
	Finder massUpdateIdentifiedSelectFinder;
	Finder massUpdateSaveButtonFinder;
	Finder massUpdateReturnToSearchFinder;
	Finder massUpdateInstructionsFinder;
	Finder editProductLinkFinder;
	Finder editProductPageContentHeaderFinder;
	Finder productConfigurationLinkFinder;
	Finder productConfigurationPageContentHeaderFinder;
	Finder inspectionScheduleLinkFinder;
	Finder inspectionSchedulePageContentHeaderFinder;
	Finder addScheduleDateFinder;
	Finder addScheduleInspectionTypeFinder;
	Finder addScheduleJobFinder;
	Finder addScheduleSaveButtonFinder;
	Finder scheduleForScheduleRowsFinder;
	Finder smartSearchTextFieldFinder;
	Finder smartSearchSearchButtonFinder;
	Finder selectDisplayColumnHeaderFinder;
	private Finder addNewComponentFindExistingDivsFinder;
	private Finder productLookupSmartSearchTextFieldFinder;
	private Finder productLookupSmartSearchLoadButtonFinder;
	private Finder productLookupSmartSearchSelectButtonFinder;
	private Finder editProductSerialNumberFinder;
	private Finder editProductRfidNumberFinder;
	private Finder editProductReferenceNumberFinder;
	private Finder editProductCustomerFinder;
	private Finder editProductDivisionFinder;
	private Finder editProductLocationFinder;
	private Finder editProductProductStatusFinder;
	private Finder editProductPurchaseOrderFinder;
	private Finder editProductIdentifiedFinder;
	private Finder editProductProductTypeFinder;
	private Finder editProductCommentsFinder;
	private Finder editProductCommentTemplatesFinder;
	private Finder editProductGenerateLinkFinder;
	
	public Assets(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			assetsFinder = id(p.getProperty("link"));
			assetsContentHeaderFinder = xpath(p.getProperty("contentheader"));
			productInformationContentHeaderFinder = xpath(p.getProperty("productinfocontentheader"));
			viewAllInspectionsFinder = xpath(p.getProperty("viewallinspections"));
			inspectionForFinder = xpath(p.getProperty("inspectionforcontentheader"));
			productSearchRFIDNumberFinder = id(p.getProperty("productsearchrfidnumber"));
			productSearchSerialNumberFinder = id(p.getProperty("productsearchserialnumber"));
			productSearchOrderNumberFinder = id(p.getProperty("productsearchordernumber"));
			productSearchPurchaseOrderFinder = id(p.getProperty("productsearchpurchaseorder"));
			productSearchCustomerFinder = id(p.getProperty("productsearchcustomer"));
			productSearchDivisionFinder = id(p.getProperty("productsearchdivision"));
			productSearchJobSiteFinder = id(p.getProperty("productsearchjobsite"));
			productSearchAssignedToFinder = id(p.getProperty("productsearchassignedto"));
			productSearchLocationFinder = id(p.getProperty("productsearchlocation"));
			productSearchSalesAgentFinder = id(p.getProperty("productsearchsalesagent"));
			productSearchReferenceNumberFinder = id(p.getProperty("productsearchreferencenumber"));
			productSearchProductStatusFinder = id(p.getProperty("productsearchproductstatus"));
			productSearchProductTypeFinder = id(p.getProperty("productsearchproducttype"));
			productSearchFromDateFinder = id(p.getProperty("productsearchfromdate"));
			productSearchToDateFinder = id(p.getProperty("productsearchtodate"));
			productSearchRunButtonFinder = id(p.getProperty("productsearchrunbutton"));
			productSearchClearFormButtonFinder = value(p.getProperty("productsearchclearformbutton"));
			productSearchExpandSearchCriteriaFinder = id(p.getProperty("productsearchexpandsearchcriteria"));
			productSearchExpandSelectColumnFinder = id(p.getProperty("productsearchexpandselectcolumns"));
			selectColumnSerialNumberFinder = id(p.getProperty("selectcolumnserialnumber"));
			selectColumnRFIDNumberFinder = id(p.getProperty("selectcolumnrfidnumber"));
			selectColumnCustomerNameFinder = id(p.getProperty("selectcolumncustomername"));
			selectColumnDivisionFinder = id(p.getProperty("selectcolumndivision"));
			selectColumnOrganizationFinder = id(p.getProperty("selectcolumnorganization"));
			selectColumnReferenceNumberFinder = id(p.getProperty("selectcolumnreferencenumber"));
			selectColumnProductTypeFinder = id(p.getProperty("selectcolumnproducttype"));
			selectColumnProductStatusFinder = id(p.getProperty("selectcolumnproductstatus"));
			selectColumnLastInspectionDateFinder = id(p.getProperty("selectcolumnlastinspectiondate"));
			selectColumnDateIdentifiedFinder = id(p.getProperty("selectcolumndateidentified"));
			selectColumnIdentifiedByFinder = id(p.getProperty("selectcolumnidentifiedby"));
			selectColumnDescriptionFinder = id(p.getProperty("selectcolumndescription"));
			selectColumnLocationFinder = id(p.getProperty("selectcolumnlocation"));
			selectColumnOrderDescriptionFinder = id(p.getProperty("selectcolumnorderdescription"));
			selectColumnOrderNumberFinder = id(p.getProperty("selectcolumnordernumber"));
			selectColumnPurchaseOrderFinder = id(p.getProperty("selectcolumnpurchaseorder"));
			selectColumnFormFinder = id(p.getProperty("selectcolumnform"));
			searchCriteriaFormFinder = id(p.getProperty("searchcriteriaform"));
			productSearchResultsContentHeaderFinder = xpath(p.getProperty("productsearchresultscontentheader"));
			productAttributesSelectColumnsFinder = id(p.getProperty("selectcolumnproductattributes"));
			searchHasNoResultsFinder = xpath(p.getProperty("searchhasnoresults"));
			manageInspectionLinkFinder = id(p.getProperty("manageinspectionlink"));
			inspectionGroupsContentHeaderFinder = xpath(p.getProperty("inspectiongroupscontentheader"));
			totalProductsSpanFinder = xpath(p.getProperty("totalproductsspan"));
			noResultsReturnedFinder = xpath(p.getProperty("totalproductsnoresultreturned"));
			printAllManufacturerCertificatesFinder = xpath(p.getProperty("printallmanufacturercertificates"));
			printAllManufacturerCertificatesWarningFinder = xpath(p.getProperty("printallmanufacturercertificateswarning"));
			printAllManufacturerCertificatesWarningToolTipFinder = id(p.getProperty("printallmanufacturercertswarningtooltip"));
			exportToExcelFinder = xpath(p.getProperty("exporttoexcel"));
			exportToExcelWarningToolTipFinder = id(p.getProperty("exporttoexcelwarningtooltip"));
			exportToExcelWarningFinder = xpath(p.getProperty("exporttoexcelwarning"));
			massUpdateLinkFinder = xpath(p.getProperty("massupdate"));
			massUpdateContentHeaderFinder = xpath(p.getProperty("massupdatecontentheader"));
			massUpdateWarningFinder = xpath(p.getProperty("massupdatewarning"));
			massUpdateWarningToolTipFinder = id(p.getProperty("massupdatewarningtooltip"));
			massUpdateCustomerNameFinder = xpath(p.getProperty("massupdatecustomername"));
			massUpdateDivisionFinder = xpath(p.getProperty("massupdatedivision"));
			massUpdateCustomerDivisionSelectFinder = id(p.getProperty("massupdatecustomerdivisonselect"));
			massUpdateJobSiteFinder = id(p.getProperty("massupdatejobsite"));
			massUpdateJobSiteSelectFinder = id(p.getProperty("massupdatejobsiteselect"));
			massUpdateAssignedToFinder = id(p.getProperty("massupdateassignedto"));
			massUpdateAssignedToSelectFinder = id(p.getProperty("massupdateassignedtoselect"));
			massUpdateProductStatusFinder = id(p.getProperty("massupdateproductstatus"));
			massUpdateProductStatusSelectFinder = id(p.getProperty("massupdateproductstatusselect"));
			massUpdatePurchaseOrderFinder = id(p.getProperty("massupdatepurchaseorder"));
			massUpdatePurchaseOrderSelectFinder = id(p.getProperty("massupdatepurchaseorderselect"));
			massUpdateLocationFinder = id(p.getProperty("massupdatelocation"));
			massUpdateLocationSelectFinder = id(p.getProperty("massupdatelocationselect"));
			massUpdateIdentifiedFinder = id(p.getProperty("massupdateidentified"));
			massUpdateIdentifiedSelectFinder = id(p.getProperty("massupdateidentifiedselect"));
			massUpdateSaveButtonFinder = id(p.getProperty("massupdatesavebutton"));
			massUpdateReturnToSearchFinder = text(p.getProperty("massupdatereturntosearch"));
			massUpdateInstructionsFinder = xpath(p.getProperty("massupdateinstructions"));
			editProductLinkFinder = xpath(p.getProperty("editproductlink"));
			editProductPageContentHeaderFinder = xpath(p.getProperty("editproductcontentheader"));
			productConfigurationLinkFinder = xpath(p.getProperty("productconfigurationlink"));
			productConfigurationPageContentHeaderFinder = xpath(p.getProperty("productconfigurationcontentheader"));
			inspectionScheduleLinkFinder = xpath(p.getProperty("inspectionschedulelink"));
			inspectionSchedulePageContentHeaderFinder = xpath(p.getProperty("inspectionschedulecontentheader"));
			addScheduleDateFinder = id(p.getProperty("addscheduledate"));
			addScheduleInspectionTypeFinder = id(p.getProperty("addscheduleinspectiontype"));
			addScheduleJobFinder = id(p.getProperty("addschedulejob"));
			addScheduleSaveButtonFinder = id(p.getProperty("addschedulesavebutton"));
			scheduleForScheduleRowsFinder = xpath(p.getProperty("scheduleforrows"));
			smartSearchTextFieldFinder = id(p.getProperty("smartsearchtextfield"));
			smartSearchSearchButtonFinder = id(p.getProperty("smartsearchbutton"));
			selectDisplayColumnHeaderFinder = xpath(p.getProperty("selectdisplaycolumnheader"));
			addNewComponentFindExistingDivsFinder = xpath(p.getProperty("addnewcomponentfindexistingdivs"));
			productLookupSmartSearchTextFieldFinder = xpath(p.getProperty("productlookupsmartsearchtextfield"));
			productLookupSmartSearchLoadButtonFinder = xpath(p.getProperty("productlookupsmartsearchloadbutton"));
			productLookupSmartSearchSelectButtonFinder = xpath(p.getProperty("productlookupsmartsearchselectbutton"));
			editProductSerialNumberFinder = xpath(p.getProperty("editproductserialnumber"));
			editProductRfidNumberFinder = xpath(p.getProperty("editproductrfidnumber"));
			editProductReferenceNumberFinder = xpath(p.getProperty("editproductreferencenumber"));
			editProductCustomerFinder = xpath(p.getProperty("editproductcustomer"));
			editProductDivisionFinder = xpath(p.getProperty("editproductdivision"));
			editProductLocationFinder = xpath(p.getProperty("editproductlocation"));
			editProductProductStatusFinder = xpath(p.getProperty("editproductproductstatus"));
			editProductPurchaseOrderFinder = xpath(p.getProperty("editproductpurchaseorder"));
			editProductIdentifiedFinder = xpath(p.getProperty("editproductidentified"));
			editProductProductTypeFinder = xpath(p.getProperty("editproductproducttype"));
			editProductCommentsFinder = xpath(p.getProperty("editproductcomments"));
			editProductCommentTemplatesFinder = xpath(p.getProperty("editproductcommenttemplates"));
			editProductGenerateLinkFinder = xpath(p.getProperty("editproductgeneratelink"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	/**
	 * Goes to the Assets page. Same as clicking on the Asset link at the top of page.
	 * 
	 * @throws Exception
	 */
	public void gotoAssets() throws Exception {
		Link assetsLink = ie.link(assetsFinder);
		assertTrue("Could not find the link to Assets", assetsLink.exists());
		assetsLink.click();
		checkAssetsPageContentHeader();
	}
	
	/**
	 * Checks to make sure we arrived at the Assets page.
	 * 
	 * @throws Exception
	 */
	public void checkAssetsPageContentHeader() throws Exception {
		HtmlElement assetsContentHeader = ie.htmlElement(assetsContentHeaderFinder);
		assertTrue("Could not find Assets page content header '" + p.getProperty("contentheader") + "'", assetsContentHeader.exists());
	}
	
	/**
	 * Checks the header tag at the top of the body to make sure we are on the correct page.
	 * 
	 * @param serialNumber
	 * @throws Exception
	 */
	public void checkProductInformationPageContentHeader(String serialNumber) throws Exception {
		HtmlElement productInformationContentHeader = ie.htmlElement(productInformationContentHeaderFinder);
		assertTrue("Could not find Product Information page content header", productInformationContentHeader.exists());
		assertTrue("Could not find 'Asset - " + serialNumber + "'", productInformationContentHeader.text().contains(serialNumber));
	}

	/**
	 * This is a complex method. It will go to Assets, enter the serial number into
	 * the search criteria, run the search then click on the link to the product it
	 * finds so you end up at the Product Information page for that serial number.
	 *
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoProductInformation(String serialNumber) throws Exception {
		assertNotNull(serialNumber);
		ProductSearchCriteria p = new ProductSearchCriteria(serialNumber);
		assertNotNull(p);
		gotoAssets();
		setProductSearchCriteria(p);
		gotoProductSearchResults();
		Link product = ie.link(text(serialNumber));
		assertTrue("Could not find the link to the product information. Serial number: '" + serialNumber + "'", product.exists());
		product.click();
		checkProductInformationPageContentHeader(serialNumber);
	}
	
	/**
	 * The product attributes on the Select Columns form are dynamic based
	 * on the search criteria. After you update the search criteria you can
	 * call this method to get a list of the LABELs under the Product
	 * Attributes column.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getDynamicSelectColumns() throws Exception {
		List<String> result = new ArrayList<String>();
		Div dynamicProductAttributes = ie.div(productAttributesSelectColumnsFinder);
		assertTrue("Could not find the Product Attributes on the Select Columns form", dynamicProductAttributes.exists());
		Checkboxes cbs = dynamicProductAttributes.checkboxes();
		assertNotNull(cbs);
		Iterator<Checkbox> i = cbs.iterator();
		while(i.hasNext()) {
			Checkbox value = i.next();
			String key = value.label(xpath("../LABEL")).text();
			result.add(key);
		}
		return result;
	}
	
	/**
	 * Expands the Search Results section on the Product Search Results page.
	 *  
	 * @throws Exception
	 */
	public void expandProductSearchResultsSearchCriteria() throws Exception {
		Link searchCriteriaExpander = ie.link(productSearchExpandSearchCriteriaFinder);
		assertTrue("Could not find the link to expand the Search Criteria information", searchCriteriaExpander.exists());
		searchCriteriaExpander.click();
		checkSearchCriteriaExpanded();
	}
	
	/**
	 * Checks to see if the search criteria form exists and is visible.
	 * 
	 * @throws Exception
	 */
	private void checkSearchCriteriaExpanded() throws Exception {
		Form searchCriteriaForm = ie.form(searchCriteriaFormFinder);
		assertTrue("Could not find the Search Criteria form", searchCriteriaForm.exists());
		boolean hidden = searchCriteriaForm.style().contains("display: none");
		assertFalse("The Search Criteria form appears to be hidden", hidden);
	}

	/**
	 * Expands the Select Columns on the Product Search and Product Search
	 * Results page.
	 * 
	 * @throws Exception
	 */
	public void expandProductSearchSelectColumns() throws Exception {
		Link selectColumnsExpander = ie.link(productSearchExpandSelectColumnFinder);
		assertTrue("Could not find the link to expand the Select Columns information", selectColumnsExpander.exists());
		selectColumnsExpander.click();
		checkSelectColumnsExpanded();
	}

	/**
	 * Checks that the Select Column form exists and is visible.
	 * 
	 * @throws Exception
	 */
	private void checkSelectColumnsExpanded() throws Exception {
		Div selectColumnsForm = ie.div(selectColumnFormFinder);
		assertTrue("Could not find the Select Columns form", selectColumnsForm.exists());
		boolean hidden = selectColumnsForm.style().contains("display: none");
		assertFalse("The Select Columns form appears to be hidden", hidden);
	}

	/**
	 * Fills in the Select Columns form.
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void setProductSearchColumns(ProductSearchSelectColumns c) throws Exception {
		Checkbox serialNumber = ie.checkbox(selectColumnSerialNumberFinder);
		assertTrue("Could not find the checkbox for the serial number", serialNumber.exists());
		serialNumber.set(c.getSerialNumber());

		Checkbox rfidNumber = ie.checkbox(selectColumnRFIDNumberFinder);
		assertTrue("Could not find the checkbox for the RFID number", rfidNumber.exists());
		rfidNumber.set(c.getRFIDNumber());

		Checkbox customer = ie.checkbox(selectColumnCustomerNameFinder);
		assertTrue("Could not find the checkbox for the customer name", customer.exists());
		customer.set(c.getCustomerName());

		Checkbox division = ie.checkbox(selectColumnDivisionFinder);
		assertTrue("Could not find the checkbox for the division", division.exists());
		division.set(c.getDivision());

		Checkbox organization = ie.checkbox(selectColumnOrganizationFinder);
		assertTrue("Could not find the checkbox for the organization", organization.exists());
		organization.set(c.getOrganization());

		Checkbox referenceNumber = ie.checkbox(selectColumnReferenceNumberFinder);
		assertTrue("Could not find the checkbox for the reference number", referenceNumber.exists());
		referenceNumber.set(c.getReferenceNumber());

		Checkbox productType = ie.checkbox(selectColumnProductTypeFinder);
		assertTrue("Could not find the checkbox for the product type", productType.exists());
		productType.set(c.getProductType());

		Checkbox productStatus = ie.checkbox(selectColumnProductStatusFinder);
		assertTrue("Could not find the checkbox for the product status", productStatus.exists());
		productStatus.set(c.getProductStatus());

		Checkbox lastInspectionDate = ie.checkbox(selectColumnLastInspectionDateFinder);
		assertTrue("Could not find the checkbox for the last inspection date", lastInspectionDate.exists());
		lastInspectionDate.set(c.getLastInspectionDate());

		Checkbox dateIdentified = ie.checkbox(selectColumnDateIdentifiedFinder);
		assertTrue("Could not find the checkbox for the date identified", dateIdentified.exists());
		dateIdentified.set(c.getDateIdentified());

		Checkbox identifiedBy = ie.checkbox(selectColumnIdentifiedByFinder);
		assertTrue("Could not find the checkbox for the identified by", identifiedBy.exists());
		identifiedBy.set(c.getIdentifiedBy());

		Checkbox description = ie.checkbox(selectColumnDescriptionFinder);
		assertTrue("Could not find the checkbox for the description", description.exists());
		description.set(c.getDescription());

		Checkbox location = ie.checkbox(selectColumnLocationFinder);
		assertTrue("Could not find the checkbox for the location", location.exists());
		location.set(c.getLocation());

		Checkbox orderDescription = ie.checkbox(selectColumnOrderDescriptionFinder);
		assertTrue("Could not find the checkbox for the order description", orderDescription.exists());
		orderDescription.set(c.getOrderDescription());

		Checkbox orderNumber = ie.checkbox(selectColumnOrderNumberFinder);
		assertTrue("Could not find the checkbox for the order number", orderNumber.exists());
		orderNumber.set(c.getOrderNumber());

		Checkbox purchaseOrder = ie.checkbox(selectColumnPurchaseOrderFinder);
		assertTrue("Could not find the checkbox for the purchase order", purchaseOrder.exists());
		purchaseOrder.set(c.getPurchaseOrder());
	}

	/**
	 * Fills in the product search criteria form.
	 * 
	 * @param p
	 * @throws Exception
	 */
	public void setProductSearchCriteria(ProductSearchCriteria p) throws Exception {
		assertNotNull(p);
	
		TextField rfidNumber = ie.textField(productSearchRFIDNumberFinder);
		assertTrue("Could not find the RFID Number text field", rfidNumber.exists());
		if(p.getRFIDNumber() != null) {
			rfidNumber.set(p.getRFIDNumber());
		}

		TextField serialNumber = ie.textField(productSearchSerialNumberFinder);
		assertTrue("Could not find the Serial Number text field", serialNumber.exists());
		if(p.getSerialNumber() != null) {
			serialNumber.set(p.getSerialNumber());
		}

		TextField orderNumber = ie.textField(productSearchOrderNumberFinder);
		assertTrue("Could not find the Order Number text field", orderNumber.exists());
		if(p.getOrderNumber() != null) {
			orderNumber.set(p.getOrderNumber());
		}

		TextField purchaseNumber = ie.textField(productSearchPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Number text field", purchaseNumber.exists());
		if(p.getPurchaseOrder() != null) {
			purchaseNumber.set(p.getPurchaseOrder());
		}

		if(p.getCustomer() != null) {
			setCustomer(p.getCustomer());
		}

		if(p.getDivision() != null) {
			SelectList division = ie.selectList(productSearchDivisionFinder);
			assertTrue("Could not find the Division select list", division.exists());
			Option o = division.option(text(p.getDivision()));
			assertTrue("Could not find the division '" + p.getDivision() + "' on the division list.", o.exists());
			o.select();
		}
		
		if(p.getJobSite() != null) {
			SelectList jobSite = ie.selectList(productSearchJobSiteFinder);
			assertTrue("Could not find the Job Site select list", jobSite.exists());
			Option o = jobSite.option(text(p.getJobSite()));
			assertTrue("Could not find the job site '" + p.getJobSite() + "' on the job site list.", o.exists());
			o.select();
		}
		
		if(p.getAssignedTo() != null) {
			SelectList assignedTo = ie.selectList(productSearchAssignedToFinder);
			assertTrue("Could not find the Assigned To select list", assignedTo.exists());
			Option o = assignedTo.option(text(p.getAssignedTo()));
			assertTrue("Could not find the user '" + p.getAssignedTo() + "' on the assigned to list.", o.exists());
			o.select();
		}

		TextField location = ie.textField(productSearchLocationFinder);
		assertTrue("Could not find the Location text field", location.exists());
		if(p.getLocation() != null) {
			location.set(p.getLocation());
		}

		TextField referenceNumber = ie.textField(productSearchReferenceNumberFinder);
		assertTrue("Could not find the Reference Number text field", referenceNumber.exists());
		if(p.getReferenceNumber() != null) {
			referenceNumber.set(p.getReferenceNumber());
		}
		
		SelectList productStatus = ie.selectList(productSearchProductStatusFinder);
		assertTrue("Could not find the Product Status select list", productStatus.exists());
		if(p.getProductStatus() != null) {
			Option o = productStatus.option(text(p.getProductStatus()));
			assertTrue("Could not find the product status '" + p.getProductStatus() + "' on the product status list.", o.exists());
			o.select();
		}

		SelectList productType = ie.selectList(productSearchProductTypeFinder);
		assertTrue("Could not find the Product Type select list", productType.exists());
		if(p.getProductType() != null) {
			Option o = productType.option(text(p.getProductType()));
			assertTrue("Could not find the product type '" + p.getProductType() + "' on the product type list.", o.exists());
			o.select();
			productType.fireEvent("onchange");
		}

		TextField fromDate = ie.textField(productSearchFromDateFinder);
		assertTrue("Could not find the From Date text field", fromDate.exists());
		if(p.getFromDate() != null) {
			fromDate.set(p.getFromDate());
		}
		
		TextField toDate = ie.textField(productSearchToDateFinder);
		assertTrue("Could not find the Reference Number text field", toDate.exists());
		if(p.getToDate() != null) {
			toDate.set(p.getToDate());
		}
	}

	/**
	 * This helper method will set the customer on the Search Criteria form
	 * and wait for it to update the Division information.
	 * 
	 * @param c
	 * @throws Exception
	 */
	private void setCustomer(String c) throws Exception {
		SelectList customer = ie.selectList(productSearchCustomerFinder);
		assertTrue("Could not find the Customer select list", customer.exists());
		Option o = customer.option(text(c));
		assertTrue("Could not find the customer '" + c + "' on the customer list.", o.exists());
		o.select();
		customer.fireEvent("onchange");
		misc.waitForJavascript();
	}

	/**
	 * Clicks the Run button on the Product Search page.
	 * 
	 * @throws Exception
	 */
	public void gotoProductSearchResults() throws Exception {
		Button run = ie.button(productSearchRunButtonFinder);
		assertTrue("Could not find the Run button", run.exists());
		run.click();
		
		checkProductSearchResultsPage();
	}

	/**
	 * Checks for the content header on the Product Search Results page.
	 * 
	 * @throws Exception
	 */
	public void checkProductSearchResultsPage() throws Exception {
		HtmlElement productSearchResultsContentHeader = ie.htmlElement(productSearchResultsContentHeaderFinder);
		assertTrue("Could not find Assets page content header", productSearchResultsContentHeader.exists());
	}

	/**
	 * Goes to the Inspection For - serialNumber. Assumes you are already
	 * on the Product Information page.
	 * 
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoInspectionsFor(String serialNumber) throws Exception {
		assertNotNull(serialNumber);
		Link viewAllInspections = ie.link(viewAllInspectionsFinder);
		assertTrue("Could not find 'Inspections' link on Product Information page", viewAllInspections.exists());
		viewAllInspections.click();
		checkInspectionsForContentHeader();
	}

	/**
	 * Checks for the header on the Inspection For - serialNumber page.
	 * 
	 * @throws Exception
	 */
	public void checkInspectionsForContentHeader() throws Exception {
		HtmlElement inspectionForContentHeader = ie.htmlElement(inspectionForFinder);
		assertTrue("Could not find Inspection For page content header '" + p.getProperty("inspectionforcontentheader") + "'", inspectionForContentHeader.exists());
	}
	
	/**
	 * This returns a list of all the customers on the Customer list
	 * on the Product Search Search Criteria form. It assumes you are
	 * already at the page with the form, e.g. gotoAssets().
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getCustomersOnSearchCriteria() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList customer = ie.selectList(productSearchCustomerFinder);
		assertTrue("Could not find the Customer select list", customer.exists());
		Options customers = customer.options();
		Iterator<Option> i = customers.iterator();
		// If there are many options on the select list, we don't want
		// to refresh the page while processing the list of options.
		misc.stopMonitorStatus();
		while(i.hasNext()) {
			Option o = i.next();
			String c = o.text();
			if(!c.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		misc.startMonitorStatus();
		return results;
	}
	
	/**
	 * This returns a list of all the divisions on the Division list
	 * on the Product Search Search Criteria form. It assumes you are
	 * already at the page with the form, e.g. gotoAssets(). It will
	 * select the given customer and wait for the Division select list
	 * to update before generating the list.
	 * 
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public List<String> getDivisionsOnSearchCriteria(String customer) throws Exception {
		List<String> results = new ArrayList<String>();
		setCustomer(customer);
		SelectList division = ie.selectList(productSearchDivisionFinder);
		assertTrue("Could not find the Division select list", division.exists());
		Options divisions = division.options();
		Iterator<Option> i = divisions.iterator();
		// If there are many options on the select list, we don't want
		// to refresh the page while processing the list of options.
		misc.stopMonitorStatus();
		while(i.hasNext()) {
			Option o = i.next();
			String d = o.text();
			if(!d.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		misc.startMonitorStatus();
		return results;
	}

	/**
	 * This returns a list of all the product statuses on the Product
	 * Status list on the Product Search Search Criteria form. It 
	 * assumes you are already at the page with the form, e.g. gotoAssets().
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getProductStatusesOnSearchCriteria() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList status = ie.selectList(productSearchProductStatusFinder);
		assertTrue("Could not find the Product Status select list", status.exists());
		Options statuses = status.options();
		Iterator<Option> i = statuses.iterator();
		// If there are many options on the select list, we don't want
		// to refresh the page while processing the list of options.
		misc.stopMonitorStatus();
		while(i.hasNext()) {
			Option o = i.next();
			String ps = o.text();
			if(!ps.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		misc.startMonitorStatus();
		return results;
	}
	
	/**
	 * This returns a list of all the product types on the Product
	 * Type list on the Product Search Search Criteria form. It 
	 * assumes you are already at the page with the form, e.g. gotoAssets().
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getProductTypesOnSearchCriteria() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList productTypes = ie.selectList(productSearchProductTypeFinder);
		assertTrue("Could not find the Product Type select list", productTypes.exists());
		Options types = productTypes.options();
		Iterator<Option> i = types.iterator();
		// If there are many options on the select list, we don't want
		// to refresh the page while processing the list of options.
		misc.stopMonitorStatus();
		while(i.hasNext()) {
			Option o = i.next();
			String pt = o.text();
			if(!pt.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		misc.startMonitorStatus();
		return results;
		
	}

	/**
	 * Click the Clear Form button on the Product Search Criteria form.
	 * 
	 * @throws Exception
	 */
	public void gotoProductSearchClearForm() throws Exception {
		Button clearForm = ie.button(productSearchClearFormButtonFinder);
		assertTrue("Could not find the Clear Form button", clearForm.exists());
		clearForm.click();
	}

	/**
	 * Get a list of all the serial numbers for all the
	 * products in a Product Search Result. It assumes
	 * the search results have the serial number column
	 * available.
	 * 
	 * If the search result is an empty page this will
	 * return an empty list. i.e. results.size() == 0.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getProductSearchResultsSerialNumbers() throws Exception {
		List<String> results = new ArrayList<String>();
		Link next;
		boolean more;
		do {
			next = ie.link(text("Next>"));
			more = next.exists(); 
			results.addAll(getProductSearchResultsCurrentPage());
			if(more) {
				next.click();
			}
		} while(more);
		
		return results;
	}

	private Collection<String> getProductSearchResultsCurrentPage() throws Exception {
		List<String> results = new ArrayList<String>();
		HtmlElement p = ie.htmlElement(searchHasNoResultsFinder);
		if(p.exists()) {
			return results;
		}
		TableRow tr = ie.row(xpath("//TABLE[@id='resultsTable']/TBODY/TR[1]"));
		assertTrue("Could not find the header row for search results table.", tr.exists());
		int index = 0, serialNumberColumn = -1;
		for(index = 0; index < tr.columnCount(); index++) {
			TableCell th = tr.cell(index);
			if(th.text().trim().equals("Serial Number")) {
				serialNumberColumn = index;
				break;
			}
		}
		assertFalse("Could not find the column for Serial Numbers.", serialNumberColumn == -1);
		Links serialNumbers = ie.links(xpath("//TABLE[@id='resultsTable']/TBODY/TR/TD[" + (serialNumberColumn+1) + "]/A"));
		assertNotNull("Could not find any serial number links.", serialNumbers);
		Iterator<Link> i = serialNumbers.iterator();
		while(i.hasNext()) {
			results.add(i.next().text().trim());
		}
		return results;
	}
	
	/**
	 * Go to the Product Information page from the Product Search
	 * Results page using the Info link. It assumes the product is
	 * on the current page. You can ensure this by searching for
	 * the exact serial number then calling this method.
	 * 
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoProductInformationViaInfoLink(String serialNumber) throws Exception {
		TableRow tr = ie.row(xpath("//TD/A[text()='" + serialNumber + "']/../.."));
		assertTrue("Could not find the row with serial number: '" + serialNumber + "'", tr.exists());
		Link l = tr.link(text("/Info/"));
		assertTrue("Could not find the Info link for the product: '" + serialNumber + "'", l.exists());
		l.click();
		ie.waitUntilReady();
		checkProductInformationPageContentHeader(serialNumber);
	}
	
	/**
	 * Goes to the Manage Inspection link on the Product Information
	 * page. It assumes you are already on the Product Information page.
	 * 
	 * @param serialNumber
	 * @throws Exception
	 */
	public void gotoInspectionGroups(String serialNumber) throws Exception {
		Link manageInspections = ie.link(manageInspectionLinkFinder);
		assertTrue("Could not find the Manage Inspection link on the current page", manageInspections.exists());
		manageInspections.click();
		checkInspectionGroupsContentHeader(serialNumber);
	}

	private void checkInspectionGroupsContentHeader(String serialNumber) throws Exception {
		HtmlElement inspectionGroupsContentHeader = ie.htmlElement(inspectionGroupsContentHeaderFinder);
		assertTrue("Could not find Inspect page content header", inspectionGroupsContentHeader.exists());
		assertTrue("Could not find 'Inspect - " + serialNumber + "'", inspectionGroupsContentHeader.text().contains(serialNumber));
	}

	/**
	 * Returns the number of products found in a Product Search.
	 * Basically, gives you the number at the bottom of each
	 * search results page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getTotalNumberOfProducts() throws Exception {
		long result = -1;
		HtmlElement noResultsReturned = ie.htmlElement(noResultsReturnedFinder);
		if(noResultsReturned.exists()) {
			result = 0;
		} else {
			Span total = ie.span(totalProductsSpanFinder);
			assertTrue("Could not find the Total Products information at the bottom of Search Results", total.exists());
			String s = total.text().trim();
			result = Long.parseLong(s.replace("Total Products ", ""));
		}
		return result;
	}
	
	/**
	 * If the search results are greater than 1000 products,
	 * clicking the print all manufacturer certificates link
	 * should pop open a tool tip like dialog telling the user
	 * to fix their search results. This confirms that tool tip
	 * appears and have the right information.
	 * 
	 * @throws Exception
	 */
	public void printAllManufacturerCertificatesWarningOver1000Products() throws Exception {
		String message = "You can only print out result list of 1000 or less. Please refine your results so that there are less than 1000.";
		Link printAllCerts = ie.link(printAllManufacturerCertificatesWarningFinder);
		assertTrue("Could not find link to print all manufacturer certificates with more than 1000 products", printAllCerts.exists());
		printAllCerts.click();
		Div warning = ie.div(printAllManufacturerCertificatesWarningToolTipFinder);
		assertTrue("Could not find the warning about more than 1000 products.", warning.exists());
		String s = warning.text();
		assertTrue("Tool tip warning does not contain the message '" + message + "'", s.contains(message));
	}

	/**
	 * Prints all the manufacturer certificates associated with the
	 * products on the current search result. Currently, if there are
	 * more than 1000 products in the search results, the link will
	 * not exist and this method will throw an exception. It would be
	 * best to call getTotalNumberOfProducts() and see if there are
	 * less than 1000 products if you don't want this to throw an
	 * exception.
	 * 
	 * @throws Exception
	 */
	public void printAllManufacturerCertificates() throws Exception {
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		Link printAllCerts = ie.link(printAllManufacturerCertificatesFinder);
		assertTrue("Could not find link to print all manufacturer certificates", printAllCerts.exists());
		printAllCerts.click();
		misc.clickLightboxOKbutton(message);
	}

	/**
	 * If the search results are greater than 10000 products,
	 * clicking the export to excel link
	 * should pop open a tool tip like dialog telling the user
	 * to fix their search results. This confirms that tool tip
	 * appears and have the right information.
	 * 
	 * @throws Exception
	 */
	public void exportToExcelWarningOver10000Products() throws Exception {
		String message = "You can only export a maximum of 10000 results to excel. Please refine your results so that there are less than 10000.";
		Link export = ie.link(exportToExcelWarningFinder);
		assertTrue("Could not find link to export to Excel with more than 10000 products", export.exists());
		export.click();
		Div warning = ie.div(exportToExcelWarningToolTipFinder);
		assertTrue("Could not find the warning about more than 10000 products.", warning.exists());
		String s = warning.text();
		assertTrue("Tool tip warning does not contain the message '" + message + "'", s.contains(message));
	}

	public void exportToExcel() throws Exception {
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		Link export = ie.link(exportToExcelFinder);
		assertTrue("Could not find the export to Excel link", export.exists());
		export.click();
		misc.clickLightboxOKbutton(message);
	}

	public void gotoMassUpdate() throws Exception {
		Link massUpdate = ie.link(massUpdateLinkFinder);
		assertTrue("Could not find the link to go to Mass Update", massUpdate.exists());
		massUpdate.click();
		checkMassUpdateContentHeader();
		checkMassUpdateInstructions();
	}

	private void checkMassUpdateInstructions() throws Exception {
		HtmlElement massUpdateInstructions = ie.htmlElement(massUpdateInstructionsFinder);
		assertTrue("Could not find the Instructions on the Mass Update Product page.", massUpdateInstructions.exists());
		String s = "To update a field click the check box beside that field and set the desired value. Then click save. This could take some time to complete the update, please be patient.";
		assertTrue("Could not find the Instructions '" + s + "' on the Mass Update Products page", massUpdateInstructions.html().contains(s));
	}

	private void checkMassUpdateContentHeader() throws Exception {
		HtmlElement massUpdateContentHeader = ie.htmlElement(massUpdateContentHeaderFinder);
		assertTrue("Could not find Mass Update page content header", massUpdateContentHeader.exists());
	}
	
	public void gotoMassUpdateWarningOver1000Products() throws Exception {
		String message = "You can only mass update a maximum of 1000 results at one time. Please refine your results so that there are less than 1000.";
		Link update = ie.link(massUpdateWarningFinder);
		assertTrue("Could not find link to export to Excel with more than 1000 products", update.exists());
		update.click();
		Div warning = ie.div(massUpdateWarningToolTipFinder);
		assertTrue("Could not find the warning about more than 1000 products.", warning.exists());
		String s = warning.text();
		assertTrue("Tool tip warning does not contain the message '" + message + "'", s.contains(message));
	}
	
	public void setMassUpdate(MassUpdateForm m) throws Exception {
		assertNotNull(m);
		misc.stopMonitorStatus();
		String customerName = m.getCustomerName();
		if(customerName != null) {
			SelectList customer = ie.selectList(massUpdateCustomerNameFinder);
			assertTrue("Could not find the select list for Customer Name", customer.exists());
			Option o = customer.option(text(customerName));
			assertTrue("Could not find the customer '" + customerName + "'", o.exists());
			o.select();
			customer.fireEvent("onchange");
			Checkbox customerSelected = ie.checkbox(massUpdateCustomerDivisionSelectFinder);
			assertTrue("Could not find the Select checkbox for customer/divison", customerSelected.exists());
			customerSelected.set(true);
			misc.waitForJavascript();	// wait for customer list to update division
		}
		
		String div = m.getDivision();
		if(div != null) {
			SelectList division = ie.selectList(massUpdateDivisionFinder);
			assertTrue("Could not find the select list for Division", division.exists());
			Option o = division.option(text(div));
			assertTrue("Could not find the division '" + div + "'", o.exists());
			o.select();
			Checkbox divisionSelected = ie.checkbox(massUpdateCustomerDivisionSelectFinder);
			assertTrue("Could not find the Select checkbox for customer/divison", divisionSelected.exists());
			divisionSelected.set(true);
		}
		
		String js = m.getJobSite();
		if(js != null) {
			SelectList jobSites = ie.selectList(massUpdateJobSiteFinder);
			assertTrue("Could not find the select list for Job Sites", jobSites.exists());
			Option o = jobSites.option(text(js));
			assertTrue("Could not find the job site '" + js + "' in the list of job sites", o.exists());
			o.select();
			Checkbox jobSiteSelected = ie.checkbox(massUpdateJobSiteSelectFinder);
			assertTrue("Could not find the Select checkbox for job sites", jobSiteSelected.exists());
			jobSiteSelected.set(true);
		}
		
		String at = m.getAssignedTo();
		if(at != null) {
			SelectList assignedTo = ie.selectList(massUpdateAssignedToFinder);
			assertTrue("Could not find the select list for Assigned To", assignedTo.exists());
			Option o = assignedTo.option(text(at));
			assertTrue("Could not find the assigned to '" + at + "' in the list of assigned to", o.exists());
			o.select();
			Checkbox assignedToSelected = ie.checkbox(massUpdateAssignedToSelectFinder);
			assertTrue("Could not find the Select checkbox for job sites", assignedToSelected.exists());
			assignedToSelected.set(true);
		}

		String ps = m.getProductStatus();
		if(ps != null) {
			SelectList productStatus = ie.selectList(massUpdateProductStatusFinder);
			assertTrue("Could not find the select list for Product Status", productStatus.exists());
			Option o = productStatus.option(text(ps));
			assertTrue("Could not find the product status '" + ps + "'", o.exists());
			o.select();
			Checkbox productStatusSelected = ie.checkbox(massUpdateProductStatusSelectFinder);
			assertTrue("Could not find the Select checkbox for product status", productStatusSelected.exists());
			productStatusSelected.set(true);
		}

		String po = m.getPurchaseOrder();
		if(po != null) {
			TextField purchaseOrder = ie.textField(massUpdatePurchaseOrderFinder);
			assertTrue("Could not find the text field for Purchase Order", purchaseOrder.exists());
			purchaseOrder.set(po);
			Checkbox purchaseOrderSelected = ie.checkbox(massUpdatePurchaseOrderSelectFinder);
			assertTrue("Could not find the Select checkbox for purchase order", purchaseOrderSelected.exists());
			purchaseOrderSelected.set(true);
		}

		String loc = m.getLocation();
		if(loc != null) {
			TextField location = ie.textField(massUpdateLocationFinder);
			assertTrue("Could not find the text field for Location", location.exists());
			location.set(loc);
			Checkbox locationSelected = ie.checkbox(massUpdateLocationSelectFinder);
			assertTrue("Could not find the Select checkbox for location", locationSelected.exists());
			locationSelected.set(true);
		}

		String ident = m.getIdentified();
		if(ident != null) {
			TextField identified = ie.textField(massUpdateIdentifiedFinder);
			assertTrue("Could not find the text field for Identified", identified.exists());
			identified.set(ident);
			Checkbox identifiedSelected = ie.checkbox(massUpdateIdentifiedSelectFinder);
			assertTrue("Could not find the Select checkbox for identified", identifiedSelected.exists());
			identifiedSelected.set(true);
		}
		misc.startMonitorStatus();
	}

	public void gotoSaveMassUpdate() throws Exception {
		Button save = ie.button(massUpdateSaveButtonFinder);
		assertTrue("Could not find the Save button on mass update", save.exists());
		misc.createThreadToCloseAreYouSureDialog();
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	/**
	 * This selects the link on the Mass Update page to bring you
	 * back to the Product Search Results page.
	 * 
	 * @throws Exception
	 */
	public void gotoProductSearchResultsFromMassUpdate() throws Exception {
		Link returnToProductSearchLink = ie.link(massUpdateReturnToSearchFinder);
		assertTrue("Could not find the link to return to search.", returnToProductSearchLink.exists());
		returnToProductSearchLink.click();
		checkProductSearchResultsPage();
	}

	/**
	 * This assumes you are on the Product Information page and want to switch
	 * to the Edit product page.
	 * 
	 * @throws Exception
	 */
	public void gotoEditProduct(String serialNumber) throws Exception {
		Link editProduct = ie.link(editProductLinkFinder);
		assertTrue("Could not find the link to Edit Product", editProduct.exists());
		editProduct.click();
		checkEditProductPageContentHeader(serialNumber);
	}

	private void checkEditProductPageContentHeader(String serialNumber) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(editProductPageContentHeaderFinder);
		String expected = "Asset - " + serialNumber;
		assertTrue("Could not find the '" + expected + "' header", contentHeader.exists());
		String s = contentHeader.text();
		assertTrue("Expected '" + expected + "' but found '" + s + "'", s.contains(expected));
	}

	public void gotoProductConfiguration(String serialNumber) throws Exception {
		Link productConfig = ie.link(productConfigurationLinkFinder);
		assertTrue("Could not find the link to Product Configuration", productConfig.exists());
		productConfig.click();
		checkProductConfigurationPageContentHeader(serialNumber);
	}

	private void checkProductConfigurationPageContentHeader(String serialNumber) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(productConfigurationPageContentHeaderFinder);
		String expected = "Asset - " + serialNumber;
		assertTrue("Could not find the '" + expected + "' header", contentHeader.exists());
		String s = contentHeader.text().trim();
		assertTrue("Expected '" + expected + "' but found '" + s + "'", s.equals(expected));
	}

	public void gotoInspectionSchedule(String serialNumber) throws Exception {
		Link inspectionSchedule = ie.link(inspectionScheduleLinkFinder);
		assertTrue("Could not find the link to Inspection Schedule", inspectionSchedule.exists());
		inspectionSchedule.click();
		checkInspectionSchedulePageContentHeader(serialNumber);
		ie.waitUntilReady();
	}

	private void checkInspectionSchedulePageContentHeader(String serialNumber) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(inspectionSchedulePageContentHeaderFinder);
		String expected = "Asset - " + serialNumber;
		assertTrue("Could not find the '" + expected + "' header", contentHeader.exists());
		String s = contentHeader.text().trim();
		assertTrue("Expected '" + expected + "' but found '" + s + "'", s.contains(expected));
	}
	
	/**
	 * Adds a schedule to the currently viewed product. Assumes you have used
	 * gotoProductInformationViaSmartSearch to get to the Product Information
	 * page then used gotoInspectionSchedule to get to the page for scheduling
	 * an inspection on a product.
	 * 
	 * @param scheduleDate
	 * @param inspectionType
	 * @param job
	 * @throws Exception
	 */
	public void addScheduleFor(String scheduleDate, String inspectionType, String job) throws Exception {
		TextField schedule = ie.textField(addScheduleDateFinder);
		assertTrue("Could not find the text field for Schedule Date", schedule.exists());
		schedule.set(scheduleDate);
		SelectList addScheduleInspectionType = ie.selectList(addScheduleInspectionTypeFinder);
		assertTrue("Could not find the select list for Inspection Type", addScheduleInspectionType.exists());
		Option o = addScheduleInspectionType.option(text(inspectionType));
		assertTrue("Could not find the inspection type '" + inspectionType + "'", o.exists());
		o.select(true);
		if(job != null) {
			SelectList jobs = ie.selectList(addScheduleJobFinder);
			assertTrue("Could not find the select list for Job", jobs.exists());
			Option o2 = jobs.option(text(job));
			assertTrue("Could not find the job '" + job + "' in the job list", o2.exists());
			o2.select(true);
		}
		Button save = ie.button(addScheduleSaveButtonFinder);
		assertTrue("Could not find the Save button for adding a schedule", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void editScheduleFor(String scheduleDate, String inspectionType, String job, String newScheduleDate, String newJob) throws Exception {
		assertNotNull(scheduleDate);
		assertNotNull(inspectionType);
		assertNotNull(newScheduleDate);
		misc.stopMonitorStatus();
		TableRows trs = ie.rows(scheduleForScheduleRowsFinder);
		assertNotNull("Could not find the rows containing schedule for product", trs);
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			assertTrue("Could not find a row in the schedule list", tr.exists());
			String it = tr.cell(0).text().trim();
			String nsd = tr.span(0).text().trim();
			if(it.equals(inspectionType) && nsd.equals(scheduleDate)) {
				Link edit = tr.link(xpath("TD/SPAN/A[text()='Edit']"));
				assertTrue("Could not find the edit link for the schedule to be edited.", edit.exists());
				String id = tr.id().replace("type_", "");
				edit.click();
				misc.waitForJavascript();
				TextField date = ie.textField(id("schedule_" + id + "_nextDate"));
				assertTrue("Could not find the date text field after clicking Edit", date.exists());
				date.set(newScheduleDate);
				Link save = date.link(xpath("../SPAN/A[1]"));
				assertTrue("Could not find the Save link to save the new date", save.exists());
				save.click();
				misc.checkForErrorMessagesOnCurrentPage();
				break;
			}
		}
		misc.startMonitorStatus();
	}
	
	public int SmartSearch(String searchString) throws Exception {
		TextField find = ie.textField(smartSearchTextFieldFinder);
		assertTrue("Could not find the Smart Search text field", find.exists());
		find.focus();
		find.set(searchString);
		Button search = ie.button(smartSearchSearchButtonFinder);
		assertTrue("Could not find the Smart Search Search button", search.exists());
		search.click();
		return misc.getSmartSearchResultCount();
	}

	public String getSelectDisplayColumnsHeader() throws Exception {
		String result = null;
		HtmlElement header = ie.htmlElement(selectDisplayColumnHeaderFinder);
		assertTrue("Could not find the header for the Select Display Columns section", header.exists());
		result = header.text().trim();
		return result;
	}

	public void addSubProductToMasterProduct(String subProductType, String subProductSerialNumber) throws Exception {
		Divs find = ie.divs(addNewComponentFindExistingDivsFinder);
		assertNotNull(find);
		assertTrue("Could not find any sub components to add", find.length() > 0);
		Iterator<Div> i = find.iterator();
		Link l = null;
		while(i.hasNext()) {
			Div d = i.next();
			Div pt = d.div(xpath("DIV[contains(text(),'" + subProductType + "')]"));
			if(pt.exists()) {
				l = pt.link(xpath("../DIV[@class='createOptions']/A[contains(text(),'Find Existing')]"));
				break;
			}
		}
		assertNotNull(l);
		assertTrue("Could not find the Find Existing link for '" + subProductType +"'", l.exists());
		l.click();
		misc.waitForJavascript();
		TextField ss = ie.textField(productLookupSmartSearchTextFieldFinder);
		assertTrue("Could not find the Smart Search text field", ss.exists());
		ss.set(subProductSerialNumber);
		Button load = ie.button(productLookupSmartSearchLoadButtonFinder);
		assertTrue("Could not find the load button on smart search", load.exists());
		load.click();
		misc.waitForJavascript();
		Button select = ie.button(productLookupSmartSearchSelectButtonFinder);
		assertTrue("Could not find the select button for adding the product", select.exists());
		select.click();
		misc.waitForJavascript();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public List<String> getProductSearchResultsColumn(String s) throws Exception {
		List<String> results = new ArrayList<String>();
		Link next;
		boolean more;
		do {
			next = ie.link(text("Next>"));
			more = next.exists(); 
			results.addAll(getProductSearchResultsColumnCurrentPage(s));
			if(more) {
				next.click();
			}
		} while(more);
		
		return results;
	}

	private Collection<String> getProductSearchResultsColumnCurrentPage(String s) throws Exception {
		List<String> results = new ArrayList<String>();
		TableCells ths = ie.cells(xpath("//TABLE[@id='resultsTable']/TBODY/TR[1]/TH"));
		int index = 0;
		for(index = 0; index < ths.length(); index++) {
			TableCell th = ths.get(index);
			if(th.text().contains(s)) {
				break;
			}
		}
		assertFalse("Scanned all the column headers and did not find '" + s + "'", index == ths.length());
		TableCells tds = ie.cells(xpath("//TABLE[@id='resultsTable']/TBODY/TR/TD[" + (index+1) + "]"));
		Iterator<TableCell> i = tds.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			String v = td.text().trim();
			results.add(v);
		}
		return results;
	}
	
	public void editEmployeeProduct(Product p) throws Exception {
		Link generate = ie.link(editProductGenerateLinkFinder);
		assertTrue("Could not find the link to generate serial numbers", generate.exists());
		TextField serialNumber = ie.textField(editProductSerialNumberFinder);
		assertTrue("Could not find the serial number text field", serialNumber.exists());
		TextField rfid = ie.textField(editProductRfidNumberFinder);
		assertTrue("Could not find the RFID number text field", rfid.exists());
		TextField reference = ie.textField(editProductReferenceNumberFinder);
		assertTrue("Could not find the Reference number text field", reference.exists());
		SelectList customer = ie.selectList(editProductCustomerFinder);
		assertTrue("Could not find the customer select list", customer.exists());
		SelectList division = ie.selectList(editProductDivisionFinder);
		assertTrue("Could not find the division select list", division.exists());
		TextField location = ie.textField(editProductLocationFinder);
		assertTrue("Could not find the Location text field", location.exists());
		SelectList status = ie.selectList(editProductProductStatusFinder);
		assertTrue("Could not find the Product Status select list", status.exists());
		TextField po = ie.textField(editProductPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Order text field", po.exists());
		TextField identified = ie.textField(editProductIdentifiedFinder);
		assertTrue("Could not find the Identified text field", identified.exists());
		SelectList productType = ie.selectList(editProductProductTypeFinder);
		assertTrue("Could not find the Product Type select list", productType.exists());
		TextField comments = ie.textField(editProductCommentsFinder);
		assertTrue("Could not find the Comments text field", comments.exists());
		SelectList commentTemplate = ie.selectList(editProductCommentTemplatesFinder);
		assertTrue("Could not find the Comment Templates select list", commentTemplate.exists());
		
		if(p.getSerialNumber() != null) {
			serialNumber.set(p.getSerialNumber());
		}
		
		if(p.getRFIDNumber() != null) {
			rfid.set(p.getRFIDNumber());
		}
		
		if(p.getReferenceNumber() != null) {
			reference.set(p.getReferenceNumber());
		}
		
		if(p.getCustomer() != null) {
			Option o = customer.option(text(p.getCustomer()));
			assertTrue("Could not find the customer '" + p.getCustomer() + "'", o.exists());
			o.select();
			customer.fireEvent("onchange");
			misc.waitForJavascript();
		}
		
		if(p.getDivision() != null) {
			Option o = division.option(text(p.getDivision()));
			assertTrue("Could not find the division '" + p.getDivision() + "'", o.exists());
			o.select();
		}
		
		if(p.getLocation() != null) {
			location.set(p.getLocation());
		}
		
		if(p.getProductStatus() != null) {
			Option o = status.option(text(p.getProductStatus()));
			assertTrue("Could not find the Product Status '" + p.getProductStatus() + "'", o.exists());
			o.select();
		}
		
		if(p.getPurchaseOrder() != null) {
			po.set(p.getPurchaseOrder());
		}
		
		if(p.getIdentified() != null) {
			identified.set(p.getIdentified());
		}
		
		if(p.getProductType() != null) {
			Option o = productType.option(text(p.getProductType()));
			assertTrue("Could not find the Product Type '" + p.getProductType() + "'", o.exists());
			o.select();
			productType.fireEvent("onchange");
			misc.waitForJavascript();
		}
		
		if(p.getComments() != null) {
			comments.set(p.getComments());
		}
		
		if(p.getCommentTemplate() != null) {
			Option o = commentTemplate.option(text(p.getCommentTemplate()));
			assertTrue("Could not find the Comment Template '" + p.getCommentTemplate() + "'", o.exists());
			o.select();
		}
	}

	/**
	 * If divisional is true then we assume the end user is in a division.
	 * 
	 * @param divisional
	 * @throws Exception
	 */
	public void checkEndUserEditProduct(boolean divisional) throws Exception {
		Link generate = ie.link(editProductGenerateLinkFinder);
		assertFalse("Could find the link to generate serial numbers", generate.exists());
		TextField serialNumber = ie.textField(editProductSerialNumberFinder);
		assertFalse("Could find the serial number text field", serialNumber.exists());
		TextField rfid = ie.textField(editProductRfidNumberFinder);
		assertFalse("Could find the RFID number text field", rfid.exists());
		TextField reference = ie.textField(editProductReferenceNumberFinder);
		assertTrue("Could not find the Reference number text field", reference.exists());
		SelectList customer = ie.selectList(editProductCustomerFinder);
		assertFalse("Could find the customer select list", customer.exists());
		SelectList division = ie.selectList(editProductDivisionFinder);
		assertTrue("Could not find the division select list", division.exists());
		TextField location = ie.textField(editProductLocationFinder);
		assertTrue("Could not find the Location text field", location.exists());
		SelectList status = ie.selectList(editProductProductStatusFinder);
		assertFalse("Could find the Product Status select list", status.exists());
		TextField po = ie.textField(editProductPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Order text field", po.exists());
		TextField identified = ie.textField(editProductIdentifiedFinder);
		assertFalse("Could find the Identified text field", identified.exists());
		SelectList productType = ie.selectList(editProductProductTypeFinder);
		assertFalse("Could find the Product Type select list", productType.exists());
		TextField comments = ie.textField(editProductCommentsFinder);
		assertFalse("Could find the Comments text field", comments.exists());
		SelectList commentTemplate = ie.selectList(editProductCommentTemplatesFinder);
		assertFalse("Could find the Comment Templates select list", commentTemplate.exists());
		
		if(divisional) {
			Options o = division.options();
			assertTrue("There is more than one division to select", o.length() == 1);
		}
	}

	/**
	 * Validate all the methods in this library.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		gotoAssets();
		List<String> customers = getCustomersOnSearchCriteria();
		int n = misc.getRandomInteger(customers.size());
		List<String> divisions = getDivisionsOnSearchCriteria(customers.get(n));	// sets a random customer
		List<String> productStatuses = getProductStatusesOnSearchCriteria();
		List<String> productTypes = getProductTypesOnSearchCriteria();
		getDynamicSelectColumns();
		ProductSearchCriteria prop = new ProductSearchCriteria();
		String today = misc.getDateString();
		String lastMonth = misc.getDateStringLastMonth();
		prop.setFromDate(lastMonth);
		prop.setToDate(today);
		prop.setCustomer("");														// clears customer
		setProductSearchCriteria(prop);
		expandProductSearchSelectColumns();
		ProductSearchSelectColumns c = new ProductSearchSelectColumns();
		c.setAllOn();
		setProductSearchColumns(c);
		gotoProductSearchResults();
		List<String> filteredCustomers = getProductSearchResultsColumn("Customer Name");
		String customerName = filteredCustomers.get(0);
		prop.setCustomer(customerName);
		gotoProductSearchResults();
		if(getTotalNumberOfProducts() > 1000) {
			fail("You need to set up so there are less than 1000 assets.");
		}
		gotoMassUpdate();
		gotoProductSearchResultsFromMassUpdate();
		printAllManufacturerCertificates();
		exportToExcel();
		List<String> serialNumbers = getProductSearchResultsSerialNumbers();
		String sdc = getSelectDisplayColumnsHeader();

		gotoMassUpdate();
		MassUpdateForm m = new MassUpdateForm();
		m.setCustomerName(customerName);
		setMassUpdate(m);
		gotoSaveMassUpdate();

		expandProductSearchResultsSearchCriteria();
		gotoProductSearchClearForm();
		gotoProductSearchResults();
		printAllManufacturerCertificatesWarningOver1000Products();
		gotoMassUpdateWarningOver1000Products();
		exportToExcelWarningOver10000Products();
		
		String serialNumber = serialNumbers.get(0);
		gotoProductInformationViaInfoLink(serialNumber);
		gotoEditProduct(serialNumber);
		gotoInspectionsFor(serialNumber);
		gotoProductInformation(serialNumber);
		gotoInspectionSchedule(serialNumber);
		List<String> inspectionTypes = getInspectionTypesFromAssetAddSchedulePage();
		String scheduleDate = today;
		String inspectionType = inspectionTypes.get(0);
		String job = null;
		addScheduleFor(scheduleDate, inspectionType, job);
		String newJob = null;
		String newScheduleDate = misc.getDateStringNextMonth();
		editScheduleFor(scheduleDate, inspectionType, job, newScheduleDate, newJob);
		gotoProductInformation(serialNumber);
//		gotoInspectionGroups(serialNumber);	// Waiting for WEB-1034 to be fixed
		assertTrue(SmartSearch(serialNumber) > 0);
		
		// no master products on Hercules. These get exercised by the Smoke Test
//		this.gotoProductConfiguration(serialNumber);
//		this.addSubProductToMasterProduct(subProductType, subProductSerialNumber);
//		this.editEmployeeProduct(p);
//		this.checkEndUserEditProduct(divisional);
		fail("Not fully implemented");
	}

	private List<String> getInspectionTypesFromAssetAddSchedulePage() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList addScheduleInspectionType = ie.selectList(addScheduleInspectionTypeFinder);
		assertTrue("Could not find the select list for Inspection Type", addScheduleInspectionType.exists());
		Options os = addScheduleInspectionType.options();
		Iterator<Option> i = os.iterator();
		while(i.hasNext()) {
			Option o = i.next();
			results.add(o.text().trim());
		}
		return results;
	}
	
	/*
	 * TODO
	 * public void removeScheduleFor(String scheduleDate, String inspectionType, String job) throws Exception {
	 * public void inspectNowScheduleFor(String scheduleDate, String inspectionType, String job) throws Exception {
	 * public void connectProductToOrder(String orderNumber) {
	 * public void gotoManageInspections() throws Exception {
	 * public void viewLastInspection() throws Exception {
	 * public void printPDFReportFromViewLastInspection() throws Exception {
	 * public void printObservationReportFromViewLastInspection() throws Exception {
	 * public void closeViewLastInspection() throws Exception {
	 * public List<String> getSubProducts() throws Exception {
	 * public void gotoSubProduct(String label) throws Exception {
	 * public void gotoSubProduct(int index) throws Exception {
	 * public void deleteProduct() throws Exception { // goto to Product Information, then Edit Product, then delete
	 * 
	 */
}
