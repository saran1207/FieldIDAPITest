package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This page allows the user to confirm whether or not they are prepared to archive all Procedures and Procedure Audit
 * events related to the Procedure Definition being unpublished.
 *
 * Created by Jordan Heath on 28/07/14.
 */
public class UnpublishProcedureDefinitionPage extends FieldIDTemplatePage {

    @SpringBean private ProcedureDefinitionService procedureDefinitionService;

    private ProcedureDefinition procedureDefinition;

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.unpublish_loto_procedure_warning"));
    }

    public UnpublishProcedureDefinitionPage(PageParameters pageParameters) {
        super(pageParameters);

        Long procedureDefinitionId = pageParameters.get("procedureDefinitionId").toLong();

        procedureDefinition = procedureDefinitionService.getProcedureDefinitionById(procedureDefinitionId);

        Form<Void> confirmUnpublishForm = new Form<Void>("confirmForm") {
            @Override
            protected void onSubmit() {
                procedureDefinitionService.unpublishProcedureDefinition(procedureDefinition);
                //Before navigating back to this page, we need to display an appropriate message, indicating that
                //the Procedure Definition was unpublished.
                PreviouslyPublishedListAllPage nextPage = new PreviouslyPublishedListAllPage();
                nextPage.info(new FIDLabelModel("message.unpublish", procedureDefinition.getProcedureCode()).getObject());
                setResponsePage(nextPage);
            }
        };

        confirmUnpublishForm.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new PublishedListAllPage());
            }
        });

        add(confirmUnpublishForm);
    }
}
