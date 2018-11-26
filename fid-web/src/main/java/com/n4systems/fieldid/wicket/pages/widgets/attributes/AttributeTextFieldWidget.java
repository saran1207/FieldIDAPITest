package com.n4systems.fieldid.wicket.pages.widgets.attributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;


/**
 * Reusable widget to display/modify an attribute.
 */
public class AttributeTextFieldWidget extends Panel {

    private InfoFieldBean infoFieldBean;
    private InfoOptionInput infoOptionInput;

    /**
     *
     * @param id
     * @param infoFieldBean the attribute
     * @param infoOptionInput the value (in the 'name' field)
     */
    public AttributeTextFieldWidget(String id, InfoFieldBean infoFieldBean, InfoOptionInput infoOptionInput) {
        super(id);
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInput = infoOptionInput;
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

        TextField<String> inputValueField = new TextField("inputValue", new PropertyModel(infoOptionInput, "name"));
        add(inputValueField);
    }

}
