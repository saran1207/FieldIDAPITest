package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EventTypeGroupTranslationsPage extends TranslationsPage<EventTypeGroup> {

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    public EventTypeGroupTranslationsPage() {
        super();
    }

    @Override
    protected DropDownChoice createChoice(String id) {
        return new FidDropDownChoice<EventTypeGroup>(id, Model.of(new EventTypeGroup()), eventTypeGroupService.getAllEventTypeGroups(), getChoiceRenderer());
    }

    public IChoiceRenderer<EventTypeGroup> getChoiceRenderer() {
        return new IChoiceRenderer<EventTypeGroup>() {
            @Override public Object getDisplayValue(EventTypeGroup group) {
                return group.getDisplayName();
            }
            @Override public String getIdValue(EventTypeGroup group, int index) {
                return group.getId()+"";
            }
        };
    }
}
