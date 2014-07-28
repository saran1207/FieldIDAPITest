package com.n4systems.handlers.remover.summary;

public class AssociatedPlaceEventTypeSummary extends RemovalSummary {

    private Long removeFromPlaces;
    private Long removeFromPlaceRecurrences;

    public AssociatedPlaceEventTypeSummary() {
        this(0L, 0L);
    }

    public AssociatedPlaceEventTypeSummary(Long removeFromPlaces, Long removeFromPlaceRecurrences) {
        this.removeFromPlaces = removeFromPlaces;
        this.removeFromPlaceRecurrences = removeFromPlaceRecurrences;
    }

    public Long getRemoveFromPlaces() {
        return removeFromPlaces;
    }

    public void setRemoveFromPlaces(Long removeFromPlaces) {
        this.removeFromPlaces = removeFromPlaces;
    }

    public Long getRemoveFromPlaceRecurrences() {
        return removeFromPlaceRecurrences;
    }

    public void setRemoveFromPlaceRecurrences(Long removeFromPlaceRecurrences) {
        this.removeFromPlaceRecurrences = removeFromPlaceRecurrences;
    }
}
