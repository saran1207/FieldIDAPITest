package com.n4systems.model.notificationsettings;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class NotificationSettingOwnerListLoader extends Loader<List<NotificationSettingOwner>> {
	private Long notificationSettingId;

	public NotificationSettingOwnerListLoader() {}

	@Override
	protected List<NotificationSettingOwner> load(EntityManager em) {
		QueryBuilder<NotificationSettingOwner> builder = new QueryBuilder<NotificationSettingOwner>(NotificationSettingOwner.class);
		builder.addSimpleWhere("notificationSetting.id", notificationSettingId);
		
		List<NotificationSettingOwner> custDivList = builder.getResultList(em);
		return custDivList;
	}
	
	public void setNotificationSettingId(Long notificationSettingId) {
		this.notificationSettingId = notificationSettingId;
	}
	
}
