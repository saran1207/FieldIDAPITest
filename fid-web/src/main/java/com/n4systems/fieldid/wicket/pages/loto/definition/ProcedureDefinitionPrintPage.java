package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

@ComponentWithExternalHtml
public class ProcedureDefinitionPrintPage extends FieldIDAuthenticatedPage {

    private @SpringBean PersistenceService persistenceService;

    private IModel<ProcedureDefinition> model;

    public ProcedureDefinitionPrintPage(PageParameters params) {
        super(params);
        init(createEntityModel());
    }

    public ProcedureDefinitionPrintPage(ProcedureDefinition procedureDefinition) {
        super(new PageParameters());
        init(createEntityModel(procedureDefinition.getId()));
    }

    private IModel<ProcedureDefinition> createEntityModel() {
        Preconditions.checkState(getPageParameters().get("id") != null && getPageParameters().get("id").toString() != null, "you must specify an id for a ProcedureDefinition.");
        return createEntityModel(getPageParameters().get("id").toLong());
    }

    private IModel<ProcedureDefinition> createEntityModel(Long id) {
        ProcedureDefinition procedureDefinition = persistenceService.find(ProcedureDefinition.class, id);
        return Model.of(procedureDefinition);
    }

    private void init(IModel<ProcedureDefinition> model) {
        this.model = model;
        add(new AttributeAppender("class", Model.of("print-procedure-definition")));

        add(new PrintMetaData("meta",model));
        add(new PrintAssetDescription("asset",model));
        // add ProcedureSummary ...new Panel();

        add(new PrintHeader("header",model));
        add(new PrintDetails("details", model));
        add(new PrintImages("images",model));
        add(new PrintList("list",model));
        add(new PrintFooter("footer",model));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/procedureDefinition.css");
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");
    }


    @Override
    public IMarkupFragment getMarkup() {
        return super.getMarkup();
    }
}
