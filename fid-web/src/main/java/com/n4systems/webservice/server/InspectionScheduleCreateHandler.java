package com.n4systems.webservice.server;

import com.n4systems.model.Asset;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;

public class InspectionScheduleCreateHandler {

	private final AssetByMobileGuidLoader assetByMobileGuidLoader;
	private final FilteredIdLoader<Asset> filteredProductLoader;
	private final FilteredIdLoader<InspectionType> inspectionTypeLoader;
	private final InspectionScheduleSaver saver;

	public InspectionScheduleCreateHandler(
			AssetByMobileGuidLoader assetByMobileGuidLoader,
			FilteredIdLoader<Asset> filteredProductLoader,
			FilteredIdLoader<InspectionType> inspectionTypeLoader,
			InspectionScheduleSaver saver) {
		super();
		this.assetByMobileGuidLoader = assetByMobileGuidLoader;
		this.filteredProductLoader = filteredProductLoader;
		this.inspectionTypeLoader = inspectionTypeLoader;
		this.saver = saver;
	}

	public void createNewInspectionSchedule(InspectionSchedule inspectionSchedule, InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		inspectionSchedule.setAsset(loadProduct(inspectionSchedule, inspectionScheduleServiceDTO));
		
		inspectionSchedule.setInspectionType(loadInspectionType(inspectionSchedule, inspectionScheduleServiceDTO));
		
		saver.saveOrUpdate(inspectionSchedule);
	}

	private Asset loadProduct(InspectionSchedule inspectionSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		Asset asset;
		if ( inspectionScheduleServiceDTO.isProductCreatedOnMobile()) {
			asset = assetByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getProductMobileGuid()).load();
		} else {
			asset = filteredProductLoader.setId(inspectionScheduleServiceDTO.getProductId()).load();
		}
		
		return asset;
	}
	
	private InspectionType loadInspectionType(InspectionSchedule inspectionSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		return inspectionTypeLoader.setId(inspectionScheduleServiceDTO.getInspectionTypeId()).load();
		
	}
	
	
}
