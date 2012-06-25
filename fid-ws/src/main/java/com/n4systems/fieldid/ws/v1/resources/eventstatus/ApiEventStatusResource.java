package com.n4systems.fieldid.ws.v1.resources.eventstatus;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.EventStatus;

@Component
@Path("eventStatus")
public class ApiEventStatusResource extends SetupDataResource<ApiEventStatus, EventStatus>{

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
