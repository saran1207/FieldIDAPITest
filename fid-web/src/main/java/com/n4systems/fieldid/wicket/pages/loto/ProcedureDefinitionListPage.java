package com.n4systems.fieldid.wicket.pages.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.loto.ProcedureTitleLabel;
import com.n4systems.fieldid.wicket.components.menuButton.MenuButton;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
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

    private WebMarkupContainer listContainer;
    private WebMarkupContainer blankSlate;
    private ListView listView;

    public ProcedureDefinitionListPage(PageParameters params) {
        super(params);

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
                item.add(new MenuButton<FIDLabelModel>("edit", new FIDLabelModel("label.edit"), Lists.newArrayList(new FIDLabelModel("label.delete"))){

                    @Override
                    protected WebMarkupContainer populateLink(String linkId, String labelId, ListItem<FIDLabelModel> item) {
                        return (WebMarkupContainer) new AjaxLink(linkId) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                procedureDefinitionService.deleteProcedureDefinition(procedureDefinition.getObject());
                                listView.detach();
                                listContainer.setVisible(!getList().isEmpty());
                                blankSlate.setVisible(getList().isEmpty());
                                target.add(listContainer);
                                target.add(blankSlate);
                            }
                        }.add(new Label(labelId, item.getModelObject()));
                    }

                    @Override
                    protected void buttonClicked(AjaxRequestTarget target) {
                        editProcedureDefinition(procedureDefinition.getObject());
                    }

                }.setVisible(procedureDefinition.getObject().getPublishedState().isPreApproval()));

                item.add(new Link("copyProcedureDefLink") {
                    @Override
                    public void onClick() {
                        ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(assetModel.getObject());
                        ProcedureDefinition copiedDefinition = procedureDefinitionService.copyProcedureDefinition(publishedDef, new ProcedureDefinition());
                        copiedDefinition.setPublishedState(PublishedState.DRAFT);
                        setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
                    }
                }.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject())
                        && procedureDefinition.getObject().getPublishedState().equals(PublishedState.PUBLISHED)));

                item.add(new MenuButton<PrintOptions>("print", new FIDLabelModel("label.view_print"), Lists.newArrayList(PrintOptions.values())){
                    @Override
                    protected WebMarkupContainer populateLink(String linkId, String labelId, final ListItem<PrintOptions> item) {
                        return (WebMarkupContainer) new Link(linkId) {

                            @Override
                            public void onClick() {
                               setResponsePage(new ProcedureDefinitionPrintPage(procedureDefinition.getObject(),item.getModelObject()));
                            }
                        }.add(new Label(labelId, item.getModelObject().name()));
                    }

                    @Override protected void buttonClicked(AjaxRequestTarget target) {
                        //TODO SU/DD: implement correct print options
                        setResponsePage(new ProcedureDefinitionPrintPage(procedureDefinition.getObject()));
                    }
                });
            }
        });

        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        blankSlate = new WebMarkupContainer("blankSlate");
        blankSlate.add(new Label("blankSlateMessage", new FIDLabelModel("message.no_published_procedures", assetModel.getObject().getType().getDisplayName())));
        blankSlate.add(new AjaxLink("authorLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doNewProcedureDef();
            }
        });
        blankSlate.setVisible(listView.getList().isEmpty());
        blankSlate.setOutputMarkupPlaceholderTag(true);
        add(blankSlate);
    }

    private void editProcedureDefinition(ProcedureDefinition procedureDefinition) {
        setResponsePage(new ProcedureDefinitionPage(new PageParameters().add("id",procedureDefinition.getId())));
    }

    private void doNewProcedureDef() {
        setResponsePage(new ProcedureDefinitionPage(assetModel.getObject()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/new_procedure_list.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/loto/procedures.css");
    }

    @Override
    protected Component createTitleLabel(String labelId, boolean isTopTitle) {
        if(isTopTitle)
            return new Label(labelId, new FIDLabelModel("label.procedures"));
        else
            return new ProcedureTitleLabel(labelId, assetModel);
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
