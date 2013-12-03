package com.n4systems.fieldid.wicket.components.org.events.table;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.action.ActionDetailsPage;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.ActionTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.model.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class OpenActionsCell extends Panel {

    @SpringBean private EventService eventService;
    @SpringBean private PersistenceService persistenceService;

    private ModalWindow modalWindow;
    private Component/*SchedulePicker*/ schedulePicker;

    public OpenActionsCell(String id, final IModel<Event> eventModel, final Panel eventDisplayPanel) {
        super(id);

        add(modalWindow = createModalWindow(eventModel, eventDisplayPanel));
/*
        IModel<List<Event>> eventTypesModel = createEventTypesModelForEvent(eventModel);
*/
/*        add(schedulePicker = new SchedulePicker("schedulePickerWindow", eventModel, eventTypesModel, new EventJobsForTenantModel()) {
            { setSaveButtonLabel(new FIDLabelModel("label.save")); }
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                persistenceService.update(eventModel.getObject());
                target.add(eventDisplayPanel);
            }
        });*/
        add(schedulePicker = new WebMarkupContainer("schedulePickerWindow").setVisible(false));

                setVisible(FieldIDSession.get().getSessionUser().hasAccess("createevent") && FieldIDSession.get().getSessionUser().hasAccess("editevent"));
        
        final Event schedule = eventModel.getObject();

        String startAction = "selectEventAdd.action?scheduleId=" + schedule.getId() + "&assetId=" + schedule.getAsset().getId() + "&type=" + schedule.getType().getId();
        add(new NonWicketLink("startLink", startAction, new AttributeModifier("class", "btn-secondary")));

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
                eventDisplayPanel.getDefaultModel().detach();
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
                //schedulePicker.show(target);
            }
        };
        add(editLink);
    }

/*    private IModel<List<ThingEventType>> createEventTypesModelForEvent(IModel<ThingEvent> eventModel) {
        if (eventModel.getObject().getType() == null || !eventModel.getObject().getType().getGroup().isAction()) {
            return new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(eventModel, "asset.type"));
        } else {
            return new ActionTypesForTenantModel();
        }
    }*/

    private DialogModalWindow createModalWindow(final IModel<Event> eventModel, final Panel eventDisplayPanel) {
        DialogModalWindow dialogWindow = new DialogModalWindow("modalWindow");
        dialogWindow.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public Page createPage() {
                IModel<Event> entityModel = new EntityModel<Event>(Event.class, eventModel.getObject().getId());
                return new ActionDetailsPage(new PropertyModel<CriteriaResult>(entityModel, "sourceCriteriaResult"), entityModel)
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
