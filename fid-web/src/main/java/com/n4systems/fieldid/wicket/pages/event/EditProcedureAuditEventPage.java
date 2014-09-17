package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventCriteriaEditService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.ProcedureAuditEventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.JavaScriptAlertConfirmBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureAuditCompletedListPage;
import com.n4systems.model.Event;
import com.n4systems.model.EventResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.persistence.utils.PostFetcher;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;

/**
 * Created by rrana on 2014-06-12.
 */
public class EditProcedureAuditEventPage extends ProcedureAuditEventPage {

    @SpringBean
    private EventService eventService;
    @SpringBean
    private EventCriteriaEditService criteriaEditService;
    @SpringBean
    private ProcedureAuditEventService procedureAuditEventService;

    private long uniqueId;

    public EditProcedureAuditEventPage(PageParameters parameters) {
        uniqueId = parameters.get("uniqueID").toLong();
        event = Model.of(loadExistingEvent());
        event.getObject().setResultFromCriteriaAvailable();
        if(event.getObject().isResultFromCriteriaAvailable()) {
            setEventResult(EventResult.VOID);
        } else {
            setEventResult(event.getObject().getEventResult());
        }
        fileAttachments = new ArrayList<FileAttachment>(event.getObject().getAttachments());
    }

    protected ProcedureAuditEvent loadExistingEvent() {
        ProcedureAuditEvent existingEvent = eventService.lookupExistingEvent(ProcedureAuditEvent.class, uniqueId, false, true);
        PostFetcher.postFetchFields(existingEvent, Event.PROCEDURE_AUDIT_FIELD_PATHS);
        return existingEvent;
    }

    @Override
    protected Component createCancelLink(String id) {

        return new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(ProcedureAuditEventSummaryPage.class, PageParametersBuilder.id(event.getObject().getId()));
            }
        };
    }

    @Override
    protected Link createDeleteLink(String linkId) {
        return new Link(linkId) {
            {
                add(new JavaScriptAlertConfirmBehavior(new FIDLabelModel("label.confirm_audit_delete")));
                setVisible(!event.getObject().isNew());
            }
            @Override
            public void onClick() {
                retireEvent(event.getObject());
                FieldIDSession.get().info(getString("message.audit_deleted"));
                setResponsePage(new ProcedureAuditCompletedListPage(PageParametersBuilder.uniqueId(event.getObject().getProcedureDefinition().getAsset().getId())));
            }
        };
    }

    @Override
    protected void retireEvent(ProcedureAuditEvent event) {
        procedureAuditEventService.retireEvent(event);
    }

    @Override
    protected ProcedureAuditEvent doSave() {
        saveEventBookIfNecessary();

        ProcedureAuditEvent editedEvent = event.getObject();
        editedEvent.setEventResult(getEventResult());
        criteriaEditService.storeCriteriaChanges(editedEvent);
        editedEvent.storeTransientCriteriaResults();
        return procedureAuditEventCreationService.updateEvent(editedEvent, null, fileAttachments);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_procedure_audit"));
    }

}
