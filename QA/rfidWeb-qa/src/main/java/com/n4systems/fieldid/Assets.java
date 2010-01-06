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
import com.n4systems.fieldid.admin.ManageProductTypes;
import com.n4systems.fieldid.datatypes.MassUpdateForm;
import com.n4systems.fieldid.datatypes.Owner;
import com.n4systems.fieldid.datatypes.Product;
import com.n4systems.fieldid.datatypes.ProductSearchCriteria;
import com.n4systems.fieldid.datatypes.ProductSearchSelectColumns;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Assets extends TestCase {
	
	private static final int expectedTextFieldsOnEditAsset = 3;
	IE ie = null;
	Properties p;
	InputStream in;
	FieldIDMisc misc;
	Inspect inspect;
	Admin admin;
	ManageProductTypes mpts;
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
	private Finder customerInformationEditFormTextFieldFinder;
	private Finder editCustomerProductSaveButtonFinder;
	private Finder editEmployeeProductSaveButtonFinder;
	private Finder manufactureCertificateDownloadNowLinkFinder;
	private Finder assetNextScheduledDateCellFinder;
	private Finder viewLastInspectionFromAssetsViewFinder;
	private Finder viewLastInspectionCloseButtonFinder;
	private Finder printPDFReportFromViewLastInspectionFinder;
	private Finder printObservationReportFromViewLastInspectionFinder;
	private Finder deleteAssetLinkFinder;
	private Finder confirmDeleteAssetButtonFinder;
	private Finder inspectionsWillBeDeletedFinder;
	private Finder schedulesWillBeDeletedFinder;
	private Finder subproductsWillBeDetachedFinder;
	private Finder productsWillBeDetachedFromJobsFinder;
	private Finder subProductLinksFinder;
	private Finder subProductPartOfMasterProductLinkFinder;
	private Finder selectColumnSafetyNetworkFinder;
	private Finder assetViewSerialNumberFinder;
	private Finder assetViewRFIDNumberFinder;
	private Finder assetViewPublishedFinder;
	private Finder assetViewProductTypeFinder;
	private Finder assetViewProductStatusFinder;
	private Finder assetViewIdentifiedFinder;
	private Finder assetViewOrderNumberFinder;
	private Finder assetViewCustomerFinder;
	private Finder assetViewDivisionFinder;
	private Finder assetViewLocationFinder;
	private Finder assetViewReferenceNumberFinder;
	private Finder assetViewPurchaseOrderFinder;
	private Finder assetViewCommentFinder;
	
	public Assets(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			inspect = new Inspect(ie);
			admin = new Admin(ie);
			mpts = new ManageProductTypes(ie);
			assetViewSerialNumberFinder = xpath(p.getProperty("assetviewserialnumber", "NOT SET"));
			assetViewRFIDNumberFinder = xpath(p.getProperty("assetviewrfidnumber", "NOT SET"));
			assetViewPublishedFinder = xpath(p.getProperty("assetviewpublished", "NOT SET"));
			assetViewProductTypeFinder = xpath(p.getProperty("assetviewproducttype", "NOT SET"));
			assetViewProductStatusFinder = xpath(p.getProperty("assetviewproductstatus", "NOT SET"));
			assetViewIdentifiedFinder = xpath(p.getProperty("assetviewidentified", "NOT SET"));
			assetViewOrderNumberFinder = xpath(p.getProperty("assetviewordernumber", "NOT SET"));
			assetViewCustomerFinder = xpath(p.getProperty("assetviewcustomer", "NOT SET"));
			assetViewDivisionFinder = xpath(p.getProperty("assetviewdivision", "NOT SET"));
			assetViewLocationFinder = xpath(p.getProperty("assetviewlocation", "NOT SET"));
			assetViewReferenceNumberFinder = xpath(p.getProperty("assetviewreferencenumber", "NOT SET"));
			assetViewPurchaseOrderFinder = xpath(p.getProperty("assetviewpurchaseorder", "NOT SET"));
			assetViewCommentFinder = xpath(p.getProperty("assetviewcomments", "NOT SET"));
			selectColumnSafetyNetworkFinder = xpath(p.getProperty("selectcolumnsafetynetwork", "NOT SET"));
			subProductPartOfMasterProductLinkFinder = xpath(p.getProperty("subproductlinktomasterproduct", "NOT SET"));
			subProductLinksFinder = xpath(p.getProperty("subproductlinksonmasterproductview", "NOT SET"));
			schedulesWillBeDeletedFinder = xpath(p.getProperty("scheduleswillbedeleted", "NOT SET"));
			subproductsWillBeDetachedFinder = xpath(p.getProperty("subproductswillbedetached", "NOT SET"));
			productsWillBeDetachedFromJobsFinder = xpath(p.getProperty("productswillbedetachedfromjobs", "NOT SET"));
			inspectionsWillBeDeletedFinder = xpath(p.getProperty("inspectionswillbedeleted", "NOT SET"));
			confirmDeleteAssetButtonFinder = xpath(p.getProperty("confirmdeleteproductbutton", "NOT SET"));
			deleteAssetLinkFinder = xpath(p.getProperty("deleteproductlink", "NOT SET"));
			printObservationReportFromViewLastInspectionFinder = xpath(p.getProperty("viewlastinspectionprintobservationreport", "NOT SET"));
			printPDFReportFromViewLastInspectionFinder = xpath(p.getProperty("viewlastinspectionprintpdfreport", "NOT SET"));
			viewLastInspectionCloseButtonFinder = xpath(p.getProperty("viewlastinspectionclosebutton", "NOT SET"));
			viewLastInspectionFromAssetsViewFinder = xpath(p.getProperty("viewlastinspection", "NOT SET"));
			assetNextScheduledDateCellFinder = xpath(p.getProperty("nextscheduleddatecells", "NOT SET"));
			manufactureCertificateDownloadNowLinkFinder = xpath(p.getProperty("manufacturecertificatedownloadnowlink", "NOT SET"));
			assetsFinder = id(p.getProperty("link", "NOT SET"));
			assetsContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			productInformationContentHeaderFinder = xpath(p.getProperty("productinfocontentheader", "NOT SET"));
			viewAllInspectionsFinder = xpath(p.getProperty("viewallinspections", "NOT SET"));
			inspectionForFinder = xpath(p.getProperty("inspectionforcontentheader", "NOT SET"));
			productSearchRFIDNumberFinder = id(p.getProperty("productsearchrfidnumber", "NOT SET"));
			productSearchSerialNumberFinder = id(p.getProperty("productsearchserialnumber", "NOT SET"));
			productSearchOrderNumberFinder = id(p.getProperty("productsearchordernumber", "NOT SET"));
			productSearchPurchaseOrderFinder = id(p.getProperty("productsearchpurchaseorder", "NOT SET"));
			productSearchCustomerFinder = id(p.getProperty("productsearchcustomer", "NOT SET"));
			productSearchDivisionFinder = id(p.getProperty("productsearchdivision", "NOT SET"));
			productSearchJobSiteFinder = id(p.getProperty("productsearchjobsite", "NOT SET"));
			productSearchAssignedToFinder = id(p.getProperty("productsearchassignedto", "NOT SET"));
			productSearchLocationFinder = id(p.getProperty("productsearchlocation", "NOT SET"));
			productSearchSalesAgentFinder = id(p.getProperty("productsearchsalesagent", "NOT SET"));
			productSearchReferenceNumberFinder = id(p.getProperty("productsearchreferencenumber", "NOT SET"));
			productSearchProductStatusFinder = id(p.getProperty("productsearchproductstatus", "NOT SET"));
			productSearchProductTypeFinder = id(p.getProperty("productsearchproducttype", "NOT SET"));
			productSearchFromDateFinder = id(p.getProperty("productsearchfromdate", "NOT SET"));
			productSearchToDateFinder = id(p.getProperty("productsearchtodate", "NOT SET"));
			productSearchRunButtonFinder = id(p.getProperty("productsearchrunbutton", "NOT SET"));
			productSearchClearFormButtonFinder = value(p.getProperty("productsearchclearformbutton", "NOT SET"));
			productSearchExpandSearchCriteriaFinder = id(p.getProperty("productsearchexpandsearchcriteria", "NOT SET"));
			productSearchExpandSelectColumnFinder = id(p.getProperty("productsearchexpandselectcolumns", "NOT SET"));
			selectColumnSerialNumberFinder = id(p.getProperty("selectcolumnserialnumber", "NOT SET"));
			selectColumnRFIDNumberFinder = id(p.getProperty("selectcolumnrfidnumber", "NOT SET"));
			selectColumnCustomerNameFinder = id(p.getProperty("selectcolumncustomername", "NOT SET"));
			selectColumnDivisionFinder = id(p.getProperty("selectcolumndivision", "NOT SET"));
			selectColumnOrganizationFinder = id(p.getProperty("selectcolumnorganization", "NOT SET"));
			selectColumnReferenceNumberFinder = id(p.getProperty("selectcolumnreferencenumber", "NOT SET"));
			selectColumnProductTypeFinder = id(p.getProperty("selectcolumnproducttype", "NOT SET"));
			selectColumnProductStatusFinder = id(p.getProperty("selectcolumnproductstatus", "NOT SET"));
			selectColumnLastInspectionDateFinder = id(p.getProperty("selectcolumnlastinspectiondate", "NOT SET"));
			selectColumnDateIdentifiedFinder = id(p.getProperty("selectcolumndateidentified", "NOT SET"));
			selectColumnIdentifiedByFinder = id(p.getProperty("selectcolumnidentifiedby", "NOT SET"));
			selectColumnDescriptionFinder = id(p.getProperty("selectcolumndescription", "NOT SET"));
			selectColumnLocationFinder = id(p.getProperty("selectcolumnlocation", "NOT SET"));
			selectColumnOrderDescriptionFinder = id(p.getProperty("selectcolumnorderdescription", "NOT SET"));
			selectColumnOrderNumberFinder = id(p.getProperty("selectcolumnordernumber", "NOT SET"));
			selectColumnPurchaseOrderFinder = id(p.getProperty("selectcolumnpurchaseorder", "NOT SET"));
			selectColumnFormFinder = id(p.getProperty("selectcolumnform", "NOT SET"));
			searchCriteriaFormFinder = id(p.getProperty("searchcriteriaform", "NOT SET"));
			productSearchResultsContentHeaderFinder = xpath(p.getProperty("productsearchresultscontentheader", "NOT SET"));
			productAttributesSelectColumnsFinder = id(p.getProperty("selectcolumnproductattributes", "NOT SET"));
			searchHasNoResultsFinder = xpath(p.getProperty("searchhasnoresults", "NOT SET"));
			manageInspectionLinkFinder = id(p.getProperty("manageinspectionlink", "NOT SET"));
			inspectionGroupsContentHeaderFinder = xpath(p.getProperty("inspectiongroupscontentheader", "NOT SET"));
			totalProductsSpanFinder = xpath(p.getProperty("totalproductsspan", "NOT SET"));
			noResultsReturnedFinder = xpath(p.getProperty("totalproductsnoresultreturned", "NOT SET"));
			printAllManufacturerCertificatesFinder = xpath(p.getProperty("printallmanufacturercertificates", "NOT SET"));
			printAllManufacturerCertificatesWarningFinder = xpath(p.getProperty("printallmanufacturercertificateswarning", "NOT SET"));
			printAllManufacturerCertificatesWarningToolTipFinder = id(p.getProperty("printallmanufacturercertswarningtooltip", "NOT SET"));
			exportToExcelFinder = xpath(p.getProperty("exporttoexcel", "NOT SET"));
			exportToExcelWarningToolTipFinder = id(p.getProperty("exporttoexcelwarningtooltip", "NOT SET"));
			exportToExcelWarningFinder = xpath(p.getProperty("exporttoexcelwarning", "NOT SET"));
			massUpdateLinkFinder = xpath(p.getProperty("massupdate", "NOT SET"));
			massUpdateContentHeaderFinder = xpath(p.getProperty("massupdatecontentheader", "NOT SET"));
			massUpdateWarningFinder = xpath(p.getProperty("massupdatewarning", "NOT SET"));
			massUpdateWarningToolTipFinder = id(p.getProperty("massupdatewarningtooltip", "NOT SET"));
			massUpdateCustomerNameFinder = xpath(p.getProperty("massupdatecustomername", "NOT SET"));
			massUpdateDivisionFinder = xpath(p.getProperty("massupdatedivision", "NOT SET"));
			massUpdateCustomerDivisionSelectFinder = id(p.getProperty("massupdatecustomerdivisonselect", "NOT SET"));
			massUpdateJobSiteFinder = id(p.getProperty("massupdatejobsite", "NOT SET"));
			massUpdateJobSiteSelectFinder = id(p.getProperty("massupdatejobsiteselect", "NOT SET"));
			massUpdateAssignedToFinder = id(p.getProperty("massupdateassignedto", "NOT SET"));
			massUpdateAssignedToSelectFinder = id(p.getProperty("massupdateassignedtoselect", "NOT SET"));
			massUpdateProductStatusFinder = id(p.getProperty("massupdateproductstatus", "NOT SET"));
			massUpdateProductStatusSelectFinder = id(p.getProperty("massupdateproductstatusselect", "NOT SET"));
			massUpdatePurchaseOrderFinder = id(p.getProperty("massupdatepurchaseorder", "NOT SET"));
			massUpdatePurchaseOrderSelectFinder = id(p.getProperty("massupdatepurchaseorderselect", "NOT SET"));
			massUpdateLocationFinder = id(p.getProperty("massupdatelocation", "NOT SET"));
			massUpdateLocationSelectFinder = id(p.getProperty("massupdatelocationselect", "NOT SET"));
			massUpdateIdentifiedFinder = id(p.getProperty("massupdateidentified", "NOT SET"));
			massUpdateIdentifiedSelectFinder = id(p.getProperty("massupdateidentifiedselect", "NOT SET"));
			massUpdateSaveButtonFinder = id(p.getProperty("massupdatesavebutton", "NOT SET"));
			massUpdateReturnToSearchFinder = text(p.getProperty("massupdatereturntosearch", "NOT SET"));
			massUpdateInstructionsFinder = xpath(p.getProperty("massupdateinstructions", "NOT SET"));
			editProductLinkFinder = xpath(p.getProperty("editproductlink", "NOT SET"));
			editProductPageContentHeaderFinder = xpath(p.getProperty("editproductcontentheader", "NOT SET"));
			productConfigurationLinkFinder = xpath(p.getProperty("productconfigurationlink", "NOT SET"));
			productConfigurationPageContentHeaderFinder = xpath(p.getProperty("productconfigurationcontentheader", "NOT SET"));
			inspectionScheduleLinkFinder = xpath(p.getProperty("inspectionschedulelink", "NOT SET"));
			inspectionSchedulePageContentHeaderFinder = xpath(p.getProperty("inspectionschedulecontentheader", "NOT SET"));
			addScheduleDateFinder = id(p.getProperty("addscheduledate", "NOT SET"));
			addScheduleInspectionTypeFinder = id(p.getProperty("addscheduleinspectiontype", "NOT SET"));
			addScheduleJobFinder = id(p.getProperty("addschedulejob", "NOT SET"));
			addScheduleSaveButtonFinder = id(p.getProperty("addschedulesavebutton", "NOT SET"));
			scheduleForScheduleRowsFinder = xpath(p.getProperty("scheduleforrows", "NOT SET"));
			smartSearchTextFieldFinder = id(p.getProperty("smartsearchtextfield", "NOT SET"));
			smartSearchSearchButtonFinder = id(p.getProperty("smartsearchbutton", "NOT SET"));
			selectDisplayColumnHeaderFinder = xpath(p.getProperty("selectdisplaycolumnheader", "NOT SET"));
			addNewComponentFindExistingDivsFinder = xpath(p.getProperty("addnewcomponentfindexistingdivs", "NOT SET"));
			productLookupSmartSearchTextFieldFinder = xpath(p.getProperty("productlookupsmartsearchtextfield", "NOT SET"));
			productLookupSmartSearchLoadButtonFinder = xpath(p.getProperty("productlookupsmartsearchloadbutton", "NOT SET"));
			productLookupSmartSearchSelectButtonFinder = xpath(p.getProperty("productlookupsmartsearchselectbutton", "NOT SET"));
			editProductSerialNumberFinder = xpath(p.getProperty("editproductserialnumber", "NOT SET"));
			editProductRfidNumberFinder = xpath(p.getProperty("editproductrfidnumber", "NOT SET"));
			editProductReferenceNumberFinder = xpath(p.getProperty("editproductreferencenumber", "NOT SET"));
			editProductCustomerFinder = xpath(p.getProperty("editproductcustomer", "NOT SET"));
			editProductDivisionFinder = xpath(p.getProperty("editproductdivision", "NOT SET"));
			editProductLocationFinder = xpath(p.getProperty("editproductlocation", "NOT SET"));
			editProductProductStatusFinder = xpath(p.getProperty("editproductproductstatus", "NOT SET"));
			editProductPurchaseOrderFinder = xpath(p.getProperty("editproductpurchaseorder", "NOT SET"));
			editProductIdentifiedFinder = xpath(p.getProperty("editproductidentified", "NOT SET"));
			editProductProductTypeFinder = xpath(p.getProperty("editproductproducttype", "NOT SET"));
			editProductCommentsFinder = xpath(p.getProperty("editproductcomments", "NOT SET"));
			editProductCommentTemplatesFinder = xpath(p.getProperty("editproductcommenttemplates", "NOT SET"));
			editProductGenerateLinkFinder = xpath(p.getProperty("editproductgeneratelink", "NOT SET"));
			customerInformationEditFormTextFieldFinder = xpath(p.getProperty("editproductformtextfields", "NOT SET"));
			editCustomerProductSaveButtonFinder = xpath(p.getProperty("editcustomerproductsavebutton", "NOT SET"));
			editEmployeeProductSaveButtonFinder = xpath(p.getProperty("editemployeeproductsavebutton", "NOT SET"));
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
		assertTrue("Could not find Assets page content header '" + p.getProperty("contentheader", "NOT SET") + "'", assetsContentHeader.exists());
	}
	
	/**
	 * Checks the header tag at the top of the body to make sure we are on the correct page.
	 * If you don't know serial number passing in "Asset" will work.
	 * 
	 * @param serialNumber
	 * @throws Exception
	 */
	public void checkProductInformationPageContentHeader(String serialNumber) throws Exception {
		HtmlElement productInformationContentHeader = ie.htmlElement(productInformationContentHeaderFinder);
		assertTrue("Could not find Product Information page content header", productInformationContentHeader.exists());
		assertTrue("Could not find serial number '" + serialNumber + "'", productInformationContentHeader.text().contains(serialNumber));
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

		Checkbox safetyNetwork = ie.checkbox(selectColumnSafetyNetworkFinder);
		assertTrue("Could not find the checkbox for the safety network", safetyNetwork.exists());
		safetyNetwork.set(c.getSafetyNetwork());

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
		
		misc.gotoChooseOwner();
		List<String> orgs = misc.getOrganizations();
		Owner owner = new Owner(orgs.get(0));

		if(p.getCustomer() != null) {
			owner.setCustomer(p.getCustomer());
		}

		if(p.getDivision() != null) {
			owner.setDivision(p.getDivision());
		}
		
		misc.setOwner(owner);
		misc.selectOwner();

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
		assertTrue("Could not find Inspection For page content header '" + p.getProperty("inspectionforcontentheader", "NOT SET") + "'", inspectionForContentHeader.exists());
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
		misc.gotoChooseOwner();
		SelectList customer = misc.getCustomerSelectListFromChooseOwner();
		Options customers = customer.options();
		Iterator<Option> i = customers.iterator();
		// If there are many options on the select list, we don't want
		// to refresh the page while processing the list of options.
		FieldIDMisc.stopMonitor();
		while(i.hasNext()) {
			Option o = i.next();
			String c = o.text();
			if(!c.equals("")) {
				results.add(o.text());
			}
		}
		misc.cancelOwner();
		// Turn the refresh monitor back on
		FieldIDMisc.startMonitor();
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
		FieldIDMisc.stopMonitor();
		while(i.hasNext()) {
			Option o = i.next();
			String d = o.text();
			if(!d.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		FieldIDMisc.startMonitor();
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
		FieldIDMisc.stopMonitor();
		while(i.hasNext()) {
			Option o = i.next();
			String ps = o.text();
			if(!ps.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		FieldIDMisc.startMonitor();
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
		FieldIDMisc.stopMonitor();
		while(i.hasNext()) {
			Option o = i.next();
			String pt = o.text();
			if(!pt.equals("")) {
				results.add(o.text());
			}
		}
		// Turn the refresh monitor back on
		FieldIDMisc.startMonitor();
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
	public List<String> getProductSearchResultsSerialNumbers(String s) throws Exception {
		List<String> results = new ArrayList<String>();
		Link next;
		boolean more;
		do {
			next = ie.link(text("Next>"));
			more = next.exists(); 
			results.addAll(getProductSearchResultsCurrentPage(s));
			if(more) {
				next.click();
			}
		} while(more);
		
		return results;
	}

	private Collection<String> getProductSearchResultsCurrentPage(String s) throws Exception {
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
			if(th.text().trim().equals(s)) {
				serialNumberColumn = index;
				break;
			}
		}
		assertFalse("Could not find the column for " + s + ".", serialNumberColumn == -1);
		Links serialNumbers = ie.links(xpath("//TABLE[@id='resultsTable']/TBODY/TR/TD[" + (serialNumberColumn+1) + "]/A"));
		assertNotNull("Could not find any " + s + " links.", serialNumbers);
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
		FieldIDMisc.stopMonitor();
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
		FieldIDMisc.startMonitor();
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
		misc.waitForJavascript();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void editScheduleFor(String scheduleDate, String inspectionType, String job, String newScheduleDate, String newJob) throws Exception {
		assertNotNull(scheduleDate);
		assertNotNull(inspectionType);
		assertNotNull(newScheduleDate);
		FieldIDMisc.stopMonitor();
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
		FieldIDMisc.startMonitor();
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
			next = ie.link(text("Next >"));
			more = next.exists(); 
			results.addAll(getProductSearchResultsColumnCurrentPage(s));
			if(more) {
				next.click();
			}
		} while(more);
		
		return results;
	}
	
	private TableCells helperGetProductSearchResultsColumnHeaders() throws Exception {
		TableCells ths = ie.cells(xpath("//TABLE[@id='resultsTable']/TBODY/TR[1]/TH"));
		assertNotNull(ths);
		return ths;
	}

	private Collection<String> getProductSearchResultsColumnCurrentPage(String s) throws Exception {
		List<String> results = new ArrayList<String>();
		TableCells ths = helperGetProductSearchResultsColumnHeaders();
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
			if(!v.equals("")) {			// don't add blank strings
				results.add(v);
			}
		}
		return results;
	}
	
	public void editCustomerProduct(Product p) throws Exception {
		TextField reference = ie.textField(editProductReferenceNumberFinder);
		assertTrue("Could not find the Reference number text field", reference.exists());
		SelectList division = ie.selectList(editProductDivisionFinder);
		assertTrue("Could not find the division select list", division.exists());
		TextField location = ie.textField(editProductLocationFinder);
		assertTrue("Could not find the Location text field", location.exists());
		TextField po = ie.textField(editProductPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Order text field", po.exists());

		if(p.getDivision() != null) {
			Option o = division.option(text(p.getDivision()));
			assertTrue("Could not find the division '" + p.getDivision() + "'", o.exists());
			o.select();
		}
		
		if(p.getLocation() != null) {
			location.set(p.getLocation());
		}
		
		if(p.getReferenceNumber() != null) {
			reference.set(p.getReferenceNumber());
		}
		
		if(p.getPurchaseOrder() != null) {
			po.set(p.getPurchaseOrder());
		}
	}
	
	public void saveCustomerProduct(String serialNumber) throws Exception {
		Button save = ie.button(editCustomerProductSaveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		checkProductInformationPageContentHeader(serialNumber);
	}
	
	public void saveEmployeeProduct(String serialNumber) throws Exception {
		Button save = ie.button(editEmployeeProductSaveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		checkProductInformationPageContentHeader(serialNumber);
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
		
		TextFields tfcount = ie.textFields(customerInformationEditFormTextFieldFinder);
		assertNotNull(tfcount);
		assertTrue("There are " + tfcount.length() + " text fields but I was expecting " + expectedTextFieldsOnEditAsset + ".", tfcount.length() == expectedTextFieldsOnEditAsset);
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

	public boolean isMassUpdate() throws Exception {
		Link massUpdate = ie.link(massUpdateLinkFinder);
		return massUpdate.exists();
	}

	public boolean isSchedulesAvailable() throws Exception {
		boolean result = false;
		Link inspectionSchedule = ie.link(inspectionScheduleLinkFinder);
		result = inspectionSchedule.exists();
		return result;
	}

	public void downloadManufactureCertificate() throws Exception {
		Link l = ie.link(manufactureCertificateDownloadNowLinkFinder);
		assertTrue("Cannot find the link to download a manufacture certificate", l.exists());
		l.click();
		IE certBrowser = ie.childBrowser();
		helperNewWindowPDFCheck(certBrowser);
	}
	
	private void helperNewWindowPDFCheck(IE child) throws Exception {
		try {
			String html = child.html();
			// html() called on application/pdf should lock up
			// this means it never makes it to this point
			// if we get to here, something went wrong.
			fail("Failed to open PDF in a new window");
			html.compareTo(html);
		} catch(Exception e) {
			// if the certificate printed okay, Watij will lock up
			// and we will end up here. So getting to here means a
			// pass and we do nothing.
		} finally {
			child.close();
		}
	}
	
	public void removeScheduleFor(String scheduleDate, String inspectionType, String job) throws Exception {
		if(job == null) {
			job = "no job";
		}
		TableCell td = findScheduleForCell(scheduleDate, inspectionType, job);
		Link remove = td.link(xpath("SPAN[3]/A[contains(text(),'Remove')]"));
		assertTrue("Could not find the Remove link", remove.exists());
		remove.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	private TableCell findScheduleForCell(String scheduleDate, String inspectionType, String job) throws Exception {
		TableCell result = null;
		TableCells tds = ie.cells(assetNextScheduledDateCellFinder);
		assertNotNull(tds);
		Iterator<TableCell> i = tds.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			String type = td.cell(xpath("../TD[1]")).text().trim();
			String date = td.span(0).text().trim();
			String j = td.span(1).text().trim();
			if(date.equals(scheduleDate) && type.equals(inspectionType) && j.equals(job)) {
				result = td;
			}
		}
		assertNotNull("Could not find a row with inspection type: '" + inspectionType + "' job: '" + job + "' and date: '" + scheduleDate + "'", result);
		return result;
	}

	public void inspectNowScheduleFor(String scheduleDate, String inspectionType, String job) throws Exception {
		if(job == null) {
			job = "no job";
		}
		TableCell td = findScheduleForCell(scheduleDate, inspectionType, job);
		Link inspectNow = td.link(xpath("SPAN[3]/A[contains(text(),'inspect now')]"));
		assertTrue("Could not find the inspect now link", inspectNow.exists());
		inspectNow.click();
		inspect.checkInspectionPageContentHeader(inspectionType);
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void viewLastInspection() throws Exception {
		Link view = ie.link(viewLastInspectionFromAssetsViewFinder);
		assertTrue("Could not find the link to viwe the last inspection", view.exists());
		view.click();
		misc.waitForLightBox();
	}
	
	public void closeViewLastInspection(String serialNumber) throws Exception {
		Div close = ie.div(viewLastInspectionCloseButtonFinder);
		assertTrue("Could not find the Close button for the view last inspection light box", close.exists());
		close.click();
		checkProductInformationPageContentHeader(serialNumber);
	}

	public void gotoManageInspections(String serialNumber) throws Exception {
		gotoInspectionGroups(serialNumber);
	}

	public void printPDFReportFromViewLastInspection() throws Exception {
		Frame lightbox = ie.frame(id("lightviewContent"));
		Link l = lightbox.link(printPDFReportFromViewLastInspectionFinder);
		assertNotNull("Could not find the 'PDF Report' from the view last inspection lightbox", l);
		misc.waitForJavascript();
		l.click();
		IE report = ie.childBrowser();
		helperNewWindowPDFCheck(report);
	}

	public void printObservationReportFromViewLastInspection() throws Exception {
		Frame lightbox = ie.frame(id("lightviewContent"));
		Link l = lightbox.link(printObservationReportFromViewLastInspectionFinder);
		assertNotNull("Could not find the 'Observation Report' from the view last inspection lightbox", l);
		l.click();
		IE report = ie.childBrowser();
		helperNewWindowPDFCheck(report);
	}

	/**
	 * Assumes you are already on Edit Product. Easiest way to use this is
	 * go to product via smart search, go to edit product, call this method.
	 * 
	 * To confirm the results have a look at the PNG generated.
	 * 
	 * If the second to last parameters are not null, it will also check to
	 * see of the Removal Detail page numbers match the given numbers.
	 * 
	 * @param serialNumber - serial number of the product being deleted
	 * @param inspectionsDeleted - number of inspections which will get deleted with the product
	 * @param schedulesDeleted - number of scheduled inspections which will get deleted with the product
	 * @param subProductsDetached - number of subproducts attach to this product
	 * @param productsDetached - number of products detached from Jobs.
	 * @throws Exception
	 */
	public void deleteProduct(String serialNumber, String inspectionsDeleted, String schedulesDeleted, String subProductsDetached, String productsDetached) throws Exception {
		Link delete = ie.link(deleteAssetLinkFinder);
		assertTrue("Could not find the Delete link on Edit asset", delete.exists());
		delete.click();
		checkProductInformationPageContentHeader(serialNumber);
		String filename = "deleteProduct-Removal-Details-" + serialNumber + ".png";
		misc.myWindowCapture(filename);
		String inspDeleted = getInspectionsDeleted();
		String scheDeleted = getInspectionSchedulesDeleted();
		String subDetached = getSubProductsDetached();
		String jobDetached = getProductsDetachedFromJobs();
		if(inspectionsDeleted != null) {
			assertEquals(inspectionsDeleted, inspDeleted);
		}
		if(schedulesDeleted != null) {
			assertEquals(schedulesDeleted, scheDeleted);
		}
		if(subProductsDetached != null) {
			assertEquals(subProductsDetached, subDetached);
		}
		if(productsDetached != null) {
			assertEquals(productsDetached, jobDetached);
		}
	}
	
	private String getProductsDetachedFromJobs() throws Exception {
		Label n = ie.label(productsWillBeDetachedFromJobsFinder);
		if(n.exists()) {
			return n.text().trim();
		}
		return null;
	}

	private String getSubProductsDetached() throws Exception {
		Label n = ie.label(subproductsWillBeDetachedFinder);
		assertTrue("Could not find the number of sub products which will be detached", n.exists());
		return n.text().trim();
	}

	private String getInspectionSchedulesDeleted() throws Exception {
		Label n = ie.label(schedulesWillBeDeletedFinder);
		assertTrue("Could not find the number of inspection schedules which will be deleted", n.exists());
		return n.text().trim();
	}

	private String getInspectionsDeleted() throws Exception {
		Label n = ie.label(inspectionsWillBeDeletedFinder);
		assertTrue("Could not find the number of inspections which will be deleted", n.exists());
		return n.text().trim();
	}

	public void confirmDeleteProduct() throws Exception {
		Identify identify = new Identify(ie);
		Button delete = ie.button(confirmDeleteAssetButtonFinder);
		assertTrue("Could not find the Delete button on Removal Details page", delete.exists());
		delete.click();
		misc.checkForErrorMessagesOnCurrentPage();
		identify.checkIdentifyProductPageContentHeader();
	}
	
	private Links helperGetSubProductLinks() throws Exception {
		Links subProducts = ie.links(subProductLinksFinder);
		assertNotNull(subProducts);
		return subProducts;
	}

	public List<String> getSubProducts() throws Exception {
		List<String> results = new ArrayList<String>();
		Links subProducts = helperGetSubProductLinks();
		Iterator<Link> i = subProducts.iterator();
		while(i.hasNext()) {
			Link l = i.next();
			String label = l.text().trim();
			results.add(label);
		}
		return results;
	}

	public void gotoSubProduct(String label) throws Exception {
		assertNotNull(label);
		assertFalse("Label cannot be blank", label.equals(""));
		Link l = ie.link(text(label));
		assertTrue("Could not find a subproduct with label '" + label + "'", l.exists());
		l.click();
		checkProductInformationPageContentHeader("Asset");	// If you don't know serial number passing in "Asset" will work.
	}

	public void gotoMasterProductUsingPartOf() throws Exception {
		Link l = ie.link(subProductPartOfMasterProductLinkFinder);
		assertTrue("Could not find a link to a master product", l.exists());
		l.click();
		checkProductInformationPageContentHeader("Asset");	// If you don't know serial number passing in "Asset" will work.
	}

	public void gotoSubProduct(int index) throws Exception {
		Links ls = helperGetSubProductLinks();
		Link l = ls.get(index);
		l.click();
		checkProductInformationPageContentHeader("Asset");	// If you don't know serial number passing in "Asset" will work.
	}
	
	public void connectProductToOrder(String orderNumber) throws Exception {
		// TODO
	}

	List<Link> getSortableColumnsFromSearchResults() throws Exception {
		List<Link> results = new ArrayList<Link>();
		TableCells ths = helperGetProductSearchResultsColumnHeaders();
		Iterator<TableCell> i = ths.iterator();
		while(i.hasNext()) {
			TableCell th = i.next();
			Link l = th.link(0);
			if(l.exists()) {
				results.add(l);
			}
		}
		return results;
	}

	/**
	 * Validate all the methods in this library. This method assumes:
	 * 
	 * - That 'column' is set to the header for the Serial Number column.
	 * - The first customer on the Customer drop down has divisions.
	 * 
	 * Typically, I run this against cglift with column set to "Reel/ID".
	 * 
	 * @param column - this is the header for the column with serial numbers, i.e. "Serial Number" or "Reel/ID"
	 * @param customer - this is the name of a customer who has order numbers associated with assets
	 * @throws Exception
	 */
	public void validate(String column, Owner owner) throws Exception {
		admin.gotoAdministration();
		mpts.gotoManageProductTypes();
		List<String> mpt = mpts.getMasterProductTypeNames();
		gotoAssets();
		List<String> customers = getCustomersOnSearchCriteria();
		@SuppressWarnings("unused")
		int n = misc.getRandomInteger(customers.size());
		@SuppressWarnings("unused")
		List<String> productStatuses = getProductStatusesOnSearchCriteria();
		@SuppressWarnings("unused")
		List<String> productTypes = getProductTypesOnSearchCriteria();
		getDynamicSelectColumns();
		ProductSearchCriteria prop = new ProductSearchCriteria();
		String today = misc.getDateString();
		String lastMonth = misc.getDateStringBackNMonths(3);
		prop.setFromDate(lastMonth);
		prop.setToDate(today);
		Owner tmp = new Owner(owner.getOrganization());
		prop.setOwner(tmp);			// clears customer & division
		setProductSearchCriteria(prop);
		expandProductSearchSelectColumns();
		ProductSearchSelectColumns c = new ProductSearchSelectColumns();
		c.setAllOn();
		setProductSearchColumns(c);
		gotoProductSearchResults();
		List<String> filteredCustomers = getProductSearchResultsColumn("Customer Name");
		String customerName = filteredCustomers.get(0);
		List<String> divisions = getDivisionsOnSearchCriteria(customerName);
		prop.setCustomer(customerName);
		prop.setDivision(divisions.get(0));
		gotoProductSearchResults();
		if(getTotalNumberOfProducts() > 1000) {
			fail("You need to set up so there are less than 1000 assets.");
		}
		gotoMassUpdate();
		gotoProductSearchResultsFromMassUpdate();
		printAllManufacturerCertificates();
		exportToExcel();
		List<String> serialNumbers = getProductSearchResultsSerialNumbers(column);
		@SuppressWarnings("unused")
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
		if(inspectionTypes.size() == 0) {
			fail("Serial Number: " + serialNumber + " has no inspection types. Did not test add/edit schedules");
		}
		String inspectionType = inspectionTypes.get(0);
		String job = null;
		addScheduleFor(scheduleDate, inspectionType, job);
		String newJob = null;
		String newScheduleDate = misc.getDateStringNextMonth();
		editScheduleFor(scheduleDate, inspectionType, job, newScheduleDate, newJob);
		gotoProductInformation(serialNumber);
		gotoInspectionSchedule(serialNumber);
		removeScheduleFor(newScheduleDate, inspectionType, newJob);
		addScheduleFor(scheduleDate, inspectionType, job);
		inspectNowScheduleFor(scheduleDate, inspectionType, job);
		if(inspect.isMasterInspection()) {
			inspect.gotoStartMasterInspection(inspectionType);
			inspect.gotoStoreMasterInspection();
			inspect.gotoSaveMasterInspection(serialNumber);
		} else {
			inspect.gotoSaveStandardInspection(serialNumber);			// guarantees there is a last inspection
		}
		gotoProductInformation(serialNumber);
		viewLastInspection();
		printPDFReportFromViewLastInspection();
		viewLastInspection();											// timeout causes lightbox to close
		printObservationReportFromViewLastInspection();
		closeViewLastInspection(serialNumber);
		gotoInspectionGroups(serialNumber);
		assertTrue(SmartSearch(serialNumber) > 0);
		
		
		// product is gone at this point
		gotoEditProduct(serialNumber);
		deleteProduct(serialNumber, null, null, null, null);
		confirmDeleteProduct();
		
		if(mpt.size() > 0) {
			gotoAssets();
			prop = new ProductSearchCriteria();
			prop.setProductType(mpt.get(0));
			setProductSearchCriteria(prop);
			gotoProductSearchResults();
			serialNumbers = getProductSearchResultsSerialNumbers(column);
			serialNumber = serialNumbers.get(0);
			gotoProductInformationViaInfoLink(serialNumber);
			List<String> subs = getSubProducts();
			gotoSubProduct(subs.get(0));					// goto first sub-product
			gotoMasterProductUsingPartOf();					// return to master product
			int index = subs.size()-1;
			gotoSubProduct(index);							// goto last sub-product
		}
		
		gotoAssets();
		c = new ProductSearchSelectColumns();
		c.setOrderNumber(true);
		setProductSearchColumns(c);
		prop = new ProductSearchCriteria();
		prop.setOwner(owner);
		setProductSearchCriteria(prop);
		gotoProductSearchResults();
		
		List<String> orderNumbers = getProductSearchResultsColumn("Order Number");
		orderNumbers.clear();
		// sort the columns once, we'll be on the last page and order numbers will be on page 1
		// the getProductSearchResultsSerialNumbers will return the last page only if we are on last page
		serialNumbers = getProductSearchResultsSerialNumbers(column);
		// find a product not connected to an order, serialNumbers.get(last)
		// go to the product
		// connect to order
		
		// These get exercised by the Smoke Test
//		this.gotoProductConfiguration(serialNumber);
//		this.addSubProductToMasterProduct(subProductType, subProductSerialNumber);
//		this.editEmployeeProduct(p);
//		this.checkEndUserEditProduct(divisional);
	}

	/**
	 * Compares what is on the web to the Product object. Assumes you
	 * used something like home.gotoProductInformationViaSmartSearch(p.getSerialNumber());
	 * to get to the View Asset page first.
	 * 
	 * @param p
	 */
	public void validateAsset(Product p) throws Exception {
		Product tmp = getProduct();
		assertEquals("Serial numbers don't match.", p.getSerialNumber(), tmp.getSerialNumber());
		assertEquals("Identified dates don't match.", p.getIdentified(), tmp.getIdentified());
		if(p.getAssignedTo() != null) {
			assertEquals("Assigned To fields don't match.", p.getAssignedTo(), tmp.getAssignedTo());
		}
		if(p.getComments() != null) {
			assertEquals("Comments fields don't match.", p.getComments(), tmp.getComments());
		}
		if(p.getCustomer() != null) {
			assertEquals("Customer fields don't match.", p.getCustomer(), tmp.getCustomer());
		}
		if(p.getDivision() != null) {
			assertEquals("Division fields don't match.", p.getDivision(), tmp.getDivision());
		}
		if(p.getJobSite() != null) {
			assertEquals("Job Site fields don't match.", p.getJobSite(), tmp.getJobSite());
		}
		if(p.getLocation() != null) {
			assertEquals("Location fields don't match.", p.getLocation(), tmp.getLocation());
		}
		if(p.getOrderNumber() != null) {
			assertEquals("Order Number fields don't match.", p.getOrderNumber(), tmp.getOrderNumber());
		}
		if(p.getProductStatus() != null) {
			assertEquals("Product Status fields don't match.", p.getProductStatus(), tmp.getProductStatus());
		}
		if(p.getProductType() != null) {
			assertEquals("Product Type fields don't match.", p.getProductType(), tmp.getProductType());
		}
		assertEquals("Published fields don't match.", p.getPublished(), tmp.getPublished());
		if(p.getPurchaseOrder() != null) {
			assertEquals("Purchase Order fields don't match.", p.getPurchaseOrder(), tmp.getPurchaseOrder());
		}
		if(p.getReferenceNumber() != null) {
			assertEquals("Reference Number fields don't match.", p.getReferenceNumber(), tmp.getReferenceNumber());
		}
		if(p.getRFIDNumber() != null) {
			assertEquals("RFID Number fields don't match.", p.getRFIDNumber(), tmp.getRFIDNumber());
		}
	}

	/**
	 * Creates a Product object from the information on the View Asset page.
	 * 
	 * @return
	 */
	public Product getProduct() throws Exception {
		Product p = new Product(null);
		String s;

		Span serialNumber = ie.span(assetViewSerialNumberFinder);
		assertTrue("Could not find the serial number on the Asset View page", serialNumber.exists());
		s = serialNumber.text().trim();
		p.setSerialNumber(s);
		
		Span rfidNumber = ie.span(assetViewRFIDNumberFinder);
		assertTrue("Could not find the rfid number on the Asset View page", rfidNumber.exists());
		s = rfidNumber.text().trim();
		if(s.equals(""))	s = null;
		p.setRFIDNumber(s);
		
		Span published = ie.span(assetViewPublishedFinder);
		assertTrue("Could not find the Published Over Safety Network on the Asset View page", published.exists());
		s = published.text().trim();
		p.setPublished(s);
		
		Span productType = ie.span(assetViewProductTypeFinder);
		assertTrue("Could not find the Product Type on the Asset View page", productType.exists());
		s = productType.text().trim();
		p.setProductType(s);
		
		Span productStatus = ie.span(assetViewProductStatusFinder);
		assertTrue("Could not find the Product Type on the Asset View page", productStatus.exists());
		s = productStatus.text().trim();
		if(s.equals(""))	s = null;
		p.setProductStatus(s);
		
		Span identified = ie.span(assetViewIdentifiedFinder);
		assertTrue("Could not find the identified on the Asset View page", identified.exists());
		s = identified.text().trim();
		p.setIdentified(s);
		
		Span orderNumber = ie.span(assetViewOrderNumberFinder);
		assertTrue("Could not find the Order Number on the Asset View page", orderNumber.exists());
		s = orderNumber.text().trim();
		if(s.equals("Connect To Order") || s.equals(""))	s = null;
		p.setOrderNumber(s);
		
		Span customer = ie.span(assetViewCustomerFinder);
		assertTrue("Could not find the customer on the Asset View page", customer.exists());
		s = customer.text().trim();
		p.setCustomer(s);
		
		Span division = ie.span(assetViewDivisionFinder);
		assertTrue("Could not find the division on the Asset View page", division.exists());
		s = division.text().trim();
		if(s.equals(""))	s = null;
		p.setDivision(s);
		
		Span location = ie.span(assetViewLocationFinder);
		assertTrue("Could not find the location on the Asset View page", location.exists());
		s = location.text().trim();
		if(s.equals(""))	s = null;
		p.setLocation(s);
		
		Span referenceNumber = ie.span(assetViewReferenceNumberFinder);
		assertTrue("Could not find the Reference Number on the Asset View page", referenceNumber.exists());
		s = referenceNumber.text().trim();
		if(s.equals(""))	s = null;
		p.setReferenceNumber(s);
		
		Span purchaseOrder = ie.span(assetViewPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Order on the Asset View page", purchaseOrder.exists());
		s = purchaseOrder.text().trim();
		if(s.equals(""))	s = null;
		p.setPurchaseOrder(s);

		// Comments don't always exist.
		HtmlElement comment = ie.htmlElement(assetViewCommentFinder);
		if(comment.exists()) {
			s = comment.text().trim();
		} else {
			s = "";
		}
		if(s.equals(""))	s = null;
		p.setComments(s);
		
		return p;
	}
}
