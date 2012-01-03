package com.n4systems.fieldid.selenium.pages;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.selenium.Selenium;

public class WicketFieldIDPage extends FieldIDPage {

    public WicketFieldIDPage(Selenium selenium) {
        super(selenium);
    }
    
    public WicketFieldIDPage(Selenium selenium, boolean waitForLoad) {
        super(selenium, waitForLoad);
    }

    protected void waitForWicketAjax() {
        waitForWicketAjax(DEFAULT_TIMEOUT);
    }

    protected void waitForWicketAjax(String timeout) {
        String defineWicketAjaxBusy = String.format(
                        "wicketAjaxBusy = function () {"
                                        + "for (var channelName in %1$s) {"
                                        + "if (%1$s[channelName].busy) { return true; }" + "}"
                                        + "return false;};",
                        "selenium.browserbot.getCurrentWindow().Wicket.channelManager.channels");
        String waitTarget = defineWicketAjaxBusy
                        + "!wicketAjaxBusy();";
        selenium.waitForCondition(waitTarget, timeout);
    }

    @Override
    public List<String> getFormErrorMessages() {
        List<String> result = new ArrayList<String>();

        int maxIndex = selenium.getXpathCount("//li[@class='errorMessage']").intValue();
        for(int i = 1; i <= maxIndex; i++) {
            String iterableErrorMessageLocator = "//ul/li["+i+"]/*[@class='errorMessage']";
            String s = selenium.getText(iterableErrorMessageLocator);
            result.add(s);
        }
        return result;
    }
}
