package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.components.eventtype.GroupedEventTypePicker;
import com.n4systems.fieldid.wicket.components.eventtypegroup.EventTypeGroupDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class EventTypeTranslationsPage extends TranslationsPage {

    private IModel<EventType> eventTypeModel;


    public EventTypeTranslationsPage() {

        eventTypeModel = Model.of(new EventType());

        add(new GroupedEventTypePicker("eventType", eventTypeModel, new EventTypesForTenantModel()));
    }


}
