package com.n4systems.webservice.dto;

import com.n4systems.tools.Pager;

import java.util.ArrayList;
import java.util.List;

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
