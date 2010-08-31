package com.n4systems.reporting.mapbuilders;

import static com.n4systems.reporting.PathHandler.*;
import static com.n4systems.reporting.mapbuilders.ReportField.*;

import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.StreamHelper;

public class PerformedByMapBuilder extends AbstractMapBuilder<User> {

	public PerformedByMapBuilder() {
		super(DEPERCATED_INSPECTOR_NAME, PERFORM_BY_NAME, PERFORMED_BY_POSITION, PERFORMED_BY_INITIALS);
	}

	@Override
	protected void setAllFields(User entity, Transaction transaction) {
		setField(DEPERCATED_INSPECTOR_NAME, entity.getUserLabel());
		setField(PERFORM_BY_NAME, entity.getUserLabel());
		setField(PERFORMED_BY_POSITION, entity.getPosition());
		setField(PERFORMED_BY_INITIALS, entity.getInitials());
		setField(USER_SIGNATURE_IMAGE, StreamHelper.openQuietly(getSignatureImage(entity)));
	}

}
