package com.n4systems.model.builders;

import static com.n4systems.model.builders.EventTypeGroupBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Tenant;

public class EventTypeBuilder extends EntityWithTenantBuilder<EventType> {
	private final String name;
	private final String description;
	private final boolean printable;
	private final boolean retired;
	private final boolean master;
	private final long formVersion;
	private final EventTypeGroup group; 
	private final boolean assignedToAvailable;
	private final EventForm eventForm;
	private final List<String> infoFieldNames;
    private final Set<ProofTestType> supportedProofTestTypes;
	
    public static EventTypeBuilder anEventType() {
		return anEventType(anEventTypeGroup());
	}

    public static EventTypeBuilder anEventType(EventTypeGroupBuilder eventTypeGroupBuilder) {
		return new EventTypeBuilder(null, "some Name", "some description", true, false, false, EventType.DEFAULT_FORM_VERSION, eventTypeGroupBuilder.build(), false, null, new ArrayList<String>(), new HashSet<ProofTestType>());
	}

	private EventTypeBuilder(Tenant tenant, String name, String description, boolean printable, boolean retired, boolean master, long formVersion, EventTypeGroup group, boolean assignedToAvailable, EventForm eventForm, List<String> infoFieldNames, Set<ProofTestType> supportedProofTestTypes) {
        super(tenant);
		this.name = name;
		this.description = description;
		this.printable = printable;
		this.retired = retired;
		this.master = master;
		this.formVersion = formVersion;
		this.group = group;
		this.assignedToAvailable = assignedToAvailable;
		this.eventForm = eventForm;
		this.infoFieldNames = infoFieldNames;
        this.supportedProofTestTypes = supportedProofTestTypes;
	}
	
	public EventTypeBuilder named(String name) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withDescription(String description) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withPrintable(boolean printable) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withRetired(boolean retired) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withMaster(boolean master) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withFormVersion(long formVersion) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withGroup(EventTypeGroup group) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withAssignedToAvailable() {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, true, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	

	public EventTypeBuilder withAssignedToNotAvailable() {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, false, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withEventForm(EventForm eventForm) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public EventTypeBuilder withInfoFieldNames(String ... infoFieldNames) {
		return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, Arrays.asList(infoFieldNames), supportedProofTestTypes));
	}

    public EventTypeBuilder withProofTestTypes(ProofTestType... supportedProofTestTypes) {
        return makeBuilder(new EventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, new HashSet<ProofTestType>(Arrays.asList(supportedProofTestTypes))));
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
		type.setEventForm(eventForm);
		type.setInfoFieldNames(infoFieldNames);
        type.setSupportedProofTests(supportedProofTestTypes);
		if (assignedToAvailable) {
			type.makeAssignedToAvailable();
		} else {
			type.removeAssignedTo();
		}
		return type;
	}
	
}
