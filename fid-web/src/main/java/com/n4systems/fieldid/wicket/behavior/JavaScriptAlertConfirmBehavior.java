package com.n4systems.fieldid.wicket.behavior;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class JavaScriptAlertConfirmBehavior extends Behavior {

    private IModel<String> confirmationMessage;

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
    }

    public JavaScriptAlertConfirmBehavior(IModel<String> confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        String escapedMessage = StringEscapeUtils.escapeJavaScript(confirmationMessage.getObject());
        response.renderOnDomReadyJavaScript("$('#"+component.getMarkupId()+"').click(function(e) { e.preventDefault(); var linkLocation = $(this).attr('href'); "
                + " if (confirm('"+escapedMessage+"')) { window.location = linkLocation; }  });");
    }

}
