package com.n4systems.model.builders;

import com.google.common.collect.Lists;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.asset.ScheduleSummaryEntry;

import java.util.List;

public class ScheduleSummaryEntryBuilder extends BaseBuilder<ScheduleSummaryEntry> {

    private AssetType assetType;
    private List<Long> assetIds = Lists.newArrayList();
    private List<Event> scheduledEvents = Lists.newArrayList();

    public ScheduleSummaryEntryBuilder() {
    }

    public ScheduleSummaryEntryBuilder withAssetType(AssetType assetType) {
        this.assetType = assetType;
        return this;
    }

    public ScheduleSummaryEntryBuilder withAssetIds(Long... ids) {
        this.assetIds.addAll(Lists.newArrayList(ids));
        return this;
    }

    public ScheduleSummaryEntryBuilder withScheduledEvents(Event... events) {
        this.scheduledEvents.addAll(Lists.newArrayList(events));
        return this;
    }



    @Override
    public ScheduleSummaryEntry createObject() {
        ScheduleSummaryEntry entry = new ScheduleSummaryEntry();
        entry.setAssetType(assetType);
        entry.getAssetIds().addAll(assetIds);
        entry.getSchedules().addAll(scheduledEvents);
        return entry;
    }
}
