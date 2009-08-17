package com.n4systems.handlers.remover;

import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.model.inspection.InspectionListDeleter;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypeSaver;
import com.n4systems.model.inspectiontype.InspectionFrequencySaver;
import com.n4systems.model.inspectiontype.InspectionTypeSaver;
import com.n4systems.persistence.loaders.LoaderFactory;

public class RemovalHandlerFactory {

	private final LoaderFactory loaderFacotry;
	
	public RemovalHandlerFactory(LoaderFactory loaderFactory) {
		this.loaderFacotry = loaderFactory;
	}
	
	
	public InspectionTypeArchiveHandler getInspectionTypeArchiveHandler() {
		return new InspectionTypeArchiveHandlerImpl(new InspectionTypeSaver(), 
													new InspectionListDeleter(), 
													getAssociatedInspectionTypeListDeleter(), 
													getCatalogElementRemovalHandler(), 
													new NotificationSettingDeleteHandlerImpl());
	}


	private CatalogElementRemovalHandler getCatalogElementRemovalHandler() {
		return new CatalogElementRemovalHandlerImpl(
		loaderFacotry.createCatalogLoader(), 
		new CatalogSaver());
	}

	private AssociatedInspectionTypeListDeleteHandler getAssociatedInspectionTypeListDeleter() {
		return new AssociatedInspectionTypeListDeleteHandlerImpl(loaderFacotry.createAssociatedInspectionTypesLoader(), 
																getAssociatedInspectionTypeDeleter());
	}

	private AssociatedInspectionTypeDeleteHandler getAssociatedInspectionTypeDeleter() {
		return new AssociatedInspectionTypeDeleteHandlerImpl(getInspectionFrequenciesDeleteHandler(), 
															new AssociatedInspectionTypeSaver(), 
															getScheduleDeleter());
	}
	
	private InspectionFrequenciesDeleteHandler getInspectionFrequenciesDeleteHandler() {
		return new InspectionFrequenciesDeleteHandlerImpl(loaderFacotry.createInspectionFrequenciesListLoader(), 
														new InspectionFrequencySaver());
	}

	private ScheduleListDeleteHandler getScheduleDeleter() {
		return new ScheduleListDeleteHandlerImpl();
	}
}
