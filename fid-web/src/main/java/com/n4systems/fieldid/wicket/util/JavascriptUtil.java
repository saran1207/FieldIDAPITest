package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.ajax.AjaxRequestTarget;


public class JavascriptUtil {

    public void scrollToTop(AjaxRequestTarget target) {
        target.appendJavaScript("$('html, body').animate({scrollTop: 0}, 100);");
    }

    public void fadeFeedback(AjaxRequestTarget target) {
        target.appendJavaScript("setTimeout(function() {$('.feedbackPanel').fadeOut(2000);},2000)");
    }

    public void fadeFeedback(AjaxRequestTarget target, String selector) {
        target.appendJavaScript("setTimeout(function() {$('"+selector+"').fadeOut(2000);},2000)");
    }


}
