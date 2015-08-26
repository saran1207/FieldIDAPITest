package com.n4systems.fieldid.wicket.behavior;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class ConfirmNavigationBehavior extends Behavior {

    private IModel<String> confirmationMessage;

    public ConfirmNavigationBehavior(IModel<String> confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        String escapedMessage = StringEscapeUtils.escapeJavaScript(confirmationMessage.getObject());
        response.renderJavaScript("var promptBeforeLeaving = true;" +
                "window.onbeforeunload = function() {"
                + "if (promptBeforeLeaving)"
                + "return '" + escapedMessage + "';"
                + "}", null);

    }
}
