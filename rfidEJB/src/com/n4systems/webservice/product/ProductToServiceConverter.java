package com.n4systems.webservice.product;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.inspection.LastInspectionLoader;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class ProductToServiceConverter implements ModelToServiceConverter<Product, ProductServiceDTO> {
	private final ServiceDTOBeanConverter serviceConverter;
	private final LastInspectionLoader lastInspectionLoader;
	private final ModelToServiceConverter<Inspection, InspectionServiceDTO> inspectionConverter;
	
	public ProductToServiceConverter(ServiceDTOBeanConverter serviceConverter, LastInspectionLoader lastInspectionLoader, ModelToServiceConverter<Inspection, InspectionServiceDTO> inspectionConverter) {
		this.serviceConverter = serviceConverter;
		this.lastInspectionLoader = lastInspectionLoader;
		this.inspectionConverter = inspectionConverter;
	}
	
	@Override
	public ProductServiceDTO toServiceDTO(Product model) {
		ProductServiceDTO dto = serviceConverter.convert(model);
		dto.setLastInspection(convertLastInspection(model.getId()));
		return dto;
	}
	
	private InspectionServiceDTO convertLastInspection(Long productId) {
		Inspection inspection = lastInspectionLoader.setProductId(productId).load();
		if (inspection == null) {
			return null;
		}
		return inspectionConverter.toServiceDTO(inspection);
	}

}
