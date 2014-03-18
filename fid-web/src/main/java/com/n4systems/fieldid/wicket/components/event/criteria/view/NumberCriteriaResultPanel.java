package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.NumberFieldCriteriaResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class NumberCriteriaResultPanel extends Panel{
    public NumberCriteriaResultPanel(String id, IModel<NumberFieldCriteriaResult> resultModel) {
        super(id);

        add(new Label("numberResult", new PropertyModel<Double>(resultModel, "value")));
    }
}
