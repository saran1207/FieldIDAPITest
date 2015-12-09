package com.n4systems.model.eventtype;

import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.model.Copier;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.event.EventFormSaver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class EventTypeCopier implements Copier<EventType> {
	private final Cleaner<EventType> typeCleaner;
	private final FilteredIdLoader<EventType> typeLoader;
	private final EventTypeSaver typeSaver;
	private final EventTypeUniqueNameLoader typeNameLoader;
    private final EventFormSaver formSaver;
	private final EventFormService eventFormService = new EventFormService();
    private boolean withProofTests = true;

	public EventTypeCopier(Cleaner<EventType> typeCleaner, FilteredIdLoader<EventType> typeLoader, EventTypeSaver typeSaver, EventFormSaver formSaver, EventTypeUniqueNameLoader typeNameLoader) {
		this.typeCleaner = typeCleaner;
		this.typeLoader = typeLoader;
		this.typeSaver = typeSaver;
		this.typeNameLoader = typeNameLoader;
        this.formSaver = formSaver;
	}
	
	protected EventTypeCopier(Tenant tenant, SecurityFilter filter) {
		this(new ThingEventTypeCleaner(tenant), new FilteredIdLoader<EventType>(filter, EventType.class), new EventTypeSaver(), new EventFormSaver(), new EventTypeUniqueNameLoader(filter));
	}
	
	public EventTypeCopier(Tenant tenant) {
		this(tenant, new TenantOnlySecurityFilter(tenant));
	}
	
	@Override
	public EventType copy(Long id) {
		// note we don't maintain a transaction here as we don't want our type to be in managed scope.
		// otherwise we would be editing the model as we copied
        if (withProofTests) {
            typeLoader.setPostFetchFields("eventForm.sections", "supportedProofTests", "infoFieldNames");
        } else {
            typeLoader.setPostFetchFields("eventForm.sections", "infoFieldNames");
        }
        EventType type = typeLoader.setId(id).load();
		
		typeCleaner.clean(type);

		String newName = typeNameLoader.setName(type.getName()).load();
		type.setName(newName);

		//Need to create a new copy of the event form.
		type.setEventForm(eventFormService.copyEventForm(type.getEventForm()));

        if (type.getEventForm() != null) {
            formSaver.save(type.getEventForm());
        }
		typeSaver.save(type);
		
		return type;
	}

    public void withProofTests(boolean withProofTests) {
        this.withProofTests = withProofTests;
    }
	
}
