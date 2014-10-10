package com.n4systems.fieldid.wicket.components.org.events.table;

import com.n4systems.fieldid.service.event.PlaceEventScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.action.ActionDetailsPage;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForPlaceModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.fieldid.wicket.pages.event.PerformPlaceEventPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceEventsPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OpenActionsCell extends Panel {

    @SpringBean
    private PlaceEventScheduleService placeEventScheduleService;

    private ModalWindow modalWindow;
    private SchedulePicker<PlaceEvent> schedulePicker;
    private boolean isDateUpdated = false;

    public OpenActionsCell(String id, final IModel<PlaceEvent> eventModel, final Panel eventDisplayPanel) {
        super(id, eventModel);

        final boolean canCreateEvents = FieldIDSession.get().getUserSecurityGuard().isAllowedCreateEvent();

        final PlaceEvent schedule = eventModel.getObject();

        add(modalWindow = createModalWindow(eventModel, eventDisplayPanel));

        add(schedulePicker = new SchedulePicker<PlaceEvent>("schedulePickerWindow", eventModel, new EventTypesForPlaceModel(new PropertyModel<BaseOrg>(eventModel, "place"))) {
            { setSaveButtonLabel(new FIDLabelModel("label.save")); }
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                placeEventScheduleService.updateSchedule(eventModel.getObject(), isDateUpdated);
                target.add(eventDisplayPanel);
            }

            @Override
            protected void onDatePicked(AjaxRequestTarget target) {
                isDateUpdated = true;
            }
        });


        PageParameters params = PageParametersBuilder.param("scheduleId", schedule.getId());
        params.add("type", schedule.getEventType().getId());
        params.add("placeId", schedule.getPlace().getId());

        add(new BookmarkablePageLink<PerformPlaceEventPage>("startLink", PerformPlaceEventPage.class, params).setVisible(canCreateEvents));

        add(new Link("closeLink") {
            @Override
            public void onClick() {
                setResponsePage(new CloseEventPage(PageParametersBuilder.uniqueId(schedule.getId()), (FieldIDTemplatePage) getPage()));
            }
        });

        add(new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try{
                    placeEventScheduleService.retireSchedule(schedule);
                    info(new FIDLabelModel("message.eventdeleted").getObject());
                    target.add(eventDisplayPanel);
                    target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                } catch (Exception e) {
                    error(new FIDLabelModel("error.eventdeleting").getObject());
                    target.add(((PlaceEventsPage) getPage()).getFeedbackPanel());
                }
            }
        });

        AjaxLink<Void> viewLink = new AjaxLink<Void>("viewLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.show(target);
            }
        };
        viewLink.setVisible(eventModel.getObject().isAction());
        add(viewLink);

        AjaxLink<Void> editLink = new AjaxLink<Void>("editLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                schedulePicker.show(target);
            }
        };
        editLink.setVisible(FieldIDSession.get().getSessionUser().hasAccess("createevent") && FieldIDSession.get().getSessionUser().hasAccess("editevent"));
        add(editLink);
    }

    private DialogModalWindow createModalWindow(final IModel<PlaceEvent> eventModel, final Panel eventDisplayPanel) {
        DialogModalWindow dialogWindow = new DialogModalWindow("modalWindow");
        dialogWindow.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public Page createPage() {
                IModel<Event> entityModel = new EntityModel<Event>(Event.class, eventModel.getObject().getId());
                return new ActionDetailsPage(new PropertyModel<CriteriaResult>(entityModel, "sourceCriteriaResult"), Event.class, entityModel)
                        .setAssetSummaryContext(true);
            }
        });

        dialogWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(eventDisplayPanel);
            }
        });

        dialogWindow.setInitialWidth(350);
        dialogWindow.setInitialHeight(500);
        dialogWindow.setTitle(new FIDLabelModel("label.action"));
        return dialogWindow;
    }

}
