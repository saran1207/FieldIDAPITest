package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventtypegroup.EventTypeGroupDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ReassignEventTypeGroupPage extends FieldIDFrontEndPage{

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;
    
    @SpringBean
    private EventTypeService eventTypeService;
    
    private Long eventTypeGroupId;

    private IModel<EventTypeGroup> eventTypeGroupModel;

    private IModel<EventTypeGroup> newEventTypeGroupModel;

    public ReassignEventTypeGroupPage(PageParameters params) {
        super(params);

        final EventTypeGroup group = eventTypeGroupModel.getObject();

        newEventTypeGroupModel = Model.of(new EventTypeGroup());
        add(new Label("message", new FIDLabelModel("msg.reassign_eventtypegroup", group.getDisplayName(), eventTypeGroupService.getNumberOfAssociatedEventTypes(group))));

        Form<Void> form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                EventTypeGroup newGroup = newEventTypeGroupModel.getObject();
                List<EventType> eventTypes = eventTypeService.getAllEventTypes(eventTypeGroupId);
                for (EventType type: eventTypes) {
                    type.setGroup(newGroup);
                    eventTypeService.update(type, getCurrentUser());
                }
                eventTypeGroupService.archive(group, getCurrentUser());
                FieldIDSession.get().info(new FIDLabelModel("message.eventtypegrouparchived").getObject());
                setResponsePage(new EventTypeGroupListPage());
            }
        };

        List<EventTypeGroup> groups = eventTypeGroupService.getAllEventTypeGroups();
        groups.remove(group);
        EventTypeGroupDropDownChoice groupsDropDownChoice;
        form.add(groupsDropDownChoice = new EventTypeGroupDropDownChoice("groups", newEventTypeGroupModel, groups));
        groupsDropDownChoice.setRequired(true);
        form.add(new Button("saveButton"));
        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new EventTypeGroupListPage());
            }
        });

        add(form);
        add(new FIDFeedbackPanel("feedbackPanel"));
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        if(!params.get("uniqueID").isNull()) {
            eventTypeGroupId = params.get("uniqueID").toLong();
            eventTypeGroupModel = new EntityModel<EventTypeGroup>(EventTypeGroup.class, eventTypeGroupId);
        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.eventtypegroup"));
    }

}
