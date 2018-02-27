package com.n4systems.fieldid.ws.v1.resources.eventbook;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.EventBook;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("eventBook")
public class ApiEventBookResource extends SetupDataResource<ApiEventBook, EventBook> {
	private static Logger logger = Logger.getLogger(ApiEventBookResource.class);
	
	@Autowired
    protected PersistenceService persistenceService;

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
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEventBook(ApiEventBook apiEventBook) {
		if (apiEventBook.getName() == null || apiEventBook.getOwnerId() == null || apiEventBook.getSid() == null) {
			logger.error("Event book create from " + getCurrentUser().getTenant().getName() +
					":" + getCurrentUser().getUserID() + " has missing values - name:" + apiEventBook.getName() + ", ownerId:"
					+ apiEventBook.getOwnerId() + ", sid:" + apiEventBook.getSid());
			throw new RuntimeException("Event book create has null attributes");
		}
		EventBook eventBook = convertApiEventBook(apiEventBook);		
		persistenceService.save(eventBook);
		logger.info("saved event book: " + apiEventBook.getName());
	}
	
	private EventBook convertApiEventBook(ApiEventBook apiEventBook) {
		EventBook eventBook = new EventBook();
		BaseOrg owner = persistenceService.find(BaseOrg.class, apiEventBook.getOwnerId());
		
		eventBook.setMobileId(apiEventBook.getSid());
		eventBook.setName(apiEventBook.getName());
		eventBook.setOwner(owner);
		eventBook.setTenant(owner.getTenant());
		
		return eventBook;
	}
}
