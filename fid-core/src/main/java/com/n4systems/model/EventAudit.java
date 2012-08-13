package com.n4systems.model;

import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

// NOTE : this is just a simple audit trail so we have chosen to store string representations of objects.
//   we don't want any foreign key issues caused by this table.

@Entity
@Table(name = "eventaudit")
public class EventAudit extends BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedBy")
    private User modifiedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name="owner", nullable = true)
    private String owner;

    @Column(name="location", nullable = true)
    private String location;

    @Column(name="performed_by", nullable = true)
    private String performedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="performed", nullable = true)
    private Date performed;

    @Column(name="event_book", nullable = true)
    private String eventBook;

    @Column(name="result", nullable = true)
    private String result;

    @Column(name="comments", nullable = true)
    private String comments;

    @Column(name = "asset_status", nullable = true)
    private String assetStatus;

    @Column(name = "event_status", nullable = true)
    private String eventStatus;

    @Column(name="printable", nullable = true)
    private String printable;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="eventaudit_event", joinColumns = @JoinColumn(name = "eventaudit_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "event_id"))
    private Set<Event> events;

    @Column(name="assigned_to", nullable = true)
    private String assignedTo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="nextDate", nullable = true)
    private Date nextDate;

    public EventAudit() {
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEventBook() {
        return eventBook;
    }

    public void setEventBook(String eventBook) {
        this.eventBook = eventBook;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getPerformed() {
        return performed;
    }

    public void setPerformed(Date performed) {
        this.performed = performed;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getPrintable() {
        return printable;
    }

    public void setPrintable(String printable) {
        this.printable = printable;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setAssignedUser(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

}

