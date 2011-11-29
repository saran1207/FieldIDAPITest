package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.actions.search.AssetSearchAction;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateLabel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.asset.AssetSummaryEntry;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class MassSchedulePage extends FieldIDFrontEndPage {

    List<Long> selectedIds;

    @SpringBean
    private AssetService assetService;

    public MassSchedulePage(PageParameters params) {
        super(params);

        add(CSSPackageResource.getHeaderContribution("style/newCss/schedule/mass_schedule.css"));

        SearchContainer searchContainer = verifySearchIdNotExpired(params);
        selectedIds = searchContainer.getMultiIdSelection().getSelectedIds();

        final List<AssetSummaryEntry> assetSummary = assetService.getAssetSummary(selectedIds);

        add(new ListView<AssetSummaryEntry>("assetTypeSummary", assetSummary) {
            @Override
            protected void populateItem(final ListItem<AssetSummaryEntry> item) {
                final Model<EventSchedule> eventScheduleModel = new Model<EventSchedule>(new EventSchedule());
                item.add(new Label("assetTypeName", new PropertyModel<String>(item.getModel(), "assetType.name")));
                item.add(new Label("count", new PropertyModel<Integer>(item.getModel(), "count")));

                final WebMarkupContainer schedulesContainer = createScheduleListView("schedulesContainer", item.getModel());
                item.add(schedulesContainer);

                IModel<List<EventType>> eventTypesForAssetType = new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(item.getModel(), "assetType"));

                item.add(new SchedulePicker("schedulePicker", eventScheduleModel, eventTypesForAssetType, new EventJobsForTenantModel(), -250, 0) {
                    @Override
                    protected void onPickComplete(AjaxRequestTarget target) {
                        item.getModelObject().getSchedules().add(eventScheduleModel.getObject());
                        eventScheduleModel.setObject(new EventSchedule());
                        target.addComponent(schedulesContainer);
                    }
                });
            }
        });

        Form submitForm = new Form("submitForm") {
            @Override
            protected void onSubmit() {
                for (AssetSummaryEntry assetSummaryEntry : assetSummary) {
                    for (EventSchedule eventSchedule : assetSummaryEntry.getSchedules()) {
                        System.out.println("We shall be creating schedule: " + ToStringBuilder.reflectionToString(eventSchedule));
                    }
                }
            }
        };
        add(submitForm);
        submitForm.add(new Button("submitButton"));
        submitForm.add(new NonWicketLink("returnToSearchLink", "searchResults.action?searchId="+params.getString("searchId")));
    }

    private WebMarkupContainer createScheduleListView(String containerId, final IModel<AssetSummaryEntry> model) {
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
                        target.addComponent(container);
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

    private SearchContainer verifySearchIdNotExpired(PageParameters params) {
        String searchId = params.getString("searchId");
        SearchContainer searchContainer = FieldIDSession.get().getSearchContainer(AssetSearchAction.SEARCH_CRITERIA);
        if (searchContainer == null || searchId == null || !searchId.equals(searchContainer.getSearchId())) {
            FieldIDSession.get().storeErrorMessageForStruts(new FIDLabelModel("error.searchexpired").getObject());
            throw new RedirectToUrlException("/search.action");
        }
        return searchContainer;
    }

    private LoadableDetachableModel<List<AssetSummaryEntry>> createAssetSummaryModel() {
        return new LoadableDetachableModel<List<AssetSummaryEntry>>() {
            @Override
            protected List<AssetSummaryEntry> load() {
                return assetService.getAssetSummary(selectedIds);
            }
        };
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
