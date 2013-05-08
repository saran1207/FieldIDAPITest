package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.fieldid.wicket.pages.loto.PrintOptions;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

@ComponentWithExternalHtml
public class ProcedureDefinitionPrintPage extends FieldIDAuthenticatedPage {

    private @SpringBean PersistenceService persistenceService;
    protected @SpringBean JsonRenderer renderer;


    private IModel<ProcedureDefinition> model;

    private PrintOptions mode = PrintOptions.Normal;

    public ProcedureDefinitionPrintPage(PageParameters params) {
        super(params);
        init(createEntityModel());
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
        //add print button icon
        add(new ContextImage("printIcon", "/fieldid/images/print-icon-transparent.png"));

        mode = initMode();
        add(new AttributeAppender("class", Model.of("print-procedure-definition")));
        add(new PrintMetaData("meta",model));
        add(new PrintAsset("assetpage",model));
        add(new PrintProductSummary("productsummary",model));
        add(new PrintImages("images",model));
        add(new PrintList("list",model));
        add(new PrintFooter("footer",model));


    }

    private PrintOptions initMode() {
        StringValue param = getPageParameters().get("mode");
        if (param.isEmpty() || param.isNull()) {
            return PrintOptions.Normal;
        }
        return PrintOptions.valueOf(param.toString());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/component/imageList.css");
        response.renderCSSReference("style/pageStyles/procedureDefinition.css");
        response.renderCSSReference("style/component/annotated-image.css");
        response.renderCSSReference("style/pageStyles/procedureDefinitionPrint.css");

        response.renderJavaScriptReference("javascript/component/printimages.js");
        response.renderOnDomReadyJavaScript("setupPrintPage("+ getJsonPrintOptions() +")");
    }


    @Override
    public IMarkupFragment getMarkup() {
        return super.getMarkup();
    }


    private String getJsonPrintOptions() {
        return renderer.render(new JsonPrintOption(String.valueOf(mode.getSpacing()),  model.getObject().getPublishedState().getName()));
    }


    class JsonPrintOption {

        String printOption;
        String state;

        JsonPrintOption () {}

        JsonPrintOption (String printOption, String state) {
            this.printOption = printOption;
            this.state = state;
        }

    }

}
