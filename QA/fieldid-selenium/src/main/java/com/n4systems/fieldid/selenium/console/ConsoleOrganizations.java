package com.n4systems.fieldid.selenium.console;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.datatypes.ConsoleTenant;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;
import org.junit.Assert;

public class ConsoleOrganizations extends Assert {
	Selenium selenium;
	Misc misc;
	
	// Locators
	private String organizationsTableXpath = "//DIV[@id='content']/TABLE";
	private String numberOfTenantsXpath = organizationsTableXpath + "/TBODY/TR";
	private String totalTenantsTextLocator = "xpath=//DIV[@id='content']";

	public ConsoleOrganizations(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	/**
	 * Assumes you are on the Organizations tab. Gets the list of tenant IDs.
	 * 
	 * @return
	 */
	public List<String> getListOfTenantIDs() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfTenantsXpath);
		int numTenants = n.intValue();
		String tenantIDCellLocator = "xpath=" + organizationsTableXpath + ".0.1";
		for(int i = 0; i < numTenants; i++) {
			String tenant = selenium.getTable(tenantIDCellLocator);
			result.add(tenant);
			tenantIDCellLocator = tenantIDCellLocator.replaceFirst("\\." + i, "." + (i+1));
		}
		return result;
	}
	
	public int getNumberOfTenants() {
		int result = -1;
		String s = selenium.getText(totalTenantsTextLocator);
		s = s.replaceFirst("[^0-9]*([0-9]*).*$", "$1");
		result = Integer.parseInt(s);
		return result;
	}
	
	public void gotoEditTenant(String tenant) {
		fail("Not implemented yet");
	}
	
	public void gotoCreateSuperUser() {
		fail("Not implemented yet");
	}
	
	public void setTenantInformation(ConsoleTenant ct) {
		fail("Not implemented yet");
	}
	
	public ConsoleTenant getTenantInformation() {
		ConsoleTenant ct = new ConsoleTenant();
		fail("Not implemented yet");
		return ct;
	}
	
	public void gotoSubmitTenantInformation() {
		fail("Not implemented yet");
	}
	
	public void gotoCancelTenantInformation() {
		fail("Not implemented yet");
	}
	
	public void gotoCreateSuperUser(String defaultUserName, String defaultassword) {
		fail("Not implemented yet");
	}
}

