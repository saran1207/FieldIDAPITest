package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.wicket.components.eventtype.GroupedEventTypePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.model.EventType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

public class EventTypeTranslationsPage extends TranslationsPage<EventType> {


    public EventTypeTranslationsPage() {
        super();
    }

    @Override
    protected DropDownChoice<EventType> createChoice(String id) {
        return new GroupedEventTypePicker(id, Model.of(new EventType()), new EventTypesForTenantModel());
    }

}
