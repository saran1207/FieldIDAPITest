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
import com.n4systems.model.tenant.HasSetupDataTenant;
import com.n4systems.services.InvalidSetupDataGroupClassException;
import com.n4systems.services.SetupDataGroup;
import com.n4systems.services.SetupDataLastModUpdateService;

public class SetupDataUpdateEventListener implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SetupDataUpdateEventListener.class);
	
	private static Map<Class<?>, SetupDataGroup> setupDataGroupMap;
	
	private static Map<Class<?>, SetupDataGroup> getSetupDataGroupMap() {
		if (setupDataGroupMap == null) {
			initSetupDataGroupMap();
		}
		
		return setupDataGroupMap;
	}
	
	private static synchronized void initSetupDataGroupMap() {
		logger.info("SetupDataUpdateEventListener is initializing");
		setupDataGroupMap = new HashMap<Class<?>, SetupDataGroup>();
		
		for (SetupDataGroup group: SetupDataGroup.values()) {
			for (Class<?> clazz: group.getGroupClasses()) {
				setupDataGroupMap.put(clazz, group);
			}
		}
	}
	
	public SetupDataUpdateEventListener() {
		// force initialization
		getSetupDataGroupMap();
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

	private boolean isUpdateClass(Class<?> clazz) {
		return getSetupDataGroupMap().containsKey(clazz);
	}
	
	private void updateSetupDataModDates(Object entity) {
		if (entity == null || !isUpdateClass(entity.getClass())) {
			// we do not need to update for this class
			return;
		}
		
		SetupDataGroup group = getSetupDataGroupMap().get(entity.getClass());
		SetupDataLastModUpdateService updateService = SetupDataLastModUpdateService.getInstance();
		
		if (entity instanceof HasSetupDataTenant) {
			Long tenantId = ((HasSetupDataTenant)entity).getSetupDataTenant().getId();
			updateService.touchModDate(tenantId, group);
		} else if (entity instanceof HasTenant) {
			// if the entity has a tenant, then we will update just for the specific tenant
			Long tenantId = ((HasTenant)entity).getTenant().getId();
			updateService.touchModDate(tenantId, group);
		} else if (entity instanceof CrossTenantEntity) {
			// if the entity is a CrossTenantEntity we will update for all tenants
			updateService.touchAllModDates(group);
		} else {
			// an entity was specified (programatically) that was not of type HasTenant or CrossTenantEntity.  We cannot process it.
			throw new InvalidSetupDataGroupClassException(entity.getClass());
		}
	}
}
