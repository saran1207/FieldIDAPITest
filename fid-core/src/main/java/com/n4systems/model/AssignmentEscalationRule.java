package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * This is the JPA entity which represents rows in the assignment_escalation_rules table.
 *
 * Created by Jordan Heath on 2015-08-14.
 */
@Entity
@Table(name="assignment_escalation_rules")
public class AssignmentEscalationRule extends ArchivableEntityWithTenant {

    @ManyToOne
    @JoinColumn(name = "escalate_to_user_id")
    private User escalateToUser;

    @ManyToOne
    @JoinColumn(name = "reassign_user_id")
    private User reassignUser;

    @Column(name="notify_assignee")
    private boolean notifyAssignee;

    @Column(name="overdue_quantity")
    private int overdueQuantity;

    //TODO Should this be an enum?  Probably... you lazy pirate...
    @Column(name="overdue_unit")
    private String overdueUnit;

    //This may look a little bit strange, but we only care about the event_id here.  We use this along with the
    //event_family to pull the appropriate event from the table.  This allows us to more quickly pull up the
    //Escalation Rule, since we're not also immediately querying for the Event as well.
    @Column(name="event_id")
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name="event_family")
    private EventFamily eventFamily;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="event_due_date")
    private Date eventDueDate;

    @Column(name="rule_has_run")
    private boolean ruleHasRun;

    @Column(name="subject_text")
    private String subjectText;

    @Column(name="custom_message_text")
    private String customMessageText;

    @Column(name="additional_emails")
    private String additionalEmails;

    public User getEscalateToUser() {
        return escalateToUser;
    }

    public void setEscalateToUser(User escalateToUser) {
        this.escalateToUser = escalateToUser;
    }

    public User getReassignUser() {
        return reassignUser;
    }

    public void setReassignUser(User reassignUser) {
        this.reassignUser = reassignUser;
    }

    public boolean isNotifyAssignee() {
        return notifyAssignee;
    }

    public void setNotifyAssignee(boolean notifyAssignee) {
        this.notifyAssignee = notifyAssignee;
    }

    public int getOverdueQuantity() {
        return overdueQuantity;
    }

    public void setOverdueQuantity(int overdueQuantity) {
        this.overdueQuantity = overdueQuantity;
    }

    public String getOverdueUnit() {
        return overdueUnit;
    }

    public void setOverdueUnit(String overdueUnit) {
        this.overdueUnit = overdueUnit;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public EventFamily getEventFamily() {
        return eventFamily;
    }

    public void setEventFamily(EventFamily eventFamily) {
        this.eventFamily = eventFamily;
    }

    public Date getEventDueDate() {
        return eventDueDate;
    }

    public void setEventDueDate(Date eventDueDate) {
        this.eventDueDate = eventDueDate;
    }

    public boolean isRuleHasRun() {
        return ruleHasRun;
    }

    public void setRuleHasRun(boolean ruleHasRun) {
        this.ruleHasRun = ruleHasRun;
    }

    public String getSubjectText() {
        return subjectText;
    }

    public void setSubjectText(String subjectText) {
        this.subjectText = subjectText;
    }

    public String getCustomMessageText() {
        return customMessageText;
    }

    public void setCustomMessageText(String customMessageText) {
        this.customMessageText = customMessageText;
    }

    public String getAdditionalEmails() {
        return additionalEmails;
    }

    public void setAdditionalEmails(String additionalEmails) {
        this.additionalEmails = additionalEmails;
    }

    public enum EventFamily {
        PROCEDURE_AUDIT(ProcedureAuditEvent.class), THING_EVENT(ThingEvent.class), PLACE_EVENT(PlaceEvent.class);

        private Class clazz;

        private EventFamily(Class clazz) {
            this.clazz = clazz;
        }

        public Class getEventFamilyClass() {
            return clazz;
        }
    }
}
