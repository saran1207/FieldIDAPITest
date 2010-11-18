package com.n4systems.model.notificationsettings;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class NotificationSettingsByTimezoneListLoader extends ListLoader<NotificationSetting> {

    private String timeZone;

    public NotificationSettingsByTimezoneListLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<NotificationSetting> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<NotificationSetting> query = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter);

        query.addSimpleWhere("user.timeZoneID", timeZone);

        return query.getResultList(em);
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
