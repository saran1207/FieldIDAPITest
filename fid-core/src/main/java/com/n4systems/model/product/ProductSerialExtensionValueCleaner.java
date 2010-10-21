package com.n4systems.model.product;

import rfid.ejb.entity.AssetSerialExtensionValue;

import com.n4systems.model.LegacyBaseEntityCleaner;

public class ProductSerialExtensionValueCleaner extends LegacyBaseEntityCleaner<AssetSerialExtensionValue> {

	public ProductSerialExtensionValueCleaner() {}
	
	@Override
	public void clean(AssetSerialExtensionValue obj) {
		super.clean(obj);
	}
	
}
