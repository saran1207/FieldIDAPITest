package com.n4systems.fieldid.selenium.pages;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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

    public LoginPage clickSignOut() {
        selenium.click("//div[@id='pageActions']//a[.='Sign Out']");
        return new LoginPage(selenium);
    }

	public SafetyNetworkPage clickSafetyNetworkLink() {
		selenium.click("//div[@id='pageNavigation']//a[.='Safety Network']");
		return new SafetyNetworkPage(selenium);
	}

	public SchedulesSearchPage clickSchedulesLink() {
		selenium.click("//div[@id='pageNavigation']//a[.='Schedules']");
		return new SchedulesSearchPage(selenium);
	}

	public SetupPage clickSetupLink() {
		selenium.click("//div[@id='pageNavigation']//a[contains(.,'Setup')]");
		return new SetupPage(selenium);
	}
	
	public JobsListPage clickJobsLink() {
		selenium.click("//div[@id='pageNavigation']//a[contains(.,'Jobs')]");
		return new JobsListPage(selenium);
	}
	
	public IdentifyPage clickIdentifyLink() {
		selenium.click("//div[@id='pageNavigation']//a[contains(.,'Identify')]");
		return new IdentifyPage(selenium);
	}

    public AssetsSearchPage clickAssetsLink() {
        selenium.click("//div[@id='pageNavigation']//a[contains(.,'Assets')]");
        return new AssetsSearchPage(selenium);
    }
	
	public void gotoNextPage() {
		selenium.click("//div[@class='paginationWrapper'][1]//a[contains(.,'Next')]");
		waitForPageToLoad();
	}

	public void gotoPrevPage() {
		selenium.click("//div[@class='paginationWrapper'][1]//a[contains(text(),'Previous')]");
		waitForPageToLoad();
	}

	public void gotoFirstPage() {
		selenium.click("//div[@class='paginationWrapper'][1]//a[contains(text(),'First')]");
		waitForPageToLoad();
	}
	
	public void gotoPage(int pageNumber) {
        if (getNumberOfPages() != 1 && getCurrentPage() != pageNumber) {
            selenium.type("//div[@class='paginationWrapper'][1]//input[@id='toPage']", pageNumber+"");
            waitForPageToLoad();
        }
	}

    public int getCurrentPage() {
        if (getNumberOfPages() == 1) {
            return 1;
        } else {
            return Integer.parseInt(selenium.getValue("//div[@class='paginationWrapper'][1]//input[@id='toPage']"));
        }
    }
	
	public int getNumberOfPages() {
		if (!selenium.isElementPresent("//div[@class='paginationWrapper'][1]//label[@for='lastPage']")) {
			return 1;
		} else {
			String lastPageLabel = selenium.getText("//div[@class='paginationWrapper'][1]//label[@for='lastPage']");
			String numStr = lastPageLabel.replaceAll("\\D", "");
			return Integer.parseInt(numStr);
		}
	}

    protected List<String> collectTableValuesUnderCellForCurrentPage(int firstRow, int cellNumber, String subXpath) {
        int numRows = selenium.getXpathCount("//table[@class='list']/tbody/tr").intValue();
        List<String> values = new ArrayList<String>(numRows - firstRow + 1);

        for (int i = firstRow; i <= numRows; i++) {
            String value = selenium.getText("//table[@class='list']/tbody/tr["+i+"]/td["+cellNumber+"]/"+subXpath);
            values.add(value);
        }

        return values;
    }

    protected List<String> collectTableAttributesUnderCellForCurrentPage(int firstRow, int cellNumber, String attrXpath) {
        int numRows = selenium.getXpathCount("//table[@class='list']/tbody/tr").intValue();
        List<String> attrs = new ArrayList<String>(numRows - firstRow + 1);

        for (int i = firstRow; i <= numRows; i++) {
            String completeAttributeXpath = "//table[@class='list']/tbody/tr["+i+"]/td["+cellNumber+"]/"+attrXpath;
            String xpathUpToAttribute = completeAttributeXpath.substring(0, completeAttributeXpath.lastIndexOf("@"));
            if (selenium.isElementPresent(xpathUpToAttribute)) {
                String attr = selenium.getAttribute(completeAttributeXpath);
                attrs.add(attr);
            } else {
                attrs.add(null);
            }
        }

        return attrs;
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
	
	public List<String> getFormErrorMessages() {	// gets class="errorMessage"
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
	
	public String getValidationErrorFor(String fieldInputId) {
		String fieldErrorLocator = "css=*[errorfor='" + fieldInputId + "']";
		assertThat("there is no error message for field " + fieldInputId, selenium.isElementPresent(fieldErrorLocator), is(true));
		
		assertThat(selenium.getAttribute(fieldErrorLocator + "@class"), containsString("errorMessage"));
		return selenium.getText(fieldErrorLocator);
	}
	
	protected List<String> getColumnFromTableStartingAtRow(String tableXpath, Integer columnNumber, Integer startingAtRow) {
		if (startingAtRow == null) {
			startingAtRow = 1;
		}
		
		List<String> columnValues = new ArrayList<String>();
		
		int numRows = selenium.getXpathCount(tableXpath+"//tr").intValue();
		for (int i = startingAtRow; i <= numRows; i++) {
			String cellXpath = tableXpath+"//tr["+i+"]/td["+columnNumber+"]";
			columnValues.add(selenium.getText(cellXpath).trim());
		}
		
		return columnValues;
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
