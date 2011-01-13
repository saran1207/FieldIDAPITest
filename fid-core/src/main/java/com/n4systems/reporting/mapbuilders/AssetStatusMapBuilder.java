package com.n4systems.reporting.mapbuilders;


import com.n4systems.model.AssetStatus;
import com.n4systems.persistence.Transaction;


public class AssetStatusMapBuilder extends AbstractMapBuilder<AssetStatus> {

	public AssetStatusMapBuilder() {
		super(ReportField.PRODUCT_STATUS);
	}
	
	@Override
	protected void setAllFields(AssetStatus entity, Transaction transaction) {
		setField(ReportField.PRODUCT_STATUS, entity.getName());
	}

}
