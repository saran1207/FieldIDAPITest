package com.n4systems.fieldid.wicket.behavior;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * @see http://code.google.com/p/jquery-watermark/ for jquery plugin that does the work.
 * note that the styling is a bit mucked up because you have to account for all browsers and you can't define them in the
 * same class - this means duplicating styles. yuch.
 * (when a browser doesn’t understand a selector, it invalidates the entire line of selectors except in IE 7).
 *
 * if you don't specify a className, then...
 *      .watermark                              IE
 *      input::-webkit-input-placeholder        WebKit
 *      input:-moz-placeholder                  Firefox
 *  the above styles will be used.
 */
public class Watermark extends Behavior {
    
    private String prompt;
    private String cssClass;

    public Watermark(String prompt) {
        this.prompt = prompt;
    }

    public Watermark(String cssClass, String prompt) {
        this.cssClass = cssClass;
        this.prompt = prompt;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        component.setOutputMarkupId(true);
        response.renderJavaScriptReference("javascript/jquery/watermark/jquery.watermark.js");
        String waterMarkOptions = "";
        if (StringUtils.isNotBlank(cssClass)) {
               waterMarkOptions = ",{className="+cssClass+"}";
        }
        response.renderOnLoadJavaScript("$('#"+component.getMarkupId()+"').watermark('"+prompt+"'"+waterMarkOptions+");");
    }
}
