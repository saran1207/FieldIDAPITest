package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.event.*;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeAround;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.persistence.utils.PostFetcher;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.concurrent.Callable;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ProcedureAuditEventSummaryPage extends EventSummaryPage {

    @SpringBean
    private EventService eventService;
    private IModel<ProcedureAuditEvent> eventModel;
    private IModel<ProcedureDefinition> procedureDefinitionModel;

    public ProcedureAuditEventSummaryPage(PageParameters parameters) {
        super(parameters);

        eventModel = new LocalizeModel<ProcedureAuditEvent>(Model.of(loadExistingEvent()));
        procedureDefinitionModel = new LocalizeModel<ProcedureDefinition>(new PropertyModel<ProcedureDefinition>(eventModel, "procedureDefinition"));
        eventSummaryType = EventSummaryType.PROCEDURE_AUDIT_EVENT;
    }

    @Override
    protected Panel getDetailsPanel(String id) {
        return new EventDetailsPanel(id, eventModel);
    }

    @Override
    protected Panel getEventFormPanel(String id) {
        return new EventFormViewPanel(id, eventModel, new PropertyModel<List<AbstractEvent.SectionResults>>(eventModel, "sectionResults"));
    }

    @Override
    protected Panel getEventResultPanel(String id) {
        return new EventResultPanel(id, eventModel);
    }

    @Override
    protected Panel getPostEventInfoPanel(String id) {
        return new PostEventDetailsPanel(id, eventModel);
    }

    @Override
    protected Panel getEventAttachmentsPanel(String id) {
        return new EventAttachmentsPanel(id, eventModel);
    }

    @Override
    protected Panel getEventLocationPanel(String id) {
        return new EventLocationPanel(id, eventModel);
    }

    @Override
    protected Panel getProofTestPanel(String id) {
        return new EmptyPanel(id);
    }

    @Override
    protected Panel getSubEventPanel(String id) {
        return new EmptyPanel(id);
    }

    @Override
    protected Event getEvent() {
        return eventModel.getObject();
    }

    private ProcedureAuditEvent loadExistingEvent() {
        final ProcedureAuditEvent event = eventService.lookupExistingEvent(ProcedureAuditEvent.class, uniqueId, true);
        ProcedureAuditEvent existingEvent = new LocalizeAround<ProcedureAuditEvent>(new Callable<ProcedureAuditEvent>() {
            @Override
            public ProcedureAuditEvent call() throws Exception {
                return PostFetcher.postFetchFields(event, Event.PROCEDURE_AUDIT_FIELD_PATHS);
            }
        }).call();

        return existingEvent;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(procedureDefinitionModel, "procedureCode"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.summary")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(procedureDefinitionModel.getObject().getId())).build(),
                aNavItem().label(new FIDLabelModel("label.event_summary")).page(ProcedureAuditEventSummaryPage.class).params(PageParametersBuilder.id(uniqueId)).build()
        ));
    }

}
