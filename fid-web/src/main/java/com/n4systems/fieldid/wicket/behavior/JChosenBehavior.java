package com.n4systems.fieldid.wicket.behavior;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

public class JChosenBehavior extends Behavior {

    @Override
    public void bind(Component component) {
        // NOTE : WEB-3324 most of the calls generating the error message come from here.
        // .: it is important that if you use this behavior you do it in onIntialize().
        // even better would be to refactor this "nullValid" feature out of this method.
        String nullString = component.getString("nullValid");
        if (StringUtils.isBlank(nullString)) {
            nullString = " ";
        }
        component.setOutputMarkupId(true);
        component.add(new AttributeAppender("data-placeholder", Model.of(nullString), " "));
        component.add(new AttributeAppender("class", Model.of("chzn-select"), " "));
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/chosen/chosen.jquery.js");
        response.renderCSSReference("style/plugins/chosen/chosen.css");
        response.renderOnDomReadyJavaScript("$('#"+component.getMarkupId()+"_chzn').remove();");
        response.renderOnDomReadyJavaScript("$('#"+component.getMarkupId()+"').chosen({ disable_search_threshold: 15, allow_single_deselect:"+isNullValidFor(component)+"});");
    }

    private boolean isNullValidFor(Component component) {
        if (component instanceof  DropDownChoice) {
            return ((DropDownChoice) component).isNullValid();
        }
        return false;
    }
}
