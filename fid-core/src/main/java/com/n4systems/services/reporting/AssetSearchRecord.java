package com.n4systems.services.reporting;

import com.google.common.base.Preconditions;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGroupedGpsLocation;
import com.n4systems.model.orgs.BaseOrg;

import java.io.Serializable;

public class AssetSearchRecord implements Serializable, HasGroupedGpsLocation {

    private Long count;
    private GpsLocation gpsLocation;
    private Long id;
    private String serialNumber;
    private String status;
    private String type;
    private String owner;

    public AssetSearchRecord(Long count, GpsLocation centreLocation) {
        // note : you should really only be calling this if count>1 but won't assert that just in case you really want to.
        Preconditions.checkArgument(count>0);
        this.count = count;
        this.gpsLocation = centreLocation;
    }

    private AssetSearchRecord(Long id, GpsLocation location, AssetType type, String serialNumber, AssetStatus status, BaseOrg owner) {
        this.count = 1L;
        this.id = id;
        this.type = type.getDisplayName();
        this.serialNumber = serialNumber;
        this.status = status==null ? "" :status.getDisplayName();
        this.owner = owner.getHierarchicalDisplayName();
        this.gpsLocation = location;
    }

    public AssetSearchRecord(Asset asset) {
        this(asset.getId(), asset.getGpsLocation(), asset.getType(), asset.getIdentifier(), asset.getAssetStatus(), asset.getOwner());
    }

    @Override
    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s : %s (%s) @ %s", serialNumber, type, status, owner );
    }

    @Override
    public long getCountAtLocation() {
        return count;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
