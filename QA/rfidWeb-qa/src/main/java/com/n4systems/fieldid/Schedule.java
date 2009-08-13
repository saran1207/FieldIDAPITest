package com.n4systems.fieldid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.MassUpdateScheduleForm;
import com.n4systems.fieldid.datatypes.ScheduleSearchCriteria;
import com.n4systems.fieldid.datatypes.ScheduleSearchSelectColumns;

import static watij.finders.FinderFactory.*;
import watij.elements.Button;
import watij.elements.Checkbox;
import watij.elements.HtmlElement;
import watij.elements.Link;
import watij.elements.Links;
import watij.elements.TextField;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Schedule extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	Assets assets = null;
	FieldIDMisc misc = null;
	String propertyFile = "schedule.properties";
	Finder scheduleFinder;
	Finder scheduleContentHeaderFinder;
	Finder scheduleSearchRunButtonFinder;
	Finder scheduleSearchResultsContentHeaderFinder;
	private Finder selectColumnCustomerNameFinder;
	private Finder selectColumnDivisionFinder;
	private Finder selectColumnLocationFinder;
	private Finder selectColumnScheduledDateFinder;
	private Finder selectColumnStatusFinder;
	private Finder selectColumnDaysPastDueFinder;
	private Finder selectColumnInspectionTypeFinder;
	private Finder selectColumnLastInspectionDateFinder;
	private Finder selectColumnSerialNumberFinder;
	private Finder selectColumnRFIDNumberFinder;
	private Finder selectColumnReferenceNumberFinder;
	private Finder selectColumnProductTypeFinder;
	private Finder selectColumnProductStatusFinder;
	private Finder selectColumnDateIdentifiedFinder;
	private Finder selectColumnIdentifiedByFinder;
	private Finder selectColumnDescriptionFinder;
	private Finder selectColumnPartNumberFinder;
	private Finder selectColumnOrderDescriptionFinder;
	private Finder selectColumnOrderNumberFinder;
	private Finder selectColumnPurchaseOrderFinder;
	private Finder exportToExcelFinder;
	private Finder massUpdateLinkFinder;
	private Finder massUpdateNextInspectionDateFinder;
	private Finder massUpdateSaveButtonFinder;
	private Finder massUpdateInstructionsFinder;
	private Finder scheduleMassUpdateContentHeaderFinder;
	private Finder scheduleResultsInspectNowLinksFinder;
	
	public Schedule(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			assets = new Assets(ie);
			misc = new FieldIDMisc(ie);
			scheduleResultsInspectNowLinksFinder = xpath(p.getProperty("scheduleresultsinspectnowlinks"));
			scheduleMassUpdateContentHeaderFinder = xpath(p.getProperty("massupdatecontentheader"));
			massUpdateInstructionsFinder = xpath(p.getProperty("massupdateinstructions"));
			massUpdateSaveButtonFinder = xpath(p.getProperty("massupdatesavebutton"));
			massUpdateNextInspectionDateFinder = xpath(p.getProperty("massupdatenextinspectiondate"));
			massUpdateLinkFinder = xpath(p.getProperty("massupdatelink"));
			exportToExcelFinder = xpath(p.getProperty("exporttoexcel"));
			selectColumnCustomerNameFinder = xpath(p.getProperty("selectcolumncustomername"));
			selectColumnDivisionFinder = xpath(p.getProperty("selectcolumndivision"));
			selectColumnLocationFinder = xpath(p.getProperty("selectcolumnlocation"));
			selectColumnScheduledDateFinder = xpath(p.getProperty("selectcolumnscheduleddate"));
			selectColumnStatusFinder = xpath(p.getProperty("selectcolumnstatus"));
			selectColumnDaysPastDueFinder = xpath(p.getProperty("selectcolumndayspastdue"));
			selectColumnInspectionTypeFinder = xpath(p.getProperty("selectcolumninspectiontype"));
			selectColumnLastInspectionDateFinder = xpath(p.getProperty("selectcolumnlastinspectiondate"));
			selectColumnSerialNumberFinder = xpath(p.getProperty("selectcolumnserialnumber"));
			selectColumnRFIDNumberFinder = xpath(p.getProperty("selectcolumnrfidnumber"));
			selectColumnReferenceNumberFinder = xpath(p.getProperty("selectcolumnreferencenumber"));
			selectColumnProductTypeFinder = xpath(p.getProperty("selectcolumnproducttype"));
			selectColumnProductStatusFinder = xpath(p.getProperty("selectcolumnproductstatus"));
			selectColumnDateIdentifiedFinder = xpath(p.getProperty("selectcolumndateidentified"));
			selectColumnIdentifiedByFinder = xpath(p.getProperty("selectcolumnidentifiedby"));
			selectColumnDescriptionFinder = xpath(p.getProperty("selectcolumndescription"));
			selectColumnPartNumberFinder = xpath(p.getProperty("selectcolumnpartnumber"));
			selectColumnOrderDescriptionFinder = xpath(p.getProperty("selectcolumnorderdescription"));
			selectColumnOrderNumberFinder = xpath(p.getProperty("selectcolumnordernumber"));
			selectColumnPurchaseOrderFinder = xpath(p.getProperty("selectcolumnpurchaseorder"));
			scheduleFinder = id(p.getProperty("link"));
			scheduleContentHeaderFinder = xpath(p.getProperty("contentheader"));
			scheduleSearchRunButtonFinder = id(p.getProperty("schedulerunbutton"));
			scheduleSearchResultsContentHeaderFinder = xpath(p.getProperty("searchresultscontentheader"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	public void gotoSchedule() throws Exception {
		Link scheduleLink = ie.link(scheduleFinder);
		assertTrue("Could not find the link to Schedule", scheduleLink.exists());
		scheduleLink.click();
		ie.waitUntilReady();
		checkSchedulePageContentHeader();
	}

	public void checkSchedulePageContentHeader() throws Exception {
		HtmlElement scheduleContentHeader = ie.htmlElement(scheduleContentHeaderFinder);
		assertTrue("Could not find Schedule page content header '" + p.getProperty("contentheader") + "'", scheduleContentHeader.exists());
	}

	public String getSelectDisplayColumnsHeader() throws Exception {
		return assets.getSelectDisplayColumnsHeader();
	}

	public void gotoScheduleSearchResults() throws Exception {
		Button run = ie.button(scheduleSearchRunButtonFinder);
		assertTrue("Could not find the Run button", run.exists());
		run.click();
		checkSchedulesearchResultsPageContentHeader();
	}

	private void checkSchedulesearchResultsPageContentHeader() throws Exception {
		HtmlElement scheduleSearchResultsContentHeader = ie.htmlElement(scheduleSearchResultsContentHeaderFinder);
		assertTrue("Could not find Schedule Search Results page content header '" + p.getProperty("searchresultscontentheader") + "'", scheduleSearchResultsContentHeader.exists());
	}

	public void expandScheduleSearchResultsSearchCriteria() throws Exception {
		assets.expandProductSearchResultsSearchCriteria();
	}
	
	public void expandProductSearchSelectColumns() throws Exception {
		assets.expandProductSearchSelectColumns();
	}
	
	public void setScheduleSearchColumns(ScheduleSearchSelectColumns c) throws Exception {
		assertNotNull(c);

		Checkbox customer = ie.checkbox(selectColumnCustomerNameFinder);
		assertTrue("Could not find the checkbox for the customer name", customer.exists());
		customer.set(c.getCustomerName());

		Checkbox division = ie.checkbox(selectColumnDivisionFinder);
		assertTrue("Could not find the checkbox for the division", division.exists());
		division.set(c.getDivision());

		Checkbox location = ie.checkbox(selectColumnLocationFinder);
		assertTrue("Could not find the checkbox for the location", location.exists());
		location.set(c.getLocation());

		Checkbox scheduledDate = ie.checkbox(selectColumnScheduledDateFinder);
		assertTrue("Could not find the checkbox for the scheduled date", scheduledDate.exists());
		scheduledDate.set(c.getScheduledDate());

		Checkbox status = ie.checkbox(selectColumnStatusFinder);
		assertTrue("Could not find the checkbox for the status", status.exists());
		status.set(c.getStatus());

		Checkbox daysPastDue = ie.checkbox(selectColumnDaysPastDueFinder);
		assertTrue("Could not find the checkbox for the days past due", daysPastDue.exists());
		daysPastDue.set(c.getDaysPastDue());

		Checkbox inspectionType = ie.checkbox(selectColumnInspectionTypeFinder);
		assertTrue("Could not find the checkbox for the inspection type", inspectionType.exists());
		daysPastDue.set(c.getInspectionType());

		Checkbox lastInspectionDate = ie.checkbox(selectColumnLastInspectionDateFinder);
		assertTrue("Could not find the checkbox for the last inspection date", lastInspectionDate.exists());
		lastInspectionDate.set(c.getLastInspectionDate());

		Checkbox serialNumber = ie.checkbox(selectColumnSerialNumberFinder);
		assertTrue("Could not find the checkbox for the serial number", serialNumber.exists());
		serialNumber.set(c.getSerialNumber());

		Checkbox rfidNumber = ie.checkbox(selectColumnRFIDNumberFinder);
		assertTrue("Could not find the checkbox for the RFID number", rfidNumber.exists());
		rfidNumber.set(c.getRfidNumber());

		Checkbox referenceNumber = ie.checkbox(selectColumnReferenceNumberFinder);
		assertTrue("Could not find the checkbox for the reference number", referenceNumber.exists());
		referenceNumber.set(c.getReferenceNumber());

		Checkbox productType = ie.checkbox(selectColumnProductTypeFinder);
		assertTrue("Could not find the checkbox for the product type", productType.exists());
		productType.set(c.getProductType());

		Checkbox productStatus = ie.checkbox(selectColumnProductStatusFinder);
		assertTrue("Could not find the checkbox for the product status", productStatus.exists());
		productStatus.set(c.getProductStatus());

		Checkbox dateIdentified = ie.checkbox(selectColumnDateIdentifiedFinder);
		assertTrue("Could not find the checkbox for the date identified", dateIdentified.exists());
		dateIdentified.set(c.getDateIdentified());

		Checkbox identifiedBy = ie.checkbox(selectColumnIdentifiedByFinder);
		assertTrue("Could not find the checkbox for the identified by", identifiedBy.exists());
		identifiedBy.set(c.getIdentifiedBy());

		Checkbox description = ie.checkbox(selectColumnDescriptionFinder);
		assertTrue("Could not find the checkbox for the description", description.exists());
		description.set(c.getDescription());

		Checkbox partNumber = ie.checkbox(selectColumnPartNumberFinder);
		assertTrue("Could not find the checkbox for the part number", partNumber.exists());
		partNumber.set(c.getPartNumber());
		
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

	public void exportToExcel() throws Exception {
		String message = "Your report has been scheduled and will be emailed to you shortly.";
		Link export = ie.link(exportToExcelFinder);
		assertTrue("Could not find the export to Excel link", export.exists());
		export.click();
		misc.clickLightboxOKbutton(message);
	}

	public void gotoMassUpdate() throws Exception {
		Link massUpdate = getMassUpdateLink();
		assertTrue("Could not find the link to go to Mass Update", massUpdate.exists());
		massUpdate.click();
		checkMassUpdateContentHeader();
		checkMassUpdateInstructions();
	}
	
	private Link getMassUpdateLink() throws Exception {
		Link massUpdate = ie.link(massUpdateLinkFinder);
		return massUpdate;
	}

	private void checkMassUpdateInstructions() throws Exception {
		String msg1 = "To update a field click the check box beside that field and set the desired value.";
		String msg2 = "Then click save.";
		String msg3 = "This could take some time to complete the update, please be patient.";
		HtmlElement instructions = ie.htmlElement(massUpdateInstructionsFinder);
		assertTrue("Could not find the instructions for mass update page", instructions.exists());
		assertTrue("The instructions for mass update does not contain '" + msg1 + "'", instructions.text().contains(msg1));
		assertTrue("The instructions for mass update does not contain '" + msg2 + "'", instructions.text().contains(msg2));
		assertTrue("The instructions for mass update does not contain '" + msg3 + "'", instructions.text().contains(msg3));
	}

	private void checkMassUpdateContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(scheduleMassUpdateContentHeaderFinder);
		assertTrue("Could not find Schedule Mass Update page content header", header.exists());
	}

	public void setMassUpdate(MassUpdateScheduleForm m) throws Exception {
		assertNotNull(m);
		misc.stopMonitor();
		TextField nextInspectionDate = ie.textField(massUpdateNextInspectionDateFinder);
		assertTrue("Could not find the next inspection date field for mass update", nextInspectionDate.exists());
		nextInspectionDate.set(m.getNextInspectionDate());
		misc.startMonitor();
	}

	public void gotoSaveMassUpdate() throws Exception {
		Button save = ie.button(massUpdateSaveButtonFinder);
		assertTrue("Could not find the save button for mass update", save.exists());
		misc.createThreadToCloseAreYouSureDialog();
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkSchedulesearchResultsPageContentHeader();
	}

	public List<Link> getInspectNowLinks() throws Exception {
		List<Link> results = new ArrayList<Link>();
		// TODO: only gets results for first page. Add a loop
		// while
		List<Link> tmp = getInspectNowLinksFromCurrentPage();
		results.addAll(tmp);
		// wend
		return results;
	}

	private List<Link> getInspectNowLinksFromCurrentPage() throws Exception {
		List<Link> results = new ArrayList<Link>();
		Links ls = ie.links(scheduleResultsInspectNowLinksFinder);
		assertNotNull(ls);
		Iterator<Link> i = ls.iterator();
		while(i.hasNext()) {
			Link inspectNow = i.next();
			assertTrue("Couldn't find an inspect now link for some reason", inspectNow.exists());
			results.add(inspectNow);
		}
		return results;
	}

	public void setScheduleSearchCriteria(ScheduleSearchCriteria s) throws Exception {
		
	}

	public boolean isMassUpdateAvailable() throws Exception {
		boolean result = false;
		Link massUpdate = getMassUpdateLink();
		result = massUpdate.exists();
		return result;
	}
}
