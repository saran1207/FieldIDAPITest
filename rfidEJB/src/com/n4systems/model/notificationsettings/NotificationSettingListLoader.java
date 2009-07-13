package com.n4systems.model.notificationsettings;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend FilteredListLoader
public class NotificationSettingListLoader extends ListLoader<NotificationSetting> {

	public NotificationSettingListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public NotificationSettingListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<NotificationSetting> load(PersistenceManager pm, SecurityFilter filter) {
		return pm.findAll(new QueryBuilder<NotificationSetting>(NotificationSetting.class));
	}

}
