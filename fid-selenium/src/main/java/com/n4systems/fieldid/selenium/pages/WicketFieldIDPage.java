package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

public class WicketFieldIDPage extends FieldIDPage {

    public WicketFieldIDPage(Selenium selenium) {
        super(selenium);
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

}
