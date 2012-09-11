package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.action.ActionDetailsPage;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OpenActionsCell extends Panel {

    @SpringBean
    private EventService eventService;

    private ModalWindow modalWindow;

    public OpenActionsCell(String id, final IModel<Event> eventModel, final Panel eventDisplayPanel) {
        super(id);

        add(modalWindow = createModalWindow(eventModel));

        setVisible(FieldIDSession.get().getSessionUser().hasAccess("createevent") && FieldIDSession.get().getSessionUser().hasAccess("editevent"));
        
        final Event schedule = eventModel.getObject();

        String startAction = "selectEventAdd.action?scheduleId=" + schedule.getId() + "&assetId=" + schedule.getAsset().getId() + "&type=" + schedule.getType().getId();
        add(new NonWicketLink("startLink", startAction, new AttributeModifier("class", "mattButtonLeft")));

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
    }

    private DialogModalWindow createModalWindow(final IModel<Event> eventModel) {
        DialogModalWindow dialogWindow = new DialogModalWindow("modalWindow");
        dialogWindow.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public Page createPage() {
                IModel<Event> entityModel = new EntityModel<Event>(Event.class, eventModel.getObject().getId());
                return new ActionDetailsPage(new PropertyModel<CriteriaResult>(entityModel, "sourceCriteriaResult"), entityModel);
            }
        });
        dialogWindow.setInitialWidth(350);
        dialogWindow.setInitialHeight(500);
        dialogWindow.setTitle(new FIDLabelModel("label.action"));
        return dialogWindow;
    }

}
