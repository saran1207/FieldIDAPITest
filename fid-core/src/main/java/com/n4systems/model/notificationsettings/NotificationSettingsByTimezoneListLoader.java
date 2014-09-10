package com.n4systems.model.notificationsettings;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class NotificationSettingsByTimezoneListLoader extends ListLoader<NotificationSetting> {

    private String timeZone;

    public NotificationSettingsByTimezoneListLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<NotificationSetting> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<NotificationSetting> query = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter);

        query.addSimpleWhere("user.timeZoneID", timeZone);
        query.addSimpleWhere("user.state", Archivable.EntityState.ACTIVE);
        query.addSimpleWhere("tenant.disabled", false);

        return query.getResultList(em);
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
