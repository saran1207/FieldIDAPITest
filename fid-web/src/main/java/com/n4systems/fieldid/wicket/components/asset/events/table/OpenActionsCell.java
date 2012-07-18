package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.event.CloseEventPage;
import com.n4systems.model.Event;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OpenActionsCell extends Panel {

    @SpringBean
    private EventService eventService;

    public OpenActionsCell(String id, IModel<Event> eventModel, final Panel eventDisplayPanel) {
        super(id);

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
    }


}
