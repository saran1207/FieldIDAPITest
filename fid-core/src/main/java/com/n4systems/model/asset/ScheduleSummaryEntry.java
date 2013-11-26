package com.n4systems.model.asset;

import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScheduleSummaryEntry implements Serializable {

    private AssetType assetType;
    private List<Long> assetIds = new ArrayList<Long>();
    private List<ThingEvent> schedules = new ArrayList<ThingEvent>();

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public List<Long> getAssetIds() {
        return assetIds;
    }

    public int getCount() {
        return assetIds.size();
    }

    public List<ThingEvent> getSchedules() {
        return schedules;
    }

}
