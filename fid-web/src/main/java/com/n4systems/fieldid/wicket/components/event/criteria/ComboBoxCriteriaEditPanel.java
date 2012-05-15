package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.ComboBox;
import com.n4systems.model.ComboBoxCriteriaResult;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ComboBoxCriteriaEditPanel extends Panel {
    
    private ComboBox comboBox;

    public ComboBoxCriteriaEditPanel(String id, IModel<ComboBoxCriteriaResult> result) {
        super(id);

        comboBox = new ComboBox("comboBox", new PropertyModel<String>(result, "value"), new PropertyModel<List<String>>(result, "criteria.options"));
        comboBox.add(new UpdateComponentOnChange());
        add(comboBox);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnDomReadyJavaScript("new toCombo('"+comboBox.getInputName()+"');");
    }
}
