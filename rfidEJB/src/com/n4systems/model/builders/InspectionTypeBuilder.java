package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeGroupBuilder.*;

import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;

public class InspectionTypeBuilder extends EntityWithTenantBuilder<InspectionType> {
	private final String name;
	private final String description;
	private final boolean printable;
	private final boolean retired;
	private final boolean master;
	private final long formVersion;
	private final InspectionTypeGroup group;
	
	
	public InspectionTypeBuilder(String name, String description, boolean printable, boolean retired, boolean master, long formVersion, InspectionTypeGroup group) {
		super();
		this.name = name;
		this.description = description;
		this.printable = printable;
		this.retired = retired;
		this.master = master;
		this.formVersion = formVersion;
		this.group = group;
	}
	
	public static InspectionTypeBuilder anInspectionType() {
		return new InspectionTypeBuilder("some Name", "some description", true, false, false, InspectionType.DEFAULT_FORM_VERSION, anInspectionTypeGroup().build());
	}
	
	public InspectionTypeBuilder named(String name) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
	}
	
	public InspectionTypeBuilder withDescription(String description) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
	}
	
	public InspectionTypeBuilder withPrintable(boolean printable) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
	}
	
	public InspectionTypeBuilder withRetired(boolean retired) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
	}
	
	public InspectionTypeBuilder withMaster(boolean master) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
	}
	
	public InspectionTypeBuilder withFormVersion(long formVersion) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
	}
	
	public InspectionTypeBuilder withGroup(InspectionTypeGroup group) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group);
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
		type.setGroup(group);
		return type;
	}

	
	
}
