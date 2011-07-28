package com.n4systems.util;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.wrapper.AutoAttributeManagerEJBContainer;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.SecurityContext;

public class WsServiceLocator {

	// continuation of ugly hack to work around spring/ws/xfire integration. 
	// legacy code used to call ServiceLocator's static method to return new instances of beans. 
	// the desired end result is to return beans stored in springs application context. 
	// BUT, most beans require a per-request population of tenantId in order to create a securityFilter.  in wicket or struts we can use a 
	// simple interceptor/filter to get the SecurityContext bean and set the tenantId appropriately.  in xFire we don't have that luxury so 
	// i'm simply turning it into a additional parameter for all of these getter methods.

	
	
	public static final UserManager getUser(Long tenantId) {	
		return getWsBean(ServiceLocator.getUser(), tenantId);		
	}

	public static final LegacyAssetType getAssetType(Long tenantId) {
		return getWsBean(ServiceLocator.getAssetType(), tenantId);
	}

	public static final PersistenceManager getPersistenceManager(Long tenantId) {		
		return getWsBean(ServiceLocator.getPersistenceManager(),tenantId);
	}

	public static final ProofTestHandler getProofTestHandler(Long tenantId) {
		return getWsBean(ServiceLocator.getProofTestHandler(), tenantId);
	}

	public static final LegacyAsset getLegacyAssetManager(Long tenantId) {
		return getWsBean(ServiceLocator.getLegacyAssetManager(), tenantId);
	}

	public static final ServiceDTOBeanConverter getServiceDTOBeanConverter(Long tenantId) {
		return getWsBean(ServiceLocator.getServiceDTOBeanConverter(), tenantId);
	}

	public static final AutoAttributeManager getAutoAttributeManager(Long tenantId) {
		return new AutoAttributeManagerEJBContainer();
	}

	public static final EventScheduleManager getEventScheduleManager(Long tenantId) {
		return getWsBean(ServiceLocator.getEventScheduleManager(), tenantId);
	}

	public static final AssetManager getAssetManager(Long tenantId) {
		return getWsBean(ServiceLocator.getAssetManager(), tenantId);
	}

	public static final OrderManager getOrderManager(Long tenantId) {
		return getWsBean(ServiceLocator.getOrderManager(), tenantId);
	}

	private static <T> T getWsBean(T bean, Long tenantId) {
		SecurityContext context = ServiceLocator.getSecurityContext();
		context.setTenantSecurityFilter(new TenantOnlySecurityFilter(tenantId));
		return bean;
	}


}
