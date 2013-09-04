package com.n4systems.fieldid.wicket.pages.setup.translations;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class EventTypeGroupTranslationsPage extends TranslationsPage {

    private IModel<EventTypeGroup> eventTypeGroupModel;

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    public EventTypeGroupTranslationsPage() {
        eventTypeGroupModel = Model.of(new EventTypeGroup());

        add(new FidDropDownChoice<EventTypeGroup>("eventTypeGroup", eventTypeGroupModel, eventTypeGroupService.getAllEventTypeGroups(), getChoiceRenderer()));
    }

    public IChoiceRenderer<EventTypeGroup> getChoiceRenderer() {
        return new IChoiceRenderer<EventTypeGroup>() {
            @Override
            public Object getDisplayValue(EventTypeGroup group) {
                return group.getDisplayName();
            }

            @Override
            public String getIdValue(EventTypeGroup group, int index) {
                return group.getId()+"";
            }
        };
    }
}
