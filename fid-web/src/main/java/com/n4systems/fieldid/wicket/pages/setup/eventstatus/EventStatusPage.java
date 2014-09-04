package com.n4systems.fieldid.wicket.pages.setup.eventstatus;

import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.EventStatus;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventStatusPage extends FieldIDTemplatePage {

    protected Long eventStatusId;
    protected IModel<EventStatus> eventStatusModel;

    @SpringBean
    protected EventStatusService eventStatusService;

    public EventStatusPage(PageParameters params) {
        super(params);
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        if(!params.get("uniqueID").isNull()) {
            eventStatusId = params.get("uniqueID").toLong();
            eventStatusModel = new EntityModel<EventStatus>(EventStatus.class, eventStatusId);
        }else {
            eventStatusModel = new Model<EventStatus>(new EventStatus());
        }
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", eventStatusService.countEventStatusByEntityState(Archivable.EntityState.ACTIVE))).page(EventStatusListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", eventStatusService.countEventStatusByEntityState(Archivable.EntityState.ARCHIVED))).page(EventStatusArchivedListPage.class).build(),
                aNavItem().label("nav.edit").page(EventStatusFormPage.class).params(uniqueId(eventStatusId != null ? eventStatusId: 0L)).cond(eventStatusId != null).build(),
                aNavItem().label("nav.add").page(EventStatusFormPage.class).onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_event_status_long.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }
}
