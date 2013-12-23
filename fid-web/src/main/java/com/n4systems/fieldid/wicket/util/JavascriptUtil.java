package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.ajax.AjaxRequestTarget;


public class JavascriptUtil {

    public void scrollToTop(AjaxRequestTarget target) {
        target.appendJavaScript("$('html, body').animate({scrollTop: 0}, 100);");
    }
}
