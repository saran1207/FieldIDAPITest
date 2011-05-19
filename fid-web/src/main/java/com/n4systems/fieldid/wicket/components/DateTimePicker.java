package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import rfid.web.helper.SessionUser;

import java.util.Date;

public class DateTimePicker extends Panel {

    private DateTextField dateTextField;
    private WebMarkupContainer datePickerImage;
    private String otherDateFormat;
    private boolean includeTime;

    public DateTimePicker(String id, IModel<Date> dateModel) {
        this(id, dateModel, false);
    }

    public DateTimePicker(String id, IModel<Date> dateModel, boolean includeTime) {
        super(id);

        setRenderBodyOnly(true);
        addJsAndStylesheets();

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        this.includeTime = includeTime;
        this.otherDateFormat = includeTime ? sessionUser.getOtherDateTimeFormat() : sessionUser.getOtherDateFormat();

        String timeZoneName = sessionUser.getTimeZoneName();
        String dateFormat = includeTime ? sessionUser.getDateTimeFormat() : sessionUser.getDateFormat();

        add(dateTextField = new DateTextField("dateField", dateModel, dateFormat));
        add(new Label("timeZoneLabel", timeZoneName).setVisible(includeTime));
        add(datePickerImage = new WebMarkupContainer("datePickerImage"));

        dateTextField.setOutputMarkupId(true);
        datePickerImage.setOutputMarkupId(true);

        ValidationBehavior.addValidationBehaviorToComponent(dateTextField);

        Model<String> classModel = new Model<String>(includeTime ? "dateTime" : "date");
        dateTextField.add(new AttributeAppender("class", true, classModel, " "));
        dateTextField.add(new SimpleAttributeModifier("title", includeTime ? sessionUser.getDisplayDateTimeFormat() : sessionUser.getDisplayDateFormat()) {
            @Override
            public boolean isEnabled(Component component) {
                // The format should only be put as the title if there are no validation errors for this component.
                // Otherwise, the title should consist of the error message
                return !FieldIDSession.get().getFeedbackMessages().hasMessageFor(component, FeedbackMessage.ERROR);
            }
        });
    }

    private void addJsAndStylesheets() {
        add(CSSPackageResource.getHeaderContribution("style/calendar-blue.css"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/calendar.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/lang/calendar-en.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/calendar-setup.js"));
    }

    @Override
    public void renderHead(HtmlHeaderContainer container) {
        super.renderHead(container);
        container.getHeaderResponse().renderOnDomReadyJavascript(createSetupCalendarJavascript());
    }

    private String createSetupCalendarJavascript() {
        StringBuffer jsBuffer = new StringBuffer();

        jsBuffer.append("Calendar.setup( {");
        jsBuffer.append("inputField: \"").append(dateTextField.getMarkupId()).append("\",");
        jsBuffer.append("ifFormat: \"").append(otherDateFormat).append("\",");
        jsBuffer.append("daFormat: \"").append(otherDateFormat).append("\",");
        jsBuffer.append("button: \"").append(datePickerImage.getMarkupId()).append("\",");
        jsBuffer.append("showsTime: ").append(includeTime).append(",");
        jsBuffer.append("timeFormat: \"12\"");
        jsBuffer.append("} );");

        return jsBuffer.toString();
    }
}
