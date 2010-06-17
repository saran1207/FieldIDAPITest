package com.n4systems.servicedto.builders;

import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.webservice.dto.InspectionServiceDTO;

public class InspectionServiceDTOBuilder extends BaseBuilder<InspectionServiceDTO>{
	private static final long UNASSIGNED_USER_ID = 0L;

	private final boolean assignmentIncluded;
	private final long assignedUserId;


	public InspectionServiceDTOBuilder(boolean assignmentIncluded, long assignedUserId) {
		this.assignmentIncluded = assignmentIncluded;
		this.assignedUserId = assignedUserId;
	}




	public static InspectionServiceDTOBuilder anInspectionServiceDTO() {
		return new InspectionServiceDTOBuilder(false, UNASSIGNED_USER_ID);
	}
	
	
	public InspectionServiceDTOBuilder withNoAssignmentIncluded() {
		return new InspectionServiceDTOBuilder(false, UNASSIGNED_USER_ID);
	}
	
	public InspectionServiceDTOBuilder withAssignmentTo(long id) {
		return new InspectionServiceDTOBuilder(true, id);
	}
	
	

	
	
	@Override
	public InspectionServiceDTO build() {
		InspectionServiceDTO dto = new InspectionServiceDTO();
		
		dto.setAssignmentIncluded(assignmentIncluded);
		dto.setAssignedUserId(assignedUserId);
		
		return dto;
	}



}
