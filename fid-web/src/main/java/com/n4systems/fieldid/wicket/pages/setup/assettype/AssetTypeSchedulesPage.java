package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.schedule.AssetTypeScheduleService;
import com.n4systems.fieldid.wicket.components.assettype.FrequencyFormPanel;
import com.n4systems.fieldid.wicket.components.assettype.RecurrenceFormPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.YesOrNoModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.NullCoverterModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.model.RecurringAssetTypeEvent;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AssetTypeSchedulesPage extends FieldIDFrontEndPage {

    private IModel<AssetType> assetType;

    @SpringBean
    private AssetTypeService assetTypeService;

    @SpringBean
    private AssetTypeScheduleService assetTypeScheduleService;

    private WebMarkupContainer schedules;
    private WebMarkupContainer filterActions;
    private ListView frequencyList;
    private ListView recurringEventList;

    private ModalWindow frequencyModalWindow;
    private ModalWindow recurrenceModalWindow;

    public AssetTypeSchedulesPage(PageParameters params) {
        super(params);
        assetType = Model.of(assetTypeService.getAssetType(params.get("uniqueID").toLong()));

        add(schedules = new WebMarkupContainer("schedules"));
        schedules.setOutputMarkupId(true);
        schedules.add(frequencyList = new ListView<AssetTypeSchedule>("frequency", getFrequencies()) {

            @Override
            protected void populateItem(ListItem<AssetTypeSchedule> item) {
                final IModel<AssetTypeSchedule> schedule = item.getModel();
                item.add(new Label("type", new PropertyModel<String>(schedule, "eventType.displayName")));
                item.add(new Label("recurrence", new FIDLabelModel("label.every_x_days", schedule.getObject().getFrequency())));
                if(schedule.getObject().isOverride())
                    item.add(new Label("owner", new PropertyModel<String>(schedule, "owner.displayName")));
                else
                    item.add(new Label("owner", "--"));
                item.add(new AjaxLink<Void>("remove") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        assetType.getObject().getSchedules().remove(schedule.getObject());
                        assetTypeService.update(assetType.getObject());
                        assetTypeScheduleService.delete(assetTypeScheduleService.getSchedule(schedule.getObject().getId()));
                        frequencyList.detachModels();
                        target.add(schedules, filterActions);
                    }
                });
            }
        });

        schedules.add(recurringEventList = new ListView<RecurringAssetTypeEvent>("recurringEvent", getRecurringEvents()) {
            @Override
            protected void populateItem(final ListItem<RecurringAssetTypeEvent> item) {
                RecurringAssetTypeEvent event = (RecurringAssetTypeEvent) item.getDefaultModelObject();

                item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                item.add(new Label("recurrence", new EnumLabelModel(event.getRecurrence().getType())));
                item.add(new Label("org", new NullCoverterModel(new PropertyModel<String>(item.getDefaultModelObject(), "owner.name"), "---")));
                item.add(new Label("time", new DisplayTimeModel(new PropertyModel<Set<RecurrenceTime>>(item.getDefaultModelObject(), "recurrence.times"))));
                if (((RecurringAssetTypeEvent) item.getDefaultModelObject()).getOwner() == null)
                    item.add(new Label("affectAll", "---"));
                else
                    item.add(new Label("affectAll", new YesOrNoModel(new PropertyModel<Boolean>(item.getModelObject(), "ownerAndDown"))));
                item.add(new AjaxLink("remove") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        assetTypeService.purgeRecurringEvent(item.getModelObject());
                        recurringEventList.detachModels();
                        target.add(schedules, filterActions);
                    }
                });
            }
        });

        add(frequencyModalWindow = new DialogModalWindow("addFrequency").setInitialWidth(480).setInitialHeight(280));
        frequencyModalWindow.setContent(getFrequencyForm());

        add(recurrenceModalWindow = new DialogModalWindow("addRecurrence").setInitialWidth(480).setInitialHeight(280));
        recurrenceModalWindow.setContent(getRecurrenceForm());
        WebMarkupContainer scheduleActions;
        add(scheduleActions = new WebMarkupContainer("scheduleActions"));

        scheduleActions.add(new AjaxLink<Void>("scheduleFrequency") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyModalWindow.show(target);
            }
        });

        scheduleActions.add(new AjaxLink<Void>("scheduleRecurrence") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                recurrenceModalWindow.show(target);
            }
        });

        add(filterActions = new WebMarkupContainer("filters"));
        filterActions.setOutputMarkupId(true);

        filterActions.add(new AjaxLink<Void>("showAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyList.setVisible(true);
                recurringEventList.setVisible(true);
                target.add(schedules);
            }
        }.add(new Label("total", getTotal())));

        filterActions.add(new AjaxLink<Void>("showRecurring") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyList.setVisible(false);
                recurringEventList.setVisible(true);
                target.add(schedules);
            }
        }.add(new Label("totalRecurring", getTotalRecurring())));

        filterActions.add(new AjaxLink<Void>("showFrequency") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyList.setVisible(true);
                recurringEventList.setVisible(false);
                target.add(schedules);
            }
        }.add(new Label("totalFrequency", getTotalFrequency())));
    }

    private RecurrenceFormPanel getRecurrenceForm() {
        return new RecurrenceFormPanel(recurrenceModalWindow.getContentId(), assetType){
            @Override
            protected void onCreateRecurrence(AjaxRequestTarget target) {
                target.add(schedules, filterActions);
                recurrenceModalWindow.close(target);
                recurrenceModalWindow.setContent(getRecurrenceForm());
            }
        };
    }

    private FrequencyFormPanel getFrequencyForm() {
        return new FrequencyFormPanel(frequencyModalWindow.getContentId(), assetType) {
            @Override
            protected void onSaveSchedule(AjaxRequestTarget target, AssetTypeSchedule schedule) {
                assetTypeScheduleService.createSchedule(schedule);
                assetType.getObject().getSchedules().add(schedule);
                assetTypeService.update(assetType.getObject());
                target.add(schedules, filterActions);
                frequencyModalWindow.close(target);
                frequencyModalWindow.setContent(getFrequencyForm());
            }
        };
    }

    private LoadableDetachableModel<List<AssetTypeSchedule>> getFrequencies() {
        return new LoadableDetachableModel<List<AssetTypeSchedule>>() {
            @Override
            protected List<AssetTypeSchedule> load() {
                return Lists.newArrayList(assetType.getObject().getSchedules());
            }
        };
    }

    private LoadableDetachableModel<List<RecurringAssetTypeEvent>> getRecurringEvents() {
        return new LoadableDetachableModel<List<RecurringAssetTypeEvent>>() {
            @Override
            protected List<RecurringAssetTypeEvent> load() {
                return assetTypeService.getRecurringEvents(assetType.getObject());
            }
        };
    }

    private LoadableDetachableModel<Long> getTotal() {
        return new LoadableDetachableModel<Long>() {
            @Override
            protected Long load() {
                return new Integer(frequencyList.getList().size() + recurringEventList.getList().size()).longValue();
            }
        };
    }

    private LoadableDetachableModel<Long> getTotalRecurring() {
        return new LoadableDetachableModel<Long>() {
            @Override
            protected Long load() {
                return new Integer(recurringEventList.getList().size()).longValue();
            }
        };
    }

    private LoadableDetachableModel<Long> getTotalFrequency() {
        return new LoadableDetachableModel<Long>() {
            @Override
            protected Long load() {
                return new Integer(frequencyList.getList().size()).longValue();
            }
        };
    }

    class DisplayTimeModel extends Model<String> {

        private PropertyModel<Set<RecurrenceTime>> model;

        DisplayTimeModel(PropertyModel<Set<RecurrenceTime>> model) {
            this.model = model;
        }

        @Override
        public String getObject() {
            List<String> result = Lists.newArrayList();
            Iterator<RecurrenceTime> iterator = model.getObject().iterator();
            while (iterator.hasNext()) {
                result.add(convertTimeToString(iterator.next()));
            }
            return Joiner.on(",").join(result);
        }

        protected String convertTimeToString(RecurrenceTime time) {
            String monthDay = (time.hasDay()) ?
                    new LocalDate().withMonthOfYear(time.getMonth()).withDayOfMonth(time.getDayOfMonth()).toString("MMM d") + " " :
                    "";

            LocalTime localTime = new LocalTime().withHourOfDay(time.getHour()).withMinuteOfHour(time.getMinute());

            String clock = localTime.toString("hh:mm a");

            return monthDay + clock;
        }
    }

    @Override
    protected void addNavBar(String navBarId) {
        Long assetTypeId = assetType.getObject().getId();
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(AssetTypeListPage.class).build(),
                aNavItem().label("nav.edit").page(EditAssetTypePage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_type_associations").page(EventTypeAssociationsPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.schedules").page(AssetTypeSchedulesPage.class).params(PageParametersBuilder.uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.add").page(AddAssetTypePage.class).onRight().build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //response.renderCSSReference("style/newCss/asset/actions-menu.css");
        response.renderCSSReference("style/newCss/component/buttons.css");
        response.renderCSSReference("style/newCss/assetType/assetTypeSchedules.css");

        //response.renderJavaScriptReference("javascript/subMenu.js");
        //response.renderOnDomReadyJavaScript("subMenu.init();");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new TitleLabel(labelId, assetType);
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.schedule_rules"));
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    private class TitleLabel extends Fragment {
        public TitleLabel(String id, IModel<AssetType> model) {
            super(id, "title", AssetTypeSchedulesPage.this, model);
            add(new Label("name", new PropertyModel<String>(model, "displayName")));
        }
    }
}
