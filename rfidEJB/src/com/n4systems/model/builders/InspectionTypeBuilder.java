package com.n4systems.model.builders;

import com.n4systems.model.InspectionType;

public class InspectionTypeBuilder extends EntityWithTenantBuilder<InspectionType> {
	private final String name;
	private String description;
	private boolean printable;
	private boolean retired = false;
	private boolean master = false;
	private long formVersion;
	
	public InspectionTypeBuilder(String name, String description, boolean printable, boolean retired, boolean master, long formVersion) {
		super();
		this.name = name;
		this.description = description;
		this.printable = printable;
		this.retired = retired;
		this.master = master;
		this.formVersion = formVersion;
	}
	
	public static InspectionTypeBuilder anInspectionType() {
		return new InspectionTypeBuilder("some Name", "some description", true, false, false, InspectionType.DEFAULT_FORM_VERSION);
	}
	
	public InspectionTypeBuilder named(String name) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion);
	}
	
	public InspectionTypeBuilder withDescription(String description) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion);
	}
	
	public InspectionTypeBuilder withPrintable(boolean printable) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion);
	}
	
	public InspectionTypeBuilder withRetired(boolean retired) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion);
	}
	
	public InspectionTypeBuilder withMaster(boolean master) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion);
	}
	
	public InspectionTypeBuilder withFormVersion(long formVersion) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion);
	}
	
	@Override
	public InspectionType build() {
		InspectionType type = assignAbstractFields(new InspectionType());
		type.setName(name);
		type.setDescription(description);
		type.setPrintable(printable);
		type.setRetired(retired);
		type.setMaster(master);
		type.setFormVersion(formVersion);
		return type;
	}

	
	
}
