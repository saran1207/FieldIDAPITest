package com.n4systems.fieldid.wicket.components;

import com.google.gson.Gson;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.joda.time.LocalDate;
import rfid.web.helper.SessionUser;

import java.io.Serializable;
import java.util.Date;


// TODO : for now i have made two different date pickers. one for inline, one for popup.
//  these should be merged/extended into single class hierarchy.
public class DateTimePicker2 extends Panel {

    private static final String UPDATE_JS = "$.datepicker.%s($('#%s')[0]);";
    private static final String CLEAR_DATE_JS = "$('#%s').datepicker('setDate',%s);";
    private static final String JS_DATE = "new Date(%d,%d,%d)";

    protected Component dateTextField;
    protected Integer monthsDisplayed = 1;
    protected boolean enableChangeListener = true;

    public DateTimePicker2(String id, IModel<Date> dateModel) {
        super(id);

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        add(dateTextField = new WebMarkupContainer("dateField").setOutputMarkupId(true));
        if (enableChangeListener) {
        }

        dateTextField.add(new AttributeModifier("title", getDateFormat()) {
            @Override
            public boolean isEnabled(Component component) {
                // The format should only be put as the title if there are no validation errors for this component.
                // Otherwise, the title should consist of the error message
                return !FieldIDSession.get().getFeedbackMessages().hasMessageFor(component, FeedbackMessage.ERROR);
            }
        });
        dateTextField.add(new AttributeAppender("class", getEnabledModel(), " "));
        dateTextField.add(new AttributeAppender("class", Model.of(getClassModel()), " "));

        dateTextField.setOutputMarkupId(true);

        ValidationBehavior.addValidationBehaviorToComponent(dateTextField);

    }

    protected void onDateTextFieldUpdate(AjaxRequestTarget target) {
        ;; // do nothing..override if desired.
    }

    private String getDateFormat() {
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        return  sessionUser.getDateFormat();
    }

    protected String getClassModel() {
        return "datepicker date";
    }

    private IModel<?> getEnabledModel() {
        return new Model<String>() {
            @Override public String getObject() {
                return isEnabled() ? "" : "datetimepicker-disabled";
            }
        };
    }

    public DateTimePicker2 withChangeListenerDisabled() {
        this.enableChangeListener = false;
        return this;
    }

    public DateTimePicker2 withMonthsDisplayed(int months) {
        monthsDisplayed = months>0 ? months : null;
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

        String options = new Gson().toJson(createOptions());
        jsBuffer.append("jQuery('#"+dateTextField.getMarkupId()+"')."+getJSMethod()+"(" + options + ");");

        return jsBuffer.toString();
    }

    protected DateTimePickerOptions createOptions() {
        return new DateTimePickerOptions();
    }

    private String getJSMethod() {
        return "datepicker";
    }

    private String getModelDateForJS() {
        Date date = LocalDate.now().toDate();
        if (date==null) {
            return "null";
        } else {
            LocalDate ld = new LocalDate(date);
            return String.format(JS_DATE,ld.getYear(), ld.getMonthOfYear()-1, ld.getDayOfMonth());
        }
    }

    @Override
    public MarkupContainer addOrReplace(Component... childs) {
        return super.addOrReplace(childs);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Component addToDateField(Behavior... behaviors) {
        return dateTextField.add(behaviors);
    }

    // JSON object for javascript widget.
    public class DateTimePickerOptions implements Serializable {
        String showOn = "button";
        String buttonImage = "/fieldid/images/calendar-icon.png";
        Boolean buttonImageOnly = true;
        Integer numberOfMonths = monthsDisplayed;
        String dateFormat = FieldIDSession.get().getSessionUser().getJqueryDateFormat();
        Boolean changeMonth = false;
        Boolean showButtonPanel = false;
        Boolean changeYear = false;
        Boolean ampm = null;
        Boolean showOtherMonths = true;
        Boolean selectOtherMonths = true;
        String timeFormat = null;//"hh:mm TT";

        public DateTimePickerOptions() {
        }
    }


}




