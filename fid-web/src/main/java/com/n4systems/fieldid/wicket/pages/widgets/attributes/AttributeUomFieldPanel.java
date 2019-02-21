package com.n4systems.fieldid.wicket.pages.widgets.attributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel to handle display/update of a Unit Of Measure text field.
 */
public class AttributeUomFieldPanel extends Panel {

    @SpringBean
    private PersistenceService persistenceService;

    private InfoFieldBean infoFieldBean;
    private InfoOptionInput infoOptionInput;
    private boolean displayUomForm;

    public AttributeUomFieldPanel(
            String id,
            InfoFieldBean infoFieldBean,
            InfoOptionInput infoOptionInput) {
        super(id);
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInput = infoOptionInput;
        displayUomForm = false;
        addComponents();
    }

    private void addComponents() {

        add(new Label("required", new FIDLabelModel("indicator.required")) {
            @Override
            public boolean isVisible() {
                return infoFieldBean.isRequired();
            }
        });
        add(new Label("fieldName", infoFieldBean.getName()));

        final TextField amountField = new TextField("amount", new PropertyModel(infoOptionInput, "name"));
        amountField.setOutputMarkupId(true);
        amountField.setOutputMarkupPlaceholderTag(true);
        amountField.setEnabled(false);
        add(amountField);

        WebMarkupContainer uomFormContainer = new WebMarkupContainer("uomFormContainer");
        uomFormContainer.setOutputMarkupId(true);
        uomFormContainer.setOutputMarkupPlaceholderTag(true);

        AjaxLink editLink = new AjaxLink("editLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                displayUomForm = !displayUomForm;
                target.add(uomFormContainer);
            }
        };
        add(editLink);

        add(uomFormContainer);

        WebMarkupContainer uomFormHideableContainer = new WebMarkupContainer("uomFormHideableContainer") {
            @Override
            public boolean isVisible() {
                return displayUomForm;
            }
        };
        uomFormHideableContainer.setOutputMarkupId(true);
        uomFormHideableContainer.setOutputMarkupPlaceholderTag(true);
        uomFormContainer.add(uomFormHideableContainer);

        Form uomForm = new Form("uomForm");
        uomForm.setOutputMarkupId(true);
        uomForm.setOutputMarkupPlaceholderTag(true);
        uomFormHideableContainer.add(uomForm);

        IModel<UnitOfMeasure> selectedUnit = new Model(null);
        if (infoFieldBean.getUnitOfMeasure() != null) {
            selectedUnit.setObject(infoFieldBean.getUnitOfMeasure());
        }

        List<UnitOfMeasure> units = getAllUnitOfMeasures();
        List<String> unitNames = new ArrayList();
        for (UnitOfMeasure uom : units) {
            unitNames.add(uom.getName());
        }

        DropDownChoice<UnitOfMeasure> unitSelection = new DropDownChoice(
                "unitOfMeasures", selectedUnit, units,  new IChoiceRenderer<UnitOfMeasure>() {
            @Override
            public Object getDisplayValue(UnitOfMeasure object) {
                return object.getName();
            }

            @Override
            public String getIdValue(UnitOfMeasure object, int index) {
                return object.getId().toString();
            }
        });
        unitSelection.setOutputMarkupId(true);
        unitSelection.setOutputMarkupPlaceholderTag(true);

        final Map<String, String> uomValues = new HashMap();
        final DataView<UnitOfMeasure> inputUnits =
                new DataView<UnitOfMeasure>("inputUnits", new ListDataProvider<UnitOfMeasure>() {
                    @Override
                    protected List<UnitOfMeasure> getData() {
                        return getInputOrder(selectedUnit.getObject());
                    }
                }) {
                    @Override
                    protected void populateItem(Item<UnitOfMeasure> item) {
                        item.add(new Label("unitOfMeasureName", item.getModelObject().getName()));
                        item.add(new TextField("unitOfMeasureInputValue",
                                new PropertyModel<String>(uomValues, item.getModelObject().getShortName())));
                    }
                };
        inputUnits.setOutputMarkupId(true);
        inputUnits.setOutputMarkupPlaceholderTag(true);
        uomForm.add(unitSelection);
        uomForm.add(inputUnits);

        // Add Ajax Behaviour to prevent change in unit selection from generating a
        // page refresh which will lose the state in the unit selection control.
        unitSelection.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
                selectedUnit.setObject(unitSelection.getModelObject());
                target.add(uomFormHideableContainer);
            }
        });

        uomForm.add(new AjaxSubmitLink("submitForm") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                displayUomForm = false;

                /* Create the measurement string that will be set in the model */
                String unitString = "";
                for (UnitOfMeasure inputUnit : getInputOrder(unitSelection.getModelObject())) {
                    String shortName = inputUnit.getShortName();
                    String unitAmount = uomValues.get(shortName);
                    if (unitAmount != null && unitAmount.trim().length() > 0) {
                        unitString += unitAmount.trim() + " " + shortName + " ";
                    }
                }
                infoOptionInput.setName(unitString);
                target.add(amountField);
                target.add(uomFormContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        });
        uomForm.add(new AjaxLink("cancelForm") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                displayUomForm = false;
                target.add(uomFormContainer);
            }
        });
    }

    private List<UnitOfMeasure> getInputOrder(UnitOfMeasure selectedUnitOfMeasure) {
        List<UnitOfMeasure> inputOrder = new ArrayList<UnitOfMeasure>();
        UnitOfMeasure currentUnitOfMeasure = selectedUnitOfMeasure;

        while (currentUnitOfMeasure != null) {
            inputOrder.add(currentUnitOfMeasure);
            currentUnitOfMeasure = currentUnitOfMeasure.getChild();
        }
        return inputOrder;
    }

    private List<UnitOfMeasure> getAllUnitOfMeasures() {
        QueryBuilder<UnitOfMeasure> query = new QueryBuilder<UnitOfMeasure>(UnitOfMeasure.class, new OpenSecurityFilter());
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

}
