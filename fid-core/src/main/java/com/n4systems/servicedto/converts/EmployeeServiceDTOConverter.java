package com.n4systems.servicedto.converts;

import com.n4systems.model.user.User;
import com.n4systems.webservice.dto.EmployeeServiceDTO;

public class EmployeeServiceDTOConverter {

	private EmployeeServiceDTO dto;

	public EmployeeServiceDTO convert(User user) {
		dto = new EmployeeServiceDTO();
		
		dto.setId(user.getId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());

		dto.setDeleted(user.isArchived());
		
		return dto;
	}
	
	
}
