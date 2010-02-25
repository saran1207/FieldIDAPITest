package com.n4systems.webservice.server;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.model.product.ProductByMobileGuidLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;

public class InspectionScheduleCreateHandler {

	private final ProductByMobileGuidLoader productByMobileGuidLoader;
	private final FilteredIdLoader<Product> filteredProductLoader;
	private final FilteredIdLoader<InspectionType> inspectionTypeLoader;
	private final InspectionScheduleSaver saver;

	public InspectionScheduleCreateHandler(
			ProductByMobileGuidLoader productByMobileGuidLoader,
			FilteredIdLoader<Product> filteredProductLoader,
			FilteredIdLoader<InspectionType> inspectionTypeLoader,
			InspectionScheduleSaver saver) {
		super();
		this.productByMobileGuidLoader = productByMobileGuidLoader;
		this.filteredProductLoader = filteredProductLoader;
		this.inspectionTypeLoader = inspectionTypeLoader;
		this.saver = saver;
	}

	public void createNewInspectionSchedule(InspectionSchedule inspectionSchedule, InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		inspectionSchedule.setProduct(loadProduct(inspectionSchedule, inspectionScheduleServiceDTO));
		
		inspectionSchedule.setInspectionType(loadInspectionType(inspectionSchedule, inspectionScheduleServiceDTO));
		
		saver.saveOrUpdate(inspectionSchedule);
	}

	private Product loadProduct(InspectionSchedule inspectionSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		
		Product product;
		if ( inspectionScheduleServiceDTO.isProductCreatedOnMobile()) {
			product = productByMobileGuidLoader.setMobileGuid(inspectionScheduleServiceDTO.getProductMobileGuid()).load();
		} else {
			product = filteredProductLoader.setId(inspectionScheduleServiceDTO.getProductId()).load();
		}
		
		return product;
	}
	
	private InspectionType loadInspectionType(InspectionSchedule inspectionSchedule,
			InspectionScheduleServiceDTO inspectionScheduleServiceDTO) {
		return inspectionTypeLoader.setId(inspectionScheduleServiceDTO.getInspectionTypeId()).load();
		
	}
	
	
}
