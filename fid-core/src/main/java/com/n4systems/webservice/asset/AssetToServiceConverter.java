package com.n4systems.webservice.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.inspection.LastInspectionLoader;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class AssetToServiceConverter implements ModelToServiceConverter<Asset, ProductServiceDTO> {
	private final ServiceDTOBeanConverter serviceConverter;
	private final LastInspectionLoader lastInspectionLoader;
	private final ModelToServiceConverter<Event, InspectionServiceDTO> inspectionConverter;
	private boolean withPreviosInspections;
	
	public AssetToServiceConverter(ServiceDTOBeanConverter serviceConverter, LastInspectionLoader lastInspectionLoader, ModelToServiceConverter<Event, InspectionServiceDTO> inspectionConverter) {
		this.serviceConverter = serviceConverter;
		this.lastInspectionLoader = lastInspectionLoader;
		this.inspectionConverter = inspectionConverter;
	}
	
	@Override
	public ProductServiceDTO toServiceDTO(Asset model) {
		ProductServiceDTO dto = serviceConverter.convert(model);
		
		if (withPreviosInspections) {
			dto.setLastInspections(convertLastInspections(model.getId()));
		}
		
		return dto;
	}
	
	private List<InspectionServiceDTO> convertLastInspections(Long productId) {
		List<Event> events = lastInspectionLoader.setAssetId(productId).load();
		
		List<InspectionServiceDTO> dtos = new ArrayList<InspectionServiceDTO>();
		for (Event insp: events) {
			dtos.add(inspectionConverter.toServiceDTO(insp));
		}
		
		return dtos;
	}

	public void setWithPreviosInspections(boolean withPreviosInspections) {
		this.withPreviosInspections = withPreviosInspections;
	}
}
