package com.n4systems.fieldid.wicket.pages.widgets.attributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import rfid.ejb.entity.InfoFieldBean;
import rfid.web.helper.SessionUser;

import java.util.Date;

/**
 * Reusable control for updating/display attribute value
 */
public class AttributeDateFieldWidget extends Panel {

    private InfoFieldBean infoFieldBean;
    private InfoOptionInput infoOptionInput;
    private IModel<SessionUser> sessionUserModel;

    /**
     *
     * @param id
     * @param infoFieldBean the entity we are setting a value for
     * @param infoOptionInput the current value of the entity (specified by 'name' field)
     * @param sessionUserModel
     */
    public AttributeDateFieldWidget(
            String id,
            InfoFieldBean infoFieldBean,
            InfoOptionInput infoOptionInput,
            IModel<SessionUser> sessionUserModel) {
        super(id);
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInput = infoOptionInput;
        this.sessionUserModel = sessionUserModel;

        addComponents();
    }

    private void addComponents() {

        add(new Label("required", new FIDLabelModel("indicator.required")) {
            @Override
            public boolean isVisible() {
                return infoFieldBean.isRequired();
            }
        });
        add(new Label("fieldName", infoFieldBean.getName()));

        final IModel<Date> dateFieldModel = new Model<Date>() {
            @Override
            public Date getObject() {
                if (infoOptionInput == null ||infoOptionInput.getName() == null)
                    return null;
                SessionUserDateConverter dateConverter = sessionUserModel.getObject().createUserDateConverter();
                Date date = dateConverter.convertDate(infoOptionInput.getName(), infoFieldBean.isIncludeTime());
                return date;
            }

            @Override
            public void setObject(Date object) {
                if (object instanceof Date) {
                    if (infoOptionInput != null) {
                        SessionUserDateConverter dateConverter = sessionUserModel.getObject().createUserDateConverter();
                        String date = dateConverter.convertDate((Date) object, infoFieldBean.isIncludeTime());
                        infoOptionInput.setName(date);
                    }
                }
                super.setObject(object);
            }
        };

        DateTimePicker dateField = new DateTimePicker("dateField", dateFieldModel, infoFieldBean.isIncludeTime()).withNoAllDayCheckbox();
        add(dateField);
    }

}
