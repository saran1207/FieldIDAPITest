package com.n4systems.servicedto.converts;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.InspectionServiceDTO;

import static com.n4systems.util.ServiceLocator.getServiceDTOBeanConverter;

public class InspectionServiceDTOConverter {
	private ServiceDTOBeanConverter serviceDTOBeanconverter;
	private SystemSecurityGuard systemSecurityGuard;
	private AssignedUserConverter assignedUserConverter;
	private LocationConverter locationConverter;

	public InspectionServiceDTOConverter(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
		this.serviceDTOBeanconverter = getServiceDTOBeanConverter();
		
		LoaderFactory loaderFactory = createLoaderFactory(systemSecurityGuard);
		this.assignedUserConverter = createAssignedUserConverter(loaderFactory);
		this.locationConverter = createLocationConverter(loaderFactory);
	}

	private AssignedUserConverter createAssignedUserConverter(LoaderFactory loaderFactory) {
		return new AssignedUserConverterFactory(systemSecurityGuard, loaderFactory).getAssignedUserConverterForEvent();
	}

	private LoaderFactory createLoaderFactory(SystemSecurityGuard securityGuard) {
		return new LoaderFactory(new TenantOnlySecurityFilter(systemSecurityGuard.getTenantId()));
	}
	
	private LocationConverter createLocationConverter(LoaderFactory loaderFactory) {
		return new LocationServiceToContainerConverter(loaderFactory);
	}
	
	public ThingEvent convert(InspectionServiceDTO inspectionServiceDTO, ThingEvent schedule) throws Exception {

        ThingEvent event = null;
		try {

			event = serviceDTOBeanconverter.convert(inspectionServiceDTO, schedule, systemSecurityGuard.getTenantId());
			event = assignedUserConverter.convert(inspectionServiceDTO, event);
			locationConverter.convert(inspectionServiceDTO, event);
			
		} catch (Exception e) {
			throw e;
		}
		
		return event;
	}
	

}
 