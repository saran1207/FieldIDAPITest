package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.schedule.MassScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateLabel;
import com.n4systems.fieldid.wicket.components.SimpleAjaxButton;
import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.CommonEventTypesModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage;
import com.n4systems.model.*;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MassSchedulePage extends FieldIDFrontEndPage {

    private List<Long> selectedIds;
    private List<ScheduleSummaryEntry> scheduleSummary;
    private boolean duplicateDetection = true;
    private boolean aSchedulePickerIsOpen = false;
    private ThingEvent scheduleForAll = new ThingEvent();

    private WebMarkupContainer assetTypesListContainer;
    private SchedulePicker scheduleAllPicker;

    @SpringBean
    private AssetService assetService;

    @SpringBean
    private MassScheduleService massScheduleService;

    public MassSchedulePage(final IModel<AssetSearchCriteria> criteriaModel) {
        selectedIds = criteriaModel.getObject().getSelection().getSelectedIds();

        scheduleSummary = assetService.getAssetScheduleSummary(selectedIds);
        List<AssetType> assetTypesList = getAssetTypeList(scheduleSummary);
        IModel<List<? extends EventType>> commonEventTypesModel = new CommonEventTypesModel(assetTypesList);

        assetTypesListContainer = new WebMarkupContainer("assetTypesListContainer");
        add(assetTypesListContainer.setOutputMarkupId(true));

        final EventJobsForTenantModel jobsOptions = new EventJobsForTenantModel();

        add(scheduleAllPicker = createScheduleAllPicker(commonEventTypesModel, jobsOptions));
        add(new SimpleAjaxButton("openScheduleAllPickerButton", new FIDLabelModel("label.schedule_all")) {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                scheduleAllPicker.show(target);
            }
        });
        add(new WebMarkupContainer("noCommonEventTypesMessage").setVisible(commonEventTypesModel.getObject().isEmpty()));

        add(new FIDFeedbackPanel("feedbackPanel"));
        assetTypesListContainer.add(new ListView<ScheduleSummaryEntry>("assetTypeSummary", scheduleSummary) {
            @Override
            protected void populateItem(final ListItem<ScheduleSummaryEntry> item) {
                item.add(new Label("assetTypeName", new PropertyModel<String>(item.getModel(), "assetType.name")));
                item.add(new Label("count", new PropertyModel<Integer>(item.getModel(), "count")));

                final WebMarkupContainer schedulesContainer = createScheduleListView("schedulesContainer", item.getModel());
                item.add(schedulesContainer);

                IModel<List<? extends EventType>> eventTypesForAssetType = new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(item.getModel(), "assetType"));

                final SchedulePicker schedulePicker = createSchedulePicker(item, eventTypesForAssetType, jobsOptions);
                item.add(new SimpleAjaxButton("openSchedulePickerButton", new FIDLabelModel("label.add_a_schedule")) {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        schedulePicker.show(target);
                    }
                });
                item.add(schedulePicker);
                item.add(new WebMarkupContainer("noEventTypesMessage").setVisible(eventTypesForAssetType.getObject().isEmpty()));
            }
        });

        Form submitForm = new Form("submitForm") {
            @Override
            protected void onSubmit() {
                if (!hasAtLeastOneEvent(scheduleSummary)) {
                    error(new FIDLabelModel("message.mass_schedule_no_events").getObject());
                    return;
                }
                massScheduleService.performSchedules(scheduleSummary, duplicateDetection);
                FieldIDSession.get().info(new FIDLabelModel("message.mass_schedule_success").getObject());
                setResponsePage(new com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage(criteriaModel.getObject()));
            }
        };
        add(submitForm);
        submitForm.add(new CheckBox("duplicateDetection", new PropertyModel<Boolean>(this, "duplicateDetection")));
        submitForm.add(new TooltipImage("duplicateDescriptionTooltipImage", new FIDLabelModel("message.duplicate_detection_description")));
        submitForm.add(new Button("submitButton"));
        submitForm.add(new Link("returnToSearchLink") {
            @Override
            public void onClick() {
                // XXX : WEB-2714
                setResponsePage(new SearchPage(criteriaModel.getObject()));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/schedule/mass_schedule.css");
    }

    private SchedulePicker createSchedulePicker(final ListItem<ScheduleSummaryEntry> item, final IModel<List<? extends EventType>> eventTypesForAssetType, final EventJobsForTenantModel jobsOptions) {
        final Model<ThingEvent> eventScheduleModel = new Model<ThingEvent>(new ThingEvent());
        return new SchedulePicker("schedulePicker", eventScheduleModel, eventTypesForAssetType, jobsOptions) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                item.getModelObject().getSchedules().add(eventScheduleModel.getObject());
                eventScheduleModel.setObject(new ThingEvent());
                target.add(assetTypesListContainer);
            }
        };
    }

    private SchedulePicker createScheduleAllPicker(final IModel<List<? extends EventType>> commonEventTypesModel, final EventJobsForTenantModel jobsOptions) {
        return new SchedulePicker("scheduleAllPicker", new PropertyModel<ThingEvent>(this, "scheduleForAll"), commonEventTypesModel, jobsOptions) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                for (ScheduleSummaryEntry scheduleSummaryEntry : scheduleSummary) {
                    scheduleSummaryEntry.getSchedules().add(scheduleForAll);
                }
                scheduleForAll = new ThingEvent();
                target.add(assetTypesListContainer);
                updateSchedulePickerButtonsForPickerOpened(target, false);
            }
        };
    }

    private void updateSchedulePickerButtonsForPickerOpened(AjaxRequestTarget target, boolean schedulePickerOpened) {
        this.aSchedulePickerIsOpen = schedulePickerOpened;
        target.add(scheduleAllPicker);
        target.addChildren(assetTypesListContainer, SchedulePicker.class);
    }

    private WebMarkupContainer createScheduleListView(String containerId, final IModel<ScheduleSummaryEntry> model) {
        final WebMarkupContainer container = new WebMarkupContainer(containerId) {
            @Override
            public boolean isVisible() {
                return !model.getObject().getSchedules().isEmpty();
            }
        };
        container.setOutputMarkupPlaceholderTag(true);
        container.add(new ListView<Event>("schedules", new PropertyModel<List<Event>>(model, "schedules")) {
            @Override
            protected void populateItem(final ListItem<Event> item) {
                ContextImage deleteImage = new ContextImage("deleteImage", "images/small-x.png");
                deleteImage.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        model.getObject().getSchedules().remove(item.getIndex());
                        target.add(container);
                    }
                });

                item.add(deleteImage);
                item.add(new Label("eventTypeName", new PropertyModel<String>(item.getModel(), "type.name")));
                item.add(new DateLabel("scheduledDate", new PropertyModel<Date>(item.getModel(), "dueDate")).withTimeAllowed());

                WebMarkupContainer jobDescriptionContainer = new WebMarkupContainer("jobDescriptionContainer");
                jobDescriptionContainer.setVisible(item.getModelObject().getProject() != null);
                jobDescriptionContainer.add(new Label("jobName", new PropertyModel<String>(item.getModel(), "project.name")));
                item.add(jobDescriptionContainer);
            }
        });
        return container;
    }

    private boolean hasAtLeastOneEvent(List<ScheduleSummaryEntry> scheduleSummary) {
        for (ScheduleSummaryEntry scheduleSummaryEntry : scheduleSummary) {
            if (scheduleSummaryEntry.getSchedules().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private List<AssetType> getAssetTypeList(List<ScheduleSummaryEntry> scheduleSummaryEntries) {
        List<AssetType> assetTypes = new ArrayList<AssetType>(scheduleSummaryEntries.size());
        for (ScheduleSummaryEntry entry : scheduleSummaryEntries) {
            assetTypes.add(entry.getAssetType());
        }
        return assetTypes;
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

    @Override
    protected boolean useSiteWideCss() {
        return false;
    }

}
