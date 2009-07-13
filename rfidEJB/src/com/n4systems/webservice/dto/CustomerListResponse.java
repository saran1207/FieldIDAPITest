package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerListResponse extends AbstractListResponse {
	
	private List<CustomerServiceDTO> customers = new ArrayList<CustomerServiceDTO>();

	public List<CustomerServiceDTO> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerServiceDTO> customers) {
		this.customers = customers;
	}

}
