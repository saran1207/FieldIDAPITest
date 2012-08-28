package com.n4systems.fieldid.ws.v1.resources.event.actions.prioritycode;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.PriorityCode;

@Component
@Path("priorityCode")
public class ApiPriorityCodeResource extends SetupDataResource<ApiPriorityCode, PriorityCode> {

	public ApiPriorityCodeResource() {
		super(PriorityCode.class, true);
	}

	@Override
	protected ApiPriorityCode convertEntityToApiModel(PriorityCode priorityCode) {
		ApiPriorityCode apiPriorityCode = new ApiPriorityCode();
		apiPriorityCode.setSid(priorityCode.getId());
		apiPriorityCode.setModified(priorityCode.getModified());
		apiPriorityCode.setActive(priorityCode.isActive());
		apiPriorityCode.setName(priorityCode.getName());
		return apiPriorityCode;
	}

}
