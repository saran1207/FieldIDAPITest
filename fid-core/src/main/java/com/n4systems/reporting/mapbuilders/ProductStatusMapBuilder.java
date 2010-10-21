package com.n4systems.reporting.mapbuilders;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.persistence.Transaction;


public class ProductStatusMapBuilder extends AbstractMapBuilder<AssetStatus> {

	public ProductStatusMapBuilder() {
		super(ReportField.PRODUCT_STATUS);
	}
	
	@Override
	protected void setAllFields(AssetStatus entity, Transaction transaction) {
		setField(ReportField.PRODUCT_STATUS, entity.getName());
	}

}
