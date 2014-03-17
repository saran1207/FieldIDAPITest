package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UnitOfMeasureCriteriaResultPanel extends Panel{
    public UnitOfMeasureCriteriaResultPanel(String id, IModel<UnitOfMeasureCriteriaResult> resultModel) {
        super(id);

        add(new Label("primaryUnit", new PropertyModel<String>(resultModel, "primaryValue")));
        add(new Label("primaryUnitLabel", new PropertyModel<String>(resultModel, "criteria.primaryUnit.name")));

        boolean hasSecondaryUnit = ((UnitOfMeasureCriteria) resultModel.getObject().getCriteria()).getSecondaryUnit() != null;

        add(new Label("secondaryUnit", new PropertyModel<String>(resultModel, "secondaryValue")).setVisible(hasSecondaryUnit));
        add(new Label("secondaryUnitLabel", new PropertyModel<String>(resultModel, "criteria.secondaryUnit.name")).setVisible(hasSecondaryUnit));
    }
}
