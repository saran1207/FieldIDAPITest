package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class NotificationSettingRemovalService extends FieldIdPersistenceService {

    @Transactional
	public void cleanUp(EventType eventType) {
		List<NotificationSetting> notificationsToDelete = getNotificationsWithEventType(eventType);

        for (NotificationSetting notificationSetting : notificationsToDelete) {
            persistenceService.remove(notificationSetting);
        }
	}

    @Transactional
	public NotificationSettingDeleteSummary summary(EventType eventType) {
		NotificationSettingDeleteSummary summary = new NotificationSettingDeleteSummary();

		summary.setNotificationsToDelete(getNotificationsWithEventType(eventType).size());

		return summary;
	}

	private List<NotificationSetting> getNotificationsWithEventType(EventType eventType) {
		QueryBuilder<NotificationSetting> query = createUserSecurityBuilder(NotificationSetting.class);
		query.addSimpleWhere("eventType", eventType);

		return persistenceService.findAll(query);
	}

}
