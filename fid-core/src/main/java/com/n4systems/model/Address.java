package com.n4systems.model;


import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {

    private String text;
    private String description;

    @Embedded
    private GpsLocation gpsLocation;

    public Address() {
    }

    public Address(String text) {
        setText(text);
    }

    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(GpsLocation gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
