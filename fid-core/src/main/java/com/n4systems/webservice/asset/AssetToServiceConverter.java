package com.n4systems.webservice.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.event.LastEventLoader;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class AssetToServiceConverter implements ModelToServiceConverter<Asset, ProductServiceDTO> {
	private final ServiceDTOBeanConverter serviceConverter;
	private final LastEventLoader lastEventLoader;
	private final ModelToServiceConverter<Event, InspectionServiceDTO> eventConverter;
	private boolean withPreviousEvents;
	
	public AssetToServiceConverter(ServiceDTOBeanConverter serviceConverter, LastEventLoader lastEventLoader, ModelToServiceConverter<Event, InspectionServiceDTO> eventConverter) {
		this.serviceConverter = serviceConverter;
		this.lastEventLoader = lastEventLoader;
		this.eventConverter = eventConverter;
	}
	
	@Override
	public ProductServiceDTO toServiceDTO(Asset model) {
		ProductServiceDTO dto = serviceConverter.convert(model);
		
		if (withPreviousEvents) {
			dto.setLastInspections(convertLastEvents(model.getId()));
		}
		
		return dto;
	}
	
	private List<InspectionServiceDTO> convertLastEvents(Long assetId) {
		List<Event> events = lastEventLoader.setAssetId(assetId).load();
		
		List<InspectionServiceDTO> dtos = new ArrayList<InspectionServiceDTO>();
		for (Event insp: events) {
			dtos.add(eventConverter.toServiceDTO(insp));
		}
		
		return dtos;
	}

	public void setWithPreviousEvents(boolean withPreviousEvents) {
		this.withPreviousEvents = withPreviousEvents;
	}
}
