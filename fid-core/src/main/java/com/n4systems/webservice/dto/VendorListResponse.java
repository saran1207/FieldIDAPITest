package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.tools.Pager;

public class VendorListResponse extends AbstractListResponse {
	
	public VendorListResponse(Pager<?> page, int pageSize) {
		super(page, pageSize);
	}
	
	private List<VendorServiceDTO> vendors = new ArrayList<VendorServiceDTO>();

	public List<VendorServiceDTO> getVendors() {
		return vendors;
	}

	public void setVendors(List<VendorServiceDTO> vendors) {
		this.vendors = vendors;
	}
}
