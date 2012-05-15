package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import rfid.web.helper.SessionUser;

import java.util.Date;


@SuppressWarnings("serial")
public class DateTimePicker extends Panel {

    private DateTextField dateTextField;

    public DateTimePicker(String id, IModel<Date> dateModel) {
        this(id, dateModel, false);
    }

    public DateTimePicker(String id, IModel<Date> dateModel, boolean includeTime) {
        super(id);

        setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();

        String javaDateFormat = includeTime ? sessionUser.getDateTimeFormat() : sessionUser.getDateFormat();

        add(dateTextField = new DateTextField("dateField", dateModel, javaDateFormat));
        dateTextField.setOutputMarkupId(true);

        getDateTextField().setOutputMarkupId(true);

        ValidationBehavior.addValidationBehaviorToComponent(getDateTextField());

        Model<String> classModel = new Model<String>(includeTime ? "datetimepicker dateTime" : "datepicker date");
        dateTextField.add(new AttributeAppender("class", classModel, " "));
        dateTextField.add(new AttributeModifier("title", includeTime ? sessionUser.getDisplayDateTimeFormat() : sessionUser.getDisplayDateFormat()) {
            @Override
            public boolean isEnabled(Component component) {
                // The format should only be put as the title if there are no validation errors for this component.
                // Otherwise, the title should consist of the error message
                return !FieldIDSession.get().getFeedbackMessages().hasMessageFor(component, FeedbackMessage.ERROR);
            }
        });
        dateTextField.add(new AttributeAppender("class", getEnabledModel(), " " ));
    }

    private IModel<?> getEnabledModel() {
		return new Model<String>() {
			@Override public String getObject() {
				return isEnabled() ? "" : "datetimepicker-disabled";
			}
		};
	}

	@Override
    protected void onBeforeRender() {
    	super.onBeforeRender();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnLoadJavaScript(createSetupCalendarJavascript());

        // CAVEAT : the reason a special (datepicker only) version of jquery ui was brought in
        //  is because if you referenced the entire ui library it would conflict the use of some
        //  wiquery ui things.  (AutoComplete in this case).
        // the best situation would be just to have all components use the predefined wiquery js references
        //  like CoreUIJavaScriptResourceReference.
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.datepicker.min.js");

        response.renderCSSReference("style/datetimepicker.css");
        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderJavaScriptReference("javascript/jquery-ui-timepicker-addon.js");
    }

    private String createSetupCalendarJavascript() {
        StringBuffer jsBuffer = new StringBuffer();

        String jqueryDateFormat = FieldIDSession.get().getSessionUser().getJqueryDateFormat();
        
        jsBuffer.append("function initDatePicker(){");
        jsBuffer.append("jQuery('.datetimepicker').datetimepicker({");
        jsBuffer.append("showOn:'button',");
        jsBuffer.append("buttonImage:'/fieldid/images/calendar-icon.png',");
        jsBuffer.append("buttonImageOnly:true,");
        jsBuffer.append("numberOfMonths:3,");
        jsBuffer.append("showButtonPanel:true,");
        jsBuffer.append("dateFormat:\"").append(jqueryDateFormat).append("\",");
        jsBuffer.append("ampm: true,");
        jsBuffer.append("timeFormat: \"hh:mm TT\",");
        jsBuffer.append("changeMonth: true,");
        jsBuffer.append("changeYear: true");
        
        jsBuffer.append("});");
        jsBuffer.append("jQuery('.datepicker').datepicker({");
        jsBuffer.append("showOn:'button',");
        jsBuffer.append("buttonImage:'/fieldid/images/calendar-icon.png',");
        jsBuffer.append("buttonImageOnly:true,");
        jsBuffer.append("numberOfMonths:3,");
        jsBuffer.append("showButtonPanel:true,");
        jsBuffer.append("dateFormat:\"").append(jqueryDateFormat).append("\",");
        jsBuffer.append("changeMonth: true,");
        jsBuffer.append("changeYear: true");
        jsBuffer.append("});");
        jsBuffer.append("}");
        jsBuffer.append("initDatePicker();");

        return jsBuffer.toString();
    }

	public DateTextField getDateTextField() {
		return dateTextField;
	}

    public void clearInput() {
        dateTextField.clearInput();
    }

    public Component addToDateField(Behavior... behaviors) {
        return dateTextField.add(behaviors);
    }

}
