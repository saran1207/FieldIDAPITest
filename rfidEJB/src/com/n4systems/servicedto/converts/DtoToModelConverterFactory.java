package com.n4systems.servicedto.converts;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;

public class DtoToModelConverterFactory {

	public static DtoToModelConverterFactory createFactory(SystemSecurityGuard systemSecurityGuard) {
		return new DtoToModelConverterFactory(systemSecurityGuard);
	}

	
	private final SystemSecurityGuard systemSecurityGuard;

	
	private DtoToModelConverterFactory(SystemSecurityGuard systemSecurityGuard) {
		this.systemSecurityGuard = systemSecurityGuard;
	}


	public ProductServiceDTOConverter createProductConverter() {
		return new ProductServiceDTOConverter(systemSecurityGuard);
	}
	
	public InspectionServiceDTOConverter createInspectionConverter() {
		return new InspectionServiceDTOConverter(systemSecurityGuard);
	}
	

}
