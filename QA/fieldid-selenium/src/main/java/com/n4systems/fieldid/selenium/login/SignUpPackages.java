package com.n4systems.fieldid.selenium.login;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import com.n4systems.fieldid.selenium.misc.Misc;
import com.thoughtworks.selenium.Selenium;

public class SignUpPackages {
	Selenium selenium;
	Misc misc;
	
	// Locators
	private String headerText = "Sign Up For A Field ID Account";
	private String signUpForPackageHeaderLocator = "xpath=//DIV[@id='mainContent']/H1[contains(text(),'" + headerText + "')]";
	private String signUpPackagesTableXpath = "//TABLE[@id='packages']";
	private String numberOfSignUpPackagesXpath = signUpPackagesTableXpath + "/THEAD/TR[2]/TH[contains(@id,'package_')]";
	private String returnToSignInLinkLocator = "xpath=//A[contains(text(),'Return to Sign In')]";

	public SignUpPackages(Selenium selenium, Misc misc) {
		this.selenium = selenium;
		this.misc = misc;
	}
	
	public void verifySignUpPackagePage() {
		misc.info("Confirm we arrived at the Sign Up Package page");
		assertTrue("Could not find '" + headerText + "'", selenium.isElementPresent(signUpForPackageHeaderLocator));
	}
	
	public List<String> getListOfPackages() {
		List<String> result = new ArrayList<String>();
		Number n = selenium.getXpathCount(numberOfSignUpPackagesXpath);
		int numPackages = n.intValue();
		// Package names are on row 1, column 1, where row and columns start at 0, 0
		String signUpPackageCellLocator = "xpath=" + signUpPackagesTableXpath + ".1.1";
		for(int i = 0; i < numPackages; i++) {
			String signUpPackage = selenium.getTable(signUpPackageCellLocator);
			result.add(signUpPackage.trim());
			signUpPackageCellLocator = signUpPackageCellLocator.replaceFirst("\\.1\\." + (i+1), ".1." + (i+2));
		}
		return result;
	}
	
	public void gotoSignUpNow(String packageName) {
		String signUpNowLinkLocator = "xpath=" + signUpPackagesTableXpath + "/TBODY/TR[@class='signUp']/TD/A[contains(@href,'" + packageName + "')]";
		if(selenium.isElementPresent(signUpNowLinkLocator)) {
			selenium.click(signUpNowLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a Sign up Now link for package '" + packageName + "'");
		}
	}
	
	public void gotoReturnToSignIn() {
		if(selenium.isElementPresent(returnToSignInLinkLocator)) {
			selenium.click(returnToSignInLinkLocator);
			misc.waitForPageToLoadAndCheckForOopsPage();
		} else {
			fail("Could not find a link to Return to Sign In");
		}
	}
}
