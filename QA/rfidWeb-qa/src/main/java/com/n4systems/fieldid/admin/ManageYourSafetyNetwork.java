package com.n4systems.fieldid.admin;

import static watij.finders.FinderFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import watij.elements.Link;
import watij.elements.Span;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageYourSafetyNetwork extends TestCase {
	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "manageyoursafetynetwork.properties";
	Finder manageYourSafetyNetworkFinder;
	Finder manageYourSafetyNetworkContentHeaderFinder;
	Finder companyFIDACFinder;

	public ManageYourSafetyNetwork(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			manageYourSafetyNetworkFinder = text(p.getProperty("link"));
			manageYourSafetyNetworkContentHeaderFinder = xpath(p.getProperty("contentheader"));
			companyFIDACFinder = xpath(p.getProperty("companyfidac"));
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
	}

	public String getFIDAC() throws Exception {
		Span companyFIDAC = ie.span(companyFIDACFinder);
		assertTrue("Could not find the Field ID Access Code", companyFIDAC.exists());
		return companyFIDAC.text();
	}
}
