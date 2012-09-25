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
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import rfid.web.helper.SessionUser;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateTimePicker extends Panel {

    private static final String UPDATE_JS = "$.datepicker.%s($('#%s')[0]);";
    private static final String SET_DATE_JS = "$('#%s').datepicker('setDate',%s);";
    private static final String JS_DATE = "new Date(%d,%d,%d)";
    private static final String JS_DATE_WITH_TIME = "new Date(%d,%d,%d,%d,%d,%d)";

    private DateTextField dateTextField;
    private CheckBox allDayCheckbox;
    private boolean includeTime;
    private boolean allDay = true;
    private Integer monthsDisplayed = 3;
    private boolean performSetDateOnInitialization = true;

    public DateTimePicker(String id, IModel<Date> dateModel) {
        this(id, dateModel, false);
    }

    public DateTimePicker(String id, IModel<Date> dateModel, boolean includeTime) {
        super(id);

        this.includeTime = includeTime;

        setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);

        add(dateTextField = new DateTextField("dateField", dateModel) {
            // need to make the format dynamic since we can toggle the "includeTime" attribute after component created.
            @Override public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>)new DateConverter() {
                    @Override public DateFormat getDateFormat(Locale locale) {
                        if (locale == null) {
                            locale = Locale.getDefault();
                        }
                        SimpleDateFormat format = new SimpleDateFormat(DateTimePicker.this.getDateFormat(), locale);
                        return format;
                    }
                    // strip off hours if "allDay"
                    @Override public Date convertToObject(String value, Locale locale) {
                        Date date = super.convertToObject(value, locale);
                        return allDay ?
                            new LocalDate(date).toDate() :
                            date;
                    }
                };
            }

        });
        dateTextField.setOutputMarkupId(true);

        add(allDayCheckbox = new AjaxCheckBox("allDay", new PropertyModel<Boolean>(this, "allDay")) {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                String js = String.format(UPDATE_JS,
                        allDay ? "_disableTimepickerDatepicker" : "_enableTimepickerDatepicker",
                        dateTextField.getMarkupId());
                target.add(DateTimePicker.this);
                target.appendJavaScript(js);
            }
        });

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
        return  includeTime && !allDay ? sessionUser.getDateTimeFormat() : sessionUser.getDateFormat();
    }

    protected String getClassModel() {
        // XXX : these extra clases seem rendunant...if i have "datetimepicker" why do i need "dateTime"???
        return includeTime ? "datetimepicker dateTime" : "datepicker date";
    }

    private IModel<?> getEnabledModel() {
		return new Model<String>() {
			@Override public String getObject() {
				return isEnabled() ? "" : "datetimepicker-disabled";
			}
		};
	}

    public DateTimePicker withMonthsDisplayed(int months) {
        monthsDisplayed = months>0 ? months : null;
        return this;
    }

    public DateTimePicker withNoAllDayCheckbox() {
        allDay = false;
        allDayCheckbox.setVisible(false);
        return this;
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
        jsBuffer.append("jQuery('#"+dateTextField.getMarkupId()+"')."+getJSMethod()+"(" + options + ");");

        if (allDay && includeTime) {
            jsBuffer.append(String.format(UPDATE_JS,
                    "_disableTimepickerDatepicker", dateTextField.getMarkupId()));
        }

        if (performSetDateOnInitialization) {
            jsBuffer.append(String.format(SET_DATE_JS, dateTextField.getMarkupId(), getModelDateForJS()));
        }

        return jsBuffer.toString();
    }

    private String getJSMethod() {
        return includeTime ? "datetimepicker" : "datepicker";
    }

    private String getModelDateForJS() {
        Date date = dateTextField.getModelObject();
        if (date==null) {
            return "null";
        } else {
            if (includeTime) {
                LocalDateTime ld = new LocalDateTime(date);
                return String.format(JS_DATE_WITH_TIME, ld.getYear(), ld.getMonthOfYear() - 1, ld.getDayOfMonth(), ld.getHourOfDay(), ld.getMinuteOfHour(), ld.getSecondOfMinute() );
            } else {
                LocalDate ld = new LocalDate(date);
                return String.format(JS_DATE,ld.getYear(), ld.getMonthOfYear()-1, ld.getDayOfMonth());
            }
        }
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

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    // JSON object for javascript widget.
    class DateTimePickerOptions implements Serializable {
        String showOn = "button";
        String buttonImage = "/fieldid/images/calendar-icon.png";
        Boolean buttonImageOnly = true;
        Integer numberOfMonths = monthsDisplayed;
        Boolean showButtonPanel = true;
        String dateFormat = FieldIDSession.get().getSessionUser().getJqueryDateFormat();
        Boolean changeMonth = true;
        Boolean changeYear = true;
        Boolean ampm = null;
        String timeFormat = null;

        public DateTimePickerOptions() {
            if (!allDay && includeTime) {
                ampm = true;
                timeFormat = "hh:mm TT";
            }
        }
    }

    public DateTimePicker withoutPerformSetDateOnInitialization() {
        performSetDateOnInitialization = false;
        return this;
    }

}
