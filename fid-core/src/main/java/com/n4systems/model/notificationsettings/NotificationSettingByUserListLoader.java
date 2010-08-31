package com.n4systems.model.notificationsettings;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class NotificationSettingByUserListLoader extends ListLoader<NotificationSetting> {
	private Long userId;

	public NotificationSettingByUserListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<NotificationSetting> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<NotificationSetting> builder = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter);
		builder.addSimpleWhere("user.id", userId);
		builder.addOrder("name");
		
		List<NotificationSetting> settingsList =  builder.getResultList(em);
	    return settingsList;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
