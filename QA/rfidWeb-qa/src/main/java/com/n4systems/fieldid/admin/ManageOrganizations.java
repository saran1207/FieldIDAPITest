package com.n4systems.fieldid.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import com.n4systems.fieldid.*;
import com.n4systems.fieldid.datatypes.Organization;
import static watij.finders.FinderFactory.*;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.NotImplementedYetException;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageOrganizations extends TestCase {
	IE ie = null;
	Admin admin = null;
	FieldIDMisc misc = null;
	Properties p;
	InputStream in;
	String propertyFile = "manageorganizations.properties";
	Finder manageOrganizationsLinkFinder;
	Finder manageOrganizationsPageContentHeaderFinder;
	Finder backToAdministrationLinkFinder;
	Finder organizationNamesFinder;
	Finder addOrganizationLinkFinder;
	Finder addOrganizationNameFinder;
	Finder addOrganizationAdminEmailFinder;
	Finder addOrganizationNameOnCertFinder;
	Finder addOrganizationStreetAddressFinder;
	Finder addOrganizationCityFinder;
	Finder addOrganizationStateFinder;
	Finder addOrganizationCountryFinder;
	Finder addOrganizationZipFinder;
	Finder addOrganizationPhoneNumberFinder;
	Finder addOrganizationFaxFinder;
	Finder addOrganizationSaveButtonFinder;
	Finder addOrganizationCancelButtonFinder;
	Finder organizationTableRowsFinder;
	Finder manageOrganizationsPageForContentHeaderFinder;
	Finder editOrganizationNameFinder;
	Finder editOrganizationAdminEmailFinder;
	Finder editOrganizationNameOnCertFinder;
	Finder editOrganizationStreetAddressFinder;
	Finder editOrganizationCityFinder;
	Finder editOrganizationStateFinder;
	Finder editOrganizationCountryFinder;
	Finder editOrganizationZipFinder;
	Finder editOrganizationPhoneNumberFinder;
	Finder editOrganizationFaxFinder;
	Finder editOrganizationSaveButtonFinder;
	Finder editOrganizationCancelButtonFinder;

	public ManageOrganizations(IE ie) {
		this.ie = ie;
		try {
			admin = new Admin(ie);
			misc = new FieldIDMisc(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			manageOrganizationsLinkFinder = text(p.getProperty("link"));
			manageOrganizationsPageContentHeaderFinder = xpath(p.getProperty("contentheader"));
			backToAdministrationLinkFinder = xpath(p.getProperty("backtoadministrationlink"));
			organizationNamesFinder = xpath(p.getProperty("organizationnamecells"));
			addOrganizationLinkFinder = xpath(p.getProperty("addorganizationlink"));
			addOrganizationNameFinder = id(p.getProperty("addorganizationname"));
			addOrganizationAdminEmailFinder = id(p.getProperty("addorganizationadminemail"));
			addOrganizationNameOnCertFinder = id(p.getProperty("addorganizationnameoncert"));
			addOrganizationStreetAddressFinder = id(p.getProperty("addorganizationstreetaddress"));
			addOrganizationCityFinder = id(p.getProperty("addorganizationcity"));
			addOrganizationStateFinder = id(p.getProperty("addorganizationstate"));
			addOrganizationCountryFinder = id(p.getProperty("addorganizationcountry"));
			addOrganizationZipFinder = id(p.getProperty("addorganizationzip"));
			addOrganizationPhoneNumberFinder = id(p.getProperty("addorganizationphonenumber"));
			addOrganizationFaxFinder = id(p.getProperty("addorganizationfax"));
			addOrganizationSaveButtonFinder = id(p.getProperty("addorganizationsavebutton"));
			addOrganizationCancelButtonFinder = xpath(p.getProperty("addorganizationcancelbutton"));
			organizationTableRowsFinder = xpath(p.getProperty("organizationtablerows"));
			manageOrganizationsPageForContentHeaderFinder = xpath(p.getProperty("editorganizationpagecontentheader"));
			editOrganizationNameFinder = id(p.getProperty("editorganizationname"));
			editOrganizationAdminEmailFinder = id(p.getProperty("editorganizationadminemail"));
			editOrganizationNameOnCertFinder = id(p.getProperty("editorganizationnameoncert"));
			editOrganizationStreetAddressFinder = id(p.getProperty("editorganizationstreetaddress"));
			editOrganizationCityFinder = id(p.getProperty("editorganizationcity"));
			editOrganizationStateFinder = id(p.getProperty("editorganizationstate"));
			editOrganizationCountryFinder = id(p.getProperty("editorganizationcountry"));
			editOrganizationZipFinder = id(p.getProperty("editorganizationzip"));
			editOrganizationPhoneNumberFinder = id(p.getProperty("editorganizationphonenumber"));
			editOrganizationFaxFinder = id(p.getProperty("editorganizationfax"));
			editOrganizationSaveButtonFinder = id(p.getProperty("editorganizationsavebutton"));
			editOrganizationCancelButtonFinder = xpath(p.getProperty("editorganizationcancelbutton"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}

	public void gotoManageOrganizations() throws Exception {
		Link l = ie.link(manageOrganizationsLinkFinder);
		assertTrue("Could not find the link to go to Manage Organizations", l.exists());
		l.click();
		checkManageOrganizationsPageContentHeader();
	}

	private void checkManageOrganizationsPageContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(manageOrganizationsPageContentHeaderFinder);
		assertTrue("Could not find the page header for Manage Organizations", header.exists());
	}
	
	public void gotoBackToAdministration() throws Exception {
		Link l = ie.link(backToAdministrationLinkFinder);
		assertTrue("Could not find the link to go back to Administration", l.exists());
		l.click();
		admin.checkAdminPageContentHeader();
	}
	
	private TableRows getOrganizationTableRows() throws Exception {
		TableRows trs = ie.rows(organizationTableRowsFinder);
		assertNotNull("Could not find any rows with Organizations", trs);
		return trs;
	}
	
	public List<String> getOrganizationNames() throws Exception {
		List<String> results = new ArrayList<String>();
		TableRows trs = getOrganizationTableRows();
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			TableCell td = tr.cell(0);
			String name = td.text().trim();
			results.add(name);
		}
		assertTrue("There are less than one organizations", results.size() > 0);
		return results;
	}
	
	public void gotoAddOrganizationalUnit() throws Exception {
		Link l = ie.link(addOrganizationLinkFinder);
		assertTrue("Could not find a link to add an organization", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
		this.checkManageOrganizationsPageContentHeader();
	}
	
	public void setAddOrganizationForm(Organization o) throws Exception {
		assertNotNull(o);
		assertNotNull("Organization Name is required", o.getName());
		assertFalse("Organization Name cannot be blank", o.getName().equals(""));
		assertNotNull("Organization Administrator Email is required", o.getAdminEmail());
		assertFalse("Organization Administrator Email cannot be blank", o.getAdminEmail().equals(""));
		TextField name = ie.textField(addOrganizationNameFinder);
		assertTrue("Could not find the field for Name", name.exists());
		name.set(o.getName());
		TextField adminEmail = ie.textField(addOrganizationAdminEmailFinder);
		assertTrue("Could not find the field for Administrator Email", adminEmail.exists());
		adminEmail.set(o.getAdminEmail());
		TextField nameOnCert = ie.textField(addOrganizationNameOnCertFinder);
		assertTrue("Could not find the field for Name on certificate", nameOnCert.exists());
		if(o.getNameOnCert() != null) {
			nameOnCert.set(o.getNameOnCert());
		}
		TextField streetAddress = ie.textField(addOrganizationStreetAddressFinder);
		assertTrue("Could not find the field for Street Address", streetAddress.exists());
		if(o.getStreetAddress() != null) {
			streetAddress.set(o.getStreetAddress());
		}
		TextField city = ie.textField(addOrganizationCityFinder);
		assertTrue("Could not find the field for City", city.exists());
		if(o.getCity() != null) {
			city.set(o.getCity());
		}
		TextField state = ie.textField(addOrganizationStateFinder);
		assertTrue("Could not find the field for State", state.exists());
		if(o.getState() != null) {
			state.set(o.getState());
		}
		TextField country = ie.textField(addOrganizationCountryFinder);
		assertTrue("Could not find the field for Country", country.exists());
		if(o.getCountry() != null) {
			country.set(o.getCountry());
		}
		TextField zip = ie.textField(addOrganizationZipFinder);
		assertTrue("Could not find the field for Zip", zip.exists());
		if(o.getZip() != null) {
			zip.set(o.getZip());
		}
		TextField phoneNumber = ie.textField(addOrganizationPhoneNumberFinder);
		assertTrue("Could not find the field for Phone Number", phoneNumber.exists());
		if(o.getPhoneNumber() != null) {
			phoneNumber.set(o.getPhoneNumber());
		}
		TextField fax = ie.textField(addOrganizationFaxFinder);
		assertTrue("Could not find the field for Fax", fax.exists());
		if(o.getFax() != null) {
			fax.set(o.getFax());
		}
		if(o.getCertImage() != null) {
			throw new NotImplementedYetException();
		}
	}

	public void setEditOrganizationForm(Organization o) throws Exception {
		assertNotNull(o);
		assertNotNull("Organization Name is required", o.getName());
		assertFalse("Organization Name cannot be blank", o.getName().equals(""));
		assertNotNull("Organization Administrator Email is required", o.getAdminEmail());
		assertFalse("Organization Administrator Email cannot be blank", o.getAdminEmail().equals(""));
		TextField name = ie.textField(editOrganizationNameFinder);
		assertTrue("Could not find the field for Name", name.exists());
		name.set(o.getName());
		TextField adminEmail = ie.textField(editOrganizationAdminEmailFinder);
		assertTrue("Could not find the field for Administrator Email", adminEmail.exists());
		adminEmail.set(o.getAdminEmail());
		TextField nameOnCert = ie.textField(editOrganizationNameOnCertFinder);
		assertTrue("Could not find the field for Name on certificate", nameOnCert.exists());
		if(o.getNameOnCert() != null) {
			nameOnCert.set(o.getNameOnCert());
		}
		TextField streetAddress = ie.textField(editOrganizationStreetAddressFinder);
		assertTrue("Could not find the field for Street Address", streetAddress.exists());
		if(o.getStreetAddress() != null) {
			streetAddress.set(o.getStreetAddress());
		}
		TextField city = ie.textField(editOrganizationCityFinder);
		assertTrue("Could not find the field for City", city.exists());
		if(o.getCity() != null) {
			city.set(o.getCity());
		}
		TextField state = ie.textField(editOrganizationStateFinder);
		assertTrue("Could not find the field for State", state.exists());
		if(o.getState() != null) {
			state.set(o.getState());
		}
		TextField country = ie.textField(editOrganizationCountryFinder);
		assertTrue("Could not find the field for Country", country.exists());
		if(o.getCountry() != null) {
			country.set(o.getCountry());
		}
		TextField zip = ie.textField(editOrganizationZipFinder);
		assertTrue("Could not find the field for Zip", zip.exists());
		if(o.getZip() != null) {
			zip.set(o.getZip());
		}
		TextField phoneNumber = ie.textField(editOrganizationPhoneNumberFinder);
		assertTrue("Could not find the field for Phone Number", phoneNumber.exists());
		if(o.getPhoneNumber() != null) {
			phoneNumber.set(o.getPhoneNumber());
		}
		TextField fax = ie.textField(editOrganizationFaxFinder);
		assertTrue("Could not find the field for Fax", fax.exists());
		if(o.getFax() != null) {
			fax.set(o.getFax());
		}
		if(o.getCertImage() != null) {
			throw new NotImplementedYetException();
		}
	}

	public void saveAddOrganization() throws Exception {
		Button save = ie.button(addOrganizationSaveButtonFinder);
		assertTrue("Could not find the Save button on Add Organization", save.exists());
		save.click();
		checkManageOrganizationsPageContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	public void cancelAddOrganization() throws Exception {
		Button cancel = ie.button(addOrganizationCancelButtonFinder);
		assertTrue("Could not find the Cancel button on Add Organization", cancel.exists());
		cancel.click();
		checkManageOrganizationsPageContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}
	
	/**
	 * Assumes you are already on the Administration page.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		gotoManageOrganizations();
		gotoBackToAdministration();
		gotoManageOrganizations();
		List<String> units = getOrganizationNames();
		gotoAddOrganizationalUnit();
		String name = "validate-" + misc.getRandomString();
		String email = "dev@n4systems.com";
		Organization o = new Organization(name, email);
		setAddOrganizationForm(o);
		saveAddOrganization();
		gotoAddOrganizationalUnit();
		cancelAddOrganization();
		o.setNameOnCert("N4 System");
		o.setStreetAddress("179 John St.");
		o.setCity("Toronto");
		o.setState("ON");
		o.setCountry("Canada");
		o.setZip("M5T 1X4");
		o.setPhoneNumber("(416) 599-6464");
		o.setFax("(416) 599-6463");
		gotoEditOrganization(o.getName());
		setEditOrganizationForm(o);
		saveEditOrganization();
		Thread.sleep(1);
	}

	public void saveEditOrganization() throws Exception {
		Button save = ie.button(editOrganizationSaveButtonFinder);
		assertTrue("Could not find the Save button on Edit Organization", save.exists());
		save.click();
		checkManageOrganizationsPageContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void gotoEditOrganization(String name) throws Exception {
		assertNotNull(name);
		TableRows trs = getOrganizationTableRows();
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			TableCell td = tr.cell(xpath("TD[contains(text(),'" + name + "')]"));
			if(td.exists()) {
				Link edit = tr.link(xpath("TD/A[contains(text(),'Edit')]"));
				assertTrue("Found the organization row but could not find the Edit link", edit.exists());
				edit.click();
				misc.checkForErrorMessagesOnCurrentPage();
				checkManageOrganizationsPageForContentHeader();
				return;
			}
		}
		fail("Did not find Edit link for '" + name + "'");
	}

	private void checkManageOrganizationsPageForContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(manageOrganizationsPageForContentHeaderFinder);
		assertTrue("Could not find the page header for Manage Organizations - ", header.exists());
	}
}
