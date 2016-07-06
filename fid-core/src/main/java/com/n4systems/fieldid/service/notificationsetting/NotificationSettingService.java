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
        return persistenceService.findAll(getNotificationSettingByUserQueryBuilder(user));
    }

    public Long countAllUserNotifications(User user) {
        return persistenceService.count(getNotificationSettingByUserQueryBuilder(user));
    }

    private QueryBuilder<NotificationSetting> getNotificationSettingByUserQueryBuilder(User user) {
        QueryBuilder<NotificationSetting> query = createUserSecurityBuilder(NotificationSetting.class);
        query.addSimpleWhere("user", user);
        return query;
    }

    public void remove(NotificationSetting notificationSetting) {
        persistenceService.remove(notificationSetting);
    }

    public void removeAllUserNotifications(User user) {
        for(NotificationSetting setting: findAllUserNotifications(user)) {
            remove(setting);
        }
    }
}
