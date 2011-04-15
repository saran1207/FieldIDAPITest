package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class MyAccountPage extends FieldIDPage {

	public MyAccountPage(Selenium selenium) {
		super(selenium);
		if(!checkOnMyAccountPage()){
			fail("Expected to be on My Account page!");
		}
	}

	public boolean checkOnMyAccountPage() {
		return selenium.isElementPresent("//div[@id='contentTitle']/h1[contains(text(),'My Account')]");
	}

	public void deleteReport(String name) {
		selenium.click("//tr/td/a[text()='" + name + "']/../..//a[text()='Delete']");
		waitForPageToLoad();
	}

	public EmailNotificationsListPage clickEmailNotifications(){
		selenium.click("//div[@id='contentHeader']/ul/li[2]/a");
		return new EmailNotificationsListPage(selenium);
	}

    // Refer Field ID Tab
    public void clickReferFieldID() {
        clickNavOption("Refer Field ID");
    }

    public String getReferralLink() {
        return selenium.getText("//div[@id='referralLinkBox']/p");
    }

    public String getReferralCode() {
        String refLink = getReferralLink();
        int lastSlashIndex = refLink.lastIndexOf("/");
        return refLink.substring(lastSlashIndex+1);
    }

	public int getNumberOfRowsInReferrerTable() {
		int numRows = selenium.getXpathCount("//div[@id='referralsBottom']/table//tr").intValue();
        if (numRows > 0) {
            // Subtract one from the count for the headers
            numRows --;
        }
        return numRows;
	}

    public List<String> getCompanyNamesInReferralTable() {
        List<String> companyNames = new ArrayList<String>();
        for (int i = 1; i <= getNumberOfRowsInReferrerTable(); i++) {
            companyNames.add(selenium.getText("//div[@id='referralsBottom']/table//tr["+(i+1)+"]/td[1]"));
        }
        return companyNames;
    }

	public void clickDownloads() {
		clickNavOption("Downloads");
		
	}
	
	public int getNumberOfItemsInDownloadList() {
		if (!selenium.isElementPresent("//table[@class='list']")) {
			return 0;
		}
		return selenium.getXpathCount("//table[@class='list']//tr").intValue() - 1;
	}

	public void editDownLoadName(int rowNum) {
		selenium.click("//tr[" + (rowNum + 1) + "]//a[.='Edit']");
		waitForAjax();
	}
	
	public void enterName(String name) {
		selenium.type("//input[@id='downloadLinkName']", name);
	}
	
	public void clickSave() {
		selenium.click("//a[.='Save']");
		waitForAjax();
	}

	public List<String> getDownloadNames() {
		return collectTableValuesUnderCellForCurrentPage(2, 2, "span");
	}

	public void clickCancel() {
		selenium.click("//a[.='Cancel']");
		waitForAjax();
	}

	public void clickDelete(int rowNum) {
		selenium.click("//tr[" + (rowNum + 1) + "]//a[.='Delete']");
		waitForPageToLoad();
	}
}
