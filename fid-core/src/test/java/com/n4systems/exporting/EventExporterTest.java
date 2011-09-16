package com.n4systems.exporting;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.api.model.EventView;
import com.n4systems.model.Event;
import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.persistence.loaders.ListLoader;

public class EventExporterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_export() throws ExportException, ConversionException, MarshalingException, IOException {
		Event model1 = new Event();
		Event model2 = new Event();
		EventView view1 = new EventView();
		EventView view2 = new EventView();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		
		ListLoader<Event> eventLoader = createMock(ListLoader.class);
		expect(eventLoader.load()).andReturn(Arrays.asList(model1, model2));
		replay(eventLoader);
		
		ModelToViewConverter<Event, EventView> converter = createMock(ModelToViewConverter.class);
		expect(converter.toView(model1)).andReturn(view1);
		expect(converter.toView(model2)).andReturn(view2);
		replay(converter);
		
		ExportMapMarshaller<EventView> marshaler = createMock(ExportMapMarshaller.class);
		expect(marshaler.toBeanMap(view1)).andReturn(map1);
		expect(marshaler.toBeanMap(view2)).andReturn(map2);
		replay(marshaler);
		
		MapWriter writer = createMock(MapWriter.class);
		writer.write(map1);
		writer.write(map2);
		replay(writer);
		
		EventExporter exporter = new EventExporter(eventLoader, marshaler, converter);
		exporter.export(writer);
		
		verify(eventLoader);
		verify(marshaler);
		verify(converter);
		verify(writer);
	}
	
}
