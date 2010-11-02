package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.AssetBuilder.anAsset;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;
import com.n4systems.model.SubEvent;

public class SubInspectionBuilder extends BaseBuilder<SubEvent> {

	private final String name;
	private final InspectionType inspectionType;
	private final Asset asset;
	
	public static SubInspectionBuilder aSubInspection(String name) {
		return new SubInspectionBuilder(name, anInspectionType().build(), anAsset().build());
	}

	public SubInspectionBuilder(String name, InspectionType inspectionType, Asset asset) {
		this.name = name;
		this.inspectionType = inspectionType;
		this.asset = asset;
	}
	
	public SubInspectionBuilder withType(InspectionType inspectionType) {
		return new SubInspectionBuilder(name, inspectionType, asset);
	}
	
	public SubInspectionBuilder withAsset(Asset asset) {
		return new SubInspectionBuilder(name, inspectionType, asset);
	}
	
	@Override
	public SubEvent createObject() {
		SubEvent subEvent = new SubEvent();
		subEvent.setId(id);
		subEvent.setName(name);
		subEvent.setType(inspectionType);
		subEvent.setAsset(asset);
		return subEvent;
	}

}
