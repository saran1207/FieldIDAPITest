package com.n4systems.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class GpsBounds implements Serializable {

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="latitude", column = @Column(name="south_bounds") ),
            @AttributeOverride(name="longitude", column = @Column(name="west_bounds") )
    } )
    private GpsLocation swBounds;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="latitude", column = @Column(name="north_bounds") ),
            @AttributeOverride(name="longitude", column = @Column(name="east_bounds") )
    } )
    private GpsLocation neBounds;

    public GpsBounds() {
    }

    public GpsBounds(double s, double w, double n, double e) {
        this.swBounds = new GpsLocation(s,w);
        this.neBounds = new GpsLocation(n,e);
    }

    public GpsLocation getNeBounds() {
        return neBounds;
    }

    public void setNeBounds(GpsLocation neBounds) {
        this.neBounds = neBounds;
    }

    public GpsLocation getSwBounds() {
        return swBounds;
    }

    public void setSwBounds(GpsLocation swBounds) {
        this.swBounds = swBounds;
    }

    public boolean isEmpty() {
        return swBounds==null || neBounds==null;
    }

    public GpsLocation getCentre() {
        if (isEmpty()) {
            return null;
        }
        BigDecimal lat = swBounds.getLatitude().add(neBounds.getLatitude()).divide(new BigDecimal(2.0));
        BigDecimal lng = swBounds.getLongitude().add(neBounds.getLongitude()).divide(new BigDecimal(2.0));
        return new GpsLocation(lat,lng);
    }

    public GpsBounds extend(GpsLocation gpsLocation) {
        if (gpsLocation!=null && !gpsLocation.isEmpty()) {
            if (swBounds==null) swBounds = new GpsLocation(Integer.MAX_VALUE,Integer.MAX_VALUE);
            if (neBounds==null) neBounds = new GpsLocation(Integer.MIN_VALUE,Integer.MIN_VALUE);
            if (gpsLocation.getLatitude().compareTo(swBounds.getLatitude())<0) {
                swBounds.setLatitude(gpsLocation.getLatitude());
            }
            if (gpsLocation.getLatitude().compareTo(neBounds.getLatitude())>0) {
                neBounds.setLatitude(gpsLocation.getLatitude());
            }
            if (gpsLocation.getLongitude().compareTo(swBounds.getLongitude())<0) {
                swBounds.setLongitude(gpsLocation.getLongitude());
            }
            if (gpsLocation.getLongitude().compareTo(neBounds.getLongitude())>0) {
                neBounds.setLongitude(gpsLocation.getLongitude());
            }
        }
        return this;
    }
}
