package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by rrana on 2014-08-12.
 */
public class EventTypeGroupViewPage extends EventTypeGroupPage{

    @SpringBean
    private EventTypeService eventTypeService;

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    private FIDFeedbackPanel feedbackPanel;
    protected IModel<EventTypeGroup> eventTypeGroupModel;
    private Long eventTypeGroupId;

    public EventTypeGroupViewPage(){
        super();
    }

    public EventTypeGroupViewPage(PageParameters params){
        super();
        eventTypeGroupModel = Model.of(eventTypeGroupService.getEventTypeGroupById(params.get("uniqueID").toLong()));
        eventTypeGroupId = eventTypeGroupModel.getObject().getId();
    }

    public EventTypeGroupViewPage(EventTypeGroup eventTypeGroup){
        super();
        eventTypeGroupModel = Model.of(eventTypeGroup);
        eventTypeGroupId = eventTypeGroup.getId();
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", eventTypeGroupService.getEventTypeGroupsByStateCount(Archivable.EntityState.ACTIVE))).page(EventTypeGroupListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", eventTypeGroupService.getEventTypeGroupsByStateCount(Archivable.EntityState.ARCHIVED))).page(EventTypeGroupListArchivePage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view")).page(EventTypeGroupViewPage.class).params(PageParametersBuilder.uniqueId(eventTypeGroupId)).build(),
                aNavItem().label(new FIDLabelModel("nav.edit")).page(EventTypeGroupEditPage.class).params(PageParametersBuilder.uniqueId(eventTypeGroupId)).build(),
                aNavItem().label(new FIDLabelModel("nav.add")).page(EventTypeGroupAddPage.class).onRight().build()
        ));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        Link editLink = new BookmarkablePageLink("edit", EventTypeGroupEditPage.class, PageParametersBuilder.uniqueId(eventTypeGroupModel.getObject().getID()));

        add(editLink);
        add(new Label("name", Model.of(eventTypeGroupModel.getObject().getName())));
        add(new Label("title", Model.of(eventTypeGroupModel.getObject().getReportTitle())));
        if(eventTypeGroupModel.getObject().getPrintOut() != null) {
            add(new Label("pdf", Model.of(eventTypeGroupModel.getObject().getPrintOut().getName())));
        } else {
            add(new Label("pdf", Model.of("None")));
        }
        if(eventTypeGroupModel.getObject().getObservationPrintOut() != null) {
            add(new Label("observation", Model.of(eventTypeGroupModel.getObject().getObservationPrintOut().getName())));
        } else {
            add(new Label("observation", Model.of("None")));
        }

        final List<EventType> eventTypeList = eventTypeService.getAllActiveEventTypesForGroup(eventTypeGroupModel.getObject().getId());
        add(new ListView<EventType>("eventTypes", eventTypeList) {
            @Override
            protected void populateItem(ListItem<EventType> item) {
                NonWicketLink link;
                link = new NonWicketLink("eventTypeLink", "eventType.action?uniqueID=" + item.getModelObject().getId());
                link.add(new Label("eventTypeName", item.getModelObject().getDisplayName()));
                item.add(link);
            }
        });

        add(new Label("noEventTypes", new FIDLabelModel("label.noeventtypesundergroup")).setVisible(eventTypeList.isEmpty()));

        add(new NonWicketLink("addAssetEvent", "eventTypeAdd.action?newEventType=Asset&group=" + eventTypeGroupModel.getObject().getId()));
        add(new NonWicketLink("addPlaceEvent", "eventTypeAdd.action?newEventType=Place&group=" + eventTypeGroupModel.getObject().getId()));
        add(new NonWicketLink("addAction", "eventTypeAdd.action?newEventType=Action&group=" + eventTypeGroupModel.getObject().getId()));
        add(new NonWicketLink("addProcedureAudit", "eventTypeAdd.action?newEventType=ProcedureAudit&group=" + eventTypeGroupModel.getObject().getId()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/event/event_base.css");
        response.renderCSSReference("style/legacy/newCss/event/event_schedule.css");
        response.renderCSSReference("style/legacy/pageStyles/eventTypeGroup.css");
    }
}
