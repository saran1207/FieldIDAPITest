package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.LotoPrintoutType;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * This WebMarkupContainer was created in a refactor to remove repetitive logic from 4 "ActionsCell" classes used in
 * various parts of the ProcedureDefinition maintenance screens.
 *
 * Created by Jordan Heath on 15-03-09.
 */
public class LotoPrintoutOptionsContainer extends WebMarkupContainer {
    private FIDModalWindow modal;
    private ProcedureDefinition procedureDefinition;

    public LotoPrintoutOptionsContainer(String id, ProcedureDefinition procedureDefinition, FIDModalWindow modal) {
        super(id);
        this.modal = modal;
        this.procedureDefinition = procedureDefinition;

        AjaxLink shortLink = new AjaxLink("shortLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.setContent(createPrintoutPanel(LotoPrintoutType.SHORT));
                if(!modal.isShown()) {
                    modal.show(target);
                }
            }
        };

        shortLink.add(new Label("label", new FIDLabelModel("label.short_form")));
        add(shortLink);

        AjaxLink longLink = new AjaxLink("longLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.setContent(createPrintoutPanel(LotoPrintoutType.LONG));
                if(!modal.isShown()) {
                    modal.show(target);
                }
            }
        };
        longLink.add(new Label("label", new FIDLabelModel("label.long_form")));
        add(longLink);
    }

    private CreatePrintoutModalPanel createPrintoutPanel(LotoPrintoutType type) {
        return new CreatePrintoutModalPanel(FIDModalWindow.CONTENT_ID, Model.of(procedureDefinition), type) {
            @Override
            protected void closeModal(AjaxRequestTarget target) {
                target.add(modal);
                modal.close(target);
            }
        };
    }
}
