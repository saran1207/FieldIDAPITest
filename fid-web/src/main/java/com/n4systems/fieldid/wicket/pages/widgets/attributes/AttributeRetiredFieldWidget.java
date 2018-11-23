package com.n4systems.fieldid.wicket.pages.widgets.attributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import rfid.ejb.entity.InfoFieldBean;


/**
 * Created by agrabovskis on 2018-11-14.
 */
public class AttributeRetiredFieldWidget extends Panel {

    private InfoFieldBean infoFieldBean;
    private InfoOptionInput infoOptionInput;

    public AttributeRetiredFieldWidget(String id, InfoFieldBean infoFieldBean, InfoOptionInput infoOptionInput) {
        super(id);
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInput = infoOptionInput;
        addComponents();
    }

    private void addComponents() {

        String infoOptionInputName = "";
        if (infoOptionInput != null) {
            infoOptionInputName = infoOptionInput.getName();
            }

        add(new Label("fieldName", infoFieldBean.getName()));
        add(new Label("optionName", infoOptionInputName));
    }
}
