package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.SelectCriteriaResult;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SelectCriteriaEditPanel extends Panel {

    public SelectCriteriaEditPanel(String id, IModel<SelectCriteriaResult> result) {
        super(id);

        DropDownChoice<String> selectField = new DropDownChoice<String>("selectField", new PropertyModel<String>(result, "value"), new PropertyModel<List<String>>(result, "criteria.options"));
        selectField.setNullValid(true);
        selectField.add(new UpdateComponentOnChange());
        add(selectField);
    }

}
