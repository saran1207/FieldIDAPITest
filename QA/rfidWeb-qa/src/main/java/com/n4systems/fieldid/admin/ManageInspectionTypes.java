package com.n4systems.fieldid.admin;

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
import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.datatypes.ButtonGroup;
import com.n4systems.fieldid.datatypes.Criteria;
import com.n4systems.fieldid.datatypes.InspectionForm;
import com.n4systems.fieldid.datatypes.InspectionType;
import com.n4systems.fieldid.datatypes.Section;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageInspectionTypes extends TestCase {

	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "manageinspectiontypes.properties";
	Finder inspectionTypesFinder;
	Finder inspectionTypesPageContentHeaderFinder;
	Finder inspectionTypePageContentHeaderFinder;
	Finder addInspectionTypePageContentHeaderFinder;
	Finder addInspectionTypeNameFinder;
	Finder addInspectionTypeGroupFinder;
	Finder addInspectionTypePrintableFinder;
	Finder addInspectionTypeMasterInspectionFinder;
	Finder addInspectionAddAttributeButtonFinder;
	Finder addInspectionTypeSaveButtonFinder;
	Finder addInspectionTypeSaveAndAddFormButtonFinder;
	Finder addsupportedProofTestTypeFinder;
	Finder addInspectionTypeLinkFinder;
	Finder editInspectionTypesPageContentHeaderFinder;
	Finder inspectionTypesLinkFinder;
	Finder editInspectionTypeFinder;
	Finder inspectionFormLinkFinder;
	Finder inspectionFormPageContentHeaderFinder;
	Finder inspectionTypeListLinkFinder;
	Finder inspectionTypeGroupLinkFinder;
	Finder inspectionTypesTypeFinder;
	Finder editInspectionTypeNameFinder;
	Finder editInspectionTypeGroupFinder;
	Finder editInspectionTypePrintableFinder;
	Finder editInspectionTypeMasterInspectionFinder;
	Finder editInspectionTypeSaveButtonFinder;
	Finder editSupportedProofTestTypeFinder;
	Finder editInspectionAttributesFinder;
	private Finder inspectionFormAddSectionButtonFinder;
	private Finder inspectionFormSaveButtonFinder;
	private Finder manageButtonGroupsLinkFinder;
	private Finder buttonGroupNamesFieldFinder;
	private Finder supportedProofTestTypesFromEditInspectionTypeFinder;
	private Finder viewAllLinkFinder;
	private Finder manageInspectionTypePageContentHeaderFinder;
	private Finder manageButtonGroupsImDoneButtonFinder;
	private Finder viewInspectionTypeFinder;
	private Finder inspectionFormSectionNameFinder;
	private Finder addNewButtonGroupButtonFinder;
	private Finder buttonGroupPageContentHeaderFinder;
	
	public ManageInspectionTypes(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			buttonGroupPageContentHeaderFinder = xpath(p.getProperty("managebuttongrouppagecontentheader", "NOT SET"));
			addNewButtonGroupButtonFinder = xpath(p.getProperty("addnewbuttongroupbutton", "NOT SET"));
			inspectionTypesFinder = text(p.getProperty("link", "NOT SET"));
			inspectionTypesPageContentHeaderFinder = xpath(p.getProperty("inspectiontypescontentheader", "NOT SET"));
			inspectionTypePageContentHeaderFinder = xpath(p.getProperty("inspectiontypecontentheader", "NOT SET"));
			addInspectionTypePageContentHeaderFinder = xpath(p.getProperty("addinspectiontypecontentheader", "NOT SET"));
			addInspectionTypeNameFinder = id(p.getProperty("addinspectiontypename", "NOT SET"));
			addInspectionTypeGroupFinder = id(p.getProperty("addinspectiontypegroup", "NOT SET"));
			addInspectionTypePrintableFinder = id(p.getProperty("addinspectiontypeprintable", "NOT SET"));
			addInspectionTypeMasterInspectionFinder = id(p.getProperty("addinspectiontypemasterinspection", "NOT SET"));
			addInspectionTypeSaveButtonFinder = id(p.getProperty("addinspectiontypesavebutton", "NOT SET"));
			addInspectionTypeSaveAndAddFormButtonFinder = id(p.getProperty("addinspectiontypesaveandaddform", "NOT SET"));
			addsupportedProofTestTypeFinder = xpath(p.getProperty("addinspectionsupportedprooftesttypes", "NOT SET"));
			addInspectionAddAttributeButtonFinder = text(p.getProperty("addinspectionaddattributebutton", "NOT SET"));
			addInspectionTypeLinkFinder = xpath(p.getProperty("addinspectiontypelink", "NOT SET"));
			editInspectionTypesPageContentHeaderFinder = xpath(p.getProperty("editinspectiontypecontentheader", "NOT SET"));
			inspectionTypesLinkFinder = xpath(p.getProperty("inspectiontypelinks", "NOT SET"));
			editInspectionTypeFinder = xpath(p.getProperty("editinspectiontype", "NOT SET"));
			inspectionFormLinkFinder = xpath(p.getProperty("inspectionformlink", "NOT SET"));
			inspectionFormPageContentHeaderFinder = xpath(p.getProperty("inspectionformcontentheader", "NOT SET"));
			inspectionTypeListLinkFinder = xpath(p.getProperty("inspectiontypelistlink", "NOT SET"));
			inspectionTypeGroupLinkFinder = xpath(p.getProperty("inspectiontypegrouplink", "NOT SET"));
			inspectionTypesTypeFinder = xpath(p.getProperty("inspectiontypetype", "NOT SET"));
			editInspectionTypeNameFinder = id(p.getProperty("editinspectiontypename", "NOT SET"));
			editInspectionTypeGroupFinder = id(p.getProperty("editinspectiontypegroup", "NOT SET"));
			editInspectionTypePrintableFinder = id(p.getProperty("editinspectiontypeprintable", "NOT SET"));
			editInspectionTypeMasterInspectionFinder = id(p.getProperty("editinspectiontypemasterinspection", "NOT SET"));
			editInspectionTypeSaveButtonFinder = id(p.getProperty("editinspectiontypesavebutton", "NOT SET"));
			editSupportedProofTestTypeFinder = xpath(p.getProperty("editinspectionsupportedprooftesttypes", "NOT SET"));
			editInspectionAttributesFinder = xpath(p.getProperty("editinspectionattributes", "NOT SET"));
			inspectionFormAddSectionButtonFinder = xpath(p.getProperty("inspectionformaddsectionbutton", "NOT SET"));
			inspectionFormSaveButtonFinder = xpath(p.getProperty("inspectionformsavebutton", "NOT SET"));
			manageButtonGroupsLinkFinder = xpath(p.getProperty("managebuttongrouplink", "NOT SET"));
			buttonGroupNamesFieldFinder = xpath(p.getProperty("buttongroupnamesfield", "NOT SET"));
			supportedProofTestTypesFromEditInspectionTypeFinder = xpath(p.getProperty("supportedprooftesttypeseditform", "NOT SET"));
			viewAllLinkFinder = xpath(p.getProperty("viewalllink", "NOT SET"));
			manageInspectionTypePageContentHeaderFinder = xpath(p.getProperty("manageinspectiontypecontentheader", "NOT SET"));
			manageButtonGroupsImDoneButtonFinder = xpath(p.getProperty("managebuttongroupsimdonebutton", "NOT SET"));
			viewInspectionTypeFinder = xpath(p.getProperty("viewinspectiontype", "NOT SET"));
			inspectionFormSectionNameFinder = xpath(p.getProperty("inspectionformsectioncontainers", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}

	public void gotoManageInspectionTypes() throws Exception {
		Link manageEventTypeGroups = ie.link(inspectionTypesFinder);
		assertTrue("Could not find the link to Manage Inspection Types.", manageEventTypeGroups.exists());
		manageEventTypeGroups.click();
		ie.waitUntilReady();
		checkInspectionTypesPageContentHeader();
	}

	private void checkInspectionTypesPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(inspectionTypesPageContentHeaderFinder);
		assertTrue("Could not find the content header on Inspection Types page.", contentHeader.exists());
	}

	public void checkInspectionTypePageContentHeader(String inspectionType) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(inspectionTypePageContentHeaderFinder);
		assertTrue("Could not find the content header on Inspection Type page.", contentHeader.exists());
		String header = "Manage Inspection Type - " + inspectionType;
		assertEquals("Was expecting '" + header + "' but found '" + contentHeader.text().trim() + "'", header, contentHeader.text().trim());
	}

	public void checkAddInspectionTypePageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(addInspectionTypePageContentHeaderFinder);
		assertTrue("Could not find the content header on Add Inspection Types page.", contentHeader.exists());
	}

	public void addInspectionType(InspectionType it) throws Exception {
		FieldIDMisc.stopMonitor();
		assertNotNull(it);
		assertNotNull(it.getName());
		assertFalse("Inspection type must have a name.", it.getName().equals(""));
		TextField addInspectionTypeName = ie.textField(addInspectionTypeNameFinder);
		assertTrue("Could not find the name field", addInspectionTypeName.exists());
		SelectList addInspectionTypeGroup = ie.selectList(addInspectionTypeGroupFinder);
		assertTrue("Could not find the group field", addInspectionTypeGroup.exists());
		Checkbox addInspectionTypePrintable = ie.checkbox(addInspectionTypePrintableFinder);
		assertTrue("Could not find the printable checkbox", addInspectionTypePrintable.exists());
		Checkbox addInspectionTypeMasterInspection = ie.checkbox(addInspectionTypeMasterInspectionFinder);
		assertTrue("Could not find the master inspection checkbox", addInspectionTypeMasterInspection.exists());
		
		addInspectionTypeName.set(it.getName());
		String group = it.getGroup();
		if(group != null) {
			Option o = addInspectionTypeGroup.option(text(group));
			assertTrue("Could not find the group '" + group + "'", o.exists());
			o.select();
		}
		addInspectionTypePrintable.set(it.getPrintable());
		addInspectionTypeMasterInspection.set(it.getMasterInspection());
		addInspectionProofTestTypes(it.getProofTestTypes());
		addInspectionTypeAttributes(it.getInspectionAttributes());
		
		Button save = ie.button(addInspectionTypeSaveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		FieldIDMisc misc = new FieldIDMisc(ie);
		misc.checkForErrorMessagesOnCurrentPage();
		FieldIDMisc.startMonitor();
		checkInspectionTypePageContentHeader(it.getName());
	}

	private void addInspectionTypeAttributes(List<String> inspectionAttributes) throws Exception {
		assertNotNull(inspectionAttributes);
		Button addAttribute = ie.button(addInspectionAddAttributeButtonFinder);
		assertTrue("Could not find the Add Attribute button on Add Inspection Type", addAttribute.exists());
		FieldIDMisc misc = new FieldIDMisc(ie);
		for(int i = 0; i < inspectionAttributes.size(); i++) {
			addAttribute.click();
			misc.waitForJavascript();
			TextField attrib = ie.textField(id("infoFields_" + i + "_"));
			assertTrue("Could not find an Inspection Attribute field", attrib.exists());
			attrib.set(inspectionAttributes.get(i));
		}
	}

	private void addInspectionProofTestTypes(List<String> proofTestTypes) throws Exception {
		assertNotNull(proofTestTypes);
		TableRows trs = ie.rows(addsupportedProofTestTypeFinder);
		assertNotNull("Could not find the table of supported proof test types", trs);
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			String proofTestType = tr.cell(0).text().trim();
			Radio on = tr.radio(value("true"));
			Radio off = tr.radio(value("false"));
			if(proofTestTypes.contains(proofTestType)) {
				on.set();
			} else {
				off.set();
			}
		}
	}

	public void gotoAddInspectionType() throws Exception {
		Link addInspectionType = ie.link(addInspectionTypeLinkFinder);
		assertTrue("Could not find the link to Add Inspection Type", addInspectionType.exists());
		addInspectionType.click();
		checkAddInspectionTypePageContentHeader();
	}
	
	public void gotoEditInspectionTypeFromInspectionTypesPage(String inspectionType) throws Exception {
		Link edit = ie.link(xpath("//DIV[@id='pageContent']/TABLE/TBODY/TR/TD/A[text()='" + inspectionType + "']/../../TD/A[text()='Edit']"));
		assertTrue("Could not find the Edit link for '" + inspectionType + "'", edit.exists());
		edit.click();
		checkEditInspectionTypePageContentHeader();
	}

	private void checkEditInspectionTypePageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(editInspectionTypesPageContentHeaderFinder);
		assertTrue("Could not find the content header on Edit Inspection Type page.", contentHeader.exists());
	}
	
	private Links getInspectionTypeLinks() throws Exception {
		Links inspectionTypes = ie.links(inspectionTypesLinkFinder);
		assertNotNull(inspectionTypes);
		return inspectionTypes;
	}
	
	public List<String> getInspectionTypes() throws Exception {
		List<String> result = new ArrayList<String>();
		Links inspectionTypes = getInspectionTypeLinks();
		Iterator<Link> i = inspectionTypes.iterator();
		while(i.hasNext()) {
			Link l = i.next();
			result.add(l.text().trim());
		}
		return result;
	}
	
	public List<String> getMasterInspectionTypes() throws Exception {
		List<String> result = new ArrayList<String>();
		TableCells inspectionTypeTypes = ie.cells(inspectionTypesTypeFinder);
		assertNotNull(inspectionTypeTypes);
		Iterator<TableCell> i = inspectionTypeTypes.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			if(td.text().contains("Master")) {
				Link l = td.link(xpath("../TD[1]/A"));
				result.add(l.text().trim());
			}
		}
		return result;
	}
	
	public void gotoInspectionType(String inspectionType) throws Exception {
		Link inspectionTypeLink = ie.link(xpath("//DIV[@id='pageContent']/TABLE/TBODY/TR/TD/A[text()='" + inspectionType + "']"));
		assertTrue("Could not find the link to '" + inspectionType + "'", inspectionTypeLink.exists());
		inspectionTypeLink.click();
		this.checkInspectionTypePageContentHeader(inspectionType);
	}
	
	public void gotoEditInspectionTypeFromInspectionTypeInformation() throws Exception {
		Link editInspectionType = ie.link(editInspectionTypeFinder);
		assertTrue("Could not find the Edit Inspection Type link", editInspectionType.exists());
		editInspectionType.click();
		checkEditInspectionTypePageContentHeader();
	}
	
	public void editInspectionType(InspectionType it) throws Exception {
		FieldIDMisc.stopMonitor();
		assertNotNull(it);
		assertNotNull(it.getName());
		assertFalse("Inspection type must have a name.", it.getName().equals(""));
		TextField editInspectionTypeName = ie.textField(editInspectionTypeNameFinder);
		assertTrue("Could not find the name field", editInspectionTypeName.exists());
		SelectList editInspectionTypeGroup = ie.selectList(editInspectionTypeGroupFinder);
		assertTrue("Could not find the group field", editInspectionTypeGroup.exists());
		Checkbox editInspectionTypePrintable = ie.checkbox(editInspectionTypePrintableFinder);
		assertTrue("Could not find the printable checkbox",editInspectionTypePrintable.exists());
		Checkbox editInspectionTypeMasterInspection = ie.checkbox(editInspectionTypeMasterInspectionFinder);
		assertTrue("Could not find the master inspection checkbox", editInspectionTypeMasterInspection.exists());
		
		editInspectionTypeName.set(it.getName());
		String group = it.getGroup();
		if(group != null) {
			Option o = editInspectionTypeGroup.option(text(group));
			assertTrue("Could not find the group '" + group + "'", o.exists());
			o.select();
		}
		editInspectionTypePrintable.set(it.getPrintable());
		editInspectionTypeMasterInspection.set(it.getMasterInspection());
		editInspectionProofTestTypes(it.getProofTestTypes());
		addInspectionTypeAttributes(it.getInspectionAttributes());
		
		Button save = ie.button(editInspectionTypeSaveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		FieldIDMisc misc = new FieldIDMisc(ie);
		misc.checkForErrorMessagesOnCurrentPage();
		FieldIDMisc.startMonitor();
		checkInspectionTypePageContentHeader(it.getName());
	}
	
	private void editInspectionProofTestTypes(List<String> proofTestTypes) throws Exception {
		assertNotNull(proofTestTypes);
		TableRows trs = ie.rows(editSupportedProofTestTypeFinder);
		assertNotNull("Could not find the table of supported proof test types", trs);
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			String proofTestType = tr.cell(0).text().trim();
			Radio on = tr.radio(value("true"));
			Radio off = tr.radio(value("false"));
			if(proofTestTypes.contains(proofTestType)) {
				on.set();
			} else {
				off.set();
			}
		}
	}

	public void gotoInspectionForm(String inspectionType) throws Exception {
		Link inspectionFormLink = ie.link(inspectionFormLinkFinder);
		assertTrue("Could not find the Inspection Form link", inspectionFormLink.exists());
		inspectionFormLink.click();
		checkInspectionFormPageContentHeader(inspectionType);
	}

	private void checkInspectionFormPageContentHeader(String inspectionType) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(inspectionFormPageContentHeaderFinder);
		assertTrue("Could not find the content header on Inspection Form page.", contentHeader.exists());
		String header = "Manage Inspection Type - " + inspectionType;
		assertEquals("Was expecting '" + header + "' but found '" + contentHeader.text().trim() + "'", header, contentHeader.text().trim());
	}
	
	public void gotoInspectionTypesListFromInspectionTypeInformation() throws Exception {
		Link inspectionTypeListLink = ie.link(inspectionTypeListLinkFinder);
		assertTrue("Could not find the link to Inspection Type List", inspectionTypeListLink.exists());
		inspectionTypeListLink.click();
		checkInspectionTypesPageContentHeader();
	}
	
	public String getEventTypeGroupForInspectionType() throws Exception {
		Link group = getLinkToGroup();
		String groupName = group.text();
		return groupName;
	}
	
	private Link getLinkToGroup() throws Exception {
		Link group = ie.link(inspectionTypeGroupLinkFinder);
		assertTrue("Could not find a link to the Group for inspection type.", group.exists());
		return group;
	}
	
	public void gotoEventTypeGroupInspectionTypeInformation() throws Exception {
		ManageEventTypeGroups metgs = new ManageEventTypeGroups(ie);
		Link group = getLinkToGroup();
		String groupName = group.text();
		group.click();
		metgs.checkEventTypeGroupPageContentHeader(groupName);
	}
	
	/**
	 * This method will return an InspectionType object. It assumes you
	 * are on Edit Inspection Type. The Inspection Type Information page
	 * does not have all the information on it (see WEB-911) so I use the
	 * edit page to get all the information.
	 * 
	 * @return
	 * @throws Exception
	 */
	public InspectionType getInspectionType() throws Exception {
		InspectionType it = null;
		String name = getInspectionTypeName();
		assertNotNull(name);
		assertFalse("Name came back as empty string.", name.equals(""));
		String group = getInspectionTypeGroup();
		assertNotNull(group);
		assertFalse("Group came back as empty string.", group.equals(""));
		it = new InspectionType(name, group);
		it.setPrintable(isPrintable());
		it.setMasterInspection(isMasterInspection());
		List<String> proofTestTypes = new ArrayList<String>();
		List<String> inspectionAttributes = new ArrayList<String>();
		Collection<String> c = getProofTestTypes();
		proofTestTypes.addAll(c);
		if(proofTestTypes.size() > 0) {
			it.setProofTestTypes(proofTestTypes);
		}
		c = getInspectionAttributes();
		inspectionAttributes.addAll(c);
		if(inspectionAttributes.size() > 0) {
			it.setInspectionAttributes(inspectionAttributes);
		}
		
		return it;
	}

	private boolean isMasterInspection() throws Exception {
		boolean result = false;
		Checkbox master = ie.checkbox(editInspectionTypeMasterInspectionFinder);
		assertTrue("Could not find the printable information", master.exists());
		result = Boolean.parseBoolean(master.value());
		return result;
	}

	private boolean isPrintable() throws Exception {
		boolean result = false;
		Checkbox printable = ie.checkbox(editInspectionTypePrintableFinder);
		assertTrue("Could not find the printable information", printable.exists());
		result = Boolean.parseBoolean(printable.value());
		return result;
	}

	private String getInspectionTypeGroup() throws Exception {
		String group = null;
		SelectList inspectionTypeGroup = ie.selectList(editInspectionTypeGroupFinder);
		assertTrue("Could not find the inspection type group", inspectionTypeGroup.exists());
		List<String> selected = inspectionTypeGroup.getSelectedItems();
		assertNotNull(selected);
		assertTrue("Could not find a selected item in the Group list", selected.size() > 0);
		group = selected.get(0).trim();
		return group;
	}

	private String getInspectionTypeName() throws Exception {
		String name = null;
		TextField inspectionTypeName = ie.textField(editInspectionTypeNameFinder);
		assertTrue("Could not find the inspection type name", inspectionTypeName.exists());
		name = inspectionTypeName.value().trim();
		return name;
	}

	private Collection<String> getInspectionAttributes() throws Exception {
		Collection<String> result = new ArrayList<String>();
		TextFields attributes = ie.textFields(editInspectionAttributesFinder);
		assertNotNull(attributes);
		if(attributes.length() > 0) {
			Iterator<TextField> i = attributes.iterator();
			while(i.hasNext()) {
				TextField tf = i.next();
				String s = tf.value().trim();
				result.add(s);
			}
		}
		return result;
	}

	private Collection<String> getProofTestTypes() throws Exception {
		Collection<String> result = new ArrayList<String>();
		TableRows proofTestTypes = ie.rows(editSupportedProofTestTypeFinder);
		assertNotNull(proofTestTypes);
		if(proofTestTypes.length() > 0) {
			Iterator<TableRow> i = proofTestTypes.iterator();
			while(i.hasNext()) {
				TableRow tr = i.next();
				String s = tr.cell(0).text().trim();
				if(tr.cell(1).radio(0).checked()) {
					result.add(s);
				}
			}
		}
		
		return result;
	}
	
	public void validate() throws Exception {
		gotoManageInspectionTypes();
		FieldIDMisc misc = new FieldIDMisc(ie);
		misc.gotoBackToAdministration();
		ManageEventTypeGroups metgs = new ManageEventTypeGroups(ie);
		metgs.gotoManageEventTypeGroups();
		List<String> groups = metgs.getEventTypeGroups();
		misc.gotoBackToAdministration();
		gotoManageInspectionTypes();
		@SuppressWarnings("unused")
		List<String> mits = getMasterInspectionTypes();
		List<String> its = getInspectionTypes();
		String inspectionType = its.get(0);						// assumes there is at least one inspection type
		gotoInspectionType(inspectionType);
		getEventTypeGroupForInspectionType();
		gotoViewAll();
		gotoEditInspectionTypeFromInspectionTypesPage(inspectionType);
		InspectionType it = getInspectionType();
		editInspectionType(it);
		gotoEditInspectionTypeFromInspectionTypeInformation();
		List<String> proofTestTypes = getSupportedProofTestTypes();
		gotoInspectionForm(inspectionType);
		gotoManageButtonGroups();
		List<String> bg = getButtonGroupNames();
		gotoImDoneFromManageButtonGroups();
		inspectionType = "master" + misc.getRandomString(17);	// no particular reasons for limit of 17 characters
		it = new InspectionType(inspectionType);
		it.setMasterInspection(true);
		it.setPrintable(true);
		it.setProofTestTypes(proofTestTypes);
		it.setGroup(groups.get(0));								// assumes there is at least in Event Type Group
		gotoAddInspectionType();
		addInspectionType(it);
		gotoEditInspectionTypeFromInspectionTypeInformation();
		gotoViewInspectionTypeFromEditInspectionType();
		gotoInspectionTypesListFromInspectionTypeInformation();
		gotoInspectionType(inspectionType);
		gotoEventTypeGroupInspectionTypeInformation();
		misc.gotoBackToAdministration();
		gotoManageInspectionTypes();
		gotoInspectionType(inspectionType);
		gotoInspectionForm(inspectionType);
		InspectionForm form = new InspectionForm();
		// because waitForJavascript takes 5 seconds, the time for creating a form is:
		// numSections * 5 * ((numCriteria + numRecommendations + numDeficiencies) * 5)
		// So I am limiting numSections to 2, numCriteria to 2 and numDeficienies to 2
		// but numRecommendation will be limited to 2. Thus worst case scenario is:
		// 3 * 5 * ((3 + 3 + 3) * 5) or 11 minutes 15 seconds.
		int maxSections = 2;
		int maxCriteria = 2;
		int maxRecommendations = 2;
		int maxDeficiencies = 2;
		int numSections = misc.getRandomInteger(1,maxSections);
		for(int i = 0; i < numSections; i++) {
			Section s = new Section("Section #" + i);
			int numCriteria = misc.getRandomInteger(1,maxCriteria);
			for(int j = 0; j < numCriteria; j++) {
				int n = misc.getRandomInteger(bg.size());
				ButtonGroup buttons = new ButtonGroup(bg.get(n));
				buttons.setSetsResult(true);
				Criteria c = new Criteria("Criteria #" + j, buttons);
				int k;
				int numRecommendations = misc.getRandomInteger(1,maxRecommendations);
				for(k = 0; k < numRecommendations ; k++) {
					c.addRecommendation("Rec #" + k);
				}
				int numDeficiencies = misc.getRandomInteger(1,maxDeficiencies);
				for(k = 0; k < numDeficiencies ; k++) {
					c.addDeficiency("Def #" + k);
				}
				s.addCriteria(c);
			}
			form.addSection(s);
		}
		addInspectionForm(form);
		saveInspectionForm();
//		this.isInspectionType(type);
	}
	
	public void gotoViewInspectionTypeFromEditInspectionType() throws Exception {
		Link viewInspectionType = ie.link(viewInspectionTypeFinder);
		assertTrue("Could not find the View Inspection Type link", viewInspectionType.exists());
		viewInspectionType.click();
		checkViewInspectionTypePageContentHeader();
	}

	private void checkViewInspectionTypePageContentHeader() throws Exception {
		checkEditInspectionTypePageContentHeader();	// currently same header for all the pages
	}

	public void gotoImDoneFromManageButtonGroups() throws Exception {
		Button done = ie.button(manageButtonGroupsImDoneButtonFinder);
		assertTrue("Could not find the 'Im Done' button", done.exists());
		done.click();
		this.checkInspectionFormPageContentHeader();
	}

	public void checkInspectionFormPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(manageInspectionTypePageContentHeaderFinder);
		assertTrue("Could not find the content header on Manage Inspection Type page.", contentHeader.exists());
	}

	public void gotoViewAll() throws Exception {
		Link viewAll = ie.link(viewAllLinkFinder);
		assertTrue("Could not find a link to go back to the list of inspection types", viewAll.exists());
		viewAll.click();
		checkInspectionTypesPageContentHeader();
	}

	public List<String> getSupportedProofTestTypes() throws Exception {
		List<String> results = new ArrayList<String>();
		TableCells tds = ie.cells(supportedProofTestTypesFromEditInspectionTypeFinder);
		assertNotNull(tds);
		Iterator<TableCell> i = tds.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			String ptt = td.text().trim();
			results.add(ptt);
		}
		return results;
	}

	public void gotoManageButtonGroups() throws Exception {
		Link manageButtonGroups = ie.link(manageButtonGroupsLinkFinder);
		assertTrue("Could not find the 'manage' link to the Button Groups", manageButtonGroups.exists());
		manageButtonGroups.click();
		checkButtonSetupPageContentHeader();
	}

	public boolean isInspectionType(String it) throws Exception {
		boolean result = false;
		Links inspectionTypes = getInspectionTypeLinks();
		Link inspectionType = inspectionTypes.link(text(it));
		result = inspectionType.exists();
		return result;
	}
	
	public void addInspectionForm(InspectionForm form) throws Exception {
		assertNotNull(form);
		FieldIDMisc.stopMonitor();
		int sections = form.getNumberOfSections();
		FieldIDMisc misc = new FieldIDMisc(ie);
		for(int i = 0; i < sections; i++) {
			Section s = form.getSection(i);
			assertNotNull(s);
			String sectionNameString = s.getSectionName();
			Button addSection = ie.button(inspectionFormAddSectionButtonFinder);
			assertTrue("Could not find the Add Section button", addSection.exists());
			addSection.click();
			misc.waitForJavascript();
			String sectionID = "criteriaSections_" + i + "__title";
			TextField sectionName = ie.textField(id(sectionID));
			assertTrue("Could not find the section field for section #" + i, sectionName.exists());
			sectionName.set(sectionNameString);
			int criterias = s.getNumberOfCriteria();
			for(int j = 0; j < criterias; j++) {
				Criteria c = s.getCriteria(j);
				assertNotNull(c);
				String criteriaName = c.getCriteriaName();
				Finder inspectionFormAddCriteriaButtonFinder = xpath("//DIV[@id='criteriaSection_" + i + "']/H2[@class='criteriaSectionHead']/SPAN[@class='addCriteriaButton']/BUTTON[contains(text(),'Add Criteria')]");
				Button addCriteria = ie.button(inspectionFormAddCriteriaButtonFinder);
				assertTrue("Could not find the Add Criteria button", addCriteria.exists());
				addCriteria.click();
				misc.waitForJavascript();
				String criteriaID = "criteriaSections_" + i + "__criteria_" + j + "__displayText";
				TextField criteriaLabel = ie.textField(id(criteriaID));
				assertTrue("Could not find the criteria field for criteria #" + j, criteriaLabel.exists());
				criteriaLabel.set(criteriaName);
				ButtonGroup bg = c.getButtonGroup();
				String buttonGroupName = bg.getButtonGroup();
				String buttonGroupID = "criteriaSections_" + i + "__criteria_" + j + "__states_iD";
				SelectList buttonGroup = ie.selectList(id(buttonGroupID));
				assertTrue("Could not find the button group selector for section #" + i + ", criteria #" + j, buttonGroup.exists());
				Option o = buttonGroup.option(text(buttonGroupName));
				assertTrue("Could not find the button group '" + buttonGroupName + " in the list of button groups", o.exists());
				o.select();
				boolean setsResult = bg.getSetsResults();
				Finder inspectionFormSetsResultCheckboxFinder = id("criteriaSections_" + i + "__criteria_" + j + "__principal");
				Checkbox sr = ie.checkbox(inspectionFormSetsResultCheckboxFinder );
				assertTrue("Could not find the Sets Result checkbox for section #" + i + ", criteria #" + j, sr.exists());
				sr.set(setsResult);
				String expandObservationsID = "obs_open_" + i + "_" + j;
				Link expandObservationButton = ie.link(id(expandObservationsID));
				assertTrue("Could not find the Observations expander for section #" + i + ", criteria #" + j, expandObservationButton.exists());
				expandObservationButton.click();
				int k, recommendations = c.getNumberOfRecommendations();
				for(k = 0; k < recommendations; k++) {
					String r = c.getRecommendation(k);
					String addRecommendationID = "addRecommendation_" + i + "_" + j;
					Button addRecommendation = ie.button(id(addRecommendationID));
					assertTrue("Could not find the Add Recommendations button for section #" + i + ", criteria #" + j, addRecommendation.exists());
					addRecommendation.click();
					misc.waitForJavascript();
					String recommendationID = "criteriaSections_" + i + "__criteria_" + j + "__recommendations_" + k + "_";
					TextField rec = ie.textField(id(recommendationID));
					assertTrue("Could not find Recommendation field for section #" + i + ", criteria #" + j + ", recommendation #" + k, rec.exists());
					rec.set(r);
				}
				int deficiencies = c.getNumberOfDeficiencies();
				for(k = 0; k < deficiencies; k++) {
					String d = c.getDeficiency(k);
					String addDeficiencyID = "addDeficiencies_" + i + "_" + j;
					Button addDeficiency = ie.button(id(addDeficiencyID));
					assertTrue("Could not find the Add Deficiency button for section #" + i + ", criteria #" + j, addDeficiency.exists());
					addDeficiency.click();
					misc.waitForJavascript();
					String deficiencyID = "criteriaSections_" + i + "__criteria_" + j + "__deficiencies_" + k + "_";
					TextField def = ie.textField(id(deficiencyID));
					assertTrue("Could not find Deficiency field for section #" + i + ", criteria #" + j + ", deficiency #" + k, def.exists());
					def.set(d);
				}
			}
		}
		FieldIDMisc.startMonitor();
	}
	
	public void saveInspectionForm() throws Exception {
		Button save = ie.button(inspectionFormSaveButtonFinder);
		assertTrue("Could not find the Save button for Inspection Form", save.exists());
		save.click();
		ie.waitUntilReady();
		FieldIDMisc misc = new FieldIDMisc(ie);
		misc.checkForErrorMessagesOnCurrentPage();
		String msg = misc.getSuccessMessageOnCurrentPage();
		String expected = "";
		assertNotSame("Got '" + msg + "' but expected '" + expected + "'", expected, msg);
	}
	
	public List<String> getButtonGroupNames() throws Exception {
		List<String> results = new ArrayList<String>();
		TextFields names = ie.textFields(buttonGroupNamesFieldFinder);
		assertNotNull(names);
		Iterator<TextField> i = names.iterator();
		while(i.hasNext()) {
			TextField name = i.next();
			String value = name.value().trim();
			results.add(value);
		}
		return results;
	}

	public InspectionForm getInspectionForm() throws Exception {
		InspectionForm form = null;
		Divs sections = ie.divs(inspectionFormSectionNameFinder);
		if(sections.length() != 0) {
			form = new InspectionForm();
			Iterator<Div> i = sections.iterator();
			while(i.hasNext()) {
				Div section = i.next();
				assertNotNull(section);
				TextField sectionNameField = section.textField(xpath("H2[@class='criteriaSectionHead']/SPAN[@class='criteriaSectionTitle']/INPUT[1]"));
				assertTrue("Could not find a Section Name field", sectionNameField.exists());
				String sectionName = sectionNameField.value().trim();
				Section s = new Section(sectionName);
				Divs criterias = section.divs(xpath("DIV/DIV[@class='criteriaGroup']/DIV[@class='criteriaContainer']"));
				assertNotNull(criterias);
				Iterator<Div> j = criterias.iterator();
				while(j.hasNext()) {
					Div criteria = j.next();
					TextField criteriaLabelField = criteria.textField(xpath("H4/INPUT[1]"));
					String criteriaLabel = criteriaLabelField.value().trim();
					Span buttonGroupField = criteria.span(xpath("DIV[@class='criteriaContent']/SPAN/SPAN[@class='criteraStateName']"));
					String buttonGroup = buttonGroupField.text().trim();
					ButtonGroup bg = new ButtonGroup(buttonGroup);
					Checkbox sets = buttonGroupField.checkbox(xpath("../../SPAN[@class='criteriaContentLabel']/INPUT"));
					if(sets.isSet()) {
						bg.setSetsResult(true);
					}
					Criteria c = new Criteria(criteriaLabel, bg);
					TextFields recommendations = criteria.textFields(xpath("DIV[@class='criteriaContent']/DIV[@class='observationsContainer']/DIV/DIV[1]/DIV/INPUT"));
					Iterator<TextField> k = recommendations.iterator();
					while(k.hasNext()) {
						TextField recommendation = k.next();
						String rec = recommendation.value().trim();
						c.addRecommendation(rec);
					}
					TextFields deficiencies = criteria.textFields(xpath("DIV[@class='criteriaContent']/DIV[@class='observationsContainer']/DIV/DIV[2]/DIV/INPUT"));
					Iterator<TextField> m = deficiencies.iterator();
					while(m.hasNext()) {
						TextField deficiency = m.next();
						String def = deficiency.value().trim();
						c.addDeficiency(def);
					}
					s.addCriteria(c);
				}
				form.addSection(s);
			}
		}
		
		return form;
	}

	public List<String> getStandardInspectionTypes() throws Exception {
		List<String> result = new ArrayList<String>();
		TableCells inspectionTypeTypes = ie.cells(inspectionTypesTypeFinder);
		assertNotNull(inspectionTypeTypes);
		Iterator<TableCell> i = inspectionTypeTypes.iterator();
		while(i.hasNext()) {
			TableCell td = i.next();
			if(td.text().contains("Standard")) {
				Link l = td.link(xpath("../TD[1]/A"));
				result.add(l.text().trim());
			}
		}
		return result;
	}

	public void gotoAddButtonGroup() throws Exception {
		Button addGroup = ie.button(addNewButtonGroupButtonFinder);
		assertTrue("Could not find a button to add a new button group", addGroup.exists());
		addGroup.click();
		checkButtonSetupPageContentHeader();
	}

	private void checkButtonSetupPageContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(buttonGroupPageContentHeaderFinder);
		assertTrue("Could not find the page content header for managing button groups", header.exists());
	}
}
