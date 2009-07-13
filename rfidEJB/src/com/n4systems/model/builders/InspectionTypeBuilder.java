package com.n4systems.model.builders;

import com.n4systems.model.InspectionType;

public class InspectionTypeBuilder extends BaseBuilder<InspectionType> {
	
	private final String name;
	
	public static InspectionTypeBuilder anInspectionType() {
		return new InspectionTypeBuilder("some Name");
	}
	
	public InspectionTypeBuilder(String name) {
		super();
		this.name = name;
	}
	
	public InspectionTypeBuilder named(String name) {
		return new InspectionTypeBuilder(name);
	}
	
	@Override
	public InspectionType build() {
		InspectionType inspectionType = new InspectionType();
		inspectionType.setId(id);
		inspectionType.setName(name);
		return inspectionType;
	}

	
	
}
