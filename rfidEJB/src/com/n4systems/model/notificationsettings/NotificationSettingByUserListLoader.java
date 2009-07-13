package com.n4systems.model.notificationsettings;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend FilteredListLoader
public class NotificationSettingByUserListLoader extends ListLoader<NotificationSetting> {
	private Long userId;

	public NotificationSettingByUserListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public NotificationSettingByUserListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<NotificationSetting> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<NotificationSetting> builder = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter.prepareFor(NotificationSetting.class));
		builder.addSimpleWhere("user.uniqueID", userId);
		builder.addOrder("name");
		
		List<NotificationSetting> settingsList =  pm.findAll(builder);
	    return settingsList;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
