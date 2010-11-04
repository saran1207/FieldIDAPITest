package com.n4systems.model.eventtype;

import com.n4systems.model.Copier;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class EventTypeCopier implements Copier<EventType> {
	private final Cleaner<EventType> typeCleaner;
	private final FilteredIdLoader<EventType> typeLoader;
	private final EventTypeSaver typeSaver;
	private final EventTypeUniqueNameLoader typeNameLoader;
	
	public EventTypeCopier(Cleaner<EventType> typeCleaner, FilteredIdLoader<EventType> typeLoader, EventTypeSaver typeSaver, EventTypeUniqueNameLoader typeNameLoader) {
		this.typeCleaner = typeCleaner;
		this.typeLoader = typeLoader;
		this.typeSaver = typeSaver;
		this.typeNameLoader = typeNameLoader;
	}
	
	protected EventTypeCopier(Tenant tenant, SecurityFilter filter) {
		this(new EventTypeCleaner(tenant), new FilteredIdLoader<EventType>(filter, EventType.class), new EventTypeSaver(), new EventTypeUniqueNameLoader(filter));
	}
	
	public EventTypeCopier(Tenant tenant) {
		this(tenant, new TenantOnlySecurityFilter(tenant));
	}
	
	@Override
	public EventType copy(Long id) {
		// note we don't maintain a transaction here as we don't want our type to be in managed scope.
		// otherwise we would be editing the model as we copied
		EventType type = typeLoader.setId(id).setPostFetchFields("sections", "supportedProofTests", "infoFieldNames").load();
		
		typeCleaner.clean(type);

		String newName = typeNameLoader.setName(type.getName()).load();
		type.setName(newName);
		
		typeSaver.save(type);
		
		return type;
	}
	
}
