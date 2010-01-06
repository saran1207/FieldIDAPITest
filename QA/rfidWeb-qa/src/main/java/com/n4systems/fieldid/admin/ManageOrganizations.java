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
	private Finder manageOrganizationsLinkFinder;
	private Finder manageOrganizationsPageContentHeaderFinder;
	private Finder backToAdministrationLinkFinder;
	private Finder addOrganizationLinkFinder;
	private Finder addOrganizationNameFinder;
	private Finder addOrganizationNameOnCertFinder;
	private Finder addOrganizationStreetAddressFinder;
	private Finder addOrganizationCityFinder;
	private Finder addOrganizationStateFinder;
	private Finder addOrganizationCountryFinder;
	private Finder addOrganizationZipFinder;
	private Finder addOrganizationPhoneNumberFinder;
	private Finder addOrganizationFaxFinder;
	private Finder addOrganizationSaveButtonFinder;
	private Finder addOrganizationCancelButtonFinder;
	private Finder organizationTableRowsFinder;
	private Finder manageOrganizationsPageForContentHeaderFinder;
	private Finder editOrganizationNameFinder;
	private Finder editOrganizationNameOnCertFinder;
	private Finder editOrganizationStreetAddressFinder;
	private Finder editOrganizationCityFinder;
	private Finder editOrganizationStateFinder;
	private Finder editOrganizationCountryFinder;
	private Finder editOrganizationZipFinder;
	private Finder editOrganizationPhoneNumberFinder;
	private Finder editOrganizationFaxFinder;
	private Finder editOrganizationSaveButtonFinder;
	private Finder editOrganizationCancelButtonFinder;
	private Finder editPrimaryOrganizationLinkFinder;
	private Finder viewAllLinkFinder;
	private Finder manageOrganizationEditPageContentHeaderFinder;
	private Finder editOrganizationCountryTimeZoneFinder;
	private Finder editOrganizationTimeZoneFinder;
	private Finder addOrganizationCountryTimeZoneFinder;
	private Finder addOrganizationTimeZoneFinder;
	private Finder editOrganizationWebSiteAddressFinder;
	private Finder primaryOrganizationNameFinder;

	public ManageOrganizations(IE ie) {
		this.ie = ie;
		try {
			admin = new Admin(ie);
			misc = new FieldIDMisc(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			primaryOrganizationNameFinder = xpath(p.getProperty("primaryorganizationname", "NOT SET"));
			editOrganizationWebSiteAddressFinder = xpath(p.getProperty("editorganizationnamewebsiteaddress", "NOT SET"));
			addOrganizationCountryTimeZoneFinder = xpath(p.getProperty("addorganizationnamecountrytimezone", "NOT SET"));
			addOrganizationTimeZoneFinder = xpath(p.getProperty("addorganizationnametimezone", "NOT SET"));
			editOrganizationCountryTimeZoneFinder = xpath(p.getProperty("editorganizationcountrytimezone", "NOT SET"));
			editOrganizationTimeZoneFinder = xpath(p.getProperty("editorganizationtimezone", "NOT SET"));
			manageOrganizationEditPageContentHeaderFinder = xpath(p.getProperty("editorganizationheader", "NOT SET"));
			viewAllLinkFinder = xpath(p.getProperty("viewalllink", "NOT SET"));
			editPrimaryOrganizationLinkFinder = xpath(p.getProperty("editprimaryorganizationlink", "NOT SET"));
			manageOrganizationsLinkFinder = text(p.getProperty("link", "NOT SET"));
			manageOrganizationsPageContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			backToAdministrationLinkFinder = xpath(p.getProperty("backtoadministrationlink", "NOT SET"));
//			organizationNamesFinder = xpath(p.getProperty("organizationnamecells", "NOT SET"));
			addOrganizationLinkFinder = xpath(p.getProperty("addorganizationlink", "NOT SET"));
			addOrganizationNameFinder = id(p.getProperty("addorganizationname", "NOT SET"));
//			addOrganizationAdminEmailFinder = xpath(p.getProperty("addorganizationadminemail", "NOT SET"));
			addOrganizationNameOnCertFinder = id(p.getProperty("addorganizationnameoncert", "NOT SET"));
			addOrganizationStreetAddressFinder = id(p.getProperty("addorganizationstreetaddress", "NOT SET"));
			addOrganizationCityFinder = id(p.getProperty("addorganizationcity", "NOT SET"));
			addOrganizationStateFinder = id(p.getProperty("addorganizationstate", "NOT SET"));
			addOrganizationCountryFinder = id(p.getProperty("addorganizationcountry", "NOT SET"));
			addOrganizationZipFinder = id(p.getProperty("addorganizationzip", "NOT SET"));
			addOrganizationPhoneNumberFinder = id(p.getProperty("addorganizationphonenumber", "NOT SET"));
			addOrganizationFaxFinder = id(p.getProperty("addorganizationfax", "NOT SET"));
			addOrganizationSaveButtonFinder = id(p.getProperty("addorganizationsavebutton", "NOT SET"));
			addOrganizationCancelButtonFinder = xpath(p.getProperty("addorganizationcancelbutton", "NOT SET"));
			organizationTableRowsFinder = xpath(p.getProperty("organizationtablerows", "NOT SET"));
			manageOrganizationsPageForContentHeaderFinder = xpath(p.getProperty("editorganizationpagecontentheader", "NOT SET"));
			editOrganizationNameFinder = id(p.getProperty("editorganizationname", "NOT SET"));
//			editOrganizationAdminEmailFinder = id(p.getProperty("editorganizationadminemail", "NOT SET"));
			editOrganizationNameOnCertFinder = id(p.getProperty("editorganizationnameoncert", "NOT SET"));
			editOrganizationStreetAddressFinder = id(p.getProperty("editorganizationstreetaddress", "NOT SET"));
			editOrganizationCityFinder = id(p.getProperty("editorganizationcity", "NOT SET"));
			editOrganizationStateFinder = id(p.getProperty("editorganizationstate", "NOT SET"));
			editOrganizationCountryFinder = id(p.getProperty("editorganizationcountry", "NOT SET"));
			editOrganizationZipFinder = id(p.getProperty("editorganizationzip", "NOT SET"));
			editOrganizationPhoneNumberFinder = id(p.getProperty("editorganizationphonenumber", "NOT SET"));
			editOrganizationFaxFinder = id(p.getProperty("editorganizationfax", "NOT SET"));
			editOrganizationSaveButtonFinder = id(p.getProperty("editorganizationsavebutton", "NOT SET"));
			editOrganizationCancelButtonFinder = xpath(p.getProperty("editorganizationcancelbutton", "NOT SET"));
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
	
	public List<String> getSecondaryOrganizationNames() throws Exception {
		List<String> results = new ArrayList<String>();

		Link next;
		boolean more;
		do {
			next = ie.link(text("Next>"));
			more = next.exists(); 
			results.addAll(getOrganizationNamesOnCurrentPage());
			if(more) {
				next.click();
			}
		} while(more);
		
		return results;
	}
	
	private List<String> getOrganizationNamesOnCurrentPage() throws Exception {
		List<String> results = new ArrayList<String>();
		TableRows trs = getOrganizationTableRows();
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			TableCell td = tr.cell(0);
			String name = td.text().trim();
			results.add(name);
		}

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
		TextField name = ie.textField(addOrganizationNameFinder);
		assertTrue("Could not find the field for Name", name.exists());
		name.set(o.getName());
		TextField nameOnCert = ie.textField(addOrganizationNameOnCertFinder);
		assertTrue("Could not find the field for Name on certificate", nameOnCert.exists());
		if(o.getNameOnCert() != null) {
			nameOnCert.set(o.getNameOnCert());
		}
		
		SelectList countryTimeZone = ie.selectList(addOrganizationCountryTimeZoneFinder);
		assertTrue("Could not find the select list for Country (relating to Time Zone)", countryTimeZone.exists());
		String ctz = o.getCountryTimeZone();
		if(ctz != null) {
			Option ctzo = countryTimeZone.option(text("/" + ctz + "/"));
			assertTrue("Could not find the Country '" + ctz + "' in the list of Countries (relating to Time Zone", ctzo.exists());
			ctzo.select();
			misc.waitForJavascript();
		}

		SelectList timeZone = ie.selectList(addOrganizationTimeZoneFinder);
		assertTrue("Could not find the select list for Time Zone", timeZone.exists());
		String tz = o.getTimeZone();
		if(tz != null) {
			Option tzo = timeZone.option(text("/" + tz + "/"));
			assertTrue("Could not find the Time Zone '" + tz + "' in the list of Time Zone", tzo.exists());
			tzo.select();
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

	/**
	 * Used to edit non-primary organizations
	 * 
	 * @param o
	 * @throws Exception
	 */
	public void setEditOrganizationForm(Organization o) throws Exception {
		setEditOrganizationForm(o, false);
	}

	/**
	 * Can be used to edit primary or secondary organizations.
	 * 
	 * @param o
	 * @throws Exception
	 */
	public void setEditOrganizationForm(Organization o, boolean primary) throws Exception {
		assertNotNull(o);
		if(o.getName() != null) {
			TextField name = ie.textField(editOrganizationNameFinder);
			assertTrue("Could not find the field for Name", name.exists());
			name.set(o.getName());
		}

		TextField nameOnCert = ie.textField(editOrganizationNameOnCertFinder);
		assertTrue("Could not find the field for Name on certificate", nameOnCert.exists());
		if(o.getNameOnCert() != null) {
			nameOnCert.set(o.getNameOnCert());
		}
		
		SelectList countryTimeZone = ie.selectList(editOrganizationCountryTimeZoneFinder);
		assertTrue("Could not find the select list for Country (relating to Time Zone)", countryTimeZone.exists());
		String ctz = o.getCountryTimeZone();
		if(ctz != null) {
			Option ctzo = countryTimeZone.option(text("/" + ctz + "/"));
			assertTrue("Could not find the Country '" + ctz + "' in the list of Countries (relating to Time Zone", ctzo.exists());
			ctzo.select();
			misc.waitForJavascript();
		}

		SelectList timeZone = ie.selectList(editOrganizationTimeZoneFinder);
		assertTrue("Could not find the select list for Time Zone", timeZone.exists());
		String tz = o.getTimeZone();
		if(tz != null) {
			Option tzo = timeZone.option(text("/" + tz + "/"));
			assertTrue("Could not find the Time Zone '" + tz + "' in the list of Time Zone", tzo.exists());
			tzo.select();
		}
		
		if(primary) {
			TextField webSiteAddress = ie.textField(editOrganizationWebSiteAddressFinder);
			assertTrue("Could not find the field for Web Site Address", webSiteAddress.exists());
			if(o.getWebSiteAddress() != null) {
				webSiteAddress.set(o.getWebSiteAddress());
			}
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

	public Organization getOrganizationForm() throws Exception {
		Organization result = new Organization(null);
		TextField name = ie.textField(editOrganizationNameFinder);
		assertTrue("Could not find the field for Name", name.exists());
		result.setName(name.value());

		TextField nameOnCert = ie.textField(editOrganizationNameOnCertFinder);
		assertTrue("Could not find the field for Name on certificate", nameOnCert.exists());
		result.setNameOnCert(nameOnCert.value());
		
		SelectList countryTimeZone = ie.selectList(editOrganizationCountryTimeZoneFinder);
		assertTrue("Could not find the select list for Country (relating to Time Zone)", countryTimeZone.exists());
		List<String> selected = countryTimeZone.getSelectedItems();
		assertTrue("There is no Country (related to Time Zone) selected", selected.size() > 0);
		result.setCountryTimeZone(selected.get(0));

		SelectList timeZone = ie.selectList(editOrganizationTimeZoneFinder);
		assertTrue("Could not find the select list for Time Zone", timeZone.exists());
		selected = timeZone.getSelectedItems();
		assertTrue("There is no Time Zone selected", selected.size() > 0);
		result.setTimeZone(selected.get(0));

		TextField streetAddress = ie.textField(editOrganizationStreetAddressFinder);
		assertTrue("Could not find the field for Street Address", streetAddress.exists());
		result.setStreetAddress(streetAddress.value());

		TextField city = ie.textField(editOrganizationCityFinder);
		assertTrue("Could not find the field for City", city.exists());
		result.setCity(city.value());

		TextField state = ie.textField(editOrganizationStateFinder);
		assertTrue("Could not find the field for State", state.exists());
		result.setState(state.value());

		TextField country = ie.textField(editOrganizationCountryFinder);
		assertTrue("Could not find the field for Country", country.exists());
		result.setCountry(country.value());

		TextField zip = ie.textField(editOrganizationZipFinder);
		assertTrue("Could not find the field for Zip", zip.exists());
		result.setZip(zip.value());

		TextField phoneNumber = ie.textField(editOrganizationPhoneNumberFinder);
		assertTrue("Could not find the field for Phone Number", phoneNumber.exists());
		result.setPhoneNumber(phoneNumber.value());

		TextField fax = ie.textField(editOrganizationFaxFinder);
		assertTrue("Could not find the field for Fax", fax.exists());
		result.setFax(fax.value());
		
		return result;
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
		gotoEditPrimaryOrganization();
		gotoViewAll();
		gotoAddOrganizationalUnit();
		String random = misc.getRandomString();
		String name = "validate-" + random;
		Organization o = new Organization(name);
		o.setNameOnCert("Validate System");
		o.setCountryTimeZone("Canada");
		o.setTimeZone("Edmonton");
		o.setStreetAddress("123 Anywhere St.");
		o.setCity("Edmonton");
		o.setState("AB");
		o.setCountry("Canada");
		o.setZip("A1B 2C3");
		o.setPhoneNumber("(780) 555-1212");
		o.setFax("(780) 555-1213");
		setAddOrganizationForm(o);
		saveAddOrganization();
		List<String> units = getSecondaryOrganizationNames();
		assertTrue(units.contains(name));
		gotoEditOrganization(name);
		Organization tmp = getOrganization(false);
		cancelEditOrganization();
		compareOrganizations(o, tmp);
		gotoAddOrganizationalUnit();
		cancelAddOrganization();
		o.setNameOnCert("N4 System");
		o.setCountryTimeZone("Canada");
		o.setTimeZone("Toronto");
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
		gotoEditOrganization(o.getName());
		tmp = getOrganization(false);
		cancelEditOrganization();
		compareOrganizations(o, tmp);
		gotoEditPrimaryOrganization();
		o = getOrganization(true);
		if(o.getWebSiteAddress() != null && o.getWebSiteAddress().equals("")) {
			o.setWebSiteAddress("http://www.google.ca/");
		}
		setEditOrganizationForm(o, true);
		saveEditOrganization();
		gotoEditPrimaryOrganization();
		tmp = getOrganization(true);
		cancelEditOrganization();
		compareOrganizations(o, tmp);
	}
	
	public void cancelEditOrganization() throws Exception {
		Button cancel = ie.button(editOrganizationCancelButtonFinder);
		assertTrue("Could not find the Cancel button on Edit Organization", cancel.exists());
		cancel.click();
		checkManageOrganizationsPageContentHeader();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	private void compareOrganizations(Organization o, Organization tmp) throws Exception {
		assertNotNull("The original Organization is null", o);
		assertNotNull("The Organization collect from the edit page is null", tmp);
		assertEquals("The Organization name fields don't match", o.getName(), tmp.getName());
		assertEquals("The Organization name on certificate fields don't match", o.getNameOnCert(), tmp.getNameOnCert());
		assertEquals("The Organization country time zone fields don't match", o.getCountryTimeZone(), tmp.getCountryTimeZone());
		assertTrue("The Organization time zone fields don't match", tmp.getTimeZone().contains(o.getTimeZone()));
		assertEquals("The Organization street address fields don't match", o.getStreetAddress(), tmp.getStreetAddress());
		assertEquals("The Organization city fields don't match", o.getCity(), tmp.getCity());
		assertEquals("The Organization state fields don't match", o.getState(), tmp.getState());
		assertEquals("The Organization country fields don't match", o.getCountry(), tmp.getCountry());
		assertEquals("The Organization zip code fields don't match", o.getZip(), tmp.getZip());
		assertEquals("The Organization phone number fields don't match", o.getPhoneNumber(), tmp.getPhoneNumber());
		assertEquals("The Organization fax fields don't match", o.getFax(), tmp.getFax());
	}

	public Organization getOrganization(boolean primary) throws Exception {
		Organization o = new Organization(null);

		TextField name = ie.textField(editOrganizationNameFinder);
		assertTrue("Could not find the field for Name", name.exists());
		o.setName(name.value());
		
		TextField nameOnCert = ie.textField(editOrganizationNameOnCertFinder);
		assertTrue("Could not find the field for Name on certificate", nameOnCert.exists());
		o.setNameOnCert(nameOnCert.value());
		
		SelectList countryTimeZone = ie.selectList(editOrganizationCountryTimeZoneFinder);
		assertTrue("Could not find the select list for Country (relating to Time Zone)", countryTimeZone.exists());
		o.setCountryTimeZone(countryTimeZone.getSelectedItems().get(0));
		
		SelectList timeZone = ie.selectList(editOrganizationTimeZoneFinder);
		assertTrue("Could not find the select list for Time Zone", timeZone.exists());
		o.setTimeZone(timeZone.getSelectedItems().get(0));
		
		if(primary) {
			TextField webSiteAddress = ie.textField(editOrganizationWebSiteAddressFinder);
			assertTrue("Could not find the field for Web Site Address", webSiteAddress.exists());
			o.setWebSiteAddress(webSiteAddress.value());
		}

		TextField streetAddress = ie.textField(editOrganizationStreetAddressFinder);
		assertTrue("Could not find the field for Street Address", streetAddress.exists());
		o.setStreetAddress(streetAddress.value());

		TextField city = ie.textField(editOrganizationCityFinder);
		assertTrue("Could not find the field for City", city.exists());
		o.setCity(city.value());

		TextField state = ie.textField(editOrganizationStateFinder);
		assertTrue("Could not find the field for State", state.exists());
		o.setState(state.value());

		TextField country = ie.textField(editOrganizationCountryFinder);
		assertTrue("Could not find the field for Country", country.exists());
		o.setCountry(country.value());

		TextField zip = ie.textField(editOrganizationZipFinder);
		assertTrue("Could not find the field for Zip", zip.exists());
		o.setZip(zip.value());

		TextField phoneNumber = ie.textField(editOrganizationPhoneNumberFinder);
		assertTrue("Could not find the field for Phone Number", phoneNumber.exists());
		o.setPhoneNumber(phoneNumber.value());

		TextField fax = ie.textField(editOrganizationFaxFinder);
		assertTrue("Could not find the field for Fax", fax.exists());
		o.setFax(fax.value());

		return o;
	}

	public void gotoViewAll() throws Exception {
		Link l = ie.link(viewAllLinkFinder);
		assertTrue("Could not find a link to View All on Manage Organization", l.exists());
		l.click();
		checkManageOrganizationsPageContentHeader();
	}

	/**
	 * Click on the Edit link for editing the primary organization.
	 * Assumes you are already on the Manage Organizations page.
	 * 
	 * @throws Exception
	 */
	public void gotoEditPrimaryOrganization() throws Exception {
		Link edit = ie.link(editPrimaryOrganizationLinkFinder);
		assertTrue("Could not find the link to edit the primary organization", edit.exists());
		edit.click();
		checkManageOrganizationEditPageContentHeader();
	}

	private void checkManageOrganizationEditPageContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(manageOrganizationEditPageContentHeaderFinder);
		assertTrue("Could not find the page header for Manage Organization -", header.exists());
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

		Link next;
		boolean more;
		do {
			next = ie.link(text("Next>"));
			more = next.exists();
			Link edit = findEditOrganizationLinkOnCurrentPage(name);
			if(edit != null && edit.exists()) {
				edit.click();
				misc.checkForErrorMessagesOnCurrentPage();
				checkManageOrganizationsPageForContentHeader();
				return;
			}
			if(more) {
				next.click();
			}
		} while(more);
		
		fail("Did not find Edit link for '" + name + "'");
	}
	
	private Link findEditOrganizationLinkOnCurrentPage(String name) throws Exception {
		TableRows trs = getOrganizationTableRows();
		Iterator<TableRow> i = trs.iterator();
		while(i.hasNext()) {
			TableRow tr = i.next();
			TableCell td = tr.cell(xpath("TD[contains(text(),'" + name + "')]"));
			if(td.exists()) {
				Link edit = tr.link(xpath("TD/A[contains(text(),'Edit')]"));
				assertTrue("Found the organization row but could not find the Edit link", edit.exists());
				return edit;
			}
		}
		return null;
	}

	private void checkManageOrganizationsPageForContentHeader() throws Exception {
		HtmlElement header = ie.htmlElement(manageOrganizationsPageForContentHeaderFinder);
		assertTrue("Could not find the page header for Manage Organizations - ", header.exists());
	}

	public String getPrimaryOrganizationName() throws Exception {
		String result;
		Span name = ie.span(primaryOrganizationNameFinder);
		assertTrue("Could not find the primary organization name on Manage Organizations page", name.exists());
		result = name.text().trim();
		return result;
	}
}
