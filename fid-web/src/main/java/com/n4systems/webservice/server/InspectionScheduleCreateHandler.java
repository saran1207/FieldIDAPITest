package com.n4systems.webservice.server;

import com.n4systems.model.*;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;

public class InspectionScheduleCreateHandler {

	private final AssetByMobileGuidLoader assetByMobileGuidLoader;
	private final FilteredIdLoader<Asset> filteredProductLoader;
	private final FilteredIdLoader<EventType> inspectionTypeLoader;
	private final SimpleEventSaver saver;

	public InspectionScheduleCreateHandler(
			AssetByMobileGuidLoader assetByMobileGuidLoader,
			FilteredIdLoader<Asset> filteredProductLoader,
			FilteredIdLoader<EventType> inspectionTypeLoader,
			SimpleEventSaver saver) {
		super();
		this.assetByMobileGuidLoader = assetByMobileGuidLoader;
		this.filteredProductLoader = filteredProductLoader;
		this.inspectionTypeLoader = inspectionTypeLoader;
		this.saver = saver;
	}

	public void createNewInspectionSchedule(Event openEvent, InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
        Asset asset = loadProduct(openEvent, inspectionScheduleServiceDTO);
        openEvent.setAsset(asset);
		
		openEvent.setType(loadInspectionType(openEvent, inspectionScheduleServiceDTO));
        openEvent.setTenant(asset.getTenant());
        openEvent.setOwner(asset.getOwner());

		saver.saveOrUpdate(openEvent);
	}

	private Asset loadProduct(Event eventSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		Asset asset;
		if ( inspectionScheduleServiceDTO.isProductCreatedOnMobile()) {
			asset = assetByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getProductMobileGuid()).load();
		} else {
			asset = filteredProductLoader.setId(inspectionScheduleServiceDTO.getProductId()).load();
		}
		
		return asset;
	}
	
	private EventType loadInspectionType(Event openEvent,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		return inspectionTypeLoader.setId(inspectionScheduleServiceDTO.getInspectionTypeId()).load();
		
	}
	
	
}
