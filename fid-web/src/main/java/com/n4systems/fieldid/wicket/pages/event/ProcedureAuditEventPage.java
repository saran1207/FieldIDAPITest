package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.event.ProcedureAuditEventCreationService;
import com.n4systems.fieldid.wicket.components.event.prooftest.ProofTestEditPanel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForProcedureAuditModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.LotoPage;
import com.n4systems.fieldid.wicket.pages.loto.ProceduresListPage;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2014-06-12.
 */
public abstract class ProcedureAuditEventPage extends EventPage<ProcedureAuditEvent>{

    @SpringBean
    protected ProcedureAuditEventCreationService procedureAuditEventCreationService;

    @Override
    protected SchedulePicker<ProcedureAuditEvent> createSchedulePicker() {

        SchedulePicker<ProcedureAuditEvent> picker = new SchedulePicker<ProcedureAuditEvent>("schedulePicker", new PropertyModel<ProcedureAuditEvent>(ProcedureAuditEventPage.this, "scheduleToAdd"), new EventTypesForProcedureAuditModel(new PropertyModel<ProcedureDefinition>(event, "procedureDefinition")));

        picker.setVisible(false);

        return picker;

    }

    @Override
    protected ProcedureAuditEvent createNewOpenEvent() {
        ProcedureAuditEvent procedureAuditEvent = new ProcedureAuditEvent();
        procedureAuditEvent.setProcedureDefinition(event.getObject().getProcedureDefinition());
        procedureAuditEvent.setRecurringEvent(event.getObject().getRecurringEvent());
        return procedureAuditEvent;
    }

    @Override
    protected Component createTargetDetailsPanel(IModel<ProcedureAuditEvent> model) {
        return new WebMarkupContainer("targetDetailsPanel");
    }

    @Override
    protected Component createPostEventPanel(IModel<ProcedureAuditEvent> event) {
        return new WebMarkupContainer("postEventPanel");
    }

    @Override
    protected boolean supportsProofTests() {
        return false;
    }

    @Override
    protected ProofTestEditPanel createProofTestEditPanel(String id) {
        return null;
    }

    @Override
    protected Component createCancelLink(String cancelLink) {
        return new BookmarkablePageLink<ProceduresListPage>(cancelLink, ProceduresListPage.class, PageParametersBuilder.uniqueId(event.getObject().getProcedureDefinition().getAsset().getId()));
    }

    @Override
    protected boolean targetAlreadyArchived(ProcedureAuditEvent event) {
        return event.getProcedureDefinition().isArchived();
    }

    @Override
    protected void retireEvent(ProcedureAuditEvent event) {

    }

    @Override
    protected void gotoSummaryPage(ProcedureAuditEvent event) {
        setResponsePage(LotoPage.class, PageParametersBuilder.id(event.getProcedureDefinition().getAsset().getId()));
    }

    protected List<EventScheduleBundle<ProcedureDefinition>> createEventScheduleBundles() {
        List<EventScheduleBundle<ProcedureDefinition>> scheduleBundles = new ArrayList<EventScheduleBundle<ProcedureDefinition>>();

        for (ProcedureAuditEvent sched : schedules) {
            EventScheduleBundle<ProcedureDefinition> bundle = new EventScheduleBundle<ProcedureDefinition>(sched.getProcedureDefinition(), sched.getType(), sched.getProject(), sched.getDueDate());
            scheduleBundles.add(bundle);
        }

        return scheduleBundles;
    }
}
