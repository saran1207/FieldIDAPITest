package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
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

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private ProcedureService procedureService;

    private WebMarkupContainer listContainer;
    private WebMarkupContainer blankSlate;
    private ListView listView;
    private FIDFeedbackPanel feedbackPanel;

    public ProcedureDefinitionListPage(PageParameters params) {
        super(params);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

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
                item.add(new Label("approvedBy", new PropertyModel<String>(procedureDefinition, "approvedBy.displayName")));
                item.add(new Label("publishedState", new PropertyModel<String>(procedureDefinition, "publishedState.label")));

                item.add(new Link<Void>("editLink") {

                    @Override
                    public void onClick() {
                        setResponsePage(new ProcedureDefinitionPage(new PageParameters().add("id", procedureDefinition.getObject().getId())));

                    }
                }.setVisible(procedureDefinition.getObject().getPublishedState().isPreApproval()));

                item.add(new AjaxLink<Void>("deleteLink") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        procedureDefinitionService.deleteProcedureDefinition(procedureDefinition.getObject());
                        listView.detach();
                        listContainer.setVisible(!getList().isEmpty());
                        blankSlate.setVisible(getList().isEmpty());
                        target.add(listContainer);
                        target.add(blankSlate);
                    }
                });

                Link copyLink;
                item.add(copyLink = new Link("reviseLink") {
                    @Override public void onClick() {
                        ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(assetModel.getObject(), procedureDefinition.getObject().getFamilyId());
                        ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinition(publishedDef);
                        copiedDefinition.setPublishedState(PublishedState.DRAFT);
                        setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
                    }
                });
                copyLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject())
                        && procedureDefinition.getObject().getPublishedState().equals(PublishedState.PUBLISHED));
                copyLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.N));

                boolean showUnpublished = true;

                if(procedureDefinitionService.isApprovalRequired())
                    showUnpublished = procedureDefinitionService.canCurrentUserApprove();

                item.add(new AjaxLink<Void>("unpublishLink") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if(procedureService.hasOpenProcedure(procedureDefinition.getObject())) {
                            error(new FIDLabelModel("error.unpublish").getObject());
                            target.add(feedbackPanel);
                        } else {
                            procedureDefinitionService.unpublishProcedureDefinition(procedureDefinition.getObject());
                            target.add(listContainer, feedbackPanel);
                        }
                    }
                }.setVisible(showUnpublished));

                Link printLink;
                item.add(printLink = new Link("print") {
                    @Override public void onClick() {
                        setResponsePage(new ProcedureDefinitionPrintPage(PageParametersBuilder.id(procedureDefinition.getObject().getId())));
                    }
                });
                printLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.view_print"), TipsyBehavior.Gravity.W));
                printLink.add(new AttributeAppender("class", "tipsy-tooltip").setSeparator(" "));
                PopupSettings popupSettings = new PopupSettings("popupWindow", PopupSettings.SCROLLBARS).setWidth(1000).setTop(1);
                printLink.setPopupSettings(popupSettings);

            }
        });

        listContainer.setVisible(!listView.getList().isEmpty());
        add(listContainer);

        blankSlate = new WebMarkupContainer("blankSlate");
        blankSlate.add(new Label("blankSlateMessage", new FIDLabelModel("message.no_published_procedures", assetModel.getObject().getType().getDisplayName())));
        blankSlate.setVisible(listView.getList().isEmpty());
        blankSlate.setOutputMarkupPlaceholderTag(true);
        add(blankSlate);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/new_procedure_list.js");
    }

    class ProcedureDefinitionModel extends LoadableDetachableModel<List<ProcedureDefinition>> {

        @Override
        protected List<ProcedureDefinition> load() {
            return procedureDefinitionService.getActiveProcedureDefinitionsForAsset(assetModel.getObject());
        }

        @Override
        public void detach() {
            super.detach();
        }
    }

}
