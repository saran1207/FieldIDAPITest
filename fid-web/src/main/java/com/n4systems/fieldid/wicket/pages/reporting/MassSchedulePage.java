package com.n4systems.fieldid.wicket.pages.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.schedule.MassScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateLabel;
import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.CommonEventTypesModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import com.n4systems.model.search.AssetSearchCriteria;

public class MassSchedulePage extends FieldIDFrontEndPage {

    private List<Long> selectedIds;
    private List<ScheduleSummaryEntry> scheduleSummary;
    private boolean duplicateDetection = true;
    private boolean aSchedulePickerIsOpen = false;
    private EventSchedule scheduleForAll = new EventSchedule();

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
        IModel<List<EventType>> commonEventTypesModel = new CommonEventTypesModel(assetTypesList);

        assetTypesListContainer = new WebMarkupContainer("assetTypesListContainer");
        add(assetTypesListContainer.setOutputMarkupId(true));

        final EventJobsForTenantModel jobsOptions = new EventJobsForTenantModel();

        add(scheduleAllPicker = createScheduleAllPicker(commonEventTypesModel, jobsOptions));
        add(new WebMarkupContainer("noCommonEventTypesMessage").setVisible(commonEventTypesModel.getObject().isEmpty()));

        add(new FIDFeedbackPanel("feedbackPanel"));
        assetTypesListContainer.add(new ListView<ScheduleSummaryEntry>("assetTypeSummary", scheduleSummary) {
            @Override
            protected void populateItem(final ListItem<ScheduleSummaryEntry> item) {
                item.add(new Label("assetTypeName", new PropertyModel<String>(item.getModel(), "assetType.name")));
                item.add(new Label("count", new PropertyModel<Integer>(item.getModel(), "count")));

                final WebMarkupContainer schedulesContainer = createScheduleListView("schedulesContainer", item.getModel());
                item.add(schedulesContainer);

                IModel<List<EventType>> eventTypesForAssetType = new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(item.getModel(), "assetType"));

                item.add(createSchedulePicker(item, eventTypesForAssetType, jobsOptions));
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
                setResponsePage(new AssetSearchResultsPage(criteriaModel.getObject()));
            }
        };
        add(submitForm);
        submitForm.add(new CheckBox("duplicateDetection", new PropertyModel<Boolean>(this, "duplicateDetection")));
        submitForm.add(new TooltipImage("duplicateDescriptionTooltipImage", new FIDLabelModel("message.duplicate_detection_description")));
        submitForm.add(new Button("submitButton"));
        submitForm.add(new Link("returnToSearchLink") {
            @Override
            public void onClick() {
                setResponsePage(new AssetSearchResultsPage(criteriaModel.getObject()));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/schedule/mass_schedule.css");
    }

    private SchedulePicker createSchedulePicker(final ListItem<ScheduleSummaryEntry> item, final IModel<List<EventType>> eventTypesForAssetType, final EventJobsForTenantModel jobsOptions) {
        final Model<EventSchedule> eventScheduleModel = new Model<EventSchedule>(new EventSchedule());
        return new SchedulePicker("schedulePicker", new FIDLabelModel("label.add_a_schedule"), eventScheduleModel, eventTypesForAssetType, jobsOptions, -400, 0) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                item.getModelObject().getSchedules().add(eventScheduleModel.getObject());
                eventScheduleModel.setObject(new EventSchedule());
                target.add(assetTypesListContainer);
                updateSchedulePickerButtonsForPickerOpened(target, false);
            }
            @Override
            protected void onPickerOpened(AjaxRequestTarget target) {
                updateSchedulePickerButtonsForPickerOpened(target, true);
            }

            @Override
            protected void onPickerClosed(AjaxRequestTarget target) {
                updateSchedulePickerButtonsForPickerOpened(target, false);
            }

            @Override
            public boolean isOpenPickerButtonEnabled() {
                return !aSchedulePickerIsOpen;
            }
        };
    }

    private SchedulePicker createScheduleAllPicker(final IModel<List<EventType>> commonEventTypesModel, final EventJobsForTenantModel jobsOptions) {
        return new SchedulePicker("scheduleAllPicker", new FIDLabelModel("label.schedule_all"), new PropertyModel<EventSchedule>(this, "scheduleForAll"), commonEventTypesModel, jobsOptions, 0, 0) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                for (ScheduleSummaryEntry scheduleSummaryEntry : scheduleSummary) {
                    scheduleSummaryEntry.getSchedules().add(scheduleForAll);
                }
                scheduleForAll = new EventSchedule();
                target.add(assetTypesListContainer);
                updateSchedulePickerButtonsForPickerOpened(target, false);
            }

            @Override
            protected void onPickerOpened(AjaxRequestTarget target) {
                updateSchedulePickerButtonsForPickerOpened(target, true);
            }

            @Override
            protected void onPickerClosed(AjaxRequestTarget target) {
                updateSchedulePickerButtonsForPickerOpened(target, false);
            }

            @Override
            protected boolean isOpenPickerButtonEnabled() {
                return !aSchedulePickerIsOpen;
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
        container.add(new ListView<EventSchedule>("schedules", new PropertyModel<List<EventSchedule>>(model, "schedules")) {
            @Override
            protected void populateItem(final ListItem<EventSchedule> item) {
                ContextImage deleteImage = new ContextImage("deleteImage", "images/small-x.png");
                deleteImage.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        model.getObject().getSchedules().remove(item.getIndex());
                        target.add(container);
                    }
                });

                item.add(deleteImage);
                item.add(new Label("eventTypeName", new PropertyModel<String>(item.getModel(), "eventType.name")));
                item.add(new DateLabel("scheduledDate", new PropertyModel<Date>(item.getModel(), "nextStandardDate")));

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
