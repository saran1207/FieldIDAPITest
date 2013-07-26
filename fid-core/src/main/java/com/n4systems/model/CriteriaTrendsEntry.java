package com.n4systems.model;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="criteria_trends")
public class CriteriaTrendsEntry extends EntityWithTenant {

    @Column(name="dueDate")
    private Date dueDate;

    @Column(name="completedDate")
    private Date completedDate;

    @ManyToOne
    @JoinColumn(name="performedby_id")
    private User performedBy;

    @ManyToOne
    @JoinColumn(name="assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name="assigned_group_id")
    private UserGroup assignedGroup;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name="event_form_id")
    private EventForm eventForm;

    @ManyToOne
    @JoinColumn(name="event_type_id")
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name="criteria_id")
    private Criteria criteria;

    @Column(name="criteria_section_name")
    private String criteriaSectionName;

    @Column(name="criteria_name")
    private String criteriaName;

    @Column(name="result_text")
    private String resultText;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private BaseOrg owner;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public UserGroup getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(UserGroup assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    public EventForm getEventForm() {
        return eventForm;
    }

    public void setEventForm(EventForm eventForm) {
        this.eventForm = eventForm;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }

    public String getCriteriaSectionName() {
        return criteriaSectionName;
    }

    public void setCriteriaSectionName(String criteriaSectionName) {
        this.criteriaSectionName = criteriaSectionName;
    }
}
