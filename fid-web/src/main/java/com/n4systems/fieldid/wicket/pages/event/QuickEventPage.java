package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.wicket.components.DateLabel;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEvent;
import com.n4systems.services.date.DateService;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class QuickEventPage extends FieldIDFrontEndPage {

    private @SpringBean EventScheduleService eventScheduleService;
    private @SpringBean DateService dateService;

    private IModel<Asset> assetModel;

    public QuickEventPage(PageParameters params) {
        Long assetId = params.get("id").toLongObject();
        assetModel = new EntityModel<Asset>(Asset.class, assetId);

        add(new BookmarkablePageLink<Void>("manageSchedulesLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(assetId)));

        final IModel<List<ThingEvent>> eventSchedulesModel = createEventSchedulesModel(assetModel);
        add(new ListView<ThingEvent>("schedules", eventSchedulesModel) {
            @Override
            protected void populateItem(final ListItem<ThingEvent> item) {
                WebMarkupContainer daysAwayContainer = new WebMarkupContainer("daysAwayContainer");
                daysAwayContainer.add(new Label("daysAwayLabel", createDaysAwayLabelModel(item.getModel())));
                item.add(daysAwayContainer);

                item.add(new DateLabel("dateTimeLabel", ProxyModel.of(item.getModel(), on(Event.class).getDueDate())).withTimeAllowed());

                BookmarkablePageLink startEventLink = new BookmarkablePageLink<Void>("startScheduledEventLink", StartRegularOrMasterEventPage.class, createPageParameters(item.getModelObject()));
                startEventLink.add(new FlatLabel("eventTypeName", ProxyModel.of(item.getModel(), on(Event.class).getType().getName())));
                item.add(startEventLink);
            }
        });

        add(new WebMarkupContainer("noSchedulesMessageContainer") {
            @Override
            public boolean isVisible() {
                return eventSchedulesModel.getObject().isEmpty();
            }
        });

        add(new ListView<EventType>("eventTypes", new EventTypesForAssetTypeModel(ProxyModel.of(assetModel, on(Asset.class).getType()))) {
            @Override
            protected void populateItem(ListItem<EventType> item) {
                BookmarkablePageLink startEventLink = new BookmarkablePageLink<Void>("startEventLink", StartRegularOrMasterEventPage.class, createPageParameters(item.getModelObject()));
                startEventLink.add(new FlatLabel("eventTypeName", ProxyModel.of(item.getModel(), on(EventType.class).getName())));
                item.add(startEventLink);
            }
        });
    }

    private IModel<List<ThingEvent>> createEventSchedulesModel(final IModel<Asset> assetModel) {
        return new LoadableDetachableModel<List<ThingEvent>>() {
            @Override
            protected List<ThingEvent> load() {
                return eventScheduleService.getAvailableSchedulesFor(assetModel.getObject());
            }
        };
    }

    private IModel<String> createDaysAwayLabelModel(final IModel<? extends Event> scheduleModel) {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                Date dueDate = scheduleModel.getObject().getDueDate();
                int daysFromToday = dateService.getDaysFromToday(new LocalDate(dueDate));
                if (daysFromToday == -1) {
                    return getString("label.yesterday");
                } else if (daysFromToday < 0) {
                    return new FIDLabelModel("label.x_days_ago", Math.abs(daysFromToday)).getObject();
                } else if (daysFromToday == 0) {
                    return getString("label.today");
                } else if (daysFromToday == 1) {
                    return getString("label.tomorrow");
                } else {
                    return new FIDLabelModel("label.x_days_away", daysFromToday).getObject();
                }
            }
        };
    }

    private PageParameters createPageParameters(Event event) {
        return createPageParameters(event.getType())
                .add("scheduleId", event.getId());
    }

    private PageParameters createPageParameters(EventType eventType) {
        return new PageParameters()
                .add("type", eventType.getId())
                .add("assetId", assetModel.getObject().getId());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/quick_event.css");
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.launch_event"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId, NavigationItemBuilder.aNavItem()
                .label("nav.asset_information")
                .page(AssetSummaryPage.class)
                .params(PageParametersBuilder.uniqueId(assetModel.getObject().getId())).build()));
    }
}
