package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.fieldid.wicket.components.DateTimePicker;
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

        add(new DateTimePicker("datePicker", new PropertyModel<Date>(this, "selectedDate")).setIncludeTime(includeTime).withNoAllDayCheckbox());
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
