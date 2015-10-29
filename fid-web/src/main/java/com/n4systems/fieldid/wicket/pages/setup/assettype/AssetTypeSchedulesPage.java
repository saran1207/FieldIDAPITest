package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.fieldid.service.schedule.AssetTypeScheduleService;
import com.n4systems.fieldid.wicket.components.DisplayRecurrenceTimeModel;
import com.n4systems.fieldid.wicket.components.assettype.AssetTypeRecurrenceFormPanel;
import com.n4systems.fieldid.wicket.components.assettype.FrequencyFormPanel;
import com.n4systems.fieldid.wicket.components.assettype.RecurrenceFormPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.NullCoverterModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.model.RecurringAssetTypeEvent;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
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

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AssetTypeSchedulesPage extends FieldIDFrontEndPage {

    private IModel<AssetType> assetTypeModel;

    @SpringBean
    private AssetTypeService assetTypeService;

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;

    private WebMarkupContainer schedules;
    private WebMarkupContainer filterActions;
    private ListView frequencyList;
    private ListView recurringEventList;

    private AjaxLink<Void> showAllLink;
    private AjaxLink<Void> showRecurringLink;
    private AjaxLink<Void> showFrequencyLink;

    private ModalWindow frequencyModalWindow;
    private ModalWindow recurrenceModalWindow;

    public AssetTypeSchedulesPage(PageParameters params) {
        super(params);
        assetTypeModel = Model.of(assetTypeService.getAssetType(params.get("uniqueID").toLong()));

        WebMarkupContainer scheduleContent = new WebMarkupContainer("scheduleContent");

        add(scheduleContent);

        scheduleContent.add(schedules = new WebMarkupContainer("schedules"));
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
                if(schedule.getObject().isAutoSchedule())
                    item.add(new Label("addOnIdentify", new FIDLabelModel("label.add_on_identify")));
                else
                    item.add(new Label("addOnIdentify", "--"));
                item.add(new AjaxLink<Void>("remove") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        AssetType assetType = assetTypeModel.getObject();
                        assetType.getSchedules().remove(schedule.getObject());
                        assetType.touch();
                        assetTypeService.update(assetType);
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
                item.add(new Label("time", new DisplayRecurrenceTimeModel(new PropertyModel<Set<RecurrenceTime>>(item.getDefaultModelObject(), "recurrence.times"))));

                boolean hasAffectAll = false;
                if (event.getOwner() != null && (event.getOwnerAndDown())) {
                    hasAffectAll = true;
                }

                if (!hasAffectAll) {
                    item.add(new Label("affectAll", "---"));
                } else {
                    item.add(new Label("affectAll", new FIDLabelModel("label.affect_all")));
                }

                boolean hasAutoAssign = event.getAutoAssign();

                if (hasAutoAssign) {
                    item.add(new Label("autoAssign", new FIDLabelModel("label.auto_assign_short")));
                } else {
                    item.add(new Label("autoAssign", ", ---"));

                }


                item.add(new AjaxLink("remove") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        assetTypeService.purgeRecurringEvent(item.getModelObject());
                        AssetType assetType = assetTypeModel.getObject();
                        assetType.touch();
                        assetTypeService.update(assetType);
                        recurringEventList.detachModels();
                        target.add(schedules, filterActions);
                    }
                });
            }
        });

        scheduleContent.add(frequencyModalWindow = new DialogModalWindow("addFrequency").setInitialWidth(480).setInitialHeight(280));
        frequencyModalWindow.setContent(getFrequencyForm());

        scheduleContent.add(recurrenceModalWindow = new DialogModalWindow("addRecurrence").setInitialWidth(480));
        recurrenceModalWindow.setContent(getRecurrenceForm());
        WebMarkupContainer scheduleActions;
        scheduleContent.add(scheduleActions = new WebMarkupContainer("scheduleActions"));

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

        scheduleContent.add(filterActions = new WebMarkupContainer("filters"));
        filterActions.setOutputMarkupId(true);

        filterActions.add(showAllLink = new AjaxLink<Void>("showAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyList.setVisible(true);
                recurringEventList.setVisible(true);
                showAllLink.add(new AttributeModifier("class", "active"));
                showFrequencyLink.add(AttributeModifier.remove("class"));
                showRecurringLink.add(AttributeModifier.remove("class"));
                target.add(schedules ,filterActions);
            }
        });
        showAllLink.add(new Label("total", getTotal()));

        filterActions.add(showRecurringLink = new AjaxLink<Void>("showRecurring") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyList.setVisible(false);
                recurringEventList.setVisible(true);
                showRecurringLink.add(new AttributeModifier("class", "active"));
                showFrequencyLink.add(AttributeModifier.remove("class"));
                showAllLink.add(AttributeModifier.remove("class"));
                target.add(schedules ,filterActions);
            }
        });
        showRecurringLink.add(new Label("totalRecurring", getTotalRecurring()));

        filterActions.add(showFrequencyLink = new AjaxLink<Void>("showFrequency") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                frequencyList.setVisible(true);
                recurringEventList.setVisible(false);
                showFrequencyLink.add(new AttributeModifier("class", "active"));
                showAllLink.add(AttributeModifier.remove("class"));
                showRecurringLink.add(AttributeModifier.remove("class"));
                target.add(schedules ,filterActions);
            }
        });
        showFrequencyLink.add(new Label("totalFrequency", getTotalFrequency()));

        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        add(blankSlate);
        blankSlate.add(new Label("message", new FIDLabelModel("message.asset_type_schedule.blank_slate", assetTypeModel.getObject().getDisplayName())));
        blankSlate.add(new BookmarkablePageLink("associateLink", EventTypeAssociationsPage.class, PageParametersBuilder.uniqueId(assetTypeModel.getObject().getId())));

        scheduleContent.setVisible(hasAssociatedEvents());
        blankSlate.setVisible(!hasAssociatedEvents());
    }

    private boolean hasAssociatedEvents() {
        return !associatedEventTypesService.getAssociatedEventTypes(assetTypeModel.getObject(), null).isEmpty();
    }

    private RecurrenceFormPanel getRecurrenceForm() {
        return new AssetTypeRecurrenceFormPanel(recurrenceModalWindow.getContentId(), assetTypeModel){
            @Override protected void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form) {
                super.onCreateRecurrence(target,form);
                target.add(schedules, filterActions);
                recurrenceModalWindow.close(target);
                recurrenceModalWindow.setContent(getRecurrenceForm());
            }
        };
    }

    private FrequencyFormPanel getFrequencyForm() {
        return new FrequencyFormPanel(frequencyModalWindow.getContentId(), assetTypeModel) {
            @Override
            protected void onSaveSchedule(AjaxRequestTarget target, AssetTypeSchedule schedule) {
                AssetType assetType = assetTypeModel.getObject();
                assetType.getSchedules().add(schedule);
                assetType.touch();
                assetTypeService.update(assetType);
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
                return Lists.newArrayList(assetTypeModel.getObject().getSchedules());
            }
        };
    }

    private LoadableDetachableModel<List<RecurringAssetTypeEvent>> getRecurringEvents() {
        return new LoadableDetachableModel<List<RecurringAssetTypeEvent>>() {
            @Override
            protected List<RecurringAssetTypeEvent> load() {
                return assetTypeService.getRecurringEvents(assetTypeModel.getObject());
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

    @Override
    protected void addNavBar(String navBarId) {
        Long assetTypeId = assetTypeModel.getObject().getId();
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
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
        response.renderCSSReference("style/legacy/newCss/assetType/assetTypeSchedules.css");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new TitleLabel(labelId, assetTypeModel);
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
