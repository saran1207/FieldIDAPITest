package com.n4systems.fieldid.wicket.pages.massevent;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.actions.helpers.EventScheduleSuggestion;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.perform.PerformThingEventHelperService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

public class SelectSchedulesPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetService assetService;

    @SpringBean
    private EventScheduleService eventScheduleService;

    @SpringBean
    private PerformThingEventHelperService eventHelperService;

    private IModel<AssetSearchCriteria> criteriaModel;

    public SelectSchedulesPage(IModel<AssetSearchCriteria> criteriaModel, IModel<EventType> eventTypeModel) {
        this.criteriaModel = criteriaModel;

        add(new Label("message", new FIDLabelModel("message.found_multiple_schedules", eventTypeModel.getObject().getDisplayName())));

        MultiIdSelection selectedAssetIds = criteriaModel.getObject().getSelection();

        boolean hasSchedules = false;

        List<AssetWithSchedules> assetsWithSchedulesList = Lists.newArrayList();

        for (Asset asset: assetService.getAssets(selectedAssetIds.getSelectedIds())) {
            AssetWithSchedules assetWithSchedules = new AssetWithSchedules();
            assetWithSchedules.asset = asset;
            assetWithSchedules.schedules = eventScheduleService.getAvailableSchedulesForAsset(asset, eventTypeModel.getObject());
            assetWithSchedules.selectedEvent = (ThingEvent) getSuggestedEventScheduleForAsset(asset, assetWithSchedules.schedules);
            if (!assetWithSchedules.schedules.isEmpty()) {
                hasSchedules = true;
            }
            assetsWithSchedulesList.add(assetWithSchedules);
        }

        if (!hasSchedules) {
            throw new RestartResponseException(new PerformMultiEventPage(getSelectedEventsList(assetsWithSchedulesList, eventTypeModel), true) {
                @Override
                public void onCancel() {
                    setResponsePage(new SelectMassEventPage(criteriaModel));
                }
            });
        }

        Form form;
        add(form = new Form<Void>("form"));
        form.setOutputMarkupId(true);

        form.add(new AjaxLink<Void>("markUnscheduledLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                assetsWithSchedulesList.stream().forEach(assetWithSchedules -> assetWithSchedules.selectedEvent = null);
                target.add(form);
            }
        });


        form.add(new ListView<AssetWithSchedules>("asset", assetsWithSchedulesList) {
            @Override
            protected void populateItem(ListItem<AssetWithSchedules> item) {
                AssetWithSchedules assetWithSchedules = item.getModelObject();

                item.add(new Label("identifier", new PropertyModel<String>(assetWithSchedules.asset, "identifier")));
                item.add(new FidDropDownChoice<ThingEvent>("schedule",
                        new PropertyModel<ThingEvent>(assetWithSchedules, "selectedEvent"),
                        assetWithSchedules.schedules,
                        new IChoiceRenderer<ThingEvent>() {
                            @Override
                            public Object getDisplayValue(ThingEvent object) {
                                return new DayDisplayModel(new PropertyModel<>(object, "dueDate"), true).getObject();
                            }

                            @Override
                            public String getIdValue(ThingEvent object, int index) {
                                return String.valueOf(object.getId());
                            }
                        }).setNullValid(true));
            }
        });

        form.add(new SubmitLink("submitLink"){
            @Override
            public void onSubmit() {
                List<ThingEvent> selectedEventList = getSelectedEventsList(assetsWithSchedulesList, eventTypeModel);
                setResponsePage(new PerformMultiEventPage(selectedEventList, true) {
                    @Override
                    public void onCancel() {
                        setResponsePage(new SelectSchedulesPage(criteriaModel, eventTypeModel));
                    }
                });
            }
        });

        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new SearchPage(criteriaModel.getObject()));
            }
        });

    }

    public Event getSuggestedEventScheduleForAsset(Asset asset, List<ThingEvent> eventSchedules){

        if (eventSchedules.isEmpty()){
            return null;
        }

        Event suggestedSchedule = new EventScheduleSuggestion(eventSchedules).getSuggestedSchedule();

        return (suggestedSchedule == null) ? null : suggestedSchedule;
    }

    private List<ThingEvent> getSelectedEventsList(List<AssetWithSchedules> assetsWithSchedulesList, IModel<EventType> eventTypeModel) {
        List<ThingEvent> selectedEventList = Lists.newArrayList();
        for (AssetWithSchedules assetWithSchedules: assetsWithSchedulesList) {
            if(assetWithSchedules.selectedEvent == null) {
                selectedEventList.add(eventHelperService.createEvent(assetWithSchedules.asset.getId(), eventTypeModel.getObject().getId()));
            } else {
                selectedEventList.add(assetWithSchedules.selectedEvent);
            }
        }
        return selectedEventList;
    }

    private class AssetWithSchedules implements Serializable {
        protected Asset asset;
        protected ThingEvent selectedEvent;
        protected List<ThingEvent> schedules;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.select_scheduled_event_to_perform"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                setResponsePage(new SearchPage(criteriaModel.getObject()));
            }
        }.add(new Label(linkLabelId, new FIDLabelModel("label.back_to_x", new FIDLabelModel("label.search_results").getObject())));
    }
}
