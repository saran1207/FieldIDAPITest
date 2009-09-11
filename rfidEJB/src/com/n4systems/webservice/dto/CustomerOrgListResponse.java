package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.tools.Pager;

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
