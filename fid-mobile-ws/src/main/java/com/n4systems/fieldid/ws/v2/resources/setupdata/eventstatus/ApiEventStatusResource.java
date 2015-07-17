package com.n4systems.fieldid.ws.v2.resources.setupdata.eventstatus;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.EventStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("eventStatus")
public class ApiEventStatusResource extends SetupDataResourceReadOnly<ApiEventStatus, EventStatus> {

	public ApiEventStatusResource() {
		super(EventStatus.class, true);
	}

	@Override
	protected ApiEventStatus convertEntityToApiModel(EventStatus status) {
		ApiEventStatus apiStatus = new ApiEventStatus();
		apiStatus.setSid(status.getId());
		apiStatus.setModified(status.getModified());
		apiStatus.setActive(status.isActive());
		apiStatus.setName(status.getName());
		return apiStatus;
	}
}
