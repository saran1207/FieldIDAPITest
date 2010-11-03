package com.n4systems.handlers.remover;

import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.model.inspectiontype.AssociatedEventTypeSaver;
import com.n4systems.model.inspectiontype.EventFrequencySaver;
import com.n4systems.model.inspectiontype.EventTypeSaver;
import com.n4systems.persistence.loaders.LoaderFactory;

public class RemovalHandlerFactory {

	private final LoaderFactory loaderFacotry;
	
	public RemovalHandlerFactory(LoaderFactory loaderFactory) {
		this.loaderFacotry = loaderFactory;
	}
	
	
	public EventTypeArchiveHandler getInspectionTypeArchiveHandler() {
		return new EventTypeArchiveHandlerImpl(new EventTypeSaver(),
													new EventListArchiveHandlerImp(getScheduleDeleter()),
													getAssociatedEventTypeListDeleter(),
													getCatalogElementRemovalHandler(), 
													new NotificationSettingDeleteHandlerImpl());
	}


	private CatalogElementRemovalHandler getCatalogElementRemovalHandler() {
		return new CatalogElementRemovalHandlerImpl(
		loaderFacotry.createCatalogLoader(), 
		new CatalogSaver());
	}

	private AssociatedEventTypeListDeleteHandler getAssociatedEventTypeListDeleter() {
		return new AssociatedEventTypeListDeleteHandlerImpl(loaderFacotry.createAssociatedEventTypesLoader(),
																getAssociatedEventTypeDeleter());
	}

	private AssociatedEventTypeDeleteHandler getAssociatedEventTypeDeleter() {
		return new AssociatedEventTypeDeleteHandlerImpl(getEventFrequenciesDeleteHandler(),
															new AssociatedEventTypeSaver(),
															getScheduleDeleter());
	}
	
	private EventFrequenciesDeleteHandler getEventFrequenciesDeleteHandler() {
		return new EventFrequenciesDeleteHandlerImpl(loaderFacotry.createEventFrequenciesListLoader(),
														new EventFrequencySaver());
	}

	private ScheduleListDeleteHandler getScheduleDeleter() {
		return new ScheduleListDeleteHandlerImpl();
	}
}
