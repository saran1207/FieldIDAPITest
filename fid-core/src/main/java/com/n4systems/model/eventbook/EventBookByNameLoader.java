package com.n4systems.model.eventbook;

import javax.persistence.EntityManager;

import com.n4systems.model.EventBook;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventBookByNameLoader extends SecurityFilteredLoader<EventBook> {
	public BaseOrg owner;
	public String name;
	
	public EventBookByNameLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	public EventBook load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventBook> builder = new QueryBuilder<EventBook>(EventBook.class, filter);
		builder.addSimpleWhere("owner", owner);
		builder.addSimpleWhere("name", name);
		
		return builder.getSingleResult(em);
	}

	public EventBookByNameLoader setOwner(BaseOrg owner) {
		this.owner = owner;
		return this;
	}
	
	public BaseOrg getOwner() {
		return owner;
	}
	
	public EventBookByNameLoader setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}
}
