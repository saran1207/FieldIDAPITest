package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;

public class OneClickOnlyDecorator implements IAjaxCallDecorator {

    private static final String latch = "var locked = this.hasAttribute('data-wicket-blocked');" +
            " if (locked) { return false; } " +
            " this.setAttribute('data-wicket-blocked', 'data-wicket-blocked');";
    private static final String reset = "this.removeAttribute('data-wicket-blocked');";

    @Override
    public CharSequence decorateScript(Component component, CharSequence script) {
        return latch + script;
    }

    @Override
    public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
        return reset + script;
    }

    @Override
    public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
        return reset + script;
    }

}
