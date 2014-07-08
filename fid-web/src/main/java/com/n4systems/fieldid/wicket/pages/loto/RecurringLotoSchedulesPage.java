package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.wicket.components.DisplayRecurrenceTimeModel;
import com.n4systems.fieldid.wicket.components.loto.RecurringAuditFormPanel;
import com.n4systems.fieldid.wicket.components.loto.RecurringLotoFormPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.model.procedure.RecurringLotoEvent;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

public class RecurringLotoSchedulesPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetService assetService;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private RecurringScheduleService recurringScheduleService;

    private Long assetId;
    private IModel<Asset> assetModel;

    private WebMarkupContainer listContainer;
    private WebMarkupContainer noProcedureBlankSlate;
    private WebMarkupContainer blankSlate;
    private ListView listView;
    private ModalWindow lotoRecurrenceModalWindow;
    private ModalWindow auditRecurrenceModalWindow;

    public RecurringLotoSchedulesPage(PageParameters params) {
        super(params);

        add(lotoRecurrenceModalWindow = new DialogModalWindow("addLotoRecurrence").setInitialWidth(480));
        lotoRecurrenceModalWindow.setContent(new RecurringLotoFormPanel(lotoRecurrenceModalWindow.getContentId(), assetModel) {
            @Override
            protected void onCreateRecurrence(AjaxRequestTarget target) {
                lotoRecurrenceModalWindow.close(target);
                refreshContent(target);
            }
        });

        add(auditRecurrenceModalWindow = new DialogModalWindow("addAuditRecurrence").setInitialWidth(480));
        auditRecurrenceModalWindow.setContent(new RecurringAuditFormPanel(auditRecurrenceModalWindow.getContentId(), assetModel) {
            @Override
            protected void onCreateRecurrence(AjaxRequestTarget target) {
                auditRecurrenceModalWindow.close(target);
                refreshContent(target);
            }
        });

        add(listContainer = new WebMarkupContainer("listContainer"));
        listContainer.setOutputMarkupPlaceholderTag(true);
        listContainer.add(listView = new ListView<RecurringLotoEvent>("list", getRecurringEvents()) {
            @Override
            protected void populateItem(final ListItem<RecurringLotoEvent> item) {
                final RecurringLotoEvent event = item.getModelObject();

                item.add(new Label("type", new PropertyModel<String>(item.getModel(), "type.label")));
                item.add(new Label("procedureCode", new PropertyModel<String>(item.getModel(), "procedureDefinition.procedureCode")));
                item.add(new Label("recurrence", new EnumLabelModel(event.getRecurrence().getType())));
                item.add(new Label("time", new DisplayRecurrenceTimeModel(new PropertyModel<Set<RecurrenceTime>>(item.getDefaultModelObject(), "recurrence.times"))));
                item.add(new Label("assignedTo", new PropertyModel<String>(item.getModel(), "assignedUserOrGroup.assignToDisplayName")));

                item.add(new AjaxLink("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        recurringScheduleService.purgeRecurringEvent(item.getModelObject());
                        refreshContent(target);
                    }
                });
            }
        });

        add(noProcedureBlankSlate = new WebMarkupContainer("noProceduresBlankSlate"));
        add(blankSlate = new WebMarkupContainer("blankSlate"));
        noProcedureBlankSlate.setOutputMarkupPlaceholderTag(true);
        blankSlate.setOutputMarkupPlaceholderTag(true);
        setVisibility();
    }

    private void setVisibility() {
        boolean hasProcedureDefinitions = procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject());
        boolean hasRecurringEvents = recurringScheduleService.countRecurringLotoEvents(assetModel.getObject()) > 0;
        listContainer.setVisible(hasProcedureDefinitions && hasRecurringEvents);
        noProcedureBlankSlate.setVisible(!hasProcedureDefinitions);
        blankSlate.setVisible(!hasRecurringEvents && hasProcedureDefinitions);
    }

    protected void refreshContent(AjaxRequestTarget target) {
        listView.detach();
        setVisibility();
        target.add(listContainer, noProcedureBlankSlate, blankSlate);
    }

    private LoadableDetachableModel<List<RecurringLotoEvent>> getRecurringEvents() {
        return new LoadableDetachableModel<List<RecurringLotoEvent>>() {
            @Override
            protected List<RecurringLotoEvent> load() {
                return recurringScheduleService.getRecurringLotoEvents(assetModel.getObject());
            }
        };
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        assetId = params.get("uniqueID").toLong();
        assetModel = new EntityModel<Asset>(Asset.class, assetId);
        assetService.fillInSubAssetsOnAsset(assetModel.getObject());
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<AssetSummaryPage>(linkId, ProceduresListPage.class, PageParametersBuilder.uniqueId(assetId))
                .add(new Label(linkLabelId, new FIDLabelModel("label.back_to_x", new FIDLabelModel("label.procedures").getObject())));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.loto_recurring_schedules_for", assetModel.getObject().getIdentifier()));
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    private class ActionGroup extends Fragment {
        public ActionGroup(String id) {
            super(id, "addRecurrenceActionGroup", RecurringLotoSchedulesPage.this);
            WebMarkupContainer dropDownContainer;
            add(dropDownContainer = new WebMarkupContainer("dropDownContainer"));
            dropDownContainer.add(new AjaxLink("addRecurringLotoLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    lotoRecurrenceModalWindow.show(target);
                }
            });

            dropDownContainer.add(new AjaxLink("addRecurringAuditLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    auditRecurrenceModalWindow.show(target);
                }
            });

            if (!procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject())) {
                 dropDownContainer.add(new AttributeAppender("class", Model.of("disabled"), " "));
            }

        }
    }
}
