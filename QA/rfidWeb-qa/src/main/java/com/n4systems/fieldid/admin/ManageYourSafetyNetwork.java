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

import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.FieldIDMisc;

import watij.elements.Button;
import watij.elements.Checkbox;
import watij.elements.Div;
import watij.elements.HtmlElement;
import watij.elements.Label;
import watij.elements.Labels;
import watij.elements.Link;
import watij.elements.Links;
import watij.elements.Span;
import watij.elements.Spans;
import watij.elements.TableCell;
import watij.elements.TableCells;
import watij.elements.TableRow;
import watij.elements.TableRows;
import watij.elements.TextField;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageYourSafetyNetwork extends TestCase {
	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "manageyoursafetynetwork.properties";
	FieldIDMisc misc;
	ManageProductTypes mpts;
	ManageInspectionTypes mits;
	Finder manageYourSafetyNetworkFinder;
	Finder manageYourSafetyNetworkContentHeaderFinder;
	Finder companyFIDACFinder;
	private Finder safetyNetworkLinkedCompanyNamesFinder;
	private Finder FieldIDAccessCodeTextFieldFinder;
	private Finder linkToAnotherCompanyButtonFinder;
	private Finder manageYourSafetyNetworkPublishLinkFinder;
	private Finder manageSafetyNetworkPublishAllLinkFinder;
	private Finder manageSafetyNetworkPublishNoneLinkFinder;
	private Finder assetTypeLabelFinder;
	private Finder eventTypeLabelFinder;
	private Finder publishButtonFinder;
	private Finder importCatalogLinkFinder;
	private Finder selectItemsToImportButtonFinder;
	private Finder step1BodyFinder;
	private Finder backtoStep1LinkFinder;
	private Finder step2BodyFinder;
	private Finder customizeYourImportSelectionLinkFinder;
	private Finder cancelCatalogImportButtonFinder;
	private Finder step2SelectItemsSelectAllLinkFinder;
	private Finder step2SelectItemsSelectNoneLinkFinder;
	private Finder assetTypesStep2SelectItemsFinder;
	private Finder importAssetTypeSelectFinder;
	private Finder gotoStep3ButtonFinder;
	private Finder backtoStep2LinkFinder;
	private Finder step3BodyFinder;
	private Finder step2CustomizeImportSelectAllLinkFinder;
	private Finder step2CustomizeImportSelectNoneLinkFinder;
	private Finder assetTypesStep2CustomizeImportFinder;
	private Finder eventTypesStep2CustomizeImportFinder;
	private Finder gotoStep4ButtonFinder;
	private Finder returnToSafetyNetworkButtonFinder;
	private Finder step3ViewDetailsLinkFinder;
	private Finder renamedElementsTableRowsFinder;
	private Finder step4YouAreDoneMessageFinder;

	public ManageYourSafetyNetwork(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			mpts = new ManageProductTypes(ie);
			mits = new ManageInspectionTypes(ie);
			manageYourSafetyNetworkFinder = text(p.getProperty("link"));
			manageYourSafetyNetworkContentHeaderFinder = xpath(p.getProperty("contentheader"));
			companyFIDACFinder = xpath(p.getProperty("companyfidac"));
			safetyNetworkLinkedCompanyNamesFinder = xpath(p.getProperty("companynamecells"));
			FieldIDAccessCodeTextFieldFinder = xpath(p.getProperty("fieldidaccesscodetextfield"));
			linkToAnotherCompanyButtonFinder = xpath(p.getProperty("linktoanothercompanybutton"));
			manageYourSafetyNetworkPublishLinkFinder = xpath(p.getProperty("publishlink"));
			manageSafetyNetworkPublishAllLinkFinder = xpath(p.getProperty("setalllinkpublish"));
			manageSafetyNetworkPublishNoneLinkFinder = xpath(p.getProperty("setnonelinkpublish"));
			assetTypeLabelFinder = xpath(p.getProperty("assettypelabels"));
			eventTypeLabelFinder = xpath(p.getProperty("eventtypelabels"));
			publishButtonFinder = xpath(p.getProperty("publishbutton"));
			importCatalogLinkFinder = xpath(p.getProperty("importcataloglinks"));
			selectItemsToImportButtonFinder = xpath(p.getProperty("selectitemstoimportbutton"));
			step1BodyFinder = xpath(p.getProperty("step1bodyfinder"));
			backtoStep1LinkFinder = xpath(p.getProperty("backtostep1link"));
			step2BodyFinder = xpath(p.getProperty("step2bodyfinder"));
			customizeYourImportSelectionLinkFinder = xpath(p.getProperty("customizeyourselectionlink"));
			cancelCatalogImportButtonFinder = xpath(p.getProperty("cancelcatalogimportbutton"));
			step2SelectItemsSelectAllLinkFinder = xpath(p.getProperty("selectitemsselectall"));
			step2SelectItemsSelectNoneLinkFinder = xpath(p.getProperty("selectitemsselectnone"));
			assetTypesStep2SelectItemsFinder = xpath(p.getProperty("assettypesstep2selectitems"));
			importAssetTypeSelectFinder = xpath(p.getProperty("importassettypeselection"));
			gotoStep3ButtonFinder = xpath(p.getProperty("gotostep3button"));
			backtoStep2LinkFinder = xpath(p.getProperty("backtostep2link"));
			step3BodyFinder = xpath(p.getProperty("step3bodyfinder"));
			step2CustomizeImportSelectAllLinkFinder = xpath(p.getProperty("customizeimportselectall"));
			step2CustomizeImportSelectNoneLinkFinder = xpath(p.getProperty("customizeimportselectnone"));
			assetTypesStep2CustomizeImportFinder = xpath(p.getProperty("assettypesstep2customizeimport"));
			eventTypesStep2CustomizeImportFinder = xpath(p.getProperty("eventtypesstep2customizeimport"));
			gotoStep4ButtonFinder = xpath(p.getProperty("gotostep4button"));
			returnToSafetyNetworkButtonFinder = xpath(p.getProperty("returntosafetynetworkbutton"));
			step3ViewDetailsLinkFinder = xpath(p.getProperty("step3viewdetailslink"));
			renamedElementsTableRowsFinder = xpath(p.getProperty("step3viewdetailsrenamedelementsrows"));
			step4YouAreDoneMessageFinder = xpath(p.getProperty("step4youaredonemessage"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}

	public void gotoManageYourSafetyNetwork() throws Exception {
		Link link = ie.link(manageYourSafetyNetworkFinder);
		assertTrue("Could not find the link to the Manage Your Safety Network page", link.exists());
		link.click();
		checkManageSafetyNetworkPage();
	}

	private void checkManageSafetyNetworkPage() throws Exception {
		HtmlElement he = ie.htmlElement(manageYourSafetyNetworkContentHeaderFinder);
		assertTrue("Could not find the page header for Manage Your Safety Network", he.exists());
	}

	public String getFIDAC() throws Exception {
		Span companyFIDAC = ie.span(companyFIDACFinder);
		assertTrue("Could not find the Field ID Access Code", companyFIDAC.exists());
		return companyFIDAC.text();
	}

	/**
	 * Assumes the FIDAC provided is a company who is NOT linked to the current company.
	 * This is part one of two for validating Manage Your Safety Network. Half the methods
	 * are for a Manufacturer to publish there catalog to their tenants. This tests that
	 * part of the class.
	 * 
	 * validateTenant(String companyName) will validate part two of two, i.e. the importing
	 * of the published manufacturer information.
	 * 
	 * @param FIDAC
	 * @throws Exception
	 */
	public void validateManufacturer(String FIDAC) throws Exception {
		gotoManageYourSafetyNetwork();
		String s = getFIDAC();
		List<String> linkedCompanyNames = getLinkedCompanyNames();
		linkToAnotherCompany(FIDAC);
		gotoPublish();
		setAllPublish();
		setNonePublish();
		List<String> assetTypes = getAssetTypes();
		List<String> eventTypes = getEventTypes();
		setAssetTypes(assetTypes);
		setEventTypes(eventTypes);
		publish();
	}

	public void publish() throws Exception {
		Button publish = ie.button(publishButtonFinder);
		assertTrue("Could not find the Publish button", publish.exists());
		publish.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageSafetyNetworkPage();
	}

	public void setEventTypes(List<String> eventTypes) throws Exception {
		assertNotNull(eventTypes);
		Iterator<String> i = eventTypes.iterator();
		while(i.hasNext()) {
			String eventType = i.next();
			setEventType(eventType);
		}
	}

	public void setEventType(String eventType) throws Exception {
		Finder eventTypeCheckboxFinder = xpath("//H3[contains(text(),'Event Types')]/../DIV/LABEL[contains(text(),'" + eventType + "')]/../SPAN/INPUT[@type='checkbox']");
		Checkbox event = ie.checkbox(eventTypeCheckboxFinder);
		assertTrue("Could not find the asset type '" + eventType + "'", event.exists());
		event.set(true);
	}

	public void setAssetTypes(List<String> assetTypes) throws Exception {
		assertNotNull(assetTypes);
		Iterator<String> i = assetTypes.iterator();
		while(i.hasNext()) {
			String assetType = i.next();
			setAssetType(assetType);
		}
	}

	public void setAssetType(String assetType) throws Exception {
		Finder assetTypeCheckboxFinder = xpath("//H3[contains(text(),'Asset Types')]/../DIV/LABEL[contains(text(),'" + assetType + "')]/../SPAN/INPUT[@type='checkbox']");
		Checkbox asset = ie.checkbox(assetTypeCheckboxFinder);
		assertTrue("Could not find the asset type '" + assetType + "'", asset.exists());
		asset.set(true);
	}

	public List<String> getEventTypes() throws Exception {
		List<String> eventTypes = new ArrayList<String>();
		Labels events = ie.labels(eventTypeLabelFinder);
		assertNotNull(events);
		Iterator<Label> i = events.iterator();
		while(i.hasNext()) {
			Label event = i.next();
			eventTypes.add(event.text().trim());
		}
		return eventTypes;
	}

	public List<String> getAssetTypes() throws Exception {
		List<String> assetTypes = new ArrayList<String>();
		Labels assets = ie.labels(assetTypeLabelFinder);
		assertNotNull(assets);
		Iterator<Label> i = assets.iterator();
		while(i.hasNext()) {
			Label asset = i.next();
			assetTypes.add(asset.text().trim());
		}
		return assetTypes;
	}

	public void setNonePublish() throws Exception {
		Link none = ie.link(manageSafetyNetworkPublishNoneLinkFinder);
		assertTrue("Could not find the link to set no types for publishing", none.exists());
		none.click();
	}

	public void setAllPublish() throws Exception {
		Link all = ie.link(manageSafetyNetworkPublishAllLinkFinder);
		assertTrue("Could not find the link to set all types for publishing", all.exists());
		all.click();
	}

	public void gotoPublish() throws Exception {
		Link l = ie.link(manageYourSafetyNetworkPublishLinkFinder);
		assertTrue("Could not find the link to Publish", l.exists());
		l.click();
		checkManageSafetyNetworkPage();
	}

	public void linkToAnotherCompany(String fidac) throws Exception {
		TextField tf = ie.textField(FieldIDAccessCodeTextFieldFinder);
		assertTrue("Could not find the text field for linking a company via Field ID Access Code", tf.exists());
		tf.set(fidac);
		Button link = ie.button(linkToAnotherCompanyButtonFinder);
		assertTrue("Could not find the button to link to another company", link.exists());
		link.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageSafetyNetworkPage();
	}

	public List<String> getLinkedCompanyNames() throws Exception {
		List<String> names = new ArrayList<String>();
		TableCells companyNames = getcompanyNames();
		Iterator<TableCell> i = companyNames.iterator();
		while(i.hasNext()) {
			TableCell companyName = i.next();
			String s = companyName.text().trim();
			names.add(s);
		}
		return names;
	}
	
	private TableCells getcompanyNames() throws Exception {
		TableCells companyNames = ie.cells(safetyNetworkLinkedCompanyNamesFinder);
		assertNotNull(companyNames);
		return companyNames;
	}

	public void validateTenant(String companyName) throws Exception {
		gotoManageYourSafetyNetwork();
		List<String> catalogs = getLinkedCompanyWithPublishedCatalogs();
		assertTrue("Could not find the Import Catalog option for the company '" + companyName + "'", catalogs.contains(companyName));
		gotoImportCatalog(companyName);
		gotoCancelCatalogImport();
		gotoImportCatalog(companyName);
		misc.stopMonitorStatus();
		gotoStep2SelectItemsToImport();
		step2SelectItemsSelectAll();
		step2SelectItemsSelectNone();
		List<String> assetTypes = getAssetTypesFromStep2SelectItems();
		setAssetTypesForStep2SelectItems(assetTypes);
		viewDetailsForAssetTypesForStep2SelectItems(assetTypes);
		gotoStep3();
		viewDetailsForStep3();
		goBackToStep2();
		goBackToStep1();
		gotoStep2CustomizeYourImportSelection();
		step2CustomizeImportSelectAll();
		step2CustomizeImportSelectNone();
		assetTypes = getAssetTypesFromStep2CustomizeImport();
		List<String> eventTypes = getEventTypesFromStep2CustomizeImport();
		setAssetEventTypesForStep2CustomizeImport(assetTypes);
		setAssetEventTypesForStep2CustomizeImport(eventTypes);
		gotoStep3();
		viewDetailsForStep3();
		List<String> renames = getStep3DetailsOnRenaming();
		gotoStep4();
		String s = getStep4YouAreDoneMessage();
		gotoReturnToFieldIDSafetyNetwork();
		misc.startMonitorStatus();
		List<String> importedInspectionTypes = new ArrayList<String>();
		List<String> importedProductTypes = new ArrayList<String>();
		Iterator<String> a = assetTypes.iterator();
		while(a.hasNext()) {
			String temp = a.next();
			importedProductTypes.add(temp);
		}
		Iterator<String> i = eventTypes.iterator();
		while(i.hasNext()) {
			String temp = i.next();
			importedInspectionTypes.add(temp);
		}
		Iterator<String> r = renames.iterator();
		while(r.hasNext()) {
			String temp = r.next();
			String original = temp.split("'")[1];
			String renamed = temp.split("'")[3];
			if(importedProductTypes.contains(original)) {
				importedProductTypes.add(renamed);
			}
			if(importedInspectionTypes.contains(original)) {
				importedInspectionTypes.add(renamed);
			}
		}
		Thread.sleep(10*60*1000);	// sleep for 10 minutes
		
		misc.gotoBackToAdministration();
		mpts.gotoManageProductTypes();
		List<String> pts = mpts.getProductTypeNames();
		Iterator<String> j = importedProductTypes.iterator();
		while(j.hasNext()) {
			String pt = j.next();
			assertTrue("The imported product type '" + pt + "' did not import correctly.", pts.contains(pt));
		}
		misc.gotoBackToAdministration();
		mits.gotoManageInspectionTypes();
		List<String> its = mits.getInspectionTypes();
		Iterator<String> k = importedInspectionTypes.iterator();
		while(k.hasNext()) {
			String it = k.next();
			assertTrue("The imported inspect type '" + it + "' did not import correctly.", its.contains(it));
		}
	}

	public String getStep4YouAreDoneMessage() throws Exception {
		String s = null;
		HtmlElement p = ie.htmlElement(step4YouAreDoneMessageFinder);
		assertTrue("Could not find the message from step 4", p.exists());
		s = p.text();
		String now = misc.getDateString();
		String emailMessage = "An email will be sent to you once it is complete.";
		assertTrue("The message from step 4 should contain today's date but does not.", s.contains(now));
		assertTrue("The message from step 4 should tell you to expect an email.", s.contains(emailMessage));
		return s;
	}

	/**
	 * Assumes you can called viewDetailsForStep3().
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getStep3DetailsOnRenaming() throws Exception {
		List<String> results = new ArrayList<String>();
		TableRows renamedElements = ie.rows(renamedElementsTableRowsFinder);
		assertNotNull(renamedElements);
		Iterator<TableRow> i = renamedElements.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			TableCells tds = tr.cells();
			String s = "'" + tds.get(0).text().trim() + "' ";
			s += tds.get(1).text().trim();
			s += " '" + tds.get(2).text().trim() + "'";
			results.add(s);
		}
		return results;
	}

	public void viewDetailsForStep3() throws Exception {
		Link l = ie.link(step3ViewDetailsLinkFinder);
		assertTrue("Could not find a link to view details", l.exists());
		l.click();
	}

	public void gotoReturnToFieldIDSafetyNetwork() throws Exception {
		Button done = ie.button(returnToSafetyNetworkButtonFinder);
		assertTrue("Could not find the button to Return to Field ID Safety Network", done.exists());
		done.click();
		checkManageSafetyNetworkPage();
	}

	public void gotoStep4() throws Exception {
		Button cont = ie.button(gotoStep4ButtonFinder);
		assertTrue("Could not find the button to go to step 4", cont.exists());
		cont.click();
		misc.waitForJavascript();
		misc.checkForErrorMessagesOnCurrentPage();
		checkStep3Hidden();
		checkCancelCatalogImportDisabled();
		getStep4YouAreDoneMessage();
	}

	private void checkCancelCatalogImportDisabled() throws Exception {
		Button cancel = ie.button(cancelCatalogImportButtonFinder);
		assertTrue("Cancel Catalog Import is not disabled.", cancel.disabled());
	}

	public void setAssetEventTypesForStep2CustomizeImport(List<String> types) throws Exception {
		assertNotNull(types);
		assertFalse("Must be at least one asset/event type to set.", types.size() == 0);
		Div importSelection = ie.div(importAssetTypeSelectFinder);
		Iterator<String> i = types.iterator();
		while(i.hasNext()) {
			String type = i.next();
			Checkbox c = importSelection.checkbox(xpath("DIV[@class='customSelection']/DIV/LABEL[contains(text(),'" + type + "')]/../SPAN[@class='fieldHolder']/INPUT[@type='checkbox']"));
			assertTrue("Could not find the checkbox for asset/event type '" + type + "'", c.exists());
			c.set(true);
		}
	}

	public List<String> getEventTypesFromStep2CustomizeImport() throws Exception {
		List<String> eventTypes = new ArrayList<String>();
		Labels events = ie.labels(eventTypesStep2CustomizeImportFinder);
		assertNotNull(events);
		Iterator<Label> i = events.iterator();
		while(i.hasNext()) {
			Label event = i.next();
			String s = event.text().trim();
			eventTypes.add(s);
		}
		return eventTypes;
	}

	public List<String> getAssetTypesFromStep2CustomizeImport() throws Exception {
		List<String> assetTypes = new ArrayList<String>();
		Labels assets = ie.labels(assetTypesStep2CustomizeImportFinder);
		assertNotNull(assets);
		Iterator<Label> i = assets.iterator();
		while(i.hasNext()) {
			Label asset = i.next();
			String s = asset.text().trim();
			assetTypes.add(s);
		}
		return assetTypes;
	}

	public void step2CustomizeImportSelectNone() throws Exception {
		Link l = ie.link(step2CustomizeImportSelectNoneLinkFinder);
		assertTrue("Could not find the Select: None link on step 2", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void step2CustomizeImportSelectAll() throws Exception {
		Link l = ie.link(step2CustomizeImportSelectAllLinkFinder);
		assertTrue("Could not find the Select: All link on step 2", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void goBackToStep2() throws Exception {
		Link l = ie.link(backtoStep2LinkFinder);
		assertTrue("Could not find the link to go back to step 2", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkStep3Hidden();
	}

	private void checkStep3Hidden() throws Exception {
		Div step3 = ie.div(step3BodyFinder);
		assertTrue("Could not find the body of step 3", step3.exists());
		String html = step3.html();
		int i =  html.indexOf(">");
		String tag = html.substring(0, i).toLowerCase();
		assertTrue("Step 3 is not hidden", tag.contains("display: none"));
	}

	public void gotoStep3() throws Exception {
		Button cont = ie.button(gotoStep3ButtonFinder);
		assertTrue("Could not find the button to go to step 3", cont.exists());
		cont.click();
		misc.waitForLoading();
		misc.checkForErrorMessagesOnCurrentPage();
		checkStep2Hidden();
	}

	public void viewDetailsForAssetTypesForStep2SelectItems(List<String> assetTypes) throws Exception {
		assertNotNull(assetTypes);
		assertFalse("Must be at least one asset type to set.", assetTypes.size() == 0);
		Div importSelection = ie.div(importAssetTypeSelectFinder);
		Iterator<String> i = assetTypes.iterator();
		while(i.hasNext()) {
			String assetType = i.next();
			Link l = importSelection.link(xpath("UL/LI/SPAN[contains(text(),'" + assetType + "')]/../A/SPAN[contains(text(),'Details') and not(contains(text(),'Close Details'))]/.."));
			assertTrue("Could not find the Details link for asset type '" + assetType + "'", l.exists());
			l.click();
		}
	}

	public void setAssetTypesForStep2SelectItems(List<String> assetTypes) throws Exception {
		assertNotNull(assetTypes);
		assertFalse("Must be at least one asset type to set.", assetTypes.size() == 0);
		Div importSelection = ie.div(importAssetTypeSelectFinder);
		Iterator<String> i = assetTypes.iterator();
		while(i.hasNext()) {
			String assetType = i.next();
			Checkbox c = importSelection.checkbox(xpath("UL/LI/SPAN[contains(text(),'" + assetType + "')]/../SPAN[@class='fieldHolder']/INPUT[@type='checkbox']"));
			assertTrue("Could not find the checkbox for asset type '" + assetType + "'", c.exists());
			c.set(true);
		}
	}

	public List<String> getAssetTypesFromStep2SelectItems() throws Exception {
		List<String> assetTypes = new ArrayList<String>();
		Spans assets = ie.spans(assetTypesStep2SelectItemsFinder);
		assertNotNull(assets);
		Iterator<Span> i = assets.iterator();
		while(i.hasNext()) {
			Span asset = i.next();
			String s = asset.text().trim();
			assetTypes.add(s);
		}
		return assetTypes;
	}

	public void step2SelectItemsSelectNone() throws Exception {
		Link l = ie.link(step2SelectItemsSelectNoneLinkFinder);
		assertTrue("Could not find the Select: None link on step 2", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void step2SelectItemsSelectAll() throws Exception {
		Link l = ie.link(step2SelectItemsSelectAllLinkFinder);
		assertTrue("Could not find the Select: All link on step 2", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void gotoCancelCatalogImport() throws Exception {
		Button cancel = ie.button(cancelCatalogImportButtonFinder);
		assertTrue("Could not find the button to cancel the catalog import", cancel.exists());
		cancel.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkManageSafetyNetworkPage();
	}

	public void gotoStep2CustomizeYourImportSelection() throws Exception {
		Link l = ie.link(customizeYourImportSelectionLinkFinder);
		assertTrue("Could not find the customize your import selection link", l.exists());
		l.click();
		misc.waitForLoading();
		misc.checkForErrorMessagesOnCurrentPage();
		checkStep1Hidden();
	}

	public void goBackToStep1() throws Exception {
		Link l = ie.link(backtoStep1LinkFinder);
		assertTrue("Could not find the link to go back to step 1", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkStep2Hidden();
	}

	private void checkStep2Hidden() throws Exception {
		Div step2 = ie.div(step2BodyFinder);
		assertTrue("Could not find the body of step 2", step2.exists());
		String html = step2.html();
		int i =  html.indexOf(">");
		String tag = html.substring(0, i).toLowerCase();
		assertTrue("Step 2 is not hidden", tag.contains("display: none"));
	}

	public void gotoStep2SelectItemsToImport() throws Exception {
		Button selectItemsToImport = ie.button(selectItemsToImportButtonFinder);
		assertTrue("Could not find the button to select items to import", selectItemsToImport.exists());
		selectItemsToImport.click();
		misc.waitForLoading();
		misc.checkForErrorMessagesOnCurrentPage();
		checkStep1Hidden();
	}

	private void checkStep1Hidden() throws Exception {
		Div step1 = ie.div(step1BodyFinder);
		assertTrue("Could not find the body of step 1", step1.exists());
		String html = step1.html();
		int i =  html.indexOf(">");
		String tag = html.substring(0, i).toLowerCase();
		assertTrue("Step 1 is not hidden", tag.contains("display: none"));
	}

	public void gotoImportCatalog(String companyName) throws Exception {
		Links importCatalogs = ie.links(importCatalogLinkFinder);
		assertNotNull(importCatalogs);
		Iterator<Link> i = importCatalogs.iterator();
		while(i.hasNext()) {
			Link l = i.next();
			TableCell company = l.cell(xpath("../../TD[2]"));
			if(company.text().contains(companyName)) {
				l.click();
				misc.checkForErrorMessagesOnCurrentPage();
				checkManageSafetyNetworkPage();
				return;
			}
		}
		fail("Did not find the Import Catalog link for '" + companyName + "'");
	}

	/**
	 * Get a list of the Company Names who have published their catalog.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getLinkedCompanyWithPublishedCatalogs() throws Exception {
		List<String> results = new ArrayList<String>();
		TableCells companyNames = getcompanyNames();
		Iterator<TableCell> i = companyNames.iterator();
		while(i.hasNext()) {
			TableCell companyName = i.next();
			Link importCatalog = companyName.link(xpath("../TD[3]/A[contains(text(),'Import Catalog')]"));
			if(importCatalog.exists()) {
				String s = companyName.text().trim();
				results.add(s);
			}
		}
		return results;
	}
}
