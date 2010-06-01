package com.n4systems.reporting.mapbuilders;


import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.StreamHelper;

public class PerformedByMapBuilder extends AbstractMapBuilder<User> {

	public PerformedByMapBuilder() {
		super(
			ReportField.DEPERCATED_INSPECTOR_NAME,
			ReportField.PERFORM_BY_NAME,
			ReportField.PERFORMED_BY_POSITION,
			ReportField.PERFORMED_BY_INITIALS
		);
	}
	
	@Override
	protected void setAllFields(User entity, Transaction transaction) {
		setField(ReportField.DEPERCATED_INSPECTOR_NAME,			entity.getUserLabel());
		setField(ReportField.PERFORM_BY_NAME,			entity.getUserLabel());
		setField(ReportField.PERFORMED_BY_POSITION,		entity.getPosition());
		setField(ReportField.PERFORMED_BY_INITIALS, 		entity.getInitials());
		setField(ReportField.USER_SIGNATURE_IMAGE,		StreamHelper.openQuietly(PathHandler.getSignatureImage(entity)));
	}

}
