package com.n4systems.model.builders;

import static com.n4systems.model.builders.EventTypeGroupBuilder.*;

import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;

public class EventTypeBuilder extends EntityWithTenantBuilder<EventType> {
	private final String name;
	private final String description;
	private final boolean printable;
	private final boolean retired;
	private final boolean master;
	private final long formVersion;
	private final EventTypeGroup group;
	private final boolean assignedToAvailable;
	
	
	private EventTypeBuilder(String name, String description, boolean printable, boolean retired, boolean master, long formVersion, EventTypeGroup group, boolean assignedToAvailable) {
		this.name = name;
		this.description = description;
		this.printable = printable;
		this.retired = retired;
		this.master = master;
		this.formVersion = formVersion;
		this.group = group;
		this.assignedToAvailable = assignedToAvailable;
	}
	
	public static EventTypeBuilder anEventType() {
		return new EventTypeBuilder("some Name", "some description", true, false, false, EventType.DEFAULT_FORM_VERSION, anEventTypeGroup().build(), false);
	}
	
	public EventTypeBuilder named(String name) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withDescription(String description) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withPrintable(boolean printable) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withRetired(boolean retired) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withMaster(boolean master) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withFormVersion(long formVersion) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withGroup(EventTypeGroup group) {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, assignedToAvailable);
	}
	
	public EventTypeBuilder withAssignedToAvailable() {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, true);
	}
	

	public EventTypeBuilder withAssignedToNotAvailable() {
		return new EventTypeBuilder(name, description, printable, retired, master, formVersion, group, false);
	}
	
	@Override
	public EventType createObject() {
		EventType type = assignAbstractFields(new EventType());
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
