package com.n4systems.servicedto.converts;

import static com.n4systems.util.ServiceLocator.*;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.Product;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class ProductServiceDTOConverter {

	private ServiceDTOBeanConverter serviceDTOBeanconverter;
	private SystemSecurityGuard systemSecurityGuard;
	private AssignedUserConverter assignedUserConverter;
	private LocationServiceDTOConverter locationServiceDTOConverter;
	
	public ProductServiceDTOConverter(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
		this.serviceDTOBeanconverter = getServiceDTOBeanConverter();
		this.assignedUserConverter = createAssignedUserConverter(systemSecurityGuard);
		this.locationServiceDTOConverter = createLocationServiceConverter();
		
	}

	private AssignedUserConverter createAssignedUserConverter(SystemSecurityGuard systemSecurityGuard) {
		return new AssignedUserConverterFactory(systemSecurityGuard, new LoaderFactory(new TenantOnlySecurityFilter(systemSecurityGuard.getTenantId()))).getAssignedUserConverterForAsset();
	}

	private LocationServiceDTOConverter createLocationServiceConverter() {
		return new LocationServiceDTOConverter(new FilteredIdLoader<PredefinedLocation>(new TenantOnlySecurityFilter(systemSecurityGuard.getTenantId()), PredefinedLocation.class));
	}
	
	public Product convert(ProductServiceDTO productServiceDTO, Product targetProduct) {
		serviceDTOBeanconverter.convert(productServiceDTO, targetProduct, systemSecurityGuard.getTenantId());
		assignedUserConverter.convert(productServiceDTO, targetProduct);
		locationServiceDTOConverter.convert(productServiceDTO.getLocation(), targetProduct);
		
		return targetProduct;
	}
	
}
	