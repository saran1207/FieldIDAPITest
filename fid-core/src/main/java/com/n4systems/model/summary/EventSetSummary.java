package com.n4systems.model.summary;

import java.io.Serializable;
import java.text.DecimalFormat;

public class EventSetSummary implements Serializable {

    private String name;
    private Object item;
    private int eventsDue = 0;
    private int eventsCompleted = 0;
    private int eventsPassed = 0;
    private int eventsFailed = 0;
    private int eventsOutstanding = 0;
    private int closedEvents = 0;

    public int getEventsDue() {
        return eventsDue;
    }

    public void incrementEventsDue() {
        this.eventsDue++;
    }

    public int getEventsCompleted() {
        return eventsCompleted;
    }

    public void incrementEventsCompleted() {
        this.eventsCompleted++;
    }

    public int getEventsPassed() {
        return eventsPassed;
    }

    public void incrementPassedEvents() {
        this.eventsPassed++;
    }

    public int getEventsFailed() {
        return eventsFailed;
    }

    public void incrementFailedEvents() {
        this.eventsFailed++;
    }

    public int getEventsOutstanding() {
        return eventsOutstanding;
    }

    public void incrementOutstandingEvents() {
        this.eventsOutstanding++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassedPercentage() {
        return new DecimalFormat("0.#%").format(eventsPassed / (double)eventsCompleted);
    }

    public String getFailedPercentage() {
        return new DecimalFormat("0.0%").format(eventsFailed / (double)eventsCompleted);
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public int getClosedEvents() {
        return closedEvents;
    }

    public void setClosedEvents(int closedEvents) {
        this.closedEvents = closedEvents;
    }

    public void incrementClosedEvents() {
        this.closedEvents++;
    }
}
