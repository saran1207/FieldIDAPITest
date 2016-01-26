package com.n4systems.fieldid.service.notificationsetting;

import com.n4systems.fieldid.service.CrudService;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class NotificationSettingService extends CrudService<NotificationSetting> {

    public NotificationSettingService() {
        super(NotificationSetting.class);
    }

    public List<NotificationSetting> findAllUserNotifications(User user) {
        QueryBuilder<NotificationSetting> query = createUserSecurityBuilder(NotificationSetting.class);
        query.addSimpleWhere("user", user);
        return persistenceService.findAll(query);
    }

    public void remove(NotificationSetting notificationSetting) {
        persistenceService.remove(notificationSetting);
    }
}
