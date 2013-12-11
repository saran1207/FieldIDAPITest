package com.n4systems.fieldid.wicket.components.org.events.table;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.model.PlaceEvent;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ClosedActionsCell extends Panel {

    @SpringBean
    private EventService eventService;

    public ClosedActionsCell(String id, IModel<PlaceEvent> eventModel, final EventListPanel eventListPanel) {
        super(id);
        
        final PlaceEvent event = eventModel.getObject();

        AjaxLink deleteLink;
        add(deleteLink = new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try{
                    // TODO DD: need to write service to handle place events (or events generically?)
                    throw new IllegalStateException("deleting not supported");
                    //eventService.retireEvent(event);
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

        add(new NonWicketIframeLink("viewLink", "aHtml/iframe/event.action?uniqueID=" + event.getID(), true, 650, 600, new AttributeModifier("class", "btn-secondary")));
    }
}
