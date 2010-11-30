package com.n4systems.persistence.listeners;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

import com.n4systems.model.api.CrossTenantEntity;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.setupdata.SetupDataLastModDatesUpdater;
import com.n4systems.model.tenant.HasSetupDataTenant;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.Transactor;
import com.n4systems.services.InvalidSetupDataGroupClassException;
import com.n4systems.services.SetupDataGroup;
import com.n4systems.taskscheduling.TaskExecutor;

public class SetupDataUpdateEventListener implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {
	private static Logger logger = Logger.getLogger(SetupDataUpdateEventListener.class);
	
	private static final Map<Class<?>, SetupDataGroup> entityToGroupMap = new HashMap<Class<?>, SetupDataGroup>();
	static {
		for (SetupDataGroup group: SetupDataGroup.values()) {
			for (Class<?> clazz: group.getGroupClasses()) {
				entityToGroupMap.put(clazz, group);
			}
		}
	}
	
	public void onPostUpdate(PostUpdateEvent event) {
		updateSetupDataModDates(event.getEntity());
	}

	public void onPostInsert(PostInsertEvent event) {
		updateSetupDataModDates(event.getEntity());
	}

	public void onPostDelete(PostDeleteEvent event) {
		updateSetupDataModDates(event.getEntity());
	}
	
	private void updateSetupDataModDates(Object entity) {
		SetupDataGroup group = entityToGroupMap.get(entity.getClass());
		
		if (entity == null || group == null) {
			return;
		}
		
		if (entity instanceof HasSetupDataTenant) {
			Long tenantId = ((HasSetupDataTenant)entity).getSetupDataTenant().getId();
			touchModDate(group, tenantId);
		} else if (entity instanceof HasTenant) {
			Long tenantId = ((HasTenant)entity).getTenant().getId();
			touchModDate(group, tenantId);
		} else if (entity instanceof CrossTenantEntity) {
			touchAllModDates(group);
		} else {
			// if the entity is not a member of HasTenant or CrossTenantEntity.  We cannot process it.
			throw new InvalidSetupDataGroupClassException(entity.getClass());
		}
	}

	public void touchAllModDates(SetupDataGroup group) {
		touchModDate(group, null);
	}

	public void touchModDate(final SetupDataGroup group, final Long tenantId) {
		TaskExecutor.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.debug("Updating setup data mod date for tenant [" + tenantId + "], group [" + group.name() + "]");
					
					Transactor transactor = new PersistenceManagerTransactor();
					transactor.execute(new SetupDataLastModDatesUpdater(group, tenantId));
				} catch (Exception e) {
					logger.error("Failed updating SetupDataLastModDates for tenant [" + tenantId + "], group [" + group.name() + "]", e);
				}
			}
		});
	}
}
