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

import com.n4systems.fieldid.datatypes.Inspection;

import watij.elements.Button;
import watij.elements.Checkbox;
import watij.elements.HtmlElement;
import watij.elements.Link;
import watij.elements.Links;
import watij.elements.Option;
import watij.elements.SelectList;
import watij.elements.TextField;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Inspect extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	FieldIDMisc misc = null;
	String propertyFile = "inspect.properties";
	Finder inspectFinder;
	Finder inspectContentHeaderFinder;
	private Finder inspectionPageContentHeaderFinder;
	private Finder startNewInspectionListFinder;
	private Finder masterInspectionStartEventLinkFinder;
	private Finder customerBaseSelectListFinder;
	private Finder divisionBaseSelectListFinder;
	private Finder inspectorBaseSelectListFinder;
	private Finder inspectionDateBaseTextFieldFinder;
	private Finder scheduledForBaseSelectListFinder;
	private Finder inspectionBookBaseSelectListFinder;
	private Finder addNewInspectionBookBaseLinkFinder;
	private Finder commentTemplateBaseSelectListFinder;
	private Finder commentsBaseTextFieldFinder;
	private Finder printableBaseCheckboxFinder;
	private Finder productStatusBaseSelectListFinder;
	private Finder nextInspectionDateBaseTextFieldFinder;
	private Finder inspectionBookNewBaseSelectListFinder;	// after clicking 'Add new book' this appears.
	private Finder storeMasterInspectionButtonFinder;
	private Finder locationBaseTextFieldFinder;
	private Finder saveMasterInspectionButtonFinder;
	private Finder viewInspectionPageContentHeaderFinder;
	private Finder inspectSmartSearchTextFieldFinder;
	private Finder inspectSmartSearchLoadButtonFinder;
	private Finder inspectManageInspectionLinksFinder;
	private Finder inspectEditLinkFinder;
	private Finder editThisEventMasterInspectionLinkFinder;
	private Finder editInspectionPageContentHeaderFinder;
	private Finder storeEditMasterInspectionButtonFinder;
	private Finder saveEditStandardInspectionFinder;
	private Finder editOnManageInspectionsLinkFinder;
	
	public Inspect(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			editOnManageInspectionsLinkFinder = xpath(p.getProperty("editonmanageinspectionslinks"));
			saveEditStandardInspectionFinder = xpath(p.getProperty("saveeditinspection"));
			storeEditMasterInspectionButtonFinder = xpath(p.getProperty("masterinspectioneditstorebutton"));
			editInspectionPageContentHeaderFinder = xpath(p.getProperty("editinspectionpagecontentheader"));
			editThisEventMasterInspectionLinkFinder = xpath(p.getProperty("editthiseventmasterinspectionlinks"));
			inspectEditLinkFinder = xpath(p.getProperty("editinspectionlink"));
			inspectManageInspectionLinksFinder = xpath(p.getProperty("inspectmanageinspectionlinks"));
			inspectSmartSearchLoadButtonFinder = xpath(p.getProperty("inspectsmartsearchloadbutton"));
			inspectSmartSearchTextFieldFinder = xpath(p.getProperty("inspectsmartsearchtextfield"));
			viewInspectionPageContentHeaderFinder = xpath(p.getProperty("viewinspectionpagecontentheader"));
			saveMasterInspectionButtonFinder = xpath(p.getProperty("masterinspectionsavebutton"));
			storeMasterInspectionButtonFinder = xpath(p.getProperty("masterinspectionstorebutton"));
			customerBaseSelectListFinder = xpath(p.getProperty("masterinspectioncustomer"));
			divisionBaseSelectListFinder = xpath(p.getProperty("masterinspectiondivision"));
			locationBaseTextFieldFinder = xpath(p.getProperty("masterinspectionlocation"));
			inspectorBaseSelectListFinder = xpath(p.getProperty("masterinspectioninspector"));
			inspectionDateBaseTextFieldFinder = xpath(p.getProperty("masterinspectioninspectiondate"));
			scheduledForBaseSelectListFinder = xpath(p.getProperty("masterinspectionscheduledfor"));
			inspectionBookBaseSelectListFinder = xpath(p.getProperty("masterinspectioninspectionbook"));
			addNewInspectionBookBaseLinkFinder = xpath(p.getProperty("masterinspectionaddnewbook"));
			inspectionBookNewBaseSelectListFinder = xpath(p.getProperty("masterinspectioninspectionbookadd"));
			commentTemplateBaseSelectListFinder = xpath(p.getProperty("masterinspectioncommenttemplates"));
			commentsBaseTextFieldFinder = xpath(p.getProperty("masterinspectioncomments"));
			printableBaseCheckboxFinder = xpath(p.getProperty("masterinspectionprintable"));
			productStatusBaseSelectListFinder = xpath(p.getProperty("masterinspectionproductstatus"));
			nextInspectionDateBaseTextFieldFinder = xpath(p.getProperty("masterinspectionnextinspection"));
			masterInspectionStartEventLinkFinder = xpath(p.getProperty("youmustperformthisevent"));
			startNewInspectionListFinder = xpath(p.getProperty("startnewinspectionlist"));
			inspectionPageContentHeaderFinder = xpath(p.getProperty("inspectionpagecontentheader"));
			inspectFinder = xpath(p.getProperty("link"));
			inspectContentHeaderFinder = xpath(p.getProperty("contentheader"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	public void gotoInspect() throws Exception {
		Link l = ie.link(inspectFinder);
		assertTrue("Could not find the link to Inspect", l.exists());
		l.click();
		checkPageContentHeader();
	}

	private void checkPageContentHeader() throws Exception {
		HtmlElement inspectContentHeader = ie.htmlElement(inspectContentHeaderFinder);
		assertTrue("Could not find Inspect page content header '" + p.getProperty("contentheader") + "'", inspectContentHeader.exists());
	}

	/**
	 * If the inspection type is a master inspection this will take you to
	 * the page with links to start the master inspection, start the subproduct
	 * inspections or add new components. See the methods for gotoStartMasterInspection,
	 * gotoSubProductInspection, gotoChangeLabel, gotoAddNewSubProduct and
	 * gotoFindExistingSubProduct.
	 * 
	 * If the inspection type is a standard inspection this will take you to the
	 * page to conduct the actual inspection.
	 * 
	 * @param inspectionType
	 * @param master
	 * @throws Exception
	 */
	public void gotoStartNewInspection(String inspectionType, boolean master) throws Exception {
		HtmlElement ul = startNewInspectionList();
		assertNotNull(ul);
		assertTrue("Could not find the list of inspection types under Start New Inspection", ul.exists());
		Link l = ul.link(xpath("LI/A[contains(text(),'" + inspectionType + "')]"));
		assertTrue("Could not find a link to inspection type '" + inspectionType + "'", l.exists());
		l.click();
		if(master) {
			checkPageContentHeader();
		} else {
			checkInspectionPageContentHeader(inspectionType);
		}
	}

	private void checkInspectionPageContentHeader(String inspectionType) throws Exception {
		HtmlElement header = ie.htmlElement(inspectionPageContentHeaderFinder);
		assertTrue("Could not find the page content header", header.exists());
		String s = header.text().trim();
		assertTrue("Could not find the page header for inspection type '" + inspectionType + "'", s.contains(inspectionType));
	}

	public void gotoStartMasterInspection(String inspectionType) throws Exception {
		Link l = ie.link(masterInspectionStartEventLinkFinder);
		assertTrue("Could not find the link to start the master inspection", l.exists());
		l.click();
		checkInspectionPageContentHeader(inspectionType);
	}

	public void setMasterInspection(Inspection i, String book) throws Exception {
		assertNotNull(i);
		misc.stopMonitorStatus();
		SelectList customer = ie.selectList(customerBaseSelectListFinder);
		assertTrue("Could not find the customer select list", customer.exists());
		if(i.getCustomer() != null) {
			Option o = customer.option(text(i.getCustomer()));
			assertTrue("Could not find the customer '" + i.getCustomer() + "'", o.exists());
			o.select();
			misc.waitForJavascript();
		}
		SelectList division = ie.selectList(divisionBaseSelectListFinder);
		assertTrue("Could not find the division select list", division.exists());
		if(i.getDivision() != null) {
			Option o = division.option(text(i.getDivision()));
			assertTrue("Could not find the Division '" + i.getDivision() + "'", o.exists());
			o.select();
		}
		TextField location = ie.textField(locationBaseTextFieldFinder);
		assertTrue("Could not find the location select list", location.exists());
		if(i.getLocation() != null) {
			Option o = location.option(text(i.getLocation()));
			assertTrue("Could not find the location '" + i.getLocation() + "'", o.exists());
			o.select();
		}
		SelectList inspector = ie.selectList(inspectorBaseSelectListFinder);
		assertTrue("Could not find the inspector select list", inspector.exists());
		if(i.getInspector() != null) {
			Option o = inspector.option(text(i.getInspector()));
			assertTrue("Could not find the inspector '" + i.getInspector() + "'", o.exists());
			o.select();
		}
		TextField inspectionDate = ie.textField(inspectionDateBaseTextFieldFinder);
		assertTrue("Could not find the inspection date text field", inspectionDate.exists());
		if(i.getInspectionDate() != null) {
			inspectionDate.set(i.getInspectionDate());
		}
		SelectList scheduledFor = ie.selectList(scheduledForBaseSelectListFinder);
		assertTrue("Could not find the Event is scheduled for select list", scheduledFor.exists());
		if(i.getScheduleFor() != null) {
			Option o = scheduledFor.option(text(i.getScheduleFor()));
			assertTrue("Could not find the event is scheduled for '" + i.getScheduleFor() + "'", o.exists());
			o.select();
		}
		SelectList inspectionBook = ie.selectList(inspectionBookBaseSelectListFinder);
		assertTrue("Could not find the inspection book select list", inspectionBook.exists());
		Link addInspectionBook = ie.link(addNewInspectionBookBaseLinkFinder);
		assertTrue("Could not find the add new inspection book link", addInspectionBook.exists());
		if(book != null) {
			Option o = inspectionBook.option(text(i.getScheduleFor()));
			if(o.exists()) {
				o.select();
			} else {
				addInspectionBook.click();
				misc.waitForJavascript();
				TextField newInspectionBook = ie.textField(inspectionBookNewBaseSelectListFinder);
				assertTrue("Could not find the new inspection book text field", newInspectionBook.exists());
				newInspectionBook.set(book);
			}
		}
		// Inspection Form
		// Proof Test
		SelectList commentTemplate = ie.selectList(commentTemplateBaseSelectListFinder);
		assertTrue("Could not find the Comment Template select list", commentTemplate.exists());
		TextField comments = ie.textField(commentsBaseTextFieldFinder);
		if(i.getComments() != null) {
			comments.set(i.getComments());
		}
		assertTrue("Could not find the comments text field", comments.exists());
		Checkbox printable = ie.checkbox(printableBaseCheckboxFinder);
		assertTrue("Could not find the printable checkbox", printable.exists());
		printable.set(i.getPrintable());
		SelectList productStatus = ie.selectList(productStatusBaseSelectListFinder);
		assertTrue("Could not find the Product Status select list", productStatus.exists());
		if(i.getProductStatus() != null) {
			Option o = productStatus.option(text(i.getProductStatus()));
			assertTrue("Could not find the product status '" + i.getProductStatus() + "'", o.exists());
			o.select();
		}
		TextField nextInspectionDate = ie.textField(nextInspectionDateBaseTextFieldFinder);
		assertTrue("Could not find the next inspection date text field", nextInspectionDate.exists());
		if(i.getNextInspectionDate() != null) {
			nextInspectionDate.set(i.getNextInspectionDate());
		}
		// Attach A File
		misc.startMonitorStatus();
	}

	/**
	 * this stores the master inspection and brings you back to
	 * the page with sub-product inspections and add components.
	 * 
	 * @throws Exception
	 */
	public void gotoStoreMasterInspection() throws Exception {
		Button store = ie.button(storeMasterInspectionButtonFinder);
		assertTrue("Could not find the Store button", store.exists());
		store.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkPageContentHeader();
	}

	/**
	 * this saves the master inspection, sub inspections and
	 * any add component work you did.
	 * 
	 * @throws Exception
	 */
	public void gotoSaveMasterInspection(String serialNumber) throws Exception {
		Button save = ie.button(saveMasterInspectionButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		this.checkViewInspectionPageContentHeader(serialNumber);
	}

	private void checkViewInspectionPageContentHeader(String serialNumber) throws Exception {
		HtmlElement header = ie.htmlElement(viewInspectionPageContentHeaderFinder);
		assertTrue("Could not find the content header", header.exists());
		String s = header.text().trim();
		assertTrue("Could not find the serial number '" + serialNumber + "' in the page header", s.contains(serialNumber));
	}

	public void loadAssetViaSmartSearch(String serialNumber) throws Exception {
		TextField smartSearch = ie.textField(inspectSmartSearchTextFieldFinder);
		assertTrue("Could not find the Smart Search text field in Inspect", smartSearch.exists());
		smartSearch.set(serialNumber);
		Button load = ie.button(inspectSmartSearchLoadButtonFinder);
		assertTrue("Could not find the Smart Search Load button in Inspect", load.exists());
		load.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public boolean isStartNewInspectionAvailable() throws Exception {
		HtmlElement ul = startNewInspectionList();
		return ul.exists();
	}
	
	private HtmlElement startNewInspectionList() throws Exception {
		HtmlElement ul = ie.htmlElement(startNewInspectionListFinder);
		return ul;
	}

	public List<Link> getInspectionsFromManageInspections(String masterInspectionType) throws Exception {
		List<Link> results = new ArrayList<Link>();
		Links inspections = ie.links(inspectManageInspectionLinksFinder);
		assertNotNull(inspections);
		Iterator<Link> i = inspections.iterator();
		while(i.hasNext()) {
			Link l = i.next();
			String s = l.text().trim();
			if(s.equals(masterInspectionType)) {
				results.add(l);
			}
		}
		return results;
	}

	public void gotoEdit(String serialNumber) throws Exception {
		Link l = ie.link(inspectEditLinkFinder);
		assertTrue("Could not find the Edit link for editing an inspection", l.exists());
		l.click();
		checkViewInspectionPageContentHeader(serialNumber);
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void gotoEditMasterInspection(String serialNumber, String inspectionType) throws Exception {
		Link edit = ie.link(editThisEventMasterInspectionLinkFinder);
		assertTrue("Could not find the edit link to master inspection", edit.exists());
		edit.click();
		checkEditInspectionPageContentHeader(serialNumber, inspectionType);
	}

	private void checkEditInspectionPageContentHeader(String serialNumber, String inspectionType) throws Exception {
		HtmlElement header = ie.htmlElement(editInspectionPageContentHeaderFinder);
		assertTrue("Could not find the page content header", header.exists());
		String s = header.text();
		assertTrue("Inspection type '" + inspectionType + "' does not appear in the page header '" + s +"'", s.contains(inspectionType));
		assertTrue("Serial number '" + serialNumber + "' does not appear in the page header '" + s + "'", s.contains(serialNumber));
	}

	public void gotoStoreEditMasterInspection(String serialNumber) throws Exception {
		Button store = ie.button(storeEditMasterInspectionButtonFinder);
		assertTrue("Could not find the Store button", store.exists());
		store.click();
		misc.checkForErrorMessagesOnCurrentPage();
		this.checkViewInspectionPageContentHeader(serialNumber);
	}

	public void gotoSaveStandardInspection(String serialNumber) throws Exception {
		Button save = ie.button(saveEditStandardInspectionFinder);
		assertTrue("Could not find the save button", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		this.checkViewInspectionPageContentHeader(serialNumber);
	}

	public boolean isInspectionEditable() throws Exception {
		boolean result = false;
		Links edits = ie.links(editOnManageInspectionsLinkFinder);
		result = edits.length() > 0;
		return result;
	}
}
