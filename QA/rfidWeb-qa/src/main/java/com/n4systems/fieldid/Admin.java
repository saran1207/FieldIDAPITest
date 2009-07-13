package com.n4systems.fieldid;

import static watij.finders.FinderFactory.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.n4systems.fieldid.admin.ManageSystemSettings;
import com.n4systems.fieldid.admin.ManageYourSafetyNetwork;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Admin extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	ManageSystemSettings mss = null;
	ManageYourSafetyNetwork mysn = null;
	String propertyFile = "admin.properties";
	Finder adminFinder;
	Finder adminPageContentHeaderFinder;
	Finder systemAccessAndSetupSectionHeaderFinder;
	Finder yourProductsAndEquipmentSetupFinder;
	Finder autoCompleteTemplatesFinder;
	Finder dataSynchronizationFinder;
	Finder safetyNetworkFinder;
	
	public Admin(IE ie) {
		this.ie = ie;
		try {
			mss = new ManageSystemSettings(ie);
			mysn = new ManageYourSafetyNetwork(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			adminFinder = id(p.getProperty("link"));
			adminPageContentHeaderFinder = xpath(p.getProperty("admincontentheader"));
			systemAccessAndSetupSectionHeaderFinder = xpath(p.getProperty("systemaccessandsetupheader"));
			yourProductsAndEquipmentSetupFinder = xpath(p.getProperty("yourproductsandequipmentsetup"));
			autoCompleteTemplatesFinder = xpath(p.getProperty("autocompletetemplates"));
			dataSynchronizationFinder = xpath(p.getProperty("datasynchronization"));
			safetyNetworkFinder = xpath(p.getProperty("safetynetwork"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	public void validate() throws Exception {
		isAdministration();
		gotoAdministration();
		getCompanyID();
		getCompanyName();
		getFIDAC();
	}

	/**
	 * Go to the administration page. Assumes you are logged in and
	 * have access to the administration page.
	 * 
	 * @throws Exception
	 */
	public void gotoAdministration() throws Exception {
		Link adminLink = ie.link(adminFinder);
		assertTrue("Could not find the link to the Administration page", adminLink.exists());
		adminLink.click();
		checkAdminPageContentHeader();
	}

	/**
	 * Checks for the word "Administration" underneath the company logo.
	 * 
	 * @throws Exception
	 */
	public void checkAdminPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(adminPageContentHeaderFinder);
		assertTrue("Could not find the content header for Administration page", contentHeader.exists());
		HtmlElement systemAccessAndSetupSectionHeader = ie.htmlElement(systemAccessAndSetupSectionHeaderFinder);
		assertTrue("Could not find the System Access and Setup section header", systemAccessAndSetupSectionHeader.exists());
		HtmlElement yourProductsAndEquipmentSetupHeader = ie.htmlElement(yourProductsAndEquipmentSetupFinder);
		assertTrue("Could not find the System Access and Setup section header", yourProductsAndEquipmentSetupHeader.exists());
		HtmlElement autoCompleteTemplatesHeader = ie.htmlElement(autoCompleteTemplatesFinder);
		assertTrue("Could not find the System Access and Setup section header", autoCompleteTemplatesHeader.exists());
		HtmlElement dataSynchronizationHeader = ie.htmlElement(dataSynchronizationFinder);
		assertTrue("Could not find the System Access and Setup section header", dataSynchronizationHeader.exists());
		HtmlElement safetyNetworkHeader = ie.htmlElement(safetyNetworkFinder);
		assertTrue("Could not find the System Access and Setup section header", safetyNetworkHeader.exists());
	}

	/**
	 * Goes to the administration page and returns the company id
	 * from the Company Summary section.
	 * 
	 * @return The Company ID from the Company Summary
	 * @throws Exception
	 * @see gotoAdministration
	 */
	public String getCompanyID() throws Exception {
		gotoAdministration();
		mss.gotoManageSystemSettings();
		return mss.getCompanyID();
	}
	
	/**
	 * Goes to the administration page and returns the company name
	 * from the Company Summary section.
	 * 
	 * @return The Company Name from the Company Summary
	 * @throws Exception
	 * @see gotoAdministration
	 */
	public String getCompanyName() throws Exception {
		gotoAdministration();
		mss.gotoManageSystemSettings();
		return mss.getCompanyName();
	}
	
	/**
	 * Goes to the administration page and returns the Field ID
	 * Access Code from the Company Summary section.
	 * 
	 * @return The Field ID Access Code from the Company Summary
	 * @throws Exception
	 * @see gotoAdministration
	 */
	public String getFIDAC() throws Exception {
		gotoAdministration();
		mysn.gotoManageYourSafetyNetwork();
		return mysn.getFIDAC();
	}
	
	/**
	 * Checks to see if the link to the Administration page exists.
	 * 
	 * @return true if the current user can access the administration page
	 * @throws Exception
	 * @see gotoAdministration
	 */
	public boolean isAdministration() throws Exception {
		Link adminLink = ie.link(adminFinder);
		return adminLink.exists();
	}
}
