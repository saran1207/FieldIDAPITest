package com.n4systems.model.eventtype;

import com.n4systems.model.Copier;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.event.EventFormSaver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class EventTypeCopier implements Copier<EventType> {
	private final Cleaner<ThingEventType> typeCleaner;
	private final FilteredIdLoader<ThingEventType> typeLoader;
	private final EventTypeSaver typeSaver;
	private final EventTypeUniqueNameLoader typeNameLoader;
    private final EventFormSaver formSaver;
	
	public EventTypeCopier(Cleaner<ThingEventType> typeCleaner, FilteredIdLoader<ThingEventType> typeLoader, EventTypeSaver typeSaver, EventFormSaver formSaver, EventTypeUniqueNameLoader typeNameLoader) {
		this.typeCleaner = typeCleaner;
		this.typeLoader = typeLoader;
		this.typeSaver = typeSaver;
		this.typeNameLoader = typeNameLoader;
        this.formSaver = formSaver;
	}
	
	protected EventTypeCopier(Tenant tenant, SecurityFilter filter) {
		this(new EventTypeCleaner(tenant), new FilteredIdLoader<ThingEventType>(filter, ThingEventType.class), new EventTypeSaver(), new EventFormSaver(), new EventTypeUniqueNameLoader(filter));
	}
	
	public EventTypeCopier(Tenant tenant) {
		this(tenant, new TenantOnlySecurityFilter(tenant));
	}
	
	@Override
	public EventType copy(Long id) {
		// note we don't maintain a transaction here as we don't want our type to be in managed scope.
		// otherwise we would be editing the model as we copied
        ThingEventType type = typeLoader.setId(id).setPostFetchFields("eventForm.sections", "supportedProofTests", "infoFieldNames").load();
		
		typeCleaner.clean(type);

		String newName = typeNameLoader.setName(type.getName()).load();
		type.setName(newName);

        if (type.getEventForm() != null) {
            formSaver.save(type.getEventForm());
        }
		typeSaver.save(type);
		
		return type;
	}
	
}
