package com.n4systems.subscription.netsuite.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductInformation {
	
	private String fieldid;
	private String name;
	private Long nsrecordid;
	
	private List<ContractLength> contractlengths;
	private Map<Integer, ContractLength> contractLengthMap;

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
		this.contractLengthMap = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getNsrecordid() {
		return nsrecordid;
	}

	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
	}

	public Map<Integer, ContractLength> retrieveContractLengthMap() {
		
		if (contractLengthMap == null) {
			contractLengthMap = new HashMap<Integer, ContractLength>();
			
			if (contractlengths != null) {
				for (ContractLength contractLength : contractlengths) {
					contractLengthMap.put(contractLength.getMonths(), contractLength);
				}
			}
		}
		
		return contractLengthMap;
	}
}
