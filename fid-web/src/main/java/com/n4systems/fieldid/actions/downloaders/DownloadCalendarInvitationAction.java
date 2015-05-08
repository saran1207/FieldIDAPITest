package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.NotifyEventAssigneeService;
import com.n4systems.model.Event;
import com.n4systems.util.mail.ICalAttachmentBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;

public class DownloadCalendarInvitationAction extends DownloadAction {

    @Autowired
    EventService eventService;

    private static Logger logger = Logger.getLogger(DownloadCalendarInvitationAction.class);

    public DownloadCalendarInvitationAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doDownload() {
        Event event = eventService.lookupExistingEvent(Event.class, getUniqueID());

        return getEventInvitation(event);
    }

    private String getEventInvitation(Event event) {
        if(event == null) {
            addActionError( getText( "error.noevent" ) );
            return MISSING;
        }

        String subject= NotifyEventAssigneeService.getCalendarInvitationSubject(event);

        byte [] invitation = ICalAttachmentBuilder.anICalAttachment()
                .withSubject(subject)
                .withStartDate(event.getDueDate())
                .withAllDayEvent(isAllDayEvent(event.getDueDate()))
                .withAttendee(event.getAssignee())
                .build();

        try {
            setFileName("invite.ics");
            sendFile(new ByteArrayInputStream(invitation));
        } catch (Exception e) {
            logger.error("Unable to download event cert", e);
            return ERROR;
        }
        return SUCCESS;
    }

    private Boolean isAllDayEvent(Date dueDate) {
        return DateUtils.truncate(dueDate, Calendar.DATE).equals(dueDate);
    }
}
