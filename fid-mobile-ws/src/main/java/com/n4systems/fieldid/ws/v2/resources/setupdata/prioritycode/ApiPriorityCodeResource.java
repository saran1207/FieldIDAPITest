package com.n4systems.fieldid.ws.v2.resources.setupdata.prioritycode;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.PriorityCode;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("priorityCode")
public class ApiPriorityCodeResource extends SetupDataResourceReadOnly<ApiPriorityCode, PriorityCode> {

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
