package com.n4systems.model.notificationsettings;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class NotificationSettingByTenantListLoader extends ListLoader<NotificationSetting> {

	public NotificationSettingByTenantListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<NotificationSetting> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<NotificationSetting> builder = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter);
		List<NotificationSetting> settingsList =  builder.getResultList(em);
	    return settingsList;
	}
}
