package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ClosedActionsCell extends Panel {

    @SpringBean
    private EventService eventService;

    public ClosedActionsCell(String id, IModel<Event> eventModel, final EventListPanel eventListPanel) {
        super(id);
        
        final Event event = eventModel.getObject();
        WebMarkupContainer menu = new WebMarkupContainer("menu");

        AjaxLink deleteLink;
        menu.add(deleteLink = new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try{
                    eventService.retireEvent(event);
                } catch (Exception e) {
                    error(new FIDLabelModel("error.eventdeleting").getObject());
                    target.add(((AssetEventsPage)getPage()).getFeedbackPanel());

                }
                info(new FIDLabelModel("message.eventdeleted").getObject());
                eventListPanel.getDefaultModel().detach();
                target.add(eventListPanel);
                target.add(((FieldIDFrontEndPage) getPage()).getTopFeedbackPanel());

            }
        });

        String viewButtonStyle;
        if(FieldIDSession.get().getSessionUser().hasAccess("editevent")) {
            viewButtonStyle = "mattButtonLeft";
        } else {
            viewButtonStyle = "mattButton";
            menu.setVisible(false);
        }
        add(menu);
        add(new NonWicketIframeLink("viewLink", "aHtml/iframe/event.action?uniqueID=" + event.getID(), true, 650, 600, new AttributeModifier("class", viewButtonStyle)));
    }
}
