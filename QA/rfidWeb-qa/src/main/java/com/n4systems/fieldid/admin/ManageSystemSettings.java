package com.n4systems.fieldid.admin;

import static watij.finders.FinderFactory.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.n4systems.fieldid.Admin;
import com.n4systems.fieldid.FieldIDMisc;
import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageSystemSettings extends TestCase {
	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "managesystemsettings.properties";
	FieldIDMisc misc;
	Finder settingsFinder;
	Finder settingsPageContentHeaderFinder;
	Finder webSiteAddressFinder;
	Finder saveButtonFinder;
	Finder removeSystemLogoFinder;
	Finder systemLogoPreviewImageFinder;
	Finder cancelLinkFinder;
	Finder embeddedLoginCodeFinder;
	Finder companyIDFinder;
	Finder companyNameFinder;
	
	public ManageSystemSettings(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			settingsFinder = text(p.getProperty("link", "NOT SET"));
			settingsPageContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			webSiteAddressFinder = id(p.getProperty("websiteaddress", "NOT SET"));
			saveButtonFinder = id(p.getProperty("savebutton", "NOT SET"));
			removeSystemLogoFinder = id(p.getProperty("removesystemlogo", "NOT SET"));
			systemLogoPreviewImageFinder = id(p.getProperty("systemlogopreviewimage", "NOT SET"));
			cancelLinkFinder = xpath(p.getProperty("cancellink", "NOT SET"));
			embeddedLoginCodeFinder = xpath(p.getProperty("embeddedlogincodespan", "NOT SET"));
			companyIDFinder = xpath(p.getProperty("companyid", "NOT SET"));
			companyNameFinder = xpath(p.getProperty("companyname", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}

	/**
	 * This is a method which will validate all the
	 * methods in this class function correctly.
	 * It assumes you have already called gotoAdministration().
	 * Every time you add new functionality to this
	 * class, you should add some code here to test
	 * that functionality. For the most part we want
	 * to be sure the Finders are working okay.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		gotoManageSystemSettings();
		getEmbeddedLoginCode();
		getCompanyID();
//		getCompanyName();	// moved to Manage Organizations
		String url = getWebSiteAddress();
		setWebSiteAddress(url);
		saveChangesToSystemSettings();
		clickRemoveSystemLogo();
		cancelChangesToSystemSettings();
	}
	
	public String getCompanyID() throws Exception {
		Span companyID = ie.span(companyIDFinder);
		assertTrue("Could not find the company ID", companyID.exists());
		return companyID.text();
	}

	public String getCompanyName() throws Exception {
		ManageOrganizations mos = new ManageOrganizations(ie);
		String companyName = mos.getPrimaryOrganizationName();
		return companyName;
	}
	
	public void cancelChangesToSystemSettings() throws Exception {
		Link cancel = ie.link(cancelLinkFinder);
		assertTrue("Could not find a Cancel link", cancel.exists());
		cancel.click();
		Admin admin = new Admin(ie);
		admin.checkAdminPageContentHeader();
	}

	/**
	 * Goes to the Manage System Users page. Assumes you are
	 * already on the Administration page.
	 * 
	 * @throws Exception
	 */
	public void gotoManageSystemSettings() throws Exception {
		Link manageSettings = ie.link(settingsFinder);
		assertTrue("Could not find the link to Manage System Settings", manageSettings.exists());
		manageSettings.click();
		checkSettingsPageContentHeader();
	}

	private void checkSettingsPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(settingsPageContentHeaderFinder);
		assertTrue("Could not find the content header on Manage System Settings page.", contentHeader.exists());
	}

	public String getWebSiteAddress() throws Exception {
		String url = null;
		TextField webSite = ie.textField(webSiteAddressFinder);
		assertTrue("Could not find the Web Site Address text field", webSite.exists());
		url = webSite.value();
		return url;
	}

	public void setWebSiteAddress(String url) throws Exception {
		TextField webSite = ie.textField(webSiteAddressFinder);
		assertTrue("Could not find the Web Site Address text field", webSite.exists());
		webSite.set(url);
	}
	
	public void saveChangesToSystemSettings() throws Exception {
		Button save = ie.button(saveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public void clickRemoveSystemLogo() throws Exception {
		Link remove = ie.link(removeSystemLogoFinder);
		assertTrue("Could not find the link to remove the system logo", remove.exists());
		remove.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkImagePreviewNotAvailable();
	}

	public void checkImagePreviewNotAvailable() throws Exception {
		Image imagePreview = ie.image(systemLogoPreviewImageFinder);
		Span imageSpan = imagePreview.span(xpath(".."));
		assertTrue("System Logo preview was available but we weren't expecting it to be.", misc.checkForHidden(imageSpan.html()));
	}

	public void checkImagePreviewAvailable() throws Exception {
		Image imagePreview = ie.image(systemLogoPreviewImageFinder);
		Span imageSpan = imagePreview.span(xpath(".."));
		assertFalse("System Logo preview was not available but we weren't expecting it to be.", misc.checkForHidden(imageSpan.style()));
	}
	
	public String getEmbeddedLoginCode() throws Exception {
		Span code = ie.span(embeddedLoginCodeFinder);
		assertTrue("Could not find the embedded login code", code.exists());
		return code.text();
	}
}
