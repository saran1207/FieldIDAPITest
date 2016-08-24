package com.n4systems.webservice.dto;

import com.n4systems.tools.Pager;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrgListResponse extends AbstractListResponse {

	public CustomerOrgListResponse() {}

	public CustomerOrgListResponse(Pager<?> page, int pageSize) {
		super(page, pageSize);
	}

	private List<CustomerOrgServiceDTO> customers = new ArrayList<CustomerOrgServiceDTO>();

	public List<CustomerOrgServiceDTO> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerOrgServiceDTO> customers) {
		this.customers = customers;
	}
}
