package com.n4systems.export.converters;

import com.n4systems.model.AssetType;

public class AssetTypeConverter extends ExportValueConverter<AssetType> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(AssetType.class);
	}

	@Override
	public String convertValue(AssetType type) {
		return type.getName();
	}

}
