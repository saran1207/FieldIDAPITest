package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.persistence.utils.PostFetcher;

abstract public class SafetyNetworkEventLoader extends SecurityFilteredLoader<ThingEvent> implements IdLoader<SafetyNetworkEventLoader> {
	private final NonSecureIdLoader<ThingEvent> eventLoader;
	
	public SafetyNetworkEventLoader(SecurityFilter filter, NonSecureIdLoader<ThingEvent> eventLoader) {
		super(filter);
		this.eventLoader = eventLoader;
	}
	
	public SafetyNetworkEventLoader(SecurityFilter filter) {
		this(filter, new NonSecureIdLoader<ThingEvent>(ThingEvent.class));
	}

	abstract protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Event event);
	
	@Override
	public ThingEvent load(EntityManager em, SecurityFilter filter) {
		// we want this session to be read-only since enhancement may change fields on the entities
		PersistenceManager.setSessionReadOnly(em);
		
		//we pretty much always need to postfetch all fields
		eventLoader.setPostFetchPaths(Event.ALL_FIELD_PATHS);
		
		// to load this event we will first do an unsecured load by id
        ThingEvent event = eventLoader.load(em);
		
		if (event == null) {
			return null;
		}
		
		// XXX the following is a hack to post-load the fields for each sub event.
		// we need a better way of doing this
		PostFetcher.postFetchFields(event.getSubEvents(), SubEvent.ALL_FIELD_PATHS);
		
		// if the event is actually one ours, we can stop here
		if (filter.getOwner().canAccess(event.getOwner())) {
			return event;
		}
		
		if (!accessAllowed(em, filter, event)) {
			throw new SecurityException("Network event failed security check");
		}
		
		// now we need to make sure the event is security enhanced
        ThingEvent enhancedEvent = EntitySecurityEnhancer.enhance(event, filter);
		return enhancedEvent;
	}

	public SafetyNetworkEventLoader setId(Long id) {
		eventLoader.setId(id);
		return this;
	}
}
