package com.n4systems.model.eventbook;

import javax.persistence.EntityManager;

import com.n4systems.model.EventBook;
import com.n4systems.model.security.SecurityFilter;

public class EventBookFindOrCreateLoader extends EventBookByNameLoader {
	private final EventBookSaver saver;
	
	public EventBookFindOrCreateLoader(SecurityFilter filter) {
		this(filter, new EventBookSaver());
	}
	
	public EventBookFindOrCreateLoader(SecurityFilter filter, EventBookSaver saver) {
		super(filter);
		this.saver = saver;
	}

	@Override
	public EventBook load(EntityManager em, SecurityFilter filter) {
		EventBook book = createIfNull(em, super.load(em, filter));
		return book;
	}

	private EventBook createIfNull(EntityManager em, EventBook book) {
		if (book == null) {
			book = new EventBook();
			book.setName(getName());
			book.setOpen(true);
			book.setOwner(getOwner());
			book.setTenant(getOwner().getTenant());
			
			saver.save(em, book);
		}
		return book;
	}

}
