package com.n4systems.model.summary;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class EventResolutionSummary implements Serializable {

    private EventSetSummary baseSummary = new EventSetSummary();

    private Map<AssetType, EventSetSummary> assetTypeEventSummaries = new TreeMap<AssetType, EventSetSummary>(new AssetTypeNameComparator());
    private Map<EventType, EventSetSummary> eventTypeEventSummaries = new TreeMap<EventType, EventSetSummary>(new EventTypeNameComparator());

    public EventSetSummary getOrCreateSummary(EventType eventType) {
        if (eventTypeEventSummaries.get(eventType) == null) {
            final EventSetSummary eventSetSummary = new EventSetSummary();
            eventSetSummary.setName(eventType.getName());
            eventTypeEventSummaries.put(eventType, eventSetSummary);
        }
        return eventTypeEventSummaries.get(eventType);
    }

    public EventSetSummary getOrCreateSummary(AssetType assetType) {
        if (assetTypeEventSummaries.get(assetType) == null) {
            final EventSetSummary eventSetSummary = new EventSetSummary();
            eventSetSummary.setName(assetType.getName());
            assetTypeEventSummaries.put(assetType, eventSetSummary);
        }
        return assetTypeEventSummaries.get(assetType);
    }

    class EventTypeNameComparator implements Comparator<EventType>, Serializable {
        @Override
        public int compare(EventType eventType, EventType eventType2) {
            return eventType.getName().compareTo(eventType2.getName());
        }
    }

    class AssetTypeNameComparator implements Comparator<AssetType>, Serializable {
        @Override
        public int compare(AssetType assetType, AssetType assetType2) {
            return assetType.getName().compareTo(assetType2.getName());
        }
    }

    public EventSetSummary getBaseSummary() {
        return baseSummary;
    }

    public Map<AssetType, EventSetSummary> getAssetTypeEventSummaries() {
        return assetTypeEventSummaries;
    }

    public Map<EventType, EventSetSummary> getEventTypeEventSummaries() {
        return eventTypeEventSummaries;
    }

}
