package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.text.LabelledAutoCompleteUser;
import com.n4systems.fieldid.wicket.components.text.LabelledDropDown;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class DetailsPanel extends Panel {

    private TextField procedureCode;


    public DetailsPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class", Model.of("details")));

        add(new LabelledTextField<String>("procedureCode", "label.procedure_code", ProxyModel.of(model, on(ProcedureDefinition.class).getProcedureCode())));

        add(new LabelledTextField<String>("identifier", "label.identifier", ProxyModel.of(model, on(ProcedureDefinition.class).getElectronicIdentifier())));

        add(new LabelledDropDown<String>("revision", "label.revision_number", ProxyModel.of(model, on(ProcedureDefinition.class).getRevisionNumber()) ) {
            @Override protected List<String> getChoices() {
                return Lists.newArrayList("1","2");
            }
        });

        add(new LabelledTextArea<String>("warnings", "label.warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())));

        add(new LabelledAutoCompleteUser("user", "label.developed_by", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy())));

        add(new LabelledTextField<String>("equipmentNumber", "label.equipment_number", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentNumber())));

        // TODO : change location to a string.
        add(new LabelledTextField<String>("equipmentLocation", "label.equipment_location", ProxyModel.of(model, on(ProcedureDefinition.class).getElectronicIdentifier())));

        add(new LabelledTextField<String>("equipmentDescription", "label.equipment_description", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentDescription())));

        add(new AjaxLink("cancel") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });
        add(new AjaxLink("continue") {
            @Override public void onClick(AjaxRequestTarget target) {
                doContinue(target);
            }
        });
    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }

}
