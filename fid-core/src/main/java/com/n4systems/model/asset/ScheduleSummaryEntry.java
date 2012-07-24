package com.n4systems.model.asset;

import com.n4systems.model.AssetType;
import com.n4systems.model.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScheduleSummaryEntry implements Serializable {

    private AssetType assetType;
    private List<Long> assetIds = new ArrayList<Long>();
    private List<Event> schedules = new ArrayList<Event>();

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

    public List<Event> getSchedules() {
        return schedules;
    }

}
