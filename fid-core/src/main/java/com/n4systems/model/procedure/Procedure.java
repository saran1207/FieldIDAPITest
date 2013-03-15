package com.n4systems.model.procedure;

import com.n4systems.model.GpsLocation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "procedures")
public class Procedure extends EntityWithTenant {

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ProcedureDefinition type;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "performedby_id")
    private User performedBy;

    @Column(name="date")
    private Date date;

    private GpsLocation gpsLocation;

    // TODO: Results?


    public ProcedureDefinition getType() {
        return type;
    }

    public void setType(ProcedureDefinition type) {
        this.type = type;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(GpsLocation gpsLocation) {
        this.gpsLocation = gpsLocation;
    }
}
