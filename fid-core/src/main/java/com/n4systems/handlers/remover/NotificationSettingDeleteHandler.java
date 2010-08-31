package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.InspectionType;

public interface NotificationSettingDeleteHandler extends  RemovalHandler<NotificationSettingDeleteSummary> {
	public abstract NotificationSettingDeleteHandler forInspectionType(InspectionType inspectionType);
}