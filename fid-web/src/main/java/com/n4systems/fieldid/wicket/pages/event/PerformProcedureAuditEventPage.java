package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.perform.PerformProcedureAuditEventHelperService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureAuditListPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.tools.FileDataContainer;
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
public class PerformProcedureAuditEventPage extends ProcedureAuditEventPage {

    @SpringBean
    private EventService eventService;
    @SpringBean private PersistenceService persistenceService;
    @SpringBean private EventScheduleService eventScheduleService;

    @SpringBean private PerformProcedureAuditEventHelperService procedureAuditEventHelperService;

    public PerformProcedureAuditEventPage(Long scheduleId, Long procedureDefinitionId, Long typeId) {
        try {
            ProcedureAuditEvent procedureAuditEvent = procedureAuditEventHelperService.createEvent(scheduleId, procedureDefinitionId, typeId);
            event = Model.of(procedureAuditEvent);

            setEventResult(event.getObject().getEventResult());
            fileAttachments = new ArrayList<FileAttachment>();

            doAutoSchedule();
            setAssetOwnerUpdate(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PerformProcedureAuditEventPage(PageParameters parameters) {
        this(parameters.get("scheduleId").isEmpty()?null:parameters.get("scheduleId").toLongObject(),
                parameters.get("procedureDefinitionId").toLongObject(),
                parameters.get("type").toLongObject());
    }

    @Override
    protected Component createCancelLink(String id) {

        return new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new ProcedureAuditListPage());
            }
        };
    }

    @Override
    protected AbstractEvent doSave() {
        event.getObject().storeTransientCriteriaResults();
        event.getObject().setEventResult(getEventResult());

        FileDataContainer fileDataContainer = null;
        if (event.getObject().getType().isThingEventType()) {
            fileDataContainer = proofTestEditPanel.getFileDataContainer();
        }

        Event savedEvent = procedureAuditEventCreationService.createEventWithSchedules(event.getObject(), 0L, fileDataContainer, fileAttachments, createEventScheduleBundles());

        return savedEvent;
    }


    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_procedure_audit"));
    }

}
