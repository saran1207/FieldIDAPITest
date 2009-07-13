package com.n4systems.model.notificationsettings;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

//TODO: Update this class to extend FilteredListLoader
public class NotificationSettingOwnerListLoader extends ListLoader<NotificationSettingOwner> {
	private Long notificationSettingId;

	public NotificationSettingOwnerListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public NotificationSettingOwnerListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<NotificationSettingOwner> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<NotificationSettingOwner> builder = new QueryBuilder<NotificationSettingOwner>(NotificationSettingOwner.class);
		builder.addSimpleWhere("notificationSetting.id", notificationSettingId);
		
		List<NotificationSettingOwner> custDivList = pm.findAll(builder);
		return custDivList;
	}
	
	public void setNotificationSettingId(Long notificationSettingId) {
		this.notificationSettingId = notificationSettingId;
	}
	
}
