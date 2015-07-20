package com.n4systems.fieldid.ws.v2.resources.setupdata.eventbook;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadWrite;
import com.n4systems.model.EventBook;
import com.n4systems.model.orgs.BaseOrg;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Component
@Path("eventBook")
public class ApiEventBookResource extends SetupDataResourceReadWrite<ApiEventBook, EventBook> {

	public ApiEventBookResource() {
		super("mobileId", EventBook.class, false);
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

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEventBook(ApiEventBook apiEventBook) {
		EventBook eventBook = convertApiEventBook(apiEventBook);
		persistenceService.save(eventBook);
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
