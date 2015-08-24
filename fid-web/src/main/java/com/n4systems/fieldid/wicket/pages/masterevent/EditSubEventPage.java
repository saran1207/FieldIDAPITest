package com.n4systems.fieldid.wicket.pages.masterevent;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class EditSubEventPage extends SubEventPage {

    public EditSubEventPage(IModel<ThingEvent> masterEvent, IModel<SubEvent> subEvent) {
        this.masterEvent = masterEvent;
        this.event = subEvent;
        this.fileAttachments = subEvent.getObject().getAttachments() == null ? Lists.newArrayList() : subEvent.getObject().getAttachments();
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_subevent"));
    }

    @Override
    protected void onSave() {
        if (masterEvent.getObject().isNew()) {
            setResponsePage(new PerformMasterEventPage(masterEvent));
        } else {
            setResponsePage(new EditMasterEventPage(masterEvent));
        }
    }
}
