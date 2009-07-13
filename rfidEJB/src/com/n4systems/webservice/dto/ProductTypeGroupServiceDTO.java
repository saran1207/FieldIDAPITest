package com.n4systems.webservice.dto;

public class ProductTypeGroupServiceDTO extends AbstractBaseServiceDTO {

	private String name;
	
	private long orderIdx;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getOrderIdx() {
		return orderIdx;
	}

	public void setOrderIdx(long orderIdx) {
		this.orderIdx = orderIdx;
	}
}
