package com.n4systems.fieldid.api.mobile.resources.eventstatus;

import com.n4systems.fieldid.api.mobile.resources.SetupDataResource;
import com.n4systems.model.EventStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

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
