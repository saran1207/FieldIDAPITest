package com.n4systems.model;

import javax.persistence.*;
import java.io.Serializable;

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
}
