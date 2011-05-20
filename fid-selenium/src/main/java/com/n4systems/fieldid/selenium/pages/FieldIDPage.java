package com.n4systems.fieldid.selenium.pages;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.fieldid.selenium.components.LocationPicker;
import com.n4systems.fieldid.selenium.components.OrgPicker;
import com.n4systems.fieldid.selenium.lib.PageErrorException;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;
import com.thoughtworks.selenium.Selenium;

public class FieldIDPage extends WebPage {
	private static final Logger logger = Logger.getLogger(FieldIDPage.class);
	
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
    
    public ReportingPage clickReportingLink(){
        selenium.click("//div[@id='pageNavigation']//a[contains(.,'Reporting')]");
        return new ReportingPage(selenium);
    }

    public MyAccountPage clickMyAccount() {
        selenium.click("//div[@id='pageActions']//a[.='My Account']");
        return new MyAccountPage(selenium);
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
    
    protected List<String> collectTableHeaders() {
    	int numColumns = selenium.getXpathCount("//table[@class='list']/tbody/tr/th").intValue();
		List<String> headers = new ArrayList<String>(numColumns);
		
		for (int i = 2; i <= numColumns; i++) {
			String headerXpath = "//table[@class='list']/tbody/tr/th[" + i + "]";
			headers.add(selenium.getText(headerXpath));
		}
		
		return headers;
	}
	
	protected void clickNavOption(String navOption) {
        clickNavOption(navOption, DEFAULT_TIMEOUT, true);
	}

    protected void clickNavOption(String navOption, boolean waitForPageToLoad) {
        clickNavOption(navOption, DEFAULT_TIMEOUT, waitForPageToLoad);
    }

    protected void clickNavOption(String navOption, String timeout) {
        clickNavOption(navOption, timeout, true);
    }

	protected void clickNavOption(String navOption, String timeout, boolean waitForPageToLoad) {
        if (getActiveNavOption().equals(navOption)) {
            return;
        }
		selenium.click("//ul[contains(@class,'options')]//a[contains(., '"+ navOption +"')]");

        if (waitForPageToLoad)
		    waitForPageToLoad(timeout);
	}

    protected String getActiveNavOption() {
        return selenium.getText("//ul[contains(@class,'options')]/li[contains(@class, 'selected')]").trim();
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
		return selenium.getText("//ul[contains(@class,'options')]/li[contains(@class,'selected')]").trim();
	}

	public OrgPicker getOrgPicker() {
		return new OrgPicker(selenium);
	}
	
	public LocationPicker getLocationPicker(){
		return new LocationPicker(selenium);
	}
	
	public AssetPage search(String criteria) {
		selenium.type("//input[@id='searchText']", criteria);
		selenium.click("//input[@id='smartSearchButton']");
		return new AssetPage(selenium);
	}
	
	public SmartSearchResultsPage searchWithMultipleResults(String criteria){
		selenium.type("//input[@id='searchText']", criteria);
		selenium.click("//input[@id='smartSearchButton']");
		return new SmartSearchResultsPage(selenium);
	}
	
	public void setCheckBoxValue(String locator, boolean checked) {
		if(checked){
			selenium.check(locator);
		} else {
			selenium.uncheck(locator);
		}
	}

    public void checkAndFireClick(String checkboxLocator) {
        selenium.check(checkboxLocator);
        selenium.fireEvent(checkboxLocator, "click");
    }

    public void forceSessionTimeout() {
        selenium.deleteAllVisibleCookies();
		selenium.runScript("testSession();");
        new ConditionWaiter(new Predicate(){
            @Override
            public boolean evaluate() {
                return isSessionExpiredLightboxVisible();
            }
        }).run("Session Expired lightbox never appeared.");
	}

	public boolean isSessionExpiredLightboxVisible() {
		String sessionExpiredLightboxLocator = "xpath=//DIV[@class='lv_Title' and contains(text(),'Session Expired')]";
		return selenium.isElementPresent(sessionExpiredLightboxLocator) && selenium.isVisible(sessionExpiredLightboxLocator);
	}
	
	public String getPageTitleText() {
		return selenium.getText("//div[@id='contentTitle']/h1");
	}
	
	protected void assertTitleAndTab(String expectedTitle, String expectedTab) {
		String currentTitle = getPageTitleText();
		assertThat(String.format("Expected title [%s], Current title [%s]", expectedTitle, currentTitle), currentTitle, containsString(expectedTitle));
		
		
		String currentTab = getCurrentTab();
		assertEquals(String.format("Expected tab [%s], Current tab [%s]", expectedTab, currentTab), expectedTab, currentTab);
	}
	
	protected void throwExceptionOnFormError(boolean waitForPageToLoad) {
		if (waitForPageToLoad)
			waitForPageToLoad();
		
		List<String> formErrors = getFormErrorMessages();
		if (!formErrors.isEmpty()) {
			throw new PageErrorException(formErrors);
		}
	}
}
