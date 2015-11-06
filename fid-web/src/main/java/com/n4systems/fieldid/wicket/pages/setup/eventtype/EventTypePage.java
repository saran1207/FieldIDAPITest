package com.n4systems.fieldid.wicket.pages.setup.eventtype;

import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.pages.setup.observationcount.ObservationCountResultConfigurationPage;
import com.n4systems.fieldid.wicket.pages.setup.score.result.ScoreResultConfigurationPage;
import com.n4systems.model.EventType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventTypePage extends FieldIDTemplatePage {

    protected Long eventTypeId;
    protected IModel<EventType> eventTypeModel;
    @SpringBean
    protected EventTypeService eventTypeService;

    public EventTypePage(PageParameters params) {
        super(params);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_event_type_named", eventTypeModel.getObject().getDisplayName()));
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        eventTypeId = params.get("uniqueID").toLong();
        eventTypeModel = Model.of(eventTypeService.getEventType(eventTypeId));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("eventTypes.action").build(),
                aNavItem().label("nav.view").page("eventType.action").params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.edit").page("eventTypeEdit.action").params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.event_form").page(EventFormEditPage.class).params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.observations").page(ObservationCountResultConfigurationPage.class).params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.scoring").page(ScoreResultConfigurationPage.class).params(uniqueId(eventTypeId)).build(),
                aNavItem().label("nav.rules").page(RulesPage.class).params(uniqueId(eventTypeId)).cond(eventTypeModel.getObject().isThingEventType()).build(),
                aNavItem().label("nav.asset_type_associations").page("selectAssetTypes.action").params(param("eventTypeId", eventTypeId)).cond(eventTypeModel.getObject().isThingEventType()).build()));
    }

}
