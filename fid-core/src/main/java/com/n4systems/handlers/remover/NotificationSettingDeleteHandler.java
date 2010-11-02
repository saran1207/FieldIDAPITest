package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.EventType;

public interface NotificationSettingDeleteHandler extends  RemovalHandler<NotificationSettingDeleteSummary> {
	public abstract NotificationSettingDeleteHandler forEventType(EventType eventType);
}