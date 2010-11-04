package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.event.EventToViewConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Event;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.persistence.loaders.ListLoader;

public class EventExporter implements Exporter {
	private final ExportMapMarshaler<EventView> marshaler;
	private final ListLoader<Event> eventLoader;
	private final ModelToViewConverter<Event, EventView> converter;

	public EventExporter(ListLoader<Event> eventLoader, ExportMapMarshaler<EventView> marshaler, ModelToViewConverter<Event, EventView> converter) {
		this.eventLoader = eventLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public EventExporter(ListLoader<Event> eventLoader, NextEventDateByEventLoader nextDateLoader) {
		this(eventLoader, new ExportMapMarshaler<EventView>(EventView.class), new EventToViewConverter(nextDateLoader));
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		EventView view;
		
		for (Event event : eventLoader.load()) {
			try {
				view = converter.toView(event);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export event [%s]", event), e);
			}
		}
	}

}
