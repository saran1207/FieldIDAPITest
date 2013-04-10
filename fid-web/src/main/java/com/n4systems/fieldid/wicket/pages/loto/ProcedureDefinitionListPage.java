package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ProcedureDefinitionListPage extends LotoPage {

    private @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    enum NewMode {
        COPY_EXISTING("label.copy_existing"), FROM_SCRATCH("label.start_blank");

        private String label;
        NewMode(String label) {this.label = label;}
        public String getLabel() {return label;}
    }

    public ProcedureDefinitionListPage(PageParameters params) {
        super(params);

        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("activeLink", ProcedureDefinitionListPage.class, PageParametersBuilder.uniqueId(getAssetId())));
        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("previouslyPublishedListLink", PreviouslyPublishedListPage.class, PageParametersBuilder.uniqueId(getAssetId())));

        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");

        listContainer.add(new ListView<ProcedureDefinition>("list", new ProcedureDefinitionModel()) {

            @Override
            protected void populateItem(ListItem<ProcedureDefinition> item) {
                final ProcedureDefinition procedureDefinition = item.getModelObject();
                item.add(new Label("name", new PropertyModel<String>(procedureDefinition, "procedureCode")));
                item.add(new Label("revisionNumber", new PropertyModel<String>(procedureDefinition, "revisionNumber")));
                item.add(new Label("developedBy", new PropertyModel<String>(procedureDefinition, "developedBy.displayName")));
                item.add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "created"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("approvedBy", new PropertyModel<String>(procedureDefinition, "approvedBy")));
                item.add(new Label("publishedState", new PropertyModel<String>(procedureDefinition, "publishedState.label")));
                item.add(new Link("edit") {
                    @Override public void onClick() {
                        editProcedureDefinition(procedureDefinition);
                    }
                }.setVisible(procedureDefinition.getPublishedState().equals(PublishedState.DRAFT)));
                item.add(new Link("print") {
                    @Override public void onClick() {

                    }
                }.setVisible(!procedureDefinition.getPublishedState().equals(PublishedState.DRAFT)));
            }
        });

        listContainer.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject()));
        add(listContainer);

        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        blankSlate.add(new Label("blankSlateMessage", new FIDLabelModel("message.no_published_procedures", assetModel.getObject().getType().getDisplayName())));
        blankSlate.add(new AjaxLink("authorLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doNewProcedureDef(NewMode.FROM_SCRATCH);
            }
        });
        blankSlate.setVisible(!procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject()));
        add(blankSlate);
    }

    private void editProcedureDefinition(ProcedureDefinition procedureDefinition) {
        setResponsePage(new ProcedureDefinitionPage(new PageParameters().add("id",procedureDefinition.getId())));
    }

    private void doNewProcedureDef(NewMode mode) {
        switch (mode) {
            case FROM_SCRATCH:
                setResponsePage(new ProcedureDefinitionPage(newProcedureDefinition()));
                break;
            default:
                // currently only one mode supported.
                break;
        }
    }

    private IModel<ProcedureDefinition> newProcedureDefinition() {
        ProcedureDefinition pd = new ProcedureDefinition();
        pd.setAsset(assetModel.getObject());
        pd.setTenant(assetModel.getObject().getTenant());
        pd.setPublishedState(PublishedState.DRAFT);
        // NOTE : revision number will be populated by service.  really, could just use ID for revision number AFAIK?
        return Model.of(pd);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/new_procedure_list.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/loto/procedures.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedures"));
    }


    class ProcedureDefinitionModel extends LoadableDetachableModel<List<ProcedureDefinition>> {

        @Override
        protected List<ProcedureDefinition> load() {
            return procedureDefinitionService.getActiveProceduresForAsset(assetModel.getObject());
        }
    }

}
