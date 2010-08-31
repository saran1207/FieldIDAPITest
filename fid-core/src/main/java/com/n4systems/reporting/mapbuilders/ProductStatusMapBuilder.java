package com.n4systems.reporting.mapbuilders;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.persistence.Transaction;


public class ProductStatusMapBuilder extends AbstractMapBuilder<ProductStatusBean> {

	public ProductStatusMapBuilder() {
		super(ReportField.PRODUCT_STATUS);
	}
	
	@Override
	protected void setAllFields(ProductStatusBean entity, Transaction transaction) {
		setField(ReportField.PRODUCT_STATUS, entity.getName());
	}

}
