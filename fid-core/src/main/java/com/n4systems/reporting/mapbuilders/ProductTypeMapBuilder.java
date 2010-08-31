package com.n4systems.reporting.mapbuilders;

import com.n4systems.model.ProductType;
import com.n4systems.persistence.Transaction;

public class ProductTypeMapBuilder extends AbstractMapBuilder<ProductType> {

	public ProductTypeMapBuilder() {
		super(
			ReportField.PRODUCT_TYPE_NAME,
			ReportField.PRODUCT_TYPE_NAME_LEGACY,
			ReportField.PRODUCT_TYPE_WARNING,
			ReportField.PRODUCT_TYPE_CERT_TEXT
		);
	}
	
	@Override
	protected void setAllFields(ProductType entity, Transaction transaction) {
		setField(ReportField.PRODUCT_TYPE_NAME,			entity.getName());
		setField(ReportField.PRODUCT_TYPE_NAME_LEGACY,	entity.getName());
		setField(ReportField.PRODUCT_TYPE_WARNING,		entity.getWarnings());
		setField(ReportField.PRODUCT_TYPE_CERT_TEXT,	entity.getManufactureCertificateText());
	}

}
