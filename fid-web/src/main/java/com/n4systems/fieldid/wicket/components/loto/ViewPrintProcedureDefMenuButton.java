package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.menuButton.MenuButton;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.loto.PrintOptions;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;

public class ViewPrintProcedureDefMenuButton extends MenuButton<PrintOptions> {

    private IModel<ProcedureDefinition> procedureDefinition;

    public ViewPrintProcedureDefMenuButton(String id, IModel<ProcedureDefinition> procedureDefinition) {
        super(id, new FIDLabelModel("label.view_print"), Lists.newArrayList(PrintOptions.values()));
        this.procedureDefinition = procedureDefinition;
    }

    @Override
    protected WebMarkupContainer populateLink(String linkId, String labelId, final ListItem<PrintOptions> item) {
        return (WebMarkupContainer) new Link(linkId) {
            @Override
            public void onClick() {
                onPrintOptionSelected(procedureDefinition, item.getModelObject());
            }
        }.add(new Label(labelId, item.getModelObject().name()));
    }

    @Override
    protected void buttonClicked(AjaxRequestTarget target) {
        onPrintOptionSelected(procedureDefinition, PrintOptions.Normal);
    }

    protected void onPrintOptionSelected(IModel<ProcedureDefinition> procedureDefinition, PrintOptions printOption) {};


}
