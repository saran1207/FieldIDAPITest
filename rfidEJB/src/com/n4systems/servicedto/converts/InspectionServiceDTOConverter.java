package com.n4systems.servicedto.converts;

import static com.n4systems.util.ServiceLocator.*;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.Inspection;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.InspectionServiceDTO;

public class InspectionServiceDTOConverter {
	private ServiceDTOBeanConverter serviceDTOBeanconverter;
	private SystemSecurityGuard systemSecurityGuard;
	private AssignedUserConverter assignedUserConverter;

	public InspectionServiceDTOConverter(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
		this.serviceDTOBeanconverter = getServiceDTOBeanConverter();
		this.assignedUserConverter = createAssignedUserConverter(systemSecurityGuard);
	}

	private AssignedUserConverter createAssignedUserConverter(SystemSecurityGuard systemSecurityGuard) {
		return new AssignedUserConverterFactory(systemSecurityGuard, new LoaderFactory(new TenantOnlySecurityFilter(systemSecurityGuard.getTenantId()))).getAssignedUserConverterForEvent();
	}

	public Inspection convert(InspectionServiceDTO inspectionServiceDTO) throws Exception {
		
		Inspection inspection = null;
		try {
			
			inspection = serviceDTOBeanconverter.convert(inspectionServiceDTO, systemSecurityGuard.getTenantId());
			inspection = assignedUserConverter.convert(inspectionServiceDTO, inspection);
			
		} catch (Exception e) {
			throw e;
		}
		
		return inspection;
	}
	

}
 