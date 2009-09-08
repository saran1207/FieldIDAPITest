package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrgListResponse extends AbstractListResponse {

	private List<CustomerOrgServiceDTO> customers = new ArrayList<CustomerOrgServiceDTO>();

	public List<CustomerOrgServiceDTO> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerOrgServiceDTO> customers) {
		this.customers = customers;
	}
}
