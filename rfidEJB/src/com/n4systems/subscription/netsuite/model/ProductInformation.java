package com.n4systems.subscription.netsuite.model;

import java.util.List;

public class ProductInformation {
	
	private String fieldid;
	private String name;
	
	private List<ContractLength> contractlengths;

	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}

	public List<ContractLength> getContractlengths() {
		return contractlengths;
	}

	public void setContractlengths(List<ContractLength> contractlengths) {
		this.contractlengths = contractlengths;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
