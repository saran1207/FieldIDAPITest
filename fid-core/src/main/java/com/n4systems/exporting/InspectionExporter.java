package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.inspection.InspectionToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Event;
import com.n4systems.model.inspectionschedule.NextEventDateByEventLoader;
import com.n4systems.persistence.loaders.ListLoader;

public class InspectionExporter implements Exporter {
	private final ExportMapMarshaler<EventView> marshaler;
	private final ListLoader<Event> inspectionLoader;
	private final ModelToViewConverter<Event, EventView> converter;

	public InspectionExporter(ListLoader<Event> inspectionLoader, ExportMapMarshaler<EventView> marshaler, ModelToViewConverter<Event, EventView> converter) {
		this.inspectionLoader = inspectionLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public InspectionExporter(ListLoader<Event> inspectionLoader, NextEventDateByEventLoader nextDateLoader) {
		this(inspectionLoader, new ExportMapMarshaler<EventView>(EventView.class), new InspectionToViewConverter(nextDateLoader));
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		EventView view;
		
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
