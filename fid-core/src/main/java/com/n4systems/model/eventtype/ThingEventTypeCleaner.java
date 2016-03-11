package com.n4systems.model.eventtype;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.api.EntityWithTenantCleaner;
import com.n4systems.util.ListHelper;

import java.util.ArrayList;
import java.util.HashSet;

public class ThingEventTypeCleaner extends EntityWithTenantCleaner<EventType> {
	
    private final Cleaner<EventForm> eventFormCleaner;
	
	public ThingEventTypeCleaner(Tenant newTenant, Cleaner<EventForm> eventFormCleaner) {
		super(newTenant);
        this.eventFormCleaner = eventFormCleaner;
	}
	
	public ThingEventTypeCleaner(Tenant newTenant) {
		this(newTenant, new EventFormCleaner(newTenant));
	}
	
	@Override
	public void clean(EventType type) {
		super.clean(type);

		type.setFormVersion(EventType.DEFAULT_FORM_VERSION);
		
        if (type.getEventForm() != null) {
            eventFormCleaner.clean(type.getEventForm());
        }

		cleanSupportedProofTests(type);
		cleanInfoFieldNames(type);
	}

	private void cleanSupportedProofTests(EventType type) {
        if (type instanceof ThingEventType) {
            // Note: this is actually required so that hibernate does not move the old list to the new entity
            // when it's being copied. -mf
            ((ThingEventType)type).setSupportedProofTests(ListHelper.copy(((ThingEventType)type).getSupportedProofTests(), new HashSet<ProofTestType>()));
        }
	}
	
	private void cleanInfoFieldNames(EventType type) {
		// Note: this is actually required so that hibernate does not move the old list to the new entity
		// when it's being copied. -mf
		type.setInfoFieldNames(ListHelper.copy(type.getInfoFieldNames(), new ArrayList<String>()));
	}
}
