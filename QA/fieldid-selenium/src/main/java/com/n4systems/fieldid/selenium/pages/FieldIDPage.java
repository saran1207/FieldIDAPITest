package com.n4systems.fieldid.selenium.pages;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.thoughtworks.selenium.Selenium;

public class FieldIDPage extends WebPage {

	private static final String ERROR_MESSAGE_COUNT_XPATH = "//*[@class='errorMessage' and not(contains(@style,'display: none'))]";

	public FieldIDPage(Selenium selenium) {
		super(selenium);
	}
	
	public FieldIDPage(Selenium selenium, boolean waitForLoad) {
		super(selenium, waitForLoad);
	}
	
	public HomePage clickHomeLink() {
		selenium.click("//div[@id='pageNavigation']//a[.='Home']");
		return new HomePage(selenium);
	}
	
	public SetupPage clickSetupLink() {
		selenium.click("//div[@id='pageNavigation']//a[contains(.,'Setup')]");
		return new SetupPage(selenium);
	}
	
	public IdentifyPage clickIdentifyLink() {
		selenium.click("//div[@id='pageNavigation']//a[contains(.,'Identify')]");
		return new IdentifyPage(selenium);
	}
	
	protected void gotoNextPage() {
		selenium.click("//a[contains(.,'Next')]");
		waitForPageToLoad();
	}

	protected void gotoPrevPage() {
		selenium.click("//a[contains(text(),'Previous')]");
		waitForPageToLoad();
	}

	protected void gotoFirstPage() {
		selenium.click("//a[contains(text(),'First')]");
		waitForPageToLoad();
	}
	
	protected void gotoPage(Integer pageNumber) {
		selenium.type("//input[@id='toPage']", pageNumber.toString());
		waitForPageToLoad();
	}
	
	protected int getNumberOfPages() {
		if (!selenium.isElementPresent("//label[@for='lastPage']")) {
			return 1;
		} else {
			String lastPageLabel = selenium.getText("//label[@for='lastPage']");
			String numStr = lastPageLabel.replaceAll("\\D", "");
			return Integer.parseInt(numStr);
		}
	}
	
	protected void clickNavOption(String navOption) {
		selenium.click("//ul[@class='options ']//a[contains(., '"+ navOption +"')]");
		waitForPageToLoad();
	}
	
	protected void clickNavOption(String navOption, String timeout) {
		selenium.click("//ul[@class='options ']//a[contains(., '"+ navOption +"')]");
		waitForPageToLoad(timeout);
	}

	protected void waitForElementToBePresent(String locator)  {
		waitForElementToBePresent(locator, MiscDriver.DEFAULT_TIMEOUT);
	}

	protected void waitForElementToBePresent(String locator, String timeout)  {
		selenium.waitForCondition("var value = selenium.isElementPresent( '" + locator.replace("'", "\\'") + "'); value == true", timeout);
	}

	protected void checkForErrorMessages(String png) {
		List<String> errors = getFormErrorMessages();
		int otherErrors = countNonFormErrorMessages();
		if(isOopsPage()) {
			fail("Got the Oops page. Check the fieldid.log.");
		} else if(errors.size() > 0) {
			fail("There were errors on the page: " + errors);
		} else if(otherErrors > 0) {
			fail("There were non-form errors on the page");
		}
	}
	
	private int countNonFormErrorMessages() {
		return selenium.getXpathCount(ERROR_MESSAGE_COUNT_XPATH).intValue();
	}
	
	protected List<String> getFormErrorMessages() {	// gets class="errorMessage"
		List<String> result = new ArrayList<String>();
		
		int maxIndex = selenium.getXpathCount(ERROR_MESSAGE_COUNT_XPATH).intValue();
		for(int i = 1; i <= maxIndex; i++) {
			String iterableErrorMessageLocator = "//ul/li["+i+"]/*[@class='errorMessage' and not(contains(@style,'display: none'))]";
			String s = selenium.getText(iterableErrorMessageLocator);
			result.add(s);
		}
		return result;
	}
	
	protected List<String> getActionMessages() {
		List<String> result = new ArrayList<String>();
		
		int maxIndex = selenium.getXpathCount("//*[@class='actionMessage']").intValue();
		for(int i = 1; i <= maxIndex; i++) {
			String iterableActionMessageLocator = "//ul/li["+i+"]/*[@class='actionMessage']";
			String s = selenium.getText(iterableActionMessageLocator);
			result.add(s);
		}

		return result;
	}

	public String getCurrentTab() {
		return selenium.getText("//ul[@class='options ']/li[@class[contains(.,' selected')]]").trim();
	}

	public OrgPicker getOrgPicker() {
		return new OrgPicker(selenium);
	}
	
	public AssetPage search(String criteria) {
		selenium.type("//input[@id='searchText']", criteria);
		selenium.click("//input[@id='smartSearchButton']");
		return new AssetPage(selenium);
	}

}
