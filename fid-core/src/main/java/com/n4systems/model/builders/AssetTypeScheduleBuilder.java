package com.n4systems.model.builders;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.EventType;

public class AssetTypeScheduleBuilder extends BaseBuilder<AssetTypeSchedule> {
	
	private final AssetType assetType;
	private final EventType eventType;
	private final Long frequencyInDays;
	
	public static AssetTypeScheduleBuilder anAssetTypeSchedule() {
		return new AssetTypeScheduleBuilder(AssetTypeBuilder.anAssetType().build(), EventTypeBuilder.anEventType().build(), 365L);
	}

	private AssetTypeScheduleBuilder(AssetType assetType, EventType eventType, Long frequencyInDays) {
		this.assetType = assetType;
		this.eventType = eventType;
		this.frequencyInDays = frequencyInDays;
	}

	@Override
	public AssetTypeSchedule createObject() {
		AssetTypeSchedule schedule = new AssetTypeSchedule();
		
		schedule.setId(generateNewId());
		schedule.setAssetType(assetType);
		schedule.setEventType(eventType);
		schedule.setFrequency(frequencyInDays);
		return schedule;
	}

}
