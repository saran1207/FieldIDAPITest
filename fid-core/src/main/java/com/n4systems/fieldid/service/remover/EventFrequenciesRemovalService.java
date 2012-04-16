package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.schedule.AssetTypeScheduleService;
import com.n4systems.handlers.remover.summary.EventFrequencyDeleteSummary;
import com.n4systems.handlers.remover.summary.SimpleLongRemovalSummary;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class EventFrequenciesRemovalService extends FieldIdPersistenceService {

    @Autowired
    private AssetTypeScheduleService assetTypeScheduleService;

    @Transactional
    public SimpleLongRemovalSummary summary(AssociatedEventType associatedEventType) {
        EventFrequencyDeleteSummary eventFrequencyDeleteSummary = new EventFrequencyDeleteSummary();
        eventFrequencyDeleteSummary.setElementsToRemove(getEventFrequencies(associatedEventType).size());
        return eventFrequencyDeleteSummary;
    }

    @Transactional
	public void remove(AssociatedEventType associatedEventType) {
        associatedEventType.getAssetType();
		List<AssetTypeSchedule> frequencies = getEventFrequencies(associatedEventType);
		deleteFrequencies(frequencies);
	}

    private List<AssetTypeSchedule> getEventFrequencies(EventType eventType) {
        return assetTypeScheduleService.getAssetTypeSchedules(eventType.getId(), null);
    }

	private List<AssetTypeSchedule> getEventFrequencies(AssociatedEventType eventType) {
        return assetTypeScheduleService.getAssetTypeSchedules(eventType.getEventType().getId(), eventType.getAssetType().getId());
	}

	private int deleteFrequencies(List<AssetTypeSchedule> frequencies ) {
		for (AssetTypeSchedule assetTypeSchedule : frequencies) {
            AssetType type = assetTypeSchedule.getAssetType();
            type.getSchedules().remove(assetTypeSchedule);
            persistenceService.update(type);

            persistenceService.remove(assetTypeSchedule);
		}
		return frequencies.size();
	}

}
