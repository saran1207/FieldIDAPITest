package com.n4systems.services.reporting;

import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;

import java.io.Serializable;

public class AssetSearchRecord implements Serializable, HasGpsLocation {

    private Long count;
    private String desc;
    private GpsLocation gpsLocation;

    public AssetSearchRecord(Long count, String desc) {
        this.count = count;
        this.desc = desc;
    }

    public AssetSearchRecord(Long id, GpsLocation location, String type, String serialNumber, String status) {
        this.count = 1L;
        // Chain Sling : 2139457 (broken).
        this.desc = String.format("%s : %s (%s)", type,serialNumber,status);
        this.gpsLocation = location;
    }

    @Override
    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    @Override
    public String toString() {
       return desc;
    }
}
