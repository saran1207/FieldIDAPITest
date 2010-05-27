package com.n4systems.reporting.mapbuilders;


import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.StreamHelper;

public class InspectorMapBuilder extends AbstractMapBuilder<User> {

	public InspectorMapBuilder() {
		super(
			ReportField.INSPECTOR_IDENTIFIED_BY,
			ReportField.INSPECTOR_NAME,
			ReportField.INSPECTOR_POSITION,
			ReportField.INSPECTOR_INITIALS
		);
	}
	
	@Override
	protected void setAllFields(User entity, Transaction transaction) {
		setField(ReportField.INSPECTOR_IDENTIFIED_BY,	entity.getUserLabel());
		setField(ReportField.INSPECTOR_NAME,			entity.getUserLabel());
		setField(ReportField.INSPECTOR_POSITION,		entity.getPosition());
		setField(ReportField.INSPECTOR_INITIALS, 		entity.getInitials());
		setField(ReportField.USER_SIGNATURE_IMAGE,		StreamHelper.openQuietly(PathHandler.getSignatureImage(entity)));
	}

}
