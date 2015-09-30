package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.action.ActionsPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.ActionTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.fieldid.wicket.pages.event.PerformEventPage;
import com.n4systems.fieldid.wicket.pages.masterevent.PerformMasterEventPage;
import com.n4systems.model.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class OpenActionsCell extends Panel {

    @SpringBean
    private EventService eventService;

    @SpringBean
    private EventScheduleService eventScheduleService;

    private ModalWindow modalWindow;
    private SchedulePicker schedulePicker;

    public OpenActionsCell(String id, final IModel<ThingEvent> eventModel, final Panel eventDisplayPanel) {
        super(id);

        add(modalWindow = createModalWindow(eventModel, eventDisplayPanel));
        IModel<List<? extends EventType>> eventTypesModel = createEventTypesModelForEvent(eventModel);
        add(schedulePicker = new SchedulePicker<ThingEvent>("schedulePickerWindow", eventModel, eventTypesModel, new EventJobsForTenantModel()) {
            { setSaveButtonLabel(new FIDLabelModel("label.save")); }
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                eventScheduleService.updateSchedule(eventModel.getObject());
                target.add(eventDisplayPanel);
            }
        });

        setVisible(FieldIDSession.get().getSessionUser().hasAccess("createevent") && FieldIDSession.get().getSessionUser().hasAccess("editevent"));
        
        final ThingEvent schedule = eventModel.getObject();

        PageParameters nextParams = new PageParameters().add("assetId", schedule.getTarget().getId())
                                                        .add("type", schedule.getType().getId())
                                                        .add("scheduleId", schedule.getId());
        if ((schedule.getEventType() instanceof ThingEventType) && schedule.getThingType().isMaster()) {
            add(new BookmarkablePageLink<PerformMasterEventPage>("startLink", PerformMasterEventPage.class, nextParams));
        } else {
            add(new BookmarkablePageLink<PerformEventPage>("startLink", PerformEventPage.class, nextParams));
        }

        add(new Link("closeLink") {
            @Override
            public void onClick() {
                setResponsePage(new CloseEventPage(PageParametersBuilder.uniqueId(schedule.getId()), (FieldIDFrontEndPage) getPage()));
            }
        });

        add(new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try{
                    eventService.retireEvent(schedule);
                } catch (Exception e) {
                    error(new FIDLabelModel("error.eventdeleting").getObject());
                    target.add(((AssetEventsPage)getPage()).getFeedbackPanel());
                }
                info(new FIDLabelModel("message.eventdeleted").getObject());
                target.add(eventDisplayPanel);
                target.add(((FieldIDFrontEndPage) getPage()).getTopFeedbackPanel());
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
        add(editLink);
    }

    private IModel<List<? extends EventType>> createEventTypesModelForEvent(IModel<ThingEvent> eventModel) {
        if (eventModel.getObject().getType() == null || !eventModel.getObject().getType().isActionEventType()) {
            return new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(eventModel, "asset.type"));
        } else {
            return new ActionTypesForTenantModel();
        }
    }

    private DialogModalWindow createModalWindow(final IModel<ThingEvent> eventModel, final Panel eventDisplayPanel) {
        DialogModalWindow dialogWindow = new DialogModalWindow("modalWindow");
        IModel<Event> entityModel = Model.of((Event)eventModel.getObject());
        dialogWindow.setContent(new ActionsPanel(dialogWindow.getContentId(), new PropertyModel<CriteriaResult>(entityModel, "sourceCriteriaResult"), ThingEvent.class, entityModel, true, true));
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
