package com.n4systems.model.notificationsettings;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class NotificationSettingByTenantListLoader extends ListLoader<NotificationSetting> {
	private Long tenantId;

	public NotificationSettingByTenantListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<NotificationSetting> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<NotificationSetting> builder = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter.prepareFor(NotificationSetting.class));
		builder.addSimpleWhere("tenant.id", tenantId);
		
		List<NotificationSetting> settingsList =  builder.getResultList(em);
	    return settingsList;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
