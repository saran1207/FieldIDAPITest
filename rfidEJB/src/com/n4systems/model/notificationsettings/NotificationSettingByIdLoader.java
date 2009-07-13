package com.n4systems.model.notificationsettings;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.EntityLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * @deprecated: Use {@link FilteredIdLoader} instead
 */
@Deprecated
public class NotificationSettingByIdLoader extends EntityLoader<NotificationSetting> {
	private final SecurityFilter filter;
	private Long id;
	
	public NotificationSettingByIdLoader(SecurityFilter filter) {
		this(null, filter);
	}
	
	public NotificationSettingByIdLoader(PersistenceManager pm, SecurityFilter filter) {
	    super(pm);
	    this.filter = filter;
    }

	@Override
    protected NotificationSetting load(PersistenceManager pm) {
		QueryBuilder<NotificationSetting> builder = new QueryBuilder<NotificationSetting>(NotificationSetting.class, filter.prepareFor(NotificationSetting.class));
		
		builder.addSimpleWhere("id", id);
		
		NotificationSetting setting =  pm.find(builder);
		
	    return setting;
    }

	public void setId(Long id) {
    	this.id = id;
    }

}
