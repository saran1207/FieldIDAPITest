package com.n4systems.util.mail;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.parameter.CalendarUserType;
import biweekly.parameter.ParticipationStatus;
import biweekly.parameter.Role;
import biweekly.property.*;
import biweekly.util.Duration;
import com.n4systems.model.builders.Builder;
import com.n4systems.model.user.User;
import org.apache.commons.lang.time.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class ICalAttachmentBuilder implements Builder<byte[]> {

    private String subject;
    private Date startDate;
    private Boolean isAllDay = Boolean.FALSE;
    private User assignee;
    private User currentUser;

    private final static String DEFAULT_ORGANIZER_NAME = "FieldID";
    private final static String DEFAULT_ORGANIZER_EMAIL = "noreply@fieldid.com";
    private static String LANGUAGE = "en-us";

    public static ICalAttachmentBuilder anICalAttachment() {
        return new ICalAttachmentBuilder("", new Date(), null, null, Boolean.FALSE);
    }

    public ICalAttachmentBuilder(String subject, Date startDate, User assignee, User currentUser, Boolean isAllDay) {
        this.subject = subject;
        this.startDate = startDate;
        this.isAllDay = isAllDay;
        this.assignee = assignee;
        this.currentUser = currentUser;
    }

    public byte[] build() {
        ICalendar ical = new ICalendar();
        ical.setCalendarScale(CalendarScale.gregorian());
        ical.setMethod("REQUEST");

        VEvent event = new VEvent();

        event.setSummary(subject).setLanguage(LANGUAGE);

        event.setDateStart(new DateStart(startDate, !isAllDay));
        if(isAllDay) {
            event.setDuration(new Duration.Builder().days(1).build());
        } else {
            Date endDate = DateUtils.addHours(startDate, 1);
            event.setDateEnd(new DateEnd(endDate, !isAllDay));
        }

        if (assignee != null) {
            Attendee attendee = new Attendee(assignee.getDisplayName(), assignee.getEmailAddress());
            attendee.setRsvp(true);
            attendee.setRole(Role.ATTENDEE);
            attendee.setParticipationStatus(ParticipationStatus.NEEDS_ACTION);
            attendee.setCalendarUserType(CalendarUserType.INDIVIDUAL);
            event.addAttendee(attendee);
        }

        Organizer organizer;
        if (currentUser != null) {
            organizer = new Organizer(currentUser.getDisplayName(), currentUser.getEmailAddress());
        } else {
            organizer = new Organizer(DEFAULT_ORGANIZER_NAME, DEFAULT_ORGANIZER_EMAIL);
        }
        event.setOrganizer(organizer);

        ical.addEvent(event);

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        try {
            Biweekly.write(ical).go(bOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bOut.toByteArray();
    }

    public ICalAttachmentBuilder withSubject(String subject) {
        return new ICalAttachmentBuilder(subject, startDate, assignee, currentUser, isAllDay);
    }

    public ICalAttachmentBuilder withStartDate(Date startDate) {
        return new ICalAttachmentBuilder(subject, startDate, assignee, currentUser, isAllDay);
    }

    public ICalAttachmentBuilder withAllDayEvent(Boolean isAllDay) {
        return new ICalAttachmentBuilder(subject, startDate, assignee, currentUser, isAllDay);
    }

    public ICalAttachmentBuilder withAttendee(User attendee) {
        return new ICalAttachmentBuilder(subject, startDate, attendee, currentUser, isAllDay);
    }

    public ICalAttachmentBuilder withOrganizer(User organizer) {
        return new ICalAttachmentBuilder(subject, startDate, assignee, organizer, isAllDay);
    }

}
