package com.n4systems.servicedto.converts;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.Product;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ServiceLocator;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class ProductServiceDTOConverter {

	private ServiceDTOBeanConverter serviceDTOBeanconverter;
	private SystemSecurityGuard systemSecurityGuard;
	
	public ProductServiceDTOConverter(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
	}

	public Product convert(ProductServiceDTO productServiceDTO, Product targetProduct, long tenantId) {
		
		serviceDTOBeanconverter = ServiceLocator.getServiceDTOBeanConverter();
		Product product = serviceDTOBeanconverter.convert(productServiceDTO, targetProduct, tenantId);
		
		LoaderFactory loaderFactory = new LoaderFactory(new TenantOnlySecurityFilter(tenantId));
		
		AssignedUserConverterFactory assignedUserConverterFactory = new AssignedUserConverterFactory(systemSecurityGuard, loaderFactory);
		
		AssignedUserConverter assignedUserConverter = assignedUserConverterFactory.getAssignedUserConverter();
		
		return assignedUserConverter.convert(productServiceDTO, product);
		
	}
	
}
