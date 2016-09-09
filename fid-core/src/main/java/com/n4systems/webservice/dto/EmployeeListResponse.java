package com.n4systems.webservice.dto;

import com.n4systems.tools.Pager;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListResponse extends AbstractListResponse {
	
	private List<EmployeeServiceDTO> employees = new ArrayList<EmployeeServiceDTO>();

	public EmployeeListResponse(Pager<?> page, int pageSize) {
		super(page, pageSize);
	}
	
	public List<EmployeeServiceDTO> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeServiceDTO> users) {
		this.employees = users;
	}

}
