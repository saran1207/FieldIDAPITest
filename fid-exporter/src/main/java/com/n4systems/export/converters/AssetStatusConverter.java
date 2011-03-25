package com.n4systems.export.converters;

import com.n4systems.model.AssetStatus;

public class AssetStatusConverter extends ExportValueConverter<AssetStatus> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(AssetStatus.class);
	}

	@Override
	public String convertValue(AssetStatus status) {
		return status.getName();
	}

}
