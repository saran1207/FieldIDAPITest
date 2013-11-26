package com.n4systems.webservice.asset;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.event.LastEventLoader;
import com.n4systems.webservice.ModelToServiceConverter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;

import java.util.ArrayList;
import java.util.List;

public class AssetToServiceConverter implements ModelToServiceConverter<Asset, ProductServiceDTO> {
	private final ServiceDTOBeanConverter serviceConverter;
	private final LastEventLoader lastEventLoader;
	private final ModelToServiceConverter<ThingEvent, InspectionServiceDTO> eventConverter;
	private boolean withPreviousEvents;
	
	public AssetToServiceConverter(ServiceDTOBeanConverter serviceConverter, LastEventLoader lastEventLoader, ModelToServiceConverter<ThingEvent, InspectionServiceDTO> eventConverter) {
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
		List<ThingEvent> events = lastEventLoader.setAssetId(assetId).load();
		
		List<InspectionServiceDTO> dtos = new ArrayList<InspectionServiceDTO>();
		for (ThingEvent insp: events) {
			dtos.add(eventConverter.toServiceDTO(insp));
		}
		
		return dtos;
	}

	public void setWithPreviousEvents(boolean withPreviousEvents) {
		this.withPreviousEvents = withPreviousEvents;
	}
}
