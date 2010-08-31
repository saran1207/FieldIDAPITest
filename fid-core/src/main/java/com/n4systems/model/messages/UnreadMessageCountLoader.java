package com.n4systems.model.messages;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class UnreadMessageCountLoader extends SecurityFilteredLoader<Long> {

	public UnreadMessageCountLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Long load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Message> builder = new QueryBuilder<Message>(Message.class, filter);
		builder.addWhere(WhereClauseFactory.create("unread", true));
		
		Long unreadCount = builder.getCount(em);
		return unreadCount;
	}

}
