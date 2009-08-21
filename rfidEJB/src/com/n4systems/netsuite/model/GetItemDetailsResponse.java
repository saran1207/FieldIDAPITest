package com.n4systems.netsuite.model;

import java.util.List;

public class GetItemDetailsResponse extends AbstractResponse {

	private List<ProductInformation> itemlist;

	public List<ProductInformation> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<ProductInformation> itemlist) {
		this.itemlist = itemlist;
	}
	
}
