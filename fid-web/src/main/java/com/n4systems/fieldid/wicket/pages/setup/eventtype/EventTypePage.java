package com.n4systems.fieldid.wicket.pages.setup.eventtype;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.pages.setup.score.result.ScoreResultConfigurationPage;
import com.n4systems.model.EventType;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.IModel;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventTypePage extends FieldIDFrontEndPage {

    protected Long eventTypeId;
    protected IModel<EventType> eventTypeModel;

    public EventTypePage(PageParameters params) {
        super(params);
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        eventTypeId = params.getAsLong("uniqueID");
        eventTypeModel = new EntityModel<EventType>(EventType.class, eventTypeId);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("eventTypes.action").build(),
                aNavItem().label("nav.view").page("eventType.action").params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.edit").page("eventTypeEdit.action").params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.event_form").page(EventFormEditPage.class).params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.scoring").page(ScoreResultConfigurationPage.class).params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.asset_type_associations").page("selectAssetTypes.action").params(param("eventTypeId", eventTypeId)).build(),
                aNavItem().label("nav.add").page("eventTypeAdd.action").onRight().build(),
                aNavItem().label("nav.import").page("eventImportExport.action").params(uniqueId(eventTypeId)).onRight().build()));
    }

}
