package com.n4systems.util;

public class EventRemovalSummary {

    private Integer masterEventsToDelete=0;
    private Integer standardEventsToDelete=0;
    private Integer eventSchedulesToDelete=0;

    public Integer getMasterEventsToDelete() {
        return masterEventsToDelete;
    }

    public void setMasterEventsToDelete(int masterEventsToDelete) {
        this.masterEventsToDelete = masterEventsToDelete;
    }
    
    public void addMasterEventToDelete() {
        this.masterEventsToDelete++;
    }

    public Integer getStandardEventsToDelete() {
        return standardEventsToDelete;
    }

    public void setStandardEventsToDelete(int standardEventsToDelete) {
        this.standardEventsToDelete = standardEventsToDelete;
    }

    public void addStandardEventsToDelete() {
        this.standardEventsToDelete++;
    }

    public void addStandardEventsToDelete(int eventsToDelete) {
        this.standardEventsToDelete += eventsToDelete;
    }

    public Integer getEventSchedulesToDelete() {
        return eventSchedulesToDelete;
    }

    public void setEventSchedulesToDelete(int eventSchedulesToDelete) {
        this.eventSchedulesToDelete = eventSchedulesToDelete;
    }

    public void addEventSchedulesToDelete() {
        this.eventSchedulesToDelete++;
    }

    public void addEventSchedulesToDelete(int scheduleToDelete) {
        this.eventSchedulesToDelete += scheduleToDelete;
    }
}
