package com.n4systems.fieldid.wicket.ajax;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;

public class ConfirmAjaxCallDecorator extends AjaxCallDecorator {

    private String message;

    public ConfirmAjaxCallDecorator(String message) {
        this.message = message;
    }

    @Override
    public CharSequence decorateScript(Component c, CharSequence script) {
        return new StringBuilder("if(!confirm('").append(message).append( "')) { return false; };").append(script);
    }

}
