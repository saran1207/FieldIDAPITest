package com.n4systems.fieldid.ws.v1.resources.eventbook;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.EventBook;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("eventBook")
public class ApiEventBookResource extends SetupDataResource<ApiEventBook, EventBook> {

	public ApiEventBookResource() {
		super(EventBook.class, false);
	}

	@Override
	protected ApiEventBook convertEntityToApiModel(EventBook book) {
		ApiEventBook apiBook = new ApiEventBook();
		apiBook.setSid(book.getMobileId());
		apiBook.setModified(book.getModified());
		apiBook.setActive(book.isOpen());
		apiBook.setOwnerId(book.getOwner().getId());
		apiBook.setName(book.getName());
		return apiBook;
	}

	@Override
	protected QueryBuilder<EventBook> createFindSingleBuilder(String id) {
		QueryBuilder<EventBook> builder = createTenantSecurityBuilder(EventBook.class, true);
		builder.addWhere(WhereClauseFactory.create("mobileId", id));
		return builder;
	}
	
}
