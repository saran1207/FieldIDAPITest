package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.UnitOfMeasureListLoader;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class UnitOfMeasureDetailsPanel extends Panel {

    public UnitOfMeasureDetailsPanel(String id, IModel<UnitOfMeasureCriteria> model) {
        super(id, model);

        add(new UnitSelectForm("unitSelectForm", model));
    }

    private class UnitSelectForm extends Form {

        private DropDownChoice<UnitOfMeasure> primaryUnitSelect;
        private DropDownChoice<UnitOfMeasure> secondaryUnitSelect;

        public UnitSelectForm(String id, IModel<UnitOfMeasureCriteria> model) {
            super(id);
            List<UnitOfMeasure> unitOfMeasures = loadUnits();
            add(primaryUnitSelect = new DropDownChoice<UnitOfMeasure>("primaryUnitSelect", new PropertyModel<UnitOfMeasure>(model, "primaryUnit"), unitOfMeasures, createUnitOfMeasureRenderer()));
            add(secondaryUnitSelect = new DropDownChoice<UnitOfMeasure>("secondaryUnitSelect", new PropertyModel<UnitOfMeasure>(model, "secondaryUnit"), unitOfMeasures, createUnitOfMeasureRenderer()));

            primaryUnitSelect.add(createSubmitOnChangeBehavior());
            secondaryUnitSelect.add(createSubmitOnChangeBehavior());

            secondaryUnitSelect.setNullValid(true);
        }

        private AjaxFormSubmitBehavior createSubmitOnChangeBehavior() {
            return new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            };
        }

        private IChoiceRenderer<UnitOfMeasure> createUnitOfMeasureRenderer() {
            return new IChoiceRenderer<UnitOfMeasure>() {
                @Override
                public Object getDisplayValue(UnitOfMeasure unitOfMeasure) {
                    return unitOfMeasure.getName();
                }

                @Override
                public String getIdValue(UnitOfMeasure unitOfMeasure, int index) {
                    return unitOfMeasure.getID()+"";
                }
            };
        }

    }

    private List<UnitOfMeasure> loadUnits() {
        return new UnitOfMeasureListLoader(FieldIDSession.get().getSessionUser().getSecurityFilter()).load();
    }

}
