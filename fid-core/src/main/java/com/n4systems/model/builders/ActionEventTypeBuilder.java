package com.n4systems.model.builders;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.*;

import java.util.*;

import static com.n4systems.model.builders.EventTypeGroupBuilder.anEventTypeGroup;

public class ActionEventTypeBuilder extends EntityWithTenantBuilder<ActionEventType> {
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

    public static ActionEventTypeBuilder anEventType() {
		return anEventType(anEventTypeGroup());
	}

    public static ActionEventTypeBuilder anEventType(EventTypeGroupBuilder eventTypeGroupBuilder) {
		return new ActionEventTypeBuilder(null, "some Name", "some description", true, false, false, EventType.DEFAULT_FORM_VERSION, eventTypeGroupBuilder.build(), false, null, new ArrayList<String>(), new HashSet<ProofTestType>());
	}

	private ActionEventTypeBuilder(Tenant tenant, String name, String description, boolean printable, boolean retired, boolean master, long formVersion, EventTypeGroup group, boolean assignedToAvailable, EventForm eventForm, List<String> infoFieldNames, Set<ProofTestType> supportedProofTestTypes) {
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
	
	public ActionEventTypeBuilder named(String name) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withDescription(String description) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withPrintable(boolean printable) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withRetired(boolean retired) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withMaster(boolean master) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withFormVersion(long formVersion) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withGroup(EventTypeGroup group) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withAssignedToAvailable() {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, true, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	

	public ActionEventTypeBuilder withAssignedToNotAvailable() {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, false, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withEventForm(EventForm eventForm) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, supportedProofTestTypes));
	}
	
	public ActionEventTypeBuilder withInfoFieldNames(String ... infoFieldNames) {
		return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, Arrays.asList(infoFieldNames), supportedProofTestTypes));
	}

    public ActionEventTypeBuilder withProofTestTypes(ProofTestType... supportedProofTestTypes) {
        return makeBuilder(new ActionEventTypeBuilder(tenant, name, description, printable, retired, master, formVersion, group, assignedToAvailable, eventForm, infoFieldNames, new HashSet<ProofTestType>(Arrays.asList(supportedProofTestTypes))));
    }

	@Override
	public ActionEventType createObject() {
        ActionEventType type = assignAbstractFields(new ActionEventType());
		type.setName(name);
		type.setDescription(description);
		type.setPrintable(printable);
		type.setRetired(retired);
		type.setFormVersion(formVersion);
		type.setGroup(group);
		type.setEventForm(eventForm);
		type.setInfoFieldNames(infoFieldNames);
		if (assignedToAvailable) {
			type.makeAssignedToAvailable();
		} else {
			type.removeAssignedTo();
		}
		return type;
	}
	
}
