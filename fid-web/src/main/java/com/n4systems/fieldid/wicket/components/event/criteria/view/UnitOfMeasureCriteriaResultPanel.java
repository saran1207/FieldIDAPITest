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

        boolean hasPrimaryUnit = ((UnitOfMeasureCriteria) resultModel.getObject().getCriteria()).getPrimaryUnit() != null;
        boolean hasPrimaryValue = (resultModel.getObject().getPrimaryValue() != null) && !resultModel.getObject().getPrimaryValue().equals("");

        add(new Label("primaryUnit", new PropertyModel<String>(resultModel, "primaryValue")).setVisible(hasPrimaryUnit && hasPrimaryValue));
        add(new Label("primaryUnitLabel", new PropertyModel<String>(resultModel, "criteria.primaryUnit.name")).setVisible(hasPrimaryUnit && hasPrimaryValue));

        boolean hasSecondaryUnit = ((UnitOfMeasureCriteria) resultModel.getObject().getCriteria()).getSecondaryUnit() != null;
        boolean hasSecondaryValue = (resultModel.getObject().getSecondaryValue() != null) && !resultModel.getObject().getSecondaryValue().equals("");

        add(new Label("secondaryUnit", new PropertyModel<String>(resultModel, "secondaryValue")).setVisible(hasSecondaryUnit && hasSecondaryValue));
        add(new Label("secondaryUnitLabel", new PropertyModel<String>(resultModel, "criteria.secondaryUnit.name")).setVisible(hasSecondaryUnit && hasSecondaryValue));
    }
}
