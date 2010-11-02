package com.n4systems.webservice.server;

import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.inspectionschedule.EventScheduleSaver;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;

public class InspectionScheduleCreateHandler {

	private final AssetByMobileGuidLoader assetByMobileGuidLoader;
	private final FilteredIdLoader<Asset> filteredProductLoader;
	private final FilteredIdLoader<EventType> inspectionTypeLoader;
	private final EventScheduleSaver saver;

	public InspectionScheduleCreateHandler(
			AssetByMobileGuidLoader assetByMobileGuidLoader,
			FilteredIdLoader<Asset> filteredProductLoader,
			FilteredIdLoader<EventType> inspectionTypeLoader,
			EventScheduleSaver saver) {
		super();
		this.assetByMobileGuidLoader = assetByMobileGuidLoader;
		this.filteredProductLoader = filteredProductLoader;
		this.inspectionTypeLoader = inspectionTypeLoader;
		this.saver = saver;
	}

	public void createNewInspectionSchedule(EventSchedule eventSchedule, InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		eventSchedule.setAsset(loadProduct(eventSchedule, inspectionScheduleServiceDTO));
		
		eventSchedule.setEventType(loadInspectionType(eventSchedule, inspectionScheduleServiceDTO));
		
		saver.saveOrUpdate(eventSchedule);
	}

	private Asset loadProduct(EventSchedule eventSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		Asset asset;
		if ( inspectionScheduleServiceDTO.isProductCreatedOnMobile()) {
			asset = assetByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getProductMobileGuid()).load();
		} else {
			asset = filteredProductLoader.setId(inspectionScheduleServiceDTO.getProductId()).load();
		}
		
		return asset;
	}
	
	private EventType loadInspectionType(EventSchedule eventSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		return inspectionTypeLoader.setId(inspectionScheduleServiceDTO.getInspectionTypeId()).load();
		
	}
	
	
}
