package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.inspection.InspectionToViewConverter;
import com.n4systems.api.model.InspectionView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Event;
import com.n4systems.model.inspectionschedule.NextInspectionDateByInspectionLoader;
import com.n4systems.persistence.loaders.ListLoader;

public class InspectionExporter implements Exporter {
	private final ExportMapMarshaler<InspectionView> marshaler;
	private final ListLoader<Event> inspectionLoader;
	private final ModelToViewConverter<Event, InspectionView> converter;

	public InspectionExporter(ListLoader<Event> inspectionLoader, ExportMapMarshaler<InspectionView> marshaler, ModelToViewConverter<Event, InspectionView> converter) {
		this.inspectionLoader = inspectionLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public InspectionExporter(ListLoader<Event> inspectionLoader, NextInspectionDateByInspectionLoader nextDateLoader) {
		this(inspectionLoader, new ExportMapMarshaler<InspectionView>(InspectionView.class), new InspectionToViewConverter(nextDateLoader));
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		InspectionView view;
		
		for (Event event : inspectionLoader.load()) {
			try {
				view = converter.toView(event);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export inspection [%s]", event), e);
			}
		}
	}

}
