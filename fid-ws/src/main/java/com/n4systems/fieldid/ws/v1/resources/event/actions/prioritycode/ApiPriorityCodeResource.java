package com.n4systems.fieldid.ws.v1.resources.event.actions.prioritycode;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.PriorityCode;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

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
        if (priorityCode.getAutoSchedule() != null) {
            apiPriorityCode.setAutoScheduleType(priorityCode.getAutoSchedule().name());
        }
        apiPriorityCode.setAutoScheduleCustomDays(priorityCode.getAutoScheduleCustomDays());
		return apiPriorityCode;
	}

}
