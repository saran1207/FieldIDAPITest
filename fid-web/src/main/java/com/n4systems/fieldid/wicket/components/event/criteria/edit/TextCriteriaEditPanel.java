package com.n4systems.fieldid.wicket.components.event.criteria.edit;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.TextFieldCriteriaResult;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class TextCriteriaEditPanel extends Panel {

    public TextCriteriaEditPanel(String id, IModel<TextFieldCriteriaResult> resultModel) {
        super(id);

        TextField<String> textField = new TextField<String>("textField", new PropertyModel<String>(resultModel, "value"));
        textField.add(new UpdateComponentOnChange());
        add(textField);
    }

}
