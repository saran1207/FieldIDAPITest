package com.n4systems.fieldid.admin;

import static watij.finders.FinderFactory.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.datatypes.InspectionType;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageEventTypeGroups extends TestCase {

	IE ie = null;
	Properties p;
	InputStream in;
	FieldIDMisc misc;
	ManageInspectionTypes mits;
	String propertyFile = "manageeventtypegroups.properties";
	Finder eventTypeGroupsFinder;
	Finder eventTypeGroupsPageContentHeaderFinder;
	Finder eventTypeGroupPageContentHeaderFinder;
	Finder eventTypeGroupLinksFinder;
	Finder addEventTypeGroupFinder;
	Finder pdfReportStyleFinder;
	Finder observationReportStyleFinder;
	Finder addEventTypeGroupNameFinder;
	Finder addEventTypeGroupReportTitleFinder;
	Finder addEventTypeGroupSaveButtonFinder;
	Finder eventTypeGroupInspectionTypeLinksFinder;
	Finder addingNewInspectionTypeToEventTypeGroupLinkFinder;
	Finder backToEventTypeGroupsFinder;
	Finder eventTypeGroupsPageContentFinder;
	Finder eventTypeGroupUpdateNameFinder;
	Finder eventTypeGroupUpdateTitleFinder;
	Finder editEventTypeGroupSaveButtonFinder;
	Finder pdfReportStyleUpdateFinder;
	Finder observationReportStyleUpdateFinder;
	Finder editLinkFromPageContentOptionsSectionFinder;
	Finder editLinkFromGroupDetailsFinder;
	Finder editEventTypeGroupDeleteButtonFinder;
	private Finder eventTypeGroupViewLinkFinder;
	
	public ManageEventTypeGroups(IE ie) {
		this.ie = ie;
		try {
			misc = new FieldIDMisc(ie);
			mits = new ManageInspectionTypes(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			eventTypeGroupsFinder = text(p.getProperty("link"));
			eventTypeGroupsPageContentHeaderFinder = xpath(p.getProperty("eventtypegroupscontentheader"));
			eventTypeGroupPageContentHeaderFinder = xpath(p.getProperty("eventtypegroupcontentheader"));
			eventTypeGroupLinksFinder = xpath(p.getProperty("eventtypelinks"));
			addEventTypeGroupFinder = xpath(p.getProperty("addeventtypegroup"));
			pdfReportStyleFinder = xpath(p.getProperty("pdfreportstyles"));
			observationReportStyleFinder = xpath(p.getProperty("observationreportstyles"));
			addEventTypeGroupNameFinder = id(p.getProperty("addeventtypegroupname"));
			addEventTypeGroupReportTitleFinder = id(p.getProperty("addeventtypegroupreportname"));
			addEventTypeGroupSaveButtonFinder = id(p.getProperty("addeventtypegroupsavebutton"));
			eventTypeGroupInspectionTypeLinksFinder = xpath(p.getProperty("eventtypegroupinspectiontypelinks"));
			addingNewInspectionTypeToEventTypeGroupLinkFinder = xpath(p.getProperty("addnewinspectiontypetoeventtypegroup"));
			backToEventTypeGroupsFinder = xpath(p.getProperty("backtoeventtypegroupslink"));
			eventTypeGroupsPageContentFinder = xpath(p.getProperty("eventtypepagecontent"));
			eventTypeGroupUpdateNameFinder = id(p.getProperty("editeventtypegroupname"));
			eventTypeGroupUpdateTitleFinder = id(p.getProperty("editeventtypegrouptitle"));
			editEventTypeGroupSaveButtonFinder = id(p.getProperty("editeventtypegroupsavebutton"));
			pdfReportStyleUpdateFinder = xpath(p.getProperty("pdfreportstylesupdate"));
			observationReportStyleUpdateFinder = xpath(p.getProperty("observationreportstylesupdate"));
			editLinkFromPageContentOptionsSectionFinder = xpath(p.getProperty("editlinkfromoptionssection"));
			editLinkFromGroupDetailsFinder =xpath(p.getProperty("editlinkfromgroupdetails"));
			editEventTypeGroupDeleteButtonFinder = value(p.getProperty("editeventtypegroupdeletebutton"));
			eventTypeGroupViewLinkFinder = xpath(p.getProperty("eventtypegroupviewlink"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	/**
	 * Goes to the Manage Event Type Groups page. Assumes you are (a) on
	 * the Administration page and (b) have permission to access the page.
	 * 
	 * @throws Exception
	 */
	public void gotoManageEventTypeGroups() throws Exception {
		Link manageEventTypeGroups = ie.link(eventTypeGroupsFinder);
		assertTrue("Could not find the link to Manage Event Type Groups.", manageEventTypeGroups.exists());
		manageEventTypeGroups.click();
		ie.waitUntilReady();
		checkEventTypeGroupsPageContentHeader();
	}

	public void checkEventTypeGroupsPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(eventTypeGroupsPageContentHeaderFinder);
		assertTrue("Could not find the content header on Event Type Groups page.", contentHeader.exists());
	}
	
	public void gotoEventTypeGroup(String eventTypeGroup) throws Exception {
		assertNotNull(eventTypeGroup);
		assertFalse(eventTypeGroup.equals(""));
		Link eventTypeGroupLink = ie.link(text(eventTypeGroup));
		assertTrue("Could not find the link to the Event Type Group.", eventTypeGroupLink.exists());
		eventTypeGroupLink.click();
		checkEventTypeGroupPageContentHeader(eventTypeGroup);
	}

	public void checkEventTypeGroupPageContentHeader(String eventTypeGroup) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(eventTypeGroupPageContentHeaderFinder);
		assertTrue("Could not find the content header on Event Type Group page.", contentHeader.exists());
		String header = "Manage Event Type Group - " + eventTypeGroup;
		assertEquals(header, contentHeader.text().trim());
	}
	
	public List<String> getEventTypeGroups() throws Exception {
		List<String> result = new ArrayList<String>();
		Links ls = ie.links(eventTypeGroupLinksFinder);
		assertNotNull("Could not find the links to Event Type Groups.", ls);
		for(int i = 0; i < ls.length(); i++) {
			Link l = ls.get(i);
			result.add(l.text());
		}
		return result;
	}
	
	/**
	 * Goes to the page to add an event type group. After this you might
	 * want to call getPDFReportStyles, getObservationReportStyles or
	 * addEventTypeGroup.
	 * 
	 * @throws Exception
	 */
	public void gotoAddEventTypeGroup() throws Exception {
		Link addEventTypeGroup = ie.link(addEventTypeGroupFinder);
		assertTrue("Could not find the link to add an event type group", addEventTypeGroup.exists());
		addEventTypeGroup.click();
	}
	
	/**
	 * Returns a list of the PDF Report Styles on Add Event Type Group page.
	 * Assumes you have already called gotoAddEventTypeGroup.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getPDFReportStyles() throws Exception {
		List<String> result = new ArrayList<String>();
		Radios pdfReportStyles = getReportStyles(pdfReportStyleFinder);
		assertNotNull("Could not find the radio buttons for PDF Report Styles", pdfReportStyles);
		for(int i = 0; i < pdfReportStyles.length(); i++) {
			Radio r = pdfReportStyles.get(i);
			Div d = r.div(xpath("../../DIV[@class='printOutDescription']"));
			String s = d.text().split("\n")[0];
			result.add(s.trim());
		}
		return result;
	}
	
	private Radios getReportStyles(Finder f) throws Exception {
		Radios result = ie.radios(f);
		return result;
	}
	
	/**
	 * Returns a list of the Observation Report Styles on Add Event Type Group page.
	 * Assumes you have already called gotoAddEventTypeGroup.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getObservationReportStyles() throws Exception {
		List<String> result = new ArrayList<String>();
		Radios observationReportStyles = getReportStyles(observationReportStyleFinder);
		assertNotNull("Could not find the radio buttons for Observation Report Styles", observationReportStyles);
		for(int i = 0; i < observationReportStyles.length(); i++) {
			Radio r = observationReportStyles.get(i);
			Div d = r.div(xpath("../../DIV[@class='printOutDescription']"));
			String s = d.text().split("\n")[0];
			result.add(s.trim());
		}
		return result;
	}
	
	/**
	 * Allows you to add an event type.
	 * Assumes you have called gotoAddEventTypeGroup().
	 * 
	 * @param name
	 * @param reportTitle
	 * @param pdfReportStyle
	 * @param observationReportStyle
	 * @throws Exception
	 */
	public void addEventTypeGroup(String name, String reportTitle, String pdfReportStyle, String observationReportStyle) throws Exception {
		assertNotNull(name);
		assertFalse("Most give a non-blank name.", name.equals(""));
		assertNotNull(reportTitle);
		assertFalse("Most give a non-blank name.", reportTitle.equals(""));
		TextField nameField = ie.textField(addEventTypeGroupNameFinder);
		assertTrue("Could not find the Name field.", nameField.exists());
		nameField.set(name);
		TextField titleField = ie.textField(addEventTypeGroupReportTitleFinder);
		assertTrue("Could not find the Report Title field.", titleField.exists());
		titleField.set(reportTitle);
		if(pdfReportStyle != null) {
			setRadioButtonForReportStyle(pdfReportStyleFinder, pdfReportStyle);
		}
		if(observationReportStyle != null) {
			setRadioButtonForReportStyle(observationReportStyleFinder, observationReportStyle);
		}
		
		Button save = ie.button(addEventTypeGroupSaveButtonFinder);
		assertTrue("Could not find the Save button.", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	private void setRadioButtonForReportStyle(Finder f, String reportStyle) throws Exception {
		assertNotNull(f);
		assertNotNull(reportStyle);
		Radio r = getRadioButtonForReportStyle(f, reportStyle);
		assertNotNull(r);
		assertTrue("Could not find a radio button for '" + reportStyle + "'", r.exists());
		r.set();
	}

	private Radio getRadioButtonForReportStyle(Finder f, String reportStyle) throws Exception {
		Radio result = null;
		Radios rs = ie.radios(f);
		Iterator<Radio> i = rs.iterator();
		while(i.hasNext()) {
			Radio r = i.next();
			assertTrue("Could not find a radio button.", r.exists());
			Div d = r.div(xpath("../../DIV[@class='printOutDescription']"));
			assertTrue("Could not find the div with the description of a report style.", d.exists());
			if(d.text().contains(reportStyle)) {
				result = r;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Gets a list of the Inspection Types in an Event Type Group.
	 * It assumes you have navigated to the Event Type Group before
	 * calling this method.
	 * 
	 * Will return an empty list if there are no inspection types
	 * associated with an event type group. That is, the size of
	 * the list will be zero.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getInspectionTypesFromEventTypeGroupPage() throws Exception {
		List<String> results = new ArrayList<String>();
		Links inspectionTypeLinks = ie.links(eventTypeGroupInspectionTypeLinksFinder);
		assertNotNull("Could not find the list of inspection type links.", inspectionTypeLinks);
		Iterator<Link> i = inspectionTypeLinks.iterator();
		while(i.hasNext()) {
			Link l = i.next();
			assertTrue("Could not find one of the links for inspection types.", l.exists());
			String s = l.text();
			if(l.href().contains("inspectionType.action")) {	// Don't add the "Add a new Inspection Type to this group." link
				results.add(s);
			}
		}
		return results;
	}
	
	/**
	 * Goes back to the Event Type Groups page from within an
	 * Event Type Group page. Will work from the Group Information
	 * or Edit page.
	 * 
	 * @throws Exception
	 */
	public void gotoEventTypeGroupsFromEventTypeGroup() throws Exception {// assumes gotoEventTypeGroup was called
		Link backToEventTypeGroups = ie.link(backToEventTypeGroupsFinder);
		assertTrue("Could not find the link to get back to the Event Type Groups page", backToEventTypeGroups.exists());
		backToEventTypeGroups.click();
		ie.waitUntilReady();
		checkEventTypeGroupsPageContentHeader();
	}
	
	/**
	 * Allows you to edit an existing Event Type Group. Assumes
	 * you are on the Event Type Group page. Use the method
	 * gotoEditEventTypeGroupFromEventTypeGroupsPage.
	 * gotoEditEventTypeGroupFromEventTypeGroupPage or
	 * gotoEditEventTypeGroupFromEventTypeGroupPage2 to get to
	 * the place you can use this method.
	 * 
	 * Any inputs which are null will not be altered.
	 * 
	 * @param eventTypeName
	 * @param name
	 * @param title
	 * @param pdfReportStyle
	 * @param observationReportStyle
	 * @throws Exception
	 */
	public void editEventTypeGroup(String oldEventTypeName, String newEventTypeName, String reportTitle, String pdfReportStyle, String observationReportStyle) throws Exception {
		TextField name = ie.textField(eventTypeGroupUpdateNameFinder);
		assertTrue("Could not find the name field for updating the event type group '" + oldEventTypeName + "'", name.exists());
		if(name != null) {
			name.set(newEventTypeName);
		}
		TextField title = ie.textField(eventTypeGroupUpdateTitleFinder);
		assertTrue("Could not find the report title field for updating the event type group '" + oldEventTypeName + "'", title.exists());
		if(title != null) {
			title.set(reportTitle);
		}
		if(pdfReportStyle != null) {
			setRadioButtonForReportStyle(pdfReportStyleUpdateFinder, pdfReportStyle);
		}
		if(observationReportStyle != null) {
			setRadioButtonForReportStyle(observationReportStyleUpdateFinder, observationReportStyle);
		}

		Button save = ie.button(editEventTypeGroupSaveButtonFinder);
		assertTrue("Could not find the Save button.", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void gotoEditEventTypeGroupFromEventTypeGroupsPage(String eventTypeName) throws Exception {
		Div pageContent = getEventTypeGroupPageContentDiv();
		Link edit = pageContent.link(xpath("TABLE/TBODY/TR/TD/A[text()='" + eventTypeName + "']/../../TD/A[text()='Edit']"));
		assertTrue("Could not find the Edit link for '" + eventTypeName + "'", edit.exists());
		edit.click();
		checkEditEventTypeGroupPageContentHeader(eventTypeName);
	}
	
	private Div getEventTypeGroupPageContentDiv() throws Exception {
		Div pageContent = ie.div(eventTypeGroupsPageContentFinder);
		assertTrue("Could not find the page content.", pageContent.exists());
		return pageContent;
	}

	public void checkEditEventTypeGroupPageContentHeader(String eventTypeName) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(eventTypeGroupPageContentHeaderFinder);
		assertTrue("Could not find the content header on Event Type Group page.", contentHeader.exists());
		String header = "Manage Event Type Group - " + eventTypeName;
		assertTrue("Was expecting '" + header + "' but found '" + contentHeader.text() + "'", contentHeader.text().contains(header));
	}
	
	/**
	 * Goes to Edit the current Event Type Group. Assumes you are already
	 * on the Group Information page for the Event Type Group. This method
	 * will use the Edit link which is at the top right of the page content.
	 * 
	 * It needs the name of the event type to validate the result page.
	 * 
	 * @param eventTypeName
	 * @throws Exception
	 */
	public void gotoEditEventTypeGroupFromEventTypeGroupPage(String eventTypeName) throws Exception {
		Link edit = ie.link(editLinkFromPageContentOptionsSectionFinder);
		assertTrue("Could not find the Edit link at the top right of the page content.", edit.exists());
		edit.click();
		checkEditEventTypeGroupPageContentHeader(eventTypeName);
	}
	
	/**
	 * Goes to Edit the current Event Type Group. Assumes you are already
	 * on the Group Information page for the Event Type Group. This method
	 * will use the Edit link which is in the Group Detail header.
	 * 
	 * It needs the name of the event type to validate the result page.
	 * 
	 * @param eventTypeName
	 * @throws Exception
	 */
	public void gotoEditEventTypeGroupFromEventTypeGroupPage2(String eventTypeName) throws Exception {
		Link edit = ie.link(editLinkFromGroupDetailsFinder);
		assertTrue("Could not find the Edit link at the top right of the page content.", edit.exists());
		edit.click();
		checkEditEventTypeGroupPageContentHeader(eventTypeName);
	}
	
	public void gotoInspectionTypeFromEventTypeGroupPage(String inspectionType) throws Exception {
		Link inspectionTypeLink = ie.link(text(inspectionType));
		assertTrue("Could not find a link to the inspection type '" + inspectionType + "'", inspectionTypeLink.exists());
		inspectionTypeLink.click();
		mits.checkInspectionTypePageContentHeader(inspectionType);
	}
	
	public void deleteEventTypeGroup() throws Exception { // assumes we are on the edit event type group page
		Button delete = ie.button(editEventTypeGroupDeleteButtonFinder);
		assertTrue("Could not find the Delete button.", delete.exists());
		misc.createThreadToCloseAreYouSureDialog();
		delete.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkEventTypeGroupsPageContentHeader();
	}
	
	public void deleteEventTypeGroupFromEventTypeGroupsPage(String eventTypeName) throws Exception {
		Div pageContent = getEventTypeGroupPageContentDiv();
		Link delete = pageContent.link(xpath("TABLE/TBODY/TR/TD/A[text()='" + eventTypeName + "']/../../TD/A[text()='Delete']"));
		assertTrue("Could not find the Delete link for '" + eventTypeName + "'", delete.exists());
		delete.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkEventTypeGroupsPageContentHeader();
	}
	
	public void gotoAddInspectionTypeToEventTypeGroup() throws Exception {
		Link addInspectionType = ie.link(addingNewInspectionTypeToEventTypeGroupLinkFinder);
		assertTrue("Could not find the link to add a new inspection type to an event type group.", addInspectionType.exists());
		addInspectionType.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void addInspectionTypeToEventTypeGroup(InspectionType it) throws Exception {
		mits.checkAddInspectionTypePageContentHeader();
		mits.addInspectionType(it);
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void validate() throws Exception {
		gotoManageEventTypeGroups();
		gotoAddEventTypeGroup();
		List<String> pdfReportStyles = getPDFReportStyles();
		assertNotNull(pdfReportStyles);
		assertTrue("There has to be at least one PDF Report Style", pdfReportStyles.size() > 1);
		List<String> observationReportStyles = getObservationReportStyles();
		assertNotNull(observationReportStyles);
		assertTrue("There has to be at least one Observation Report Style", observationReportStyles.size() > 1);
		int n = misc.getRandomInteger();
		String newEventTypeGroup = "validate-" + n;
		String reportTitle = "Report Title " + n;
		n = misc.getRandomInteger(1, pdfReportStyles.size()-1);
		String pdfReportStyle = pdfReportStyles.get(n);
		n = misc.getRandomInteger(1, observationReportStyles.size()-1);
		String observationReportStyle = observationReportStyles.get(n);
		addEventTypeGroup(newEventTypeGroup , reportTitle, pdfReportStyle, observationReportStyle);
		gotoEventTypeGroupsFromEventTypeGroup();
		List<String> eventTypeGroups = getEventTypeGroups();
		assertNotNull(eventTypeGroups);
		assertTrue("Just added an Event Type Group, so there should be at least one.", eventTypeGroups.size() > 0);
		n = misc.getRandomInteger(eventTypeGroups.size());
		String eventTypeGroup = eventTypeGroups.get(n);
		gotoEditEventTypeGroupFromEventTypeGroupsPage(eventTypeGroup);
		gotoViewEventTypeGroupFromEditOrAdd(eventTypeGroup);
		gotoEditEventTypeGroupFromEventTypeGroupPage(eventTypeGroup);
		gotoViewAll();
		gotoEventTypeGroup(newEventTypeGroup);
		gotoEditEventTypeGroupFromEventTypeGroupPage2(newEventTypeGroup);
		gotoViewEventTypeGroupFromEditOrAdd(newEventTypeGroup);
		n = misc.getRandomInteger();
		String inspectionTypeName = "validate-" + n;
		InspectionType it = new InspectionType(inspectionTypeName);
		it.setMasterInspection(true);
		it.setPrintable(true);
		List<String> proofTestTypes = new ArrayList<String>();
		proofTestTypes.add(InspectionType.other);
		it.setProofTestTypes(proofTestTypes);
		List<String> inspectionAttributes = new ArrayList<String>();
		inspectionAttributes.add("validate-" + n);
		it.setInspectionAttributes(inspectionAttributes);
		gotoAddInspectionTypeToEventTypeGroup();
		addInspectionTypeToEventTypeGroup(it);
		misc.gotoBackToAdministration();
		gotoManageEventTypeGroups();
		gotoEventTypeGroup(newEventTypeGroup);
		List<String> inspectionTypes = getInspectionTypesFromEventTypeGroupPage();
		assertNotNull(inspectionTypes);
		assertTrue("Just added an inspection type so there should be at least one.", inspectionTypes.size() > 0);
		String inspectionType = it.getName();
		gotoInspectionTypeFromEventTypeGroupPage(inspectionType);
		misc.gotoBackToAdministration();
		gotoManageEventTypeGroups();
		gotoEventTypeGroup(newEventTypeGroup);
//		this.deleteEventTypeGroup();
//		this.deleteEventTypeGroupFromEventTypeGroupsPage(eventTypeGroup);
//		this.editEventTypeGroup(oldEventTypeName, newEventTypeName, reportTitle, pdfReportStyle, observationReportStyle);
//		this.gotoEventTypeGroupsFromEventTypeGroup();
//		this.gotoEventTypeGroup(eventTypeGroup);
	}

	public void gotoViewEventTypeGroupFromEditOrAdd(String eventTypeGroup) throws Exception {
		Link l = ie.link(eventTypeGroupViewLinkFinder);
		assertTrue("Could not find the link to View current Event Type Group", l.exists());
		l.click();
		checkEventTypeGroupPageContentHeader(eventTypeGroup);
	}

	public void gotoViewAll() throws Exception {
		gotoEventTypeGroupsFromEventTypeGroup();
	}
}
