package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.Date;


// TODO : for now i have made two different date pickers. one for inline, one for popup.
//  these should be merged/extended into single class hierarchy.
public class Calendar extends Panel {

    protected Component calendar;

    public Calendar(String id, IModel<Date> dateModel) {
        super(id);

        add(calendar = new WebMarkupContainer("dateField").setOutputMarkupId(true));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnLoadJavaScript(getInitDatePickerJS());

        // CAVEAT : the reason a special (datepicker only) version of jquery ui was brought in
        //  is because if you referenced the entire ui library it would conflict the use of some
        //  wiquery ui things.  (AutoComplete in this case).
        // the best situation would be just to have all components use the predefined wiquery js references
        //  i.e. renderJavaScriptReference(CoreUIJavaScriptResourceReference.get());
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");

        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderJavaScriptReference("javascript/jquery-ui-timepicker-addon.js");
        response.renderJavaScriptReference("javascript/component/agenda.js");
    }

    protected String getInitDatePickerJS() {
        StringBuffer jsBuffer = new StringBuffer();

        jsBuffer.append("calendar.init('"+ calendar.getMarkupId()+"');");

        return jsBuffer.toString();
    }

}




