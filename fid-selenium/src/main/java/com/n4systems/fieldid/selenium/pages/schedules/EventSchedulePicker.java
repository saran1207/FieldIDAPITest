package com.n4systems.fieldid.selenium.pages.schedules;

import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.thoughtworks.selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class EventSchedulePicker extends WebEntity{

    public EventSchedulePicker(Selenium selenium) {
        super(selenium);
        if (!checkEventScheduleModalWindowOpen()) {
            fail("Expected to be on event schedule page!");
        }
    }

    private boolean checkEventScheduleModalWindowOpen() {
        return selenium.isElementPresent("//div[@class='wicket-modal']//div[@class='schedulePickerTitle']") ;
    }

    public void clickCreateSchedule() {
        selenium.click("//div[@class='wicket-modal']//div[contains(@class,'formActions')]//span[contains(., 'Create Schedule')]/..");
        selenium.waitForCondition("var value = !selenium.isElementPresent('//div[@class=\\'wicket-modal\\']') || selenium.isElementPresent('//div[@class=\\'wicket-modal\\']//li[@class=\\'errorMessage\\']'); value == true", AJAX_TIMEOUT);
    }

    public void clickSaveSchedule() {
        selenium.click("//div[@class='wicket-modal']//div[contains(@class,'formActions')]//span[contains(., 'Save')]/..");
        selenium.waitForCondition("var value = !selenium.isElementPresent('//div[@class=\\'wicket-modal\\']') || selenium.isElementPresent('//div[@class=\\'wicket-modal\\']//li[@class=\\'errorMessage\\']'); value == true", AJAX_TIMEOUT);
    }

    public void setSchedule(String date, String eventType, String job) {
        if (date != null) {
            selenium.type("//input[@name='datePicker:dateField']", date);
        }
        if (eventType != null) {
            selenium.select("//select[@name='eventTypeSelect']", eventType);
        }
        if (job != null) {
            selenium.select("//select[@name='jobSelectContainer:jobSelect']", job);
        }
    }

    public List<String> getFormErrorMessages() {
        List<String> result = new ArrayList<String>();

        int maxIndex = selenium.getXpathCount("//div[@class='wicket-modal']//li[@class='errorMessage']").intValue();
        for(int i = 1; i <= maxIndex; i++) {
            String iterableErrorMessageLocator = "//div[@class='wicket-modal']//ul/li["+i+"]/*[@class='errorMessage']";
            String s = selenium.getText(iterableErrorMessageLocator);
            result.add(s);
        }
        return result;
    }
}
