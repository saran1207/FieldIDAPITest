package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.Asset;
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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ProcedureDefinitionListPage extends LotoPage {

    private @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    private WebMarkupContainer listContainer;
    private WebMarkupContainer blankSlate;
    private ListView listView;

    enum NewMode {
        COPY_EXISTING("label.copy_existing"), FROM_SCRATCH("label.start_blank");

        private String label;
        NewMode(String label) {this.label = label;}
        public String getLabel() {return label;}
    }

    public ProcedureDefinitionListPage(PageParameters params) {
        super(params);

        BookmarkablePageLink assetLink;
        add(assetLink = new BookmarkablePageLink<AssetSummaryPage>("assetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(assetId)));
        Asset asset = assetModel.getObject();
        String assetLabel = asset.getType().getDisplayName() + " / " + asset.getIdentifier();
        assetLink.add(new Label("label", assetLabel));

        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("activeLink", ProcedureDefinitionListPage.class, PageParametersBuilder.uniqueId(getAssetId())));
        add(new BookmarkablePageLink<PreviouslyPublishedListPage>("previouslyPublishedListLink", PreviouslyPublishedListPage.class, PageParametersBuilder.uniqueId(getAssetId())));

        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupPlaceholderTag(true);

        listContainer.add(listView = new ListView<ProcedureDefinition>("list", new ProcedureDefinitionModel()) {

            @Override
            protected void populateItem(final ListItem<ProcedureDefinition> item) {
                final IModel<ProcedureDefinition> procedureDefinition = item.getModel();
                item.add(new Label("name", new PropertyModel<String>(procedureDefinition, "procedureCode")));
                item.add(new Label("revisionNumber", new PropertyModel<String>(procedureDefinition, "revisionNumber")));
                item.add(new Label("developedBy", new PropertyModel<String>(procedureDefinition, "developedBy.displayName")));
                item.add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(procedureDefinition, "created"), true, getCurrentUser().getTimeZone())));
                item.add(new Label("approvedBy", new PropertyModel<String>(procedureDefinition, "approvedBy")));
                item.add(new Label("publishedState", new PropertyModel<String>(procedureDefinition, "publishedState.label")));
                item.add(new Link("edit") {
                    @Override
                    public void onClick() {
                        editProcedureDefinition(procedureDefinition.getObject());
                    }
                }.setVisible(procedureDefinition.getObject().getPublishedState().isPreApproval()));
                item.add(new AjaxLink("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        procedureDefinitionService.deleteProcedureDefinition(procedureDefinition.getObject());
                        listView.detach();
                        listContainer.setVisible(!getList().isEmpty());
                        blankSlate.setVisible(getList().isEmpty());
                        target.add(listContainer);
                        target.add(blankSlate);
                    }
                }.setVisible(procedureDefinition.getObject().getPublishedState().isPreApproval()));
                item.add(new Link("print") {
                    @Override
                    public void onClick() {
                        setResponsePage(new ProcedureDefinitionPrintPage(procedureDefinition.getObject()));
                    }
                }.setVisible(!procedureDefinition.getObject().getPublishedState().equals(PublishedState.DRAFT)));
            }
        });

        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        blankSlate = new WebMarkupContainer("blankSlate");
        blankSlate.add(new Label("blankSlateMessage", new FIDLabelModel("message.no_published_procedures", assetModel.getObject().getType().getDisplayName())));
        blankSlate.add(new AjaxLink("authorLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doNewProcedureDef(NewMode.FROM_SCRATCH);
            }
        });
        blankSlate.setVisible(listView.getList().isEmpty());
        blankSlate.setOutputMarkupPlaceholderTag(true);
        add(blankSlate);
    }

    private void editProcedureDefinition(ProcedureDefinition procedureDefinition) {
        setResponsePage(new ProcedureDefinitionPage(new PageParameters().add("id",procedureDefinition.getId())));
    }

    private void doNewProcedureDef(NewMode mode) {
        switch (mode) {
            case FROM_SCRATCH:
                setResponsePage(new ProcedureDefinitionPage(assetModel.getObject()));
                break;
            default:
                // currently only one mode supported.
                break;
        }
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

        @Override
        public void detach() {
            super.detach();
        }
    }

}
