package com.n4systems.model.pentaho;

import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rrana on 2016-03-08.
 */

@Entity
@Table(name = "pentaho_test")
public class PentahoTest implements UnsecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name="performedby")
    private String performedby;

    @Column(name="eventtype")
    private String eventtype;

    @Column(name="assettype")
    private String assettype;

    @Column(name="location")
    private String location;

    @Column(name="assetstatus")
    private String assetstatus;

    @Column(name="eventstatus")
    private String eventstatus;

    @Column(name="event_result")
    private String event_result;

    @Column(name="completedDate")
    private Date completedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerformedby() {
        return performedby;
    }

    public void setPerformedby(String performedby) {
        this.performedby = performedby;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public String getAssettype() {
        return assettype;
    }

    public void setAssettype(String assettype) {
        this.assettype = assettype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAssetstatus() {
        return assetstatus;
    }

    public void setAssetstatus(String assetstatus) {
        this.assetstatus = assetstatus;
    }

    public String getEventstatus() {
        return eventstatus;
    }

    public void setEventstatus(String eventstatus) {
        this.eventstatus = eventstatus;
    }

    public String getEvent_result() {
        return event_result;
    }

    public void setEvent_result(String event_result) {
        this.event_result = event_result;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}