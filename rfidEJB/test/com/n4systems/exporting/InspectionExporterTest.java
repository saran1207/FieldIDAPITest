package com.n4systems.exporting;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.model.InspectionView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.loaders.ListLoader;

public class InspectionExporterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_export() throws ExportException, ConversionException, MarshalingException, IOException {
		Inspection model1 = new Inspection();
		Inspection model2 = new Inspection();
		InspectionView view1 = new InspectionView();
		InspectionView view2 = new InspectionView();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		
		ListLoader<Inspection> inspectionLoader = createMock(ListLoader.class);
		expect(inspectionLoader.load()).andReturn(Arrays.asList(model1, model2));
		replay(inspectionLoader);
		
		ModelToViewConverter<Inspection, InspectionView> converter = createMock(ModelToViewConverter.class);
		expect(converter.toView(model1)).andReturn(view1);
		expect(converter.toView(model2)).andReturn(view2);
		replay(converter);
		
		ExportMapMarshaler<InspectionView> marshaler = createMock(ExportMapMarshaler.class);
		expect(marshaler.toBeanMap(view1)).andReturn(map1);
		expect(marshaler.toBeanMap(view2)).andReturn(map2);
		replay(marshaler);
		
		MapWriter writer = createMock(MapWriter.class);
		writer.write(map1);
		writer.write(map2);
		replay(writer);
		
		InspectionExporter exporter = new InspectionExporter(inspectionLoader, marshaler, converter);
		exporter.export(writer);
		
		verify(inspectionLoader);
		verify(marshaler);
		verify(converter);
		verify(writer);
	}
	
}
