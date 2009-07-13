package com.n4systems.model.notificationsettings;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class NotificationSettingByTenantListLoader extends ListLoader<NotificationSetting> {
	private Long tenantId;

	public NotificationSettingByTenantListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public NotificationSettingByTenantListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<NotificationSetting> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<NotificationSetting> builder = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter.prepareFor(NotificationSetting.class));
		builder.addSimpleWhere("tenant.id", tenantId);
		
		List<NotificationSetting> settingsList =  pm.findAll(builder);
	    return settingsList;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
