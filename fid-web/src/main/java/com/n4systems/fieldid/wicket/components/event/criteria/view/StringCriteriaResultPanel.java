package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.CriteriaResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class StringCriteriaResultPanel extends Panel{
    public StringCriteriaResultPanel(String id, IModel<? extends CriteriaResult> resultModel) {
        super(id);

        add(new Label("stringResult", new PropertyModel<String>(resultModel, "value")));
    }
}
