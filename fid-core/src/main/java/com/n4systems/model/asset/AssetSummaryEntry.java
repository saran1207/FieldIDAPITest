package com.n4systems.model.asset;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventSchedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AssetSummaryEntry implements Serializable {

    private AssetType assetType;
    private List<Long> assetIds = new ArrayList<Long>();
    private List<EventSchedule> schedules = new ArrayList<EventSchedule>();
    private int count = 0;

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
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public List<EventSchedule> getSchedules() {
        return schedules;
    }
}
