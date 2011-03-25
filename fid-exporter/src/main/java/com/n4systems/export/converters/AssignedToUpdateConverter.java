package com.n4systems.export.converters;

import com.n4systems.model.event.AssignedToUpdate;

public class AssignedToUpdateConverter extends ExportValueConverter<AssignedToUpdate> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(AssignedToUpdate.class);
	}

	@Override
	public String convertValue(AssignedToUpdate update) {
		return (update.getAssignedUser() != null) ? update.getAssignedUser().getFullName() : null;
	}

}
