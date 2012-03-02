package com.n4systems.model.summary;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import org.apache.commons.lang.time.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class EventResolutionSummary implements Serializable {

    private EventSetSummary baseSummary = new EventSetSummary();

    private Map<AssetType, EventSetSummary> assetTypeEventSummaries = new TreeMap<AssetType, EventSetSummary>(new AssetTypeNameComparator());
    private Map<EventType, EventSetSummary> eventTypeEventSummaries = new TreeMap<EventType, EventSetSummary>(new EventTypeNameComparator());
    private Map<Date, EventSetSummary> dateEventSummaries = new TreeMap<Date, EventSetSummary>();

    public EventSetSummary getOrCreateSummary(EventType eventType) {
        if (eventTypeEventSummaries.get(eventType) == null) {
            final EventSetSummary eventSetSummary = new EventSetSummary();
            eventSetSummary.setName(eventType.getName());
            eventSetSummary.setItem(eventType);
            eventTypeEventSummaries.put(eventType, eventSetSummary);
        }
        return eventTypeEventSummaries.get(eventType);
    }

    public EventSetSummary getOrCreateSummary(AssetType assetType) {
        if (assetTypeEventSummaries.get(assetType) == null) {
            final EventSetSummary eventSetSummary = new EventSetSummary();
            eventSetSummary.setName(assetType.getName());
            eventSetSummary.setItem(assetType);
            assetTypeEventSummaries.put(assetType, eventSetSummary);
        }
        return assetTypeEventSummaries.get(assetType);
    }

    public EventSetSummary getOrCreateSummary(Date date) {
        if (dateEventSummaries.get(date) == null) {
            final EventSetSummary eventSetSummary = new EventSetSummary();
            eventSetSummary.setName(new SimpleDateFormat("EEEEEE, MMMMM d yyyy").format(date));
            eventSetSummary.setItem(date);
            dateEventSummaries.put(date, eventSetSummary);
        }
        return dateEventSummaries.get(date);
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

    public Map<Date, EventSetSummary> getDateEventSummaries() {
        return dateEventSummaries;
    }

}
