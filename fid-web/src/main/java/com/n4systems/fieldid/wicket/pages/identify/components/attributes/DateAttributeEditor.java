package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.Date;

public class DateAttributeEditor extends FormComponentPanel<InfoOptionBean> {

    Date selectedDate;

    public DateAttributeEditor(String id, IModel<InfoOptionBean> infoOption) {
        super(id, infoOption);
        InfoFieldBean infoField = infoOption.getObject().getInfoField();
        boolean includeTime = infoField.isIncludeTime();

        String textField = infoOption.getObject().getName();

        if (textField != null) {
            try {
                Long longValue = Long.valueOf(textField);
                if (longValue > 0L) {
                    selectedDate = new Date(longValue);
                }
            } catch(NumberFormatException e) { }
        }


        DateTimePicker picker = new DateTimePicker("datePicker", new UserToUTCDateModel(new PropertyModel<Date>(this, "selectedDate")));
        add(picker.setIncludeTime(includeTime).withNoAllDayCheckbox().withoutPerformSetDateOnInitialization());
        ValidationBehavior.addValidationBehaviorToComponent(picker.getDateTextField());
        picker.getDateTextField().add(new ValidateIfRequiredValidator<Date>(infoField));
        picker.getDateTextField().add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                InfoOptionBean option = getModel().getObject();
                if (option.isStaticData()) {
                    throw new IllegalStateException("Attempt to edit static option -- not allowed");
                }
                if (selectedDate == null) {
                    option.setName(null);
                } else {
                    option.setName(selectedDate.getTime() + "");
                }
            }
        });
    }

    @Override
    protected void convertInput() {
        InfoOptionBean option = getModel().getObject();
        if (option.isStaticData()) {
            throw new IllegalStateException("Attempt to edit static option -- not allowed");
        }
        if (selectedDate == null) {
            option.setName(null);
        } else {
            option.setName(selectedDate.getTime() + "");
        }
        setConvertedInput(option);
    }

}
