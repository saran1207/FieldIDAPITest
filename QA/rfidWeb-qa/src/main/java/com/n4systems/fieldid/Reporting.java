package com.n4systems.fieldid;

import static watij.finders.FinderFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.Inspection;
import com.n4systems.fieldid.datatypes.ReportSearchSelectColumns;
import com.n4systems.fieldid.datatypes.ReportingSearchCriteria;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Reporting extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	Assets assets;
	FieldIDMisc misc;
	String propertyFile = "reporting.properties";
	Finder reportingFinder;
	Finder reportingContentHeaderFinder;
	Finder reportingSelectColumnsInspectionDateFinder;
	Finder reportingSelectColumnsInspectionTypeFinder;
	Finder reportingSelectColumnsCustomerNameFinder;
	Finder reportingSelectColumnsResultFinder;
	Finder reportingSelectColumnsInspectionBookFinder;
	Finder reportingSelectColumnsInspectorFinder;
	Finder reportingSelectColumnsDivisionFinder;
	Finder reportingSelectColumnsOrganizationFinder;
	Finder reportingSelectColumnsChargeFinder;
	Finder reportingSelectColumnsCommentsFinder;
	Finder reportingSelectColumnsLocationFinder;
	Finder reportingSelectColumnsPeakLoadFinder;
	Finder reportingSelectColumnsTestDurationFinder;
	Finder reportingSelectColumnsPeakLoadDurationFinder;
	Finder reportingSelectColumnsSerialNumberFinder;
	Finder reportingSelectColumnsRFIDNumberFinder;
	Finder reportingSelectColumnsReferenceNumberFinder;
	Finder reportingSelectColumnsProductTypeFinder;
	Finder reportingSelectColumnsProductStatusFinder;
	Finder reportingSelectColumnsDateIdentifiedFinder;
	Finder reportingSelectColumnsIdentifiedByFinder;
	Finder reportingSelectColumnsDescriptionFinder;
	Finder reportingSelectColumnsPartNumberFinder;
	Finder reportingSelectColumnsOrderDescriptionFinder;
	Finder reportingSelectColumnsOrderNumberFinder;
	Finder reportingSelectColumnsPurchaseOrderFinder;
	Finder reportingSearchCriteriaRFIDNumberFinder;
	Finder reportingSearchCriteriaSerialNumberFinder;
	Finder reportingSearchCriteriaEventTypeGroupFinder;
	Finder reportingSearchCriteriaInspectorFinder;
	Finder reportingSearchCriteriaOrderNumberFinder;
	Finder reportingSearchCriteriaPurchaseOrderFinder;
	Finder reportingSearchCriteriaJobSiteFinder;
	Finder reportingSearchCriteriaAssignedToFinder;
	Finder reportingSearchCriteriaCustomerFinder;
	Finder reportingSearchCriteriaDivisionFinder;
	Finder reportingSearchCriteriaInspectionBookFinder;
	Finder reportingSearchCriteriaReferenceNumberFinder;
	Finder reportingSearchCriteriaProductTypeFinder;
	Finder reportingSearchCriteriaProductStatusFinder;
	Finder reportingSearchCriteriaFromDateFinder;
	Finder reportingSearchCriteriaToDateFinder;
	Finder reportingSearchRunButtonFinder;
	Finder reportingNoResultsReturnedFinder;
	Finder totalInspectionsSpanFinder;
	Finder exportToExcelFinder;
	Finder exportToExcelWarningToolTipFinder;
	Finder exportToExcelWarningFinder;
	Finder reportingSearchResultsContentHeaderFinder;
	Finder reportResultsTableRowFinder;
	Finder reportResultsTableHeaderFinder;
	Finder printThisReportFinder;
	Finder printAllPDFReportsFinder;
	Finder printAllObservationReportsFinder;
	Finder printWarningFinder;
	Finder printWarningToolTipFinder;
	Finder savedReportsLinkFinder;
	Finder reportResultsSaveReportFinder;
	Finder reportResultsSaveReportContentHeaderFinder;
	Finder saveReportNameFinder;
	Finder reportResultsSaveReportSaveButtonFinder;
	Finder reportResultsUpdateReportFinder;
	Finder reportResultsUpdateReportContentHeaderFinder;
	Finder reportResultsSaveReportAsFinder;
	Finder reportingSearchResultsForContentHeaderFinder;
	Finder runSavedReportLinksFinder;
	Finder reportResultsStartNewReportFinder;
	Finder reportResultsUpdateReportCancelButtonFinder;
	private Finder reportingMassUpdateLinkFinder;
	private Finder reportingMassUpdateContentHeaderFinder;
	private Finder reportingMassUpdateCustomerNameFinder;
	private Finder reportingMassUpdateDivisionFinder;
	private Finder reportingMassUpdateInspectionBookFinder;
	private Finder reportingMassUpdateLocationFinder;
	private Finder reportingMassUpdatePrintableFinder;
	private Finder reportingMassUpdateSaveButtonFinder;
	private Finder massUpdateCustomerFinder;
	private Finder massUpdateDivisionFinder;
	private Finder massUpdateInspectionBookFinder;
	private Finder massUpdateLocationFinder;
	private Finder massUpdatePrintableFinder;
	private Finder summaryReportPageHeaderFinder;
	private Finder reportingSummaryReportLinkFinder;
	private Finder summaryReportProductTypeExpandLinkFinder;
	private Finder summaryReportEventTypeGroupExpandLinkFinder;
	
	public Reporting(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			assets = new Assets(ie);
			misc = new FieldIDMisc(ie);
			reportingFinder = id(p.getProperty("link"));
			reportingContentHeaderFinder = xpath(p.getProperty("contentheader"));
			reportingSelectColumnsInspectionDateFinder = id(p.getProperty("reportingselectcolumninspectiondate"));
			reportingSelectColumnsInspectionTypeFinder = id(p.getProperty("reportingselectcolumninspectiontype"));
			reportingSelectColumnsCustomerNameFinder = id(p.getProperty("reportingselectcolumncustomername"));
			reportingSelectColumnsResultFinder = id(p.getProperty("reportingselectcolumnresult"));
			reportingSelectColumnsInspectionBookFinder = id(p.getProperty("reportingselectcolumninspectionbook"));
			reportingSelectColumnsInspectorFinder = id(p.getProperty("reportingselectcolumninspector"));
			reportingSelectColumnsDivisionFinder = id(p.getProperty("reportingselectcolumndivision"));
			reportingSelectColumnsOrganizationFinder = id(p.getProperty("reportingselectcolumnorganization"));
			reportingSelectColumnsChargeFinder = id(p.getProperty("reportingselectcolumncharge"));
			reportingSelectColumnsCommentsFinder = id(p.getProperty("reportingselectcolumncomments"));
			reportingSelectColumnsLocationFinder = id(p.getProperty("reportingselectcolumnlocation"));
			reportingSelectColumnsPeakLoadFinder = id(p.getProperty("reportingselectcolumnpeakload"));
			reportingSelectColumnsTestDurationFinder = id(p.getProperty("reportingselectcolumntestduration"));
			reportingSelectColumnsPeakLoadDurationFinder = id(p.getProperty("reportingselectcolumnpeakloadduration"));
			reportingSelectColumnsSerialNumberFinder = id(p.getProperty("reportingselectcolumnserialnumber"));
			reportingSelectColumnsRFIDNumberFinder = id(p.getProperty("reportingselectcolumnrfidnumber"));
			reportingSelectColumnsReferenceNumberFinder = id(p.getProperty("reportingselectcolumnreferencenumber"));
			reportingSelectColumnsProductTypeFinder = id(p.getProperty("reportingselectcolumnproducttype"));
			reportingSelectColumnsProductStatusFinder = id(p.getProperty("reportingselectcolumnproductstatus"));
			reportingSelectColumnsDateIdentifiedFinder = id(p.getProperty("reportingselectcolumndateidentified"));
			reportingSelectColumnsIdentifiedByFinder = id(p.getProperty("reportingselectcolumnidentifiedby"));
			reportingSelectColumnsDescriptionFinder = id(p.getProperty("reportingselectcolumndescription"));
			reportingSelectColumnsPartNumberFinder = id(p.getProperty("reportingselectcolumnpartnumber"));
			reportingSelectColumnsOrderDescriptionFinder = id(p.getProperty("reportingselectcolumnorderdescription"));
			reportingSelectColumnsOrderNumberFinder = id(p.getProperty("reportingselectcolumnordernumber"));
			reportingSelectColumnsPurchaseOrderFinder = id(p.getProperty("reportingselectcolumnpurchaseorder"));
			reportingSearchCriteriaRFIDNumberFinder = id(p.getProperty("reportingsearchcriteriarfidnumber"));
			reportingSearchCriteriaSerialNumberFinder = id(p.getProperty("reportingsearchcriteriaserialnumber"));
			reportingSearchCriteriaEventTypeGroupFinder = id(p.getProperty("reportingsearchcriteriaeventtypegroup"));
			reportingSearchCriteriaInspectorFinder = id(p.getProperty("reportingsearchcriteriainspector"));
			reportingSearchCriteriaOrderNumberFinder = id(p.getProperty("reportingsearchcriteriaordernumber"));
			reportingSearchCriteriaPurchaseOrderFinder = id(p.getProperty("reportingsearchcriteriapurchaseorder"));
			reportingSearchCriteriaJobSiteFinder = id(p.getProperty("reportingsearchcriteriajobsite"));
			reportingSearchCriteriaAssignedToFinder = id(p.getProperty("reportingsearchcriteriaassignedto"));
			reportingSearchCriteriaCustomerFinder = id(p.getProperty("reportingsearchcriteriacustomer"));
			reportingSearchCriteriaDivisionFinder = id(p.getProperty("reportingsearchcriteriadivision"));
			reportingSearchCriteriaInspectionBookFinder = id(p.getProperty("reportingsearchcriteriainspectionbook"));
			reportingSearchCriteriaReferenceNumberFinder = id(p.getProperty("reportingsearchcriteriareferencenumber"));
			reportingSearchCriteriaProductTypeFinder = id(p.getProperty("reportingsearchcriteriaproducttype"));
			reportingSearchCriteriaProductStatusFinder = id(p.getProperty("reportingsearchcriteriaproductstatus"));
			reportingSearchCriteriaFromDateFinder = id(p.getProperty("reportingsearchcriteriafromdate"));
			reportingSearchCriteriaToDateFinder = id(p.getProperty("reportingsearchcriteriatodate"));
			reportingSearchRunButtonFinder = id(p.getProperty("reportingsearchrunbutton"));
			reportingNoResultsReturnedFinder = xpath(p.getProperty("reportingnoresultsreturned"));
			totalInspectionsSpanFinder = xpath(p.getProperty("reportingtotalinspections"));
			exportToExcelFinder = xpath(p.getProperty("exporttoexcel"));
			exportToExcelWarningToolTipFinder = id(p.getProperty("exporttoexcelwarningtooltip"));
			exportToExcelWarningFinder = xpath(p.getProperty("exporttoexcelwarning"));
			reportingSearchResultsContentHeaderFinder = xpath(p.getProperty("reportingsearchresultscontentheader"));
			reportResultsTableRowFinder = xpath(p.getProperty("reportresultablerows"));
			reportResultsTableHeaderFinder = xpath(p.getProperty("reportresultableheader"));
			printThisReportFinder = text(p.getProperty("printthisreport"));
			printAllPDFReportsFinder = text(p.getProperty("printallpdfreports"));
			printAllObservationReportsFinder = text(p.getProperty("printallobservationreports"));
			printWarningFinder = xpath(p.getProperty("printwarning"));
			printWarningToolTipFinder = id(p.getProperty("printwarningtooltip"));
			savedReportsLinkFinder = xpath(p.getProperty("savedreportslink"));
			reportResultsSaveReportFinder = xpath(p.getProperty("reportresultssavereport"));
			reportResultsSaveReportContentHeaderFinder = xpath(p.getProperty("reportresultssavereportcontentheader"));
			saveReportNameFinder = id(p.getProperty("savereportname"));
			reportResultsSaveReportSaveButtonFinder = id(p.getProperty("reportresultssavereportsavebutton"));
			reportResultsUpdateReportFinder = xpath(p.getProperty("reportresultsupdatereport"));
			reportResultsUpdateReportContentHeaderFinder = xpath(p.getProperty("reportresultsupdatereportcontentheader"));
			reportResultsSaveReportAsFinder = xpath(p.getProperty("reportresultssavereportas"));
			reportingSearchResultsForContentHeaderFinder = xpath(p.getProperty("reportresultsforcontentheader"));
			runSavedReportLinksFinder = xpath(p.getProperty("savedreportsrunlinks"));
			reportResultsStartNewReportFinder = xpath(p.getProperty("reportresultsstartnewreport"));
			reportResultsUpdateReportCancelButtonFinder = xpath(p.getProperty("reportresultsupdatereportcancelbutton"));
			reportingMassUpdateLinkFinder = xpath(p.getProperty("massupdatelink"));
			reportingMassUpdateContentHeaderFinder = xpath(p.getProperty("massupdatepageheader"));
			reportingMassUpdateCustomerNameFinder = xpath(p.getProperty("massupdatecustomer"));
			reportingMassUpdateDivisionFinder = xpath(p.getProperty("massupdatedivision"));
			reportingMassUpdateInspectionBookFinder = xpath(p.getProperty("massupdateinspectionbook"));
			reportingMassUpdateLocationFinder = xpath(p.getProperty("massupdatelocation"));
			reportingMassUpdatePrintableFinder = xpath(p.getProperty("massupdateprintable"));
			reportingMassUpdateSaveButtonFinder = xpath(p.getProperty("massupdatesavebutton"));
			massUpdateCustomerFinder = xpath(p.getProperty("massupdatecustomer"));
			massUpdateDivisionFinder = xpath(p.getProperty("massupdatedivision"));
			massUpdateInspectionBookFinder = xpath(p.getProperty("massupdateinspectionbook"));
			massUpdateLocationFinder = xpath(p.getProperty("massupdatelocation"));
			massUpdatePrintableFinder = xpath(p.getProperty("massupdateprintable"));
			summaryReportPageHeaderFinder = xpath(p.getProperty("summaryreportpageheader"));
			reportingSummaryReportLinkFinder = xpath(p.getProperty("reportingsummaryreportlink"));
			summaryReportProductTypeExpandLinkFinder = xpath(p.getProperty("summaryreportproducttypeexpandlinks"));
			summaryReportEventTypeGroupExpandLinkFinder = xpath(p.getProperty("summaryreporteventtypegroupexpandlinks"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	/**
	 * Go to the Reporting section. Works from pretty much anywhere in the system.
	 * 
	 * @throws Exception
	 */
	public void gotoReporting() throws Exception {
		Link reportingLink = ie.link(reportingFinder);
		assertTrue("Could not find the link to Reporting", reportingLink.exists());
		reportingLink.click();
		ie.waitUntilReady();
		checkReportingPageContentHeader();
	}

	/**
	 * Check that we made it the Reporting page.
	 * 
	 * @throws Exception
	 */
	public void checkReportingPageContentHeader() throws Exception {
		HtmlElement reportingContentHeader = ie.htmlElement(reportingContentHeaderFinder);
		assertTrue("Could not find Reporting page content header '" + p.getProperty("contentheader") + "'", reportingContentHeader.exists());
	}

	/**
	 * Expand the Select Display Columns section.
	 * 
	 * @throws Exception
	 */
	public void expandReportSelectColumns() throws Exception {
		assets.expandProductSearchSelectColumns();
	}

	/**
	 * Select which columns will be displayed. Does not require the
	 * checkboxes to be visible.
	 * 
	 * @param r
	 * @throws Exception
	 */
	public void setReportSelectColumns(ReportSearchSelectColumns r) throws Exception {
		Checkbox inspectionDate = ie.checkbox(reportingSelectColumnsInspectionDateFinder);
		assertTrue("Could not find the checkbox for the Inspection Date", inspectionDate.exists());
		inspectionDate.set(r.getInspectionDate());

		Checkbox inspectionType = ie.checkbox(reportingSelectColumnsInspectionTypeFinder);
		assertTrue("Could not find the checkbox for the Inspection Type", inspectionType.exists());
		inspectionType.set(r.getInspectionType());

		Checkbox customerName = ie.checkbox(reportingSelectColumnsCustomerNameFinder);
		assertTrue("Could not find the checkbox for the Customer Name", customerName.exists());
		customerName.set(r.getCustomerName());

		Checkbox result = ie.checkbox(reportingSelectColumnsResultFinder);
		assertTrue("Could not find the checkbox for the Result", result.exists());
		result.set(r.getResult());

		Checkbox inspectionBook = ie.checkbox(reportingSelectColumnsInspectionBookFinder);
		assertTrue("Could not find the checkbox for the Inspection Book", inspectionBook.exists());
		inspectionBook.set(r.getInspectionBook());

		Checkbox inspector = ie.checkbox(reportingSelectColumnsInspectorFinder);
		assertTrue("Could not find the checkbox for the Inspector", inspector.exists());
		inspector.set(r.getInspector());

		Checkbox division = ie.checkbox(reportingSelectColumnsDivisionFinder);
		assertTrue("Could not find the checkbox for the Division", division.exists());
		division.set(r.getDivision());

		Checkbox organization = ie.checkbox(reportingSelectColumnsOrganizationFinder);
		assertTrue("Could not find the checkbox for the Organization", organization.exists());
		organization.set(r.getOrganization());

		// Charge is no longer a validate display column
//		Checkbox charge = ie.checkbox(reportingSelectColumnsChargeFinder);
//		assertTrue("Could not find the checkbox for the Charge", charge.exists());
//		charge.set(r.getCharge());

		Checkbox comments = ie.checkbox(reportingSelectColumnsCommentsFinder);
		assertTrue("Could not find the checkbox for the Comments", comments.exists());
		comments.set(r.getComments());

		Checkbox location = ie.checkbox(reportingSelectColumnsLocationFinder);
		assertTrue("Could not find the checkbox for the Location", location.exists());
		location.set(r.getLocation());

		Checkbox peakLoad = ie.checkbox(reportingSelectColumnsPeakLoadFinder);
		assertTrue("Could not find the checkbox for the Peak Load", peakLoad.exists());
		peakLoad.set(r.getPeakLoad());

		Checkbox testDuration = ie.checkbox(reportingSelectColumnsTestDurationFinder);
		assertTrue("Could not find the checkbox for the Test Duration", testDuration.exists());
		testDuration.set(r.getTestDuration());

		Checkbox peakLoadDuration = ie.checkbox(reportingSelectColumnsPeakLoadDurationFinder);
		assertTrue("Could not find the checkbox for the Peak Load Duration", peakLoadDuration.exists());
		peakLoadDuration.set(r.getPeakLoadDuration());

		Checkbox serialNumber = ie.checkbox(reportingSelectColumnsSerialNumberFinder);
		assertTrue("Could not find the checkbox for the Serial Number", serialNumber.exists());
		serialNumber.set(r.getSerialNumber());

		Checkbox rfidNumber = ie.checkbox(reportingSelectColumnsRFIDNumberFinder);
		assertTrue("Could not find the checkbox for the RFID Number", rfidNumber.exists());
		rfidNumber.set(r.getRFIDNumber());

		Checkbox referenceNumber = ie.checkbox(reportingSelectColumnsReferenceNumberFinder);
		assertTrue("Could not find the checkbox for the Reference Number", referenceNumber.exists());
		referenceNumber.set(r.getReferenceNumber());

		Checkbox productType = ie.checkbox(reportingSelectColumnsProductTypeFinder);
		assertTrue("Could not find the checkbox for the Product Type", productType.exists());
		productType.set(r.getProductType());

		Checkbox productStatus = ie.checkbox(reportingSelectColumnsProductStatusFinder);
		assertTrue("Could not find the checkbox for the Product Status", productStatus.exists());
		productStatus.set(r.getProductStatus());

		Checkbox dateIdentified = ie.checkbox(reportingSelectColumnsDateIdentifiedFinder);
		assertTrue("Could not find the checkbox for the Date Identified", dateIdentified.exists());
		dateIdentified.set(r.getDateIdentified());

		Checkbox identifiedBy = ie.checkbox(reportingSelectColumnsIdentifiedByFinder);
		assertTrue("Could not find the checkbox for the Indentified By", identifiedBy.exists());
		identifiedBy.set(r.getIdentifiedBy());

		Checkbox description = ie.checkbox(reportingSelectColumnsDescriptionFinder);
		assertTrue("Could not find the checkbox for the Description", description.exists());
		description.set(r.getDescription());

		Checkbox partNumber = ie.checkbox(reportingSelectColumnsPartNumberFinder);
		assertTrue("Could not find the checkbox for the Part Number", partNumber.exists());
		partNumber.set(r.getPartNumber());

		Checkbox orderDescription = ie.checkbox(reportingSelectColumnsOrderDescriptionFinder);
		assertTrue("Could not find the checkbox for the Order Description", orderDescription.exists());
		orderDescription.set(r.getOrderDescription());

		Checkbox orderNumber = ie.checkbox(reportingSelectColumnsOrderNumberFinder);
		assertTrue("Could not find the checkbox for the Order Number", orderNumber.exists());
		orderNumber.set(r.getOrderNumber());

		Checkbox purchaseOrder = ie.checkbox(reportingSelectColumnsPurchaseOrderFinder);
		assertTrue("Could not find the checkbox for the Purchase Order", purchaseOrder.exists());
		purchaseOrder.set(r.getPurchaseOrder());
	}

	/**
	 * Set the search criteria form for a reporting search. Works on the main page
	 * as well as the refine search area as well.
	 * 
	 * @param r
	 * @throws Exception
	 */
	public void setReportingSearchCriteria(ReportingSearchCriteria r) throws Exception {
		TextField rfidNumber = ie.textField(reportingSearchCriteriaRFIDNumberFinder);
		assertTrue("Could not find the RFID Number text field", rfidNumber.exists());
		if(r.getRFIDNumber() != null) {
			rfidNumber.set(r.getRFIDNumber());
		}
		
		TextField serialNumber = ie.textField(reportingSearchCriteriaSerialNumberFinder);
		assertTrue("Could not find the Serial Number text field", serialNumber.exists());
		if(r.getSerialNumber() != null) {
			serialNumber.set(r.getSerialNumber());
		}
		
		SelectList eventTypeGroup = ie.selectList(reportingSearchCriteriaEventTypeGroupFinder);
		assertTrue("Could not find the Event Type Group select list", eventTypeGroup.exists());
		String eventTypeGroupText = r.getEventTypeGroup();
		if(eventTypeGroupText != null) {
			Option o = eventTypeGroup.option(text(eventTypeGroupText));
			assertTrue("Could not find the Event Type Group '" + eventTypeGroupText + "'", o.exists());
			o.select();
		}
		
		SelectList inspector = ie.selectList(reportingSearchCriteriaInspectorFinder);
		assertTrue("Could not find the Inspector select list", inspector.exists());
		String inspectorText = r.getInspector();
		if(inspectorText != null) {
			Option o = inspector.option(text(inspectorText));
			assertTrue("Could not find the Inspector '" + inspectorText + "'", o.exists());
			o.select();
		}
		
		TextField orderNumber = ie.textField(reportingSearchCriteriaOrderNumberFinder);
		assertTrue("Could not find the Order Number text field", orderNumber.exists());
		if(r.getOrderNumber() != null) {
			orderNumber.set(r.getOrderNumber());
		}
		
		TextField purchaseOrder = ie.textField(reportingSearchCriteriaPurchaseOrderFinder);
		assertTrue("Could not find the Purchase Order text field", purchaseOrder.exists());
		if(r.getPurchaseOrder() != null) {
			purchaseOrder.set(r.getPurchaseOrder());
		}
		
		SelectList jobSite = ie.selectList(reportingSearchCriteriaJobSiteFinder);
		String jobSiteText = r.getJobSite();
		if(jobSiteText != null) {
			assertTrue("Could not find the Job Site select list", jobSite.exists());
			Option o = jobSite.option(text(jobSiteText));
			assertTrue("Could not find the Job Site '" + jobSiteText + "'", o.exists());
			o.select();
		}
		
		SelectList assignedTo = ie.selectList(reportingSearchCriteriaAssignedToFinder);
		String assignedToText = r.getAssignedTo();
		if(assignedToText != null) {
			assertTrue("Could not find the Assigned To select list", assignedTo.exists());
			Option o = assignedTo.option(text(assignedToText));
			assertTrue("Could not find the Assigned To '" + assignedToText + "'", o.exists());
			o.select();
		}
		
		if(r.getCustomer() != null) {
			setCustomer(r.getCustomer());
		}
		
		SelectList division = ie.selectList(reportingSearchCriteriaDivisionFinder);
		assertTrue("Could not find the Division select list", division.exists());
		String divisionText = r.getDivision();
		if(divisionText != null) {
			Option o = division.option(text(divisionText));
			assertTrue("Could not find the Division '" + divisionText + "'", o.exists());
			o.select();
		}
		
		SelectList inspectionBook = getInspectionBookSelectList();
		String inspectionBookText = r.getInspectionBook();
		if(inspectionBookText != null) {
			Option o = inspectionBook.option(text(inspectionBookText));
			assertTrue("Could not find the Inspection Book '" + inspectionBookText + "'", o.exists());
			o.select();
		}
		
		TextField referenceNumber = ie.textField(reportingSearchCriteriaReferenceNumberFinder);
		assertTrue("Could not find the Reference Number text field", referenceNumber.exists());
		if(r.getReferenceNumber() != null) {
			referenceNumber.set(r.getReferenceNumber());
		}
		
		SelectList productType = ie.selectList(reportingSearchCriteriaProductTypeFinder);
		assertTrue("Could not find the Product Type select list", productType.exists());
		String productTypeText = r.getProductType();
		if(productTypeText != null) {
			Option o = productType.option(text(productTypeText));
			assertTrue("Could not find the Product Type '" + productTypeText + "'", o.exists());
			o.select();
		}
		
		SelectList productStatus = ie.selectList(reportingSearchCriteriaProductStatusFinder);
		assertTrue("Could not find the Product Status select list", productStatus.exists());
		String productStatusText = r.getProductStatus();
		if(productStatusText != null) {
			Option o = productStatus.option(text(productStatusText));
			assertTrue("Could not find the Product Type '" + productStatusText + "'", o.exists());
			o.select();
		}
		
		TextField fromDate = ie.textField(reportingSearchCriteriaFromDateFinder);
		assertTrue("Could not find the From Date text field", fromDate.exists());
		if(r.getFromDate() != null) {
			fromDate.set(r.getFromDate());
		}
		
		TextField toDate = ie.textField(reportingSearchCriteriaToDateFinder);
		assertTrue("Could not find the To Date text field", toDate.exists());
		if(r.getToDate() != null) {
			toDate.set(r.getToDate());
		}
	}
	
	private SelectList getInspectionBookSelectList() throws Exception {
		SelectList inspectionBook = ie.selectList(reportingSearchCriteriaInspectionBookFinder);
		assertTrue("Could not find the Inspection Book select list", inspectionBook.exists());
		return inspectionBook;
	}

	/**
	 * Helper method to set the customer and wait for the javascript to
	 * update the division information.
	 * 
	 * @param c
	 * @throws Exception
	 */
	private void setCustomer(String c) throws Exception {
		SelectList customer = ie.selectList(reportingSearchCriteriaCustomerFinder);
		assertTrue("Could not find the Customer select list", customer.exists());
		Option o = customer.option(text(c));
		assertTrue("Could not find the Customer '" + c + "'", o.exists());
		o.select();
		customer.fireEvent("onchange");
		misc.waitForJavascript();
	}

	/**
	 * Once the search criteria and display columns have been set, call this
	 * method to get the results.
	 * 
	 * @throws Exception
	 */
	public void gotoReportSearchResults() throws Exception {
		Button run = ie.button(reportingSearchRunButtonFinder);
		assertTrue("Could not find the Run button", run.exists());
		run.click();
		
		checkReportingSearchResultsPage();
	}

	private void checkReportingSearchResultsPage() throws Exception {
		HtmlElement reportingSearchResultsContentHeader = ie.htmlElement(reportingSearchResultsContentHeaderFinder);
		assertTrue("Could not find Report Results page content header", reportingSearchResultsContentHeader.exists());
	}

	/**
	 * After a reporting search result has been run, this will return the
	 * number of inspections in the result set based on the number at the
	 * bottom of the web page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getTotalNumberOfInspections() throws Exception {
		long result = -1;
		HtmlElement noResultsReturned = ie.htmlElement(reportingNoResultsReturnedFinder);
		if(noResultsReturned.exists()) {
			result = 0;
		} else {
			Span total = ie.span(totalInspectionsSpanFinder);
			assertTrue("Could not find the Total Inspections information at the bottom of Search Results", total.exists());
			String s = total.text().trim();
			result = Long.parseLong(s.replace("Total Inspections ", ""));
		}
		return result;
	}
	
	/**
	 * If the search results are greater than 10000 products,
	 * clicking the export to excel link should pop open a tool
	 * tip like dialog telling the user to fix their search
	 * results. This confirms that tool tip appears and have
	 * the right information.
	 * 
	 * @throws Exception
	 */
	public void exportToExcelWarningOver10000Reports() throws Exception {
		String message = "You can only export a maximum of 10000 results to excel. Please refine your results so that there are less than 10000.";
		Link export = ie.link(exportToExcelWarningFinder);
		assertTrue("Could not find link to export to Excel with more than 1000 reports", export.exists());
		export.click();
		Div warning = ie.div(exportToExcelWarningToolTipFinder);
		assertTrue("Could not find the warning about more than 1000 reports.", warning.exists());
		String s = warning.text();
		assertTrue("Tool tip warning does not contain the message '" + message + "'", s.contains(message));
	}

	/**
	 * Export the search results to an Excel spreadsheet which will get emailed to you.
	 * Assume the result set is less than 1000 inspections. If greater than 1000
	 * inspections, call exportToExcelWarningOver1000Products(). Use the
	 * getTotalNumberOfInspections() method to determine which method should be called.
	 * 
	 * @throws Exception
	 */
	public void exportToExcel() throws Exception {
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		Link export = ie.link(exportToExcelFinder);
		assertTrue("Could not find the export to Excel link", export.exists());
		export.click();
		misc.clickLightboxOKbutton(message);
	}

	/**
	 * Get the text being display for the Select Display Columns section.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSelectDisplayColumnsHeader() throws Exception {
		return assets.getSelectDisplayColumnsHeader();
	}

	/**
	 * Expand the reporting search criteria section. Only available on the
	 * Report Results page.
	 * 
	 * @throws Exception
	 */
	public void expandReportSearchResultsSearchCriteria() throws Exception {
		assets.expandProductSearchResultsSearchCriteria();
	}

	/**
	 * Get a list of all the Report Results into a list of ReportingSearchCriteria.
	 * Note, the results will display Inspection Date, Inspection Type and Identified
	 * Date which is not part of the ReportingSearchCriteria datatype. We store the
	 * Inspection Date in the fromData field, the Inspection Type in the eventTypeGroup
	 * field and the Identified Date into the toDate field.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ReportingSearchCriteria> getReportingSearchResults() throws Exception {
		List<ReportingSearchCriteria> results = new ArrayList<ReportingSearchCriteria>();
		List<ReportingSearchCriteria> tmp = null;
		
		boolean loopFlag = true;
		do {
			tmp = getReportingSearchResultsFromCurrentPage();
			if (tmp.size() != 0) {
				results.addAll(tmp);
			}
			
			if (ie.link(text("Next>")).exists()) {
				ie.link(text("Next>")).click();
				ie.waitUntilReady();
			} else {
				loopFlag = false;
			}
		} while (loopFlag);

		return results;
	}

	private List<ReportingSearchCriteria> getReportingSearchResultsFromCurrentPage() throws Exception {
		FieldIDMisc.stopMonitor();
		TableRows trs = ie.rows(reportResultsTableRowFinder);
		assertNotNull("Could not find the rows for the Report Results table", trs);
		TableRow header = ie.row(reportResultsTableHeaderFinder);
		assertTrue("Could not find the header row to the Report Results table", header.exists());
		List<ReportingSearchCriteria> results = new ArrayList<ReportingSearchCriteria>();
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			TableCells tds = tr.cells();
			ReportingSearchCriteria rsc = new ReportingSearchCriteria();
			for(int index = 0; index < tds.length(); index++) {
				TableCell th = header.cell(index);
				assertTrue("Could not find header for column " + index + ", zero indexed, in the Report Results table", th.exists());
				String column = th.text().trim().toLowerCase();
				TableCell td = tds.cell(index);
				assertTrue("Could not find cell " + index + " in the Report Results table", td.exists());
				String s = td.text().trim();
				if(column.equals("serial number")) {
					rsc.setSerialNumber(s);
				} else if(column.equals("reel/id")) {
					rsc.setSerialNumber(s);
				} else if(column.equals("inspection date")) {	// re-use the fromDate field
					rsc.setFromDate(s);
				} else if(column.equals("rfid number")) {
					rsc.setRFIDNumber(s);
				} else if(column.equals("inspection type")) {	// re-use the eventTypeGroup field
					rsc.setEventTypeGroup(s);
				} else if(column.equals("customer name")) {
					rsc.setCustomer(s);
				} else if(column.equals("inspector")) {
					rsc.setInspector(s);
				} else if(column.equals("division")) {
					rsc.setDivision(s);
				} else if(column.equals("reference number")) {
					rsc.setReferenceNumber(s);
				} else if(column.equals("product type")) {
					rsc.setProductType(s);
				} else if(column.equals("product status")) {
					rsc.setProductStatus(s);
				} else if(column.equals("date identified")) {	// re-use the toDate field
					rsc.setToDate(s);
				} else if(column.equals("order number")) {
					rsc.setOrderNumber(s);
				} else if(column.equals("purchase order")) {
					rsc.setPurchaseOrder(s);
				} else if(column.equals("inspection book")) {
					rsc.setInspectionBook(s);
				} else {
					continue;	// skip any column not part of ReportingSearchCriteria
				}
			}
			results.add(rsc);
		}
		FieldIDMisc.startMonitor();
		return results;
	}

	/**
	 * This method assumes you have run a Reporting search and are on page 1.
	 * The following code will essentially set up the display columns correctly:

		ReportSearchSelectColumns c = new ReportSearchSelectColumns();
		c.setAllOff();
		c.setRFIDNumber(true);
		c.setSerialNumber(true);
		c.setInspectionType(true);	// eventTypeGroup
		c.setInspector(true);
		c.setOrderNumber(true);
		c.setPurchaseOrder(true);
		c.setCustomerName(true);
		c.setDivision(true);
		c.setReferenceNumber(true);
		c.setProductType(true);
		c.setProductStatus(true);
		c.setInspectionDate(true);	// fromDate
		c.setDateIdentified(true);	// toDate
		reporting.setReportSelectColumns(c);
	
	 *
	 * So you set up the search criteria, run the code above to set the
	 * display columns, go to the reporting search results then call this
	 * method. This method will then write the results to a comma seperated
	 * value (CSV) file.
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void dumpReportingSearchResults(String filename) throws Exception {
		List<ReportingSearchCriteria> results = getReportingSearchResults();
		PrintStream csv = new PrintStream(filename);
		csv.println("Serial Number,Inspection Date,RFID Number,Inspection Type,Customer Name,Inspector,Division,Reference Number,Product Type,Product Status,Date Identified,Order Number");
		Iterator<ReportingSearchCriteria> i = results.iterator();
		while(i.hasNext()) {
			ReportingSearchCriteria rsc = i.next();
			csv.print("\"'" + rsc.getSerialNumber() + "\",");
			csv.print("\"" + rsc.getFromDate() + "\",");
			csv.print("\"" + rsc.getRFIDNumber() + "\",");
			csv.print("\"" + rsc.getEventTypeGroup() + "\",");
			csv.print("\"" + rsc.getCustomer() + "\",");
			csv.print("\"" + rsc.getInspector() + "\",");
			csv.print("\"" + rsc.getDivision() + "\",");
			csv.print("\"" + rsc.getReferenceNumber() + "\",");
			csv.print("\"" + rsc.getProductType() + "\",");
			csv.print("\"" + rsc.getProductStatus() + "\",");
			csv.print("\"" + rsc.getToDate() + "\",");
			csv.println("\"'" + rsc.getOrderNumber() + "\"");
		}
		csv.close();
	}

	public List<String> getInspectionBookOptions() throws Exception {
		FieldIDMisc.stopMonitor();
		List<String> results = new ArrayList<String>();
		SelectList inspectionBook = getInspectionBookSelectList();
		Options os = inspectionBook.options();
		assertNotNull("Could not get the options available for the Inspection Book select list", os);
		Iterator<Option> i = os.iterator();
		while(i.hasNext()) {
			Option o = i.next();
			if(o.value().equals(""))	// skip blank entry at the top of list
				continue;
			String book = o.text();
			results.add(book);
		}
		FieldIDMisc.startMonitor();
		return results;
	}

	public void printThisReport() throws Exception {
		Link thisReport = ie.link(printThisReportFinder);
		assertTrue("Could not find the link for Print->This Report", thisReport.exists());
		thisReport.click();
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		misc.clickLightboxOKbutton(message);
	}

	public void printAllPDFReports() throws Exception {
		Link report = ie.link(printAllPDFReportsFinder);
		assertTrue("Could not find the link for Print->All PDF Reports", report.exists());
		report.click();
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		misc.clickLightboxOKbutton(message);
	}

	public void printAllObservationReports() throws Exception {
		Link report = ie.link(printAllObservationReportsFinder);
		assertTrue("Could not find the link for Print->All Observation Reports", report.exists());
		report.click();
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		misc.clickLightboxOKbutton(message);
	}
	
	public void printWarningOver1000Reports() throws Exception {
		String message = "You can only print out result list of 1000 or less. Please refine your results so that there are less than 1000.";
		Link print = ie.link(printWarningFinder);
		assertTrue("Could not find link to Print with more than 1000 reports", print.exists());
		print.click();
		Div warning = ie.div(printWarningToolTipFinder);
		assertTrue("Could not find the warning about more than 1000 reports.", warning.exists());
		String s = warning.text();
		assertTrue("Tool tip warning does not contain the message '" + message + "'", s.contains(message));
	}
	
	/**
	 * This goes to the Saved Reports on the My Account area from the
	 * initial Reporting page.
	 * 
	 * @throws Exception
	 */
	public void gotoSavedReports() throws Exception {
		Link savedReports = ie.link(savedReportsLinkFinder);
		assertTrue("Could not find the link to Saved Reports", savedReports.exists());
		savedReports.click();
		// Have to create this here. If made a class variable there will be a circular dependency.
		MyAccount myAccount = new MyAccount(ie);
		myAccount.checkMyAccountContentHeader();
	}
	
	/**
	 * After running a search and landing on the Report Results page,
	 * this method will go to the Save Report page.
	 *  
	 * @throws Exception
	 */
	public void gotoSaveReport() throws Exception {
		Link saveReport = ie.link(reportResultsSaveReportFinder);
		assertTrue("Could not find the link Save Report", saveReport.exists());
		saveReport.click();
		this.checkSaveReportPageContentHeader();
	}

	public void gotoUpdateReport() throws Exception {
		Link updateReport = ie.link(reportResultsUpdateReportFinder);
		assertTrue("Could not find the link Save Report", updateReport.exists());
		updateReport.click();
		this.checkReportingPageContentHeader();
	}
	
	/**
	 * @deprecated
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void checkUpdateReportPageContentHeader() throws Exception {
		HtmlElement reportResultsUpdateReportContentHeader = ie.htmlElement(reportResultsUpdateReportContentHeaderFinder);
		assertTrue("Could not find Reporting page content header", reportResultsUpdateReportContentHeader.exists());
	}

	private void checkSaveReportPageContentHeader() throws Exception {
		HtmlElement reportResultsSaveReportContentHeader = ie.htmlElement(reportResultsSaveReportContentHeaderFinder);
		assertTrue("Could not find Save Report page content header", reportResultsSaveReportContentHeader.exists());
	}
	
	/**
	 * After calling gotoSaveReport you can call this to set the name
	 * in the given text field.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void setSaveReportName(String name) throws Exception {
		TextField reportName = ie.textField(saveReportNameFinder);
		assertTrue("Could not find the text field for Save Report Name", reportName.exists());
		reportName.set(name);
	}

	/**
	 * This method will click the Save button on Save Report. Should be
	 * used after setSaveReportName has been used.
	 * 
	 * @throws Exception
	 */
	public void saveSaveReport() throws Exception {
		Button save = ie.button(reportResultsSaveReportSaveButtonFinder);
		assertTrue("Could not find the Save button on Save Report", save.exists());
		save.click();
		checkReportingSearchResultsForPage();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	private void checkReportingSearchResultsForPage() throws Exception {
		HtmlElement reportingSearchResultsForContentHeader = ie.htmlElement(reportingSearchResultsForContentHeaderFinder);
		assertTrue("Could not find Report Results For page content header", reportingSearchResultsForContentHeader.exists());
	}

	public void gotoSaveReportAs() throws Exception {
		Link saveReport = ie.link(reportResultsSaveReportAsFinder);
		assertTrue("Could not find the link Save Report As", saveReport.exists());
		saveReport.click();
		this.checkSaveReportPageContentHeader();
	}

	public List<String> getSavedReports() throws Exception {
		List<String> results = new ArrayList<String>();
		Table savedReports = ie.table(id("savedReportList"));
		assertTrue("Could not find the Saved Reports table", savedReports.exists());
		TableCells tds = savedReports.cells(xpath("TBODY/TR/TD[2]"));
		Iterator<TableCell> i = tds.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			String s = td.text().trim();
			results.add(s);
		}
		return results;
	}

	public void gotoSavedReport(String reportName) throws Exception {
		assertNotNull(reportName);
		FieldIDMisc.stopMonitor();
		Links runs = ie.links(runSavedReportLinksFinder);
		boolean found = false;
		assertNotNull("Could not find any Saved Reports", runs);
		Iterator<Link> i = runs.iterator();
		while(i.hasNext()) {
			Link run = i.next();
			TableCell name = run.cell(xpath("../../TD[2]"));
			assertTrue("Could not find the name cell for a given 'run' link", name.exists());
			if(name.text().trim().equals(reportName)) {
				run.click();
				found = true;
				break;
			}
		}
		assertTrue("Did not find the Saved Report '" + reportName +"'", found);
		if(found) {
			checkReportingSearchResultsForPage();
		}
		FieldIDMisc.startMonitor();
	}

	public void gotoStartNewReport() throws Exception {
		Link startNewReport = ie.link(reportResultsStartNewReportFinder);
		assertTrue("Could not find the link to start a new report", startNewReport.exists());
		startNewReport.click();
		checkReportingPageContentHeader();
	}

	/**
	 * Assumes the tenant has over 10000 reports. Use Hercules.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		gotoReporting();
		
		// do a search
		ReportingSearchCriteria criteria = new ReportingSearchCriteria();
		criteria.setFromDate("01/01/01");			// January, 1st, 2001
		criteria.setToDate(misc.getDateString());	// Today
		setReportingSearchCriteria(criteria);
		expandReportSelectColumns();
		ReportSearchSelectColumns columns = new ReportSearchSelectColumns();
		columns.setAllOn();
		setReportSelectColumns(columns);
		gotoReportSearchResults();

		// attempt to print
		assertTrue("You need to use a tenant with more than 10000 reports.", getTotalNumberOfInspections() >= 10000);
		exportToExcelWarningOver10000Reports();
		printWarningOver1000Reports();
		
		// save report
		gotoSaveReport();
		String name = "validate-reporting";
		setSaveReportName(name);
		saveSaveReport();

		// filter down to smaller list
		columns.setAllOff();
		columns.setInspectionType(true);
		columns.setCustomerName(true);
		columns.setResult(true);
		columns.setInspectionBook(true);
		columns.setSerialNumber(true);
		columns.setProductType(true);
		columns.setRFIDNumber(true);
		columns.setInspectionDate(true);
		FieldIDMisc.stopMonitor();
		List<String> books = getInspectionBookOptions();
		String book = books.get(0);
		criteria.setInspectionBook(book);
		expandReportSearchResultsSearchCriteria();
		setReportingSearchCriteria(criteria);
		expandReportSelectColumns();
		setReportSelectColumns(columns);
		gotoReportSearchResults();
		FieldIDMisc.startMonitor();

		// 'save as' the refined results
		gotoSaveReportAs();
		setSaveReportName(book);
		saveSaveReport();
		
		// get a list of the reports
		gotoReporting();
		List<String> reports = getSavedReports();
		assertTrue("Could not find the report I created with 'Save'", reports.contains(name));
		assertTrue("Could not find the report I created with 'Save As'", reports.contains(book));
		gotoSavedReport(book);

		// save the results to a file
		String filename = book + ".csv";
		dumpReportingSearchResults(filename);
		
		// email an excel spreadsheet with results
		exportToExcel();
		
		// check the inspection book of the first row
		List<ReportingSearchCriteria> reportingResults = getReportingSearchResults();
		ReportingSearchCriteria temp = reportingResults.get(0);
		String result = temp.getInspectionBook();
		assertNotNull(result);
		assertNotNull(book);
		assertTrue("Inspection Book: got '" + result + "', expected '" + book + "'", result.equals(book));
		assertTrue("Select Display Columns header is wrong.", getSelectDisplayColumnsHeader().equals("Select Display Columns"));
		assertTrue("Total number of inspections was not greater than zero.", getTotalNumberOfInspections() > 0);
		printAllObservationReports();
		printAllPDFReports();
		printThisReport();
		gotoStartNewReport();
		gotoSavedReport(book);
		gotoUpdateReport();
		cancelUpdateReport();
		gotoReporting();
		gotoSavedReports();
	}

	public void cancelUpdateReport() throws Exception {
		Button cancel = ie.button(reportResultsUpdateReportCancelButtonFinder);
		assertTrue("Could not find the Cancel button on Update Report", cancel.exists());
		cancel.click();
		checkReportingSearchResultsForPage();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public List<String> getReportingSearchResultsColumn(String s) throws Exception {
		List<String> results = new ArrayList<String>();
		Link next;
		boolean more;
		do {
			next = ie.link(text("Next>"));
			more = next.exists(); 
			results.addAll(getReportingSearchResultsColumnCurrentPage(s));
			if(more) {
				next.click();
			}
		} while(more);
		
		return results;
	}

	private List<String> getReportingSearchResultsColumnCurrentPage(String s) throws Exception {
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
	
	private Link getMassUpdateLink() throws Exception {
		Link mass = ie.link(reportingMassUpdateLinkFinder);
		return mass;
	}

	public void gotoMassUpdate() throws Exception {
		Link mass = getMassUpdateLink();
		assertTrue("Could not find the link to Mass Update", mass.exists());
		mass.click();
		checkMassUpdatePageContentHeader();
	}

	private void checkMassUpdatePageContentHeader() throws Exception {
		HtmlElement reportingMassUpdateContentHeader = ie.htmlElement(reportingMassUpdateContentHeaderFinder);
		assertTrue("Could not find Report Mass Update page content header", reportingMassUpdateContentHeader.exists());
	}

	public void checkEndUserMassUpdate() throws Exception {
		SelectList customer = ie.selectList(reportingMassUpdateCustomerNameFinder);
		assertTrue("Could not find the Customer Name", customer.exists());
		SelectList division = ie.selectList(reportingMassUpdateDivisionFinder);
		assertTrue("Could not find the Division", division.exists());
		SelectList book = ie.selectList(reportingMassUpdateInspectionBookFinder);
		assertTrue("Could not find the Inspection Book", book.exists());
		TextField location = ie.textField(reportingMassUpdateLocationFinder);
		assertTrue("Could not find the Location field", location.exists());
		Checkbox printable = ie.checkbox(reportingMassUpdatePrintableFinder);
		assertTrue("Could not find the Printable checkbox", printable.exists());
	}

	public void setMassUpdate(Inspection i) throws Exception {
		assertNotNull(i);
		SelectList customer = ie.selectList(massUpdateCustomerFinder);
		assertTrue("Could not find the customer list", customer.exists());
		SelectList division = ie.selectList(massUpdateDivisionFinder);
		assertTrue("Could not find the division list", division.exists());
		SelectList inspectionBook = ie.selectList(massUpdateInspectionBookFinder);
		assertTrue("Could not find the inspection book list", inspectionBook.exists());
		TextField location = ie.textField(massUpdateLocationFinder);
		assertTrue("Could not find the location field", location.exists());
		Checkbox printable = ie.checkbox(massUpdatePrintableFinder);
		assertTrue("Could not find the printable checkbox", printable.exists());
		if(i.getCustomer() != null) {
			Option o = customer.option(text(i.getCustomer()));
			assertTrue("Could not find '" + i.getCustomer() + "' in the customer list", o.exists());
			misc.waitForJavascript();
		}
		if(i.getDivision() != null) {
			Option o = division.option(text(i.getDivision()));
			assertTrue("Could not find '" + i.getDivision() + "' in the division list", o.exists());
			misc.waitForJavascript();
		}
		if(i.getBook() != null) {
			Option o = inspectionBook.option(text(i.getBook()));
			assertTrue("Could not find '" + i.getBook() + "' in the inspection book list", o.exists());
			misc.waitForJavascript();
		}
		if(i.getLocation() != null) {
			location.set(i.getLocation());
		}
		if(i.getPrintable()) {
			printable.set(true);
		}
	}

	public void gotoSaveMassUpdate() throws Exception {
		Button save = ie.button(reportingMassUpdateSaveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		misc.createThreadToCloseAreYouSureDialog();
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkReportingSearchResultsPage();
	}

	public void gotoSummaryReport() throws Exception {
		Link l = ie.link(reportingSummaryReportLinkFinder);
		assertTrue("Could not find the link to Summary Report", l.exists());
		l.click();
		checkSummaryReportPageHeader();
	}

	private void checkSummaryReportPageHeader() throws Exception {
		HtmlElement summaryReportContentHeader = ie.htmlElement(summaryReportPageHeaderFinder);
		assertTrue("Could not find Summary Report page content header", summaryReportContentHeader.exists());
	}

	public void expandSummaryReport() throws Exception {
		expandSummaryReportProductTypes();
		expandSummaryReportEventTypeGroups();
	}

	private void expandSummaryReportEventTypeGroups() throws Exception {
		Links expands = ie.links(summaryReportEventTypeGroupExpandLinkFinder);
		assertNotNull(expands);
		Iterator<Link> i = expands.iterator();
		while(i.hasNext()) {
			Link expand = i.next();
			assertTrue("Could not find a link to expand an event type group", expand.exists());
			expand.click();
		}
	}

	private void expandSummaryReportProductTypes() throws Exception {
		Links expands = ie.links(summaryReportProductTypeExpandLinkFinder);
		assertNotNull(expands);
		Iterator<Link> i = expands.iterator();
		while(i.hasNext()) {
			Link expand = i.next();
			assertTrue("Could not find a link to expand a product type", expand.exists());
			expand.click();
		}
	}

	public boolean isMassUpdateAvailable() throws Exception {
		Link mass = getMassUpdateLink();
		return mass.exists();
	}
}
