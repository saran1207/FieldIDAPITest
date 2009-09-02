package com.n4systems.subscription.netsuite.model;

import java.util.List;

public class GetItemDetailsResponse extends AbstractResponse {

	private List<NetSuiteProductInformation> itemlist;

	public List<NetSuiteProductInformation> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<NetSuiteProductInformation> itemlist) {
		this.itemlist = itemlist;
	}
	
}
