package com.n4systems.fieldid.wicket.components.event.criteria.edit;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UnitOfMeasureCriteriaEditPanel extends Panel {

    public UnitOfMeasureCriteriaEditPanel(String id, IModel<UnitOfMeasureCriteriaResult> result) {
        super(id);

        UnitOfMeasureCriteria criteria = (UnitOfMeasureCriteria) result.getObject().getCriteria();

        TextField<String> primaryField = new TextField<String>("primaryUnit", new PropertyModel<String>(result, "primaryValue"));
        primaryField.add(new UpdateComponentOnChange());
        add(primaryField);
        add(new Label("primaryUnitLabel", new PropertyModel<String>(result, "criteria.primaryUnit.name")));

        WebMarkupContainer secondaryUnitContainer = new WebMarkupContainer("secondaryUnitContainer");
        secondaryUnitContainer.setVisible(criteria.getSecondaryUnit() != null);
        TextField<String> secondaryField = new TextField<String>("secondaryUnit", new PropertyModel<String>(result, "secondaryValue"));
        secondaryField.add(new UpdateComponentOnChange());
        secondaryUnitContainer.add(secondaryField);
        secondaryUnitContainer.add(new Label("secondaryUnitLabel", new PropertyModel<String>(result, "criteria.secondaryUnit.name")));
        
        add(secondaryUnitContainer);
    }

}
