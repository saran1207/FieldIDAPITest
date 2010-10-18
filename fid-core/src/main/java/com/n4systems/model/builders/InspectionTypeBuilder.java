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
	private final boolean assignedToAvailable;
	
	
	private InspectionTypeBuilder(String name, String description, boolean printable, boolean retired, boolean master, long formVersion, InspectionTypeGroup group, boolean assignedToAvailable) {
		this.name = name;
		this.description = description;
		this.printable = printable;
		this.retired = retired;
		this.master = master;
		this.formVersion = formVersion;
		this.group = group;
		this.assignedToAvailable = assignedToAvailable;
	}
	
	public static InspectionTypeBuilder anInspectionType() {
		return new InspectionTypeBuilder("some Name", "some description", true, false, false, InspectionType.DEFAULT_FORM_VERSION, anInspectionTypeGroup().build(), false);
	}
	
	public InspectionTypeBuilder named(String name) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withDescription(String description) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withPrintable(boolean printable) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withRetired(boolean retired) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withMaster(boolean master) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withFormVersion(long formVersion) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withGroup(InspectionTypeGroup group) {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public InspectionTypeBuilder withAssignedToAvailable() {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, true);
	}
	

	public InspectionTypeBuilder withAssignedToNotAvailable() {
		return new InspectionTypeBuilder(name, description, printable, retired, master, formVersion, group, false);
	}
	
	@Override
	public InspectionType createObject() {
		InspectionType type = assignAbstractFields(new InspectionType());
		type.setName(name);
		type.setDescription(description);
		type.setPrintable(printable);
		type.setRetired(retired);
		type.setMaster(master);
		type.setFormVersion(formVersion);
		type.setGroup(group);
		if (assignedToAvailable) {
			type.makeAssignedToAvailable();
		} else {
			type.removeAssignedTo();
		}
		return type;
	}


	

	
	
}
