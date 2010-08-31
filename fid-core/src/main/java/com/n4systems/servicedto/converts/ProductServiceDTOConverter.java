package com.n4systems.servicedto.converts;

import static com.n4systems.util.ServiceLocator.*;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.Product;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class ProductServiceDTOConverter {

	private ServiceDTOBeanConverter serviceDTOBeanconverter;
	private SystemSecurityGuard systemSecurityGuard;
	private AssignedUserConverter assignedUserConverter;
	private LocationConverter locationConverter;
	
	public ProductServiceDTOConverter(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
		this.serviceDTOBeanconverter = getServiceDTOBeanConverter();
		
		LoaderFactory loaderFactory = createLoaderFactory(systemSecurityGuard);
		this.assignedUserConverter = createAssignedUserConverter(systemSecurityGuard, loaderFactory);
		this.locationConverter = createLocationConverter(loaderFactory);
	}
	
	private LoaderFactory createLoaderFactory(SystemSecurityGuard securityGuard) {
		return new LoaderFactory(new TenantOnlySecurityFilter(systemSecurityGuard.getTenantId()));
	}

	private AssignedUserConverter createAssignedUserConverter(SystemSecurityGuard systemSecurityGuard, LoaderFactory loaderFactory) {
		return new AssignedUserConverterFactory(systemSecurityGuard, loaderFactory).getAssignedUserConverterForAsset();
	}
	
	private LocationConverter createLocationConverter(LoaderFactory loaderFactory) {
		return new LocationServiceToContainerConverter(loaderFactory);
	}
	
	public Product convert(ProductServiceDTO productServiceDTO, Product targetProduct) {
		serviceDTOBeanconverter.convert(productServiceDTO, targetProduct, systemSecurityGuard.getTenantId());
		assignedUserConverter.convert(productServiceDTO, targetProduct);
		locationConverter.convert(productServiceDTO, targetProduct);
		
		return targetProduct;
	}
	
}
	