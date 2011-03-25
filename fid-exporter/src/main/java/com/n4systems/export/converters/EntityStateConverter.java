package com.n4systems.export.converters;

import com.n4systems.model.api.Archivable.EntityState;

public class EntityStateConverter extends ExportValueConverter<EntityState> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(EntityState.class);
	}

	@Override
	public String convertValue(EntityState state) {
		return (state == EntityState.ACTIVE) ? "Active" : "Deleted";
	}

}
