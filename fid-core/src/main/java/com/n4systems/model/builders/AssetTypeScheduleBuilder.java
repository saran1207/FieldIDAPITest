package com.n4systems.model.builders;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.InspectionType;

public class AssetTypeScheduleBuilder extends BaseBuilder<AssetTypeSchedule> {
	
	private final AssetType assetType;
	private final InspectionType inspectionType;
	private final Long frequencyInDays;
	
	public static AssetTypeScheduleBuilder anAssetTypeSchedule() {
		return new AssetTypeScheduleBuilder(AssetTypeBuilder.anAssetType().build(), InspectionTypeBuilder.anInspectionType().build(), 365L);
	}

	private AssetTypeScheduleBuilder(AssetType assetType, InspectionType inspectionType, Long frequencyInDays) {
		this.assetType = assetType;
		this.inspectionType = inspectionType;
		this.frequencyInDays = frequencyInDays;
	}

	@Override
	public AssetTypeSchedule createObject() {
		AssetTypeSchedule schedule = new AssetTypeSchedule();
		
		schedule.setId(generateNewId());
		schedule.setAssetType(assetType);
		schedule.setInspectionType(inspectionType);
		schedule.setFrequency(frequencyInDays);
		return schedule;
	}

}
