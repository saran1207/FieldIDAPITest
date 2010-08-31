package com.n4systems.model.builders;

import com.n4systems.model.InspectionTypeGroup;

public class InspectionTypeGroupBuilder extends BaseBuilder<InspectionTypeGroup> {

	private final String name;

	public static InspectionTypeGroupBuilder anInspectionTypeGroup() {
		return new InspectionTypeGroupBuilder("some name");
	}
	
	
	private InspectionTypeGroupBuilder(String name) {
		this.name = name;
	}

	
	public InspectionTypeGroupBuilder withName(String name) {
		return new InspectionTypeGroupBuilder(name);
	}
	
	
	@Override
	public InspectionTypeGroup build() {
		InspectionTypeGroup inspectionTypeGroup = new InspectionTypeGroup();
		inspectionTypeGroup.setId(generateNewId());
		inspectionTypeGroup.setName(name);
		
		return inspectionTypeGroup;
	}
}
