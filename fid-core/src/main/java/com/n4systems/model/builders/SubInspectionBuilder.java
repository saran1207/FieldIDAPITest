package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.anInspectionType;
import static com.n4systems.model.builders.AssetBuilder.anAsset;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;
import com.n4systems.model.SubInspection;

public class SubInspectionBuilder extends BaseBuilder<SubInspection> {

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
	public SubInspection createObject() {
		SubInspection subInspection = new SubInspection();
		subInspection.setId(id);
		subInspection.setName(name);
		subInspection.setType(inspectionType);
		subInspection.setAsset(asset);
		return subInspection;
	}

}
