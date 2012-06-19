package com.n4systems.fieldid.wicket.components;

import com.google.gson.Gson;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import rfid.web.helper.SessionUser;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class DateTimePicker extends Panel {

    private DateTextField dateTextField;
    private CheckBox allDayCheckbox;
    private boolean includeTime;
    private boolean allDay;
    private IModel<Date> model;

    public DateTimePicker(String id, IModel<Date> dateModel) {
        this(id, dateModel, false);
    }

    public DateTimePicker(String id, IModel<Date> dateModel, boolean includeTime) {
        super(id);

        this.includeTime = includeTime;
        this.allDay = false;
        this.model = dateModel;

        setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);

        add(dateTextField = new DateTextField("dateField", dateModel) {
            @Override public String getTextFormat() {
                return getDateFormat();
            }
        });
        dateTextField.setOutputMarkupId(true);

        add(allDayCheckbox = new AjaxCheckBox("allDay", new PropertyModel<Boolean>(this,"allDay")) {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                target.add(DateTimePicker.this);
            }
        }
        );

        allDayCheckbox.setVisible(includeTime);

        getDateTextField().setOutputMarkupId(true);

        ValidationBehavior.addValidationBehaviorToComponent(dateTextField);

        dateTextField.add(new AttributeModifier("title", getDateFormat()) {
            @Override
            public boolean isEnabled(Component component) {
                // The format should only be put as the title if there are no validation errors for this component.
                // Otherwise, the title should consist of the error message
                return !FieldIDSession.get().getFeedbackMessages().hasMessageFor(component, FeedbackMessage.ERROR);
            }
        });
        dateTextField.add(new AttributeAppender("class", getEnabledModel(), " " ));
        dateTextField.add(new AttributeAppender("class", Model.of(getClassModel()), " "));
    }

    private String getDateFormat() {
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        return  includeTime ? sessionUser.getDateTimeFormat() : sessionUser.getDateFormat();
    }

    protected String getClassModel() {
        // XXX : these extra clases seem rendunant...if i have "datetimepicker" why do i need "dateTime"???
        return includeTime ? "datetimepicker dateTime" : "datepicker date";
    }

    protected String getDatePickerMethod() {
        // NOTE : this is coincidentally the same as the css class of the widget but that is not guaranteed.
        return includeTime && !allDay ? "datetimepicker" : "datepicker";
    }

    private IModel<?> getEnabledModel() {
		return new Model<String>() {
			@Override public String getObject() {
				return isEnabled() ? "" : "datetimepicker-disabled";
			}
		};
	}

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected void onBeforeRender() {
    	super.onBeforeRender();
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

        response.renderCSSReference("style/datetimepicker.css");
        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderJavaScriptReference("javascript/jquery-ui-timepicker-addon.js");
    }

    private String getInitDatePickerJS() {
        StringBuffer jsBuffer = new StringBuffer();

        String options = new Gson().toJson(new DateTimePickerOptions());
        jsBuffer.append("jQuery('#"+dateTextField.getMarkupId()+"')." + getDatePickerMethod() + "(" + options + ");");

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

    public DateTimePicker setIncludeTime(boolean includeTime) {
        this.includeTime = includeTime;
        return this;
    }

    public boolean isIncludeTime() {
        return includeTime;
    }

    public boolean isAllDay() {
        return allDay;
    }

    // JSON object for javascript widget.
    class DateTimePickerOptions implements Serializable {
        String showOn = "button";
        String buttonImage = "/fieldid/images/calendar-icon.png";
        Boolean buttonImageOnly = true;
        Integer numberOfMonths = 3;
        Boolean showButtonPanel = true;
        String dateFormat = "mm/dd/y";
        Boolean changeMonth = true;
        Boolean changeYear = true;
        Boolean ampm = null;
        String timeFormat = null;//"hh:mm TT";

        public DateTimePickerOptions() {
            if (!allDay && includeTime) {
                ampm = true;
                timeFormat = "hh:mm TT";
            }
        }
    }

}




