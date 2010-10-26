package com.n4systems.exporting;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.api.model.AssetView;
import com.n4systems.model.Asset;
import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.persistence.loaders.ListLoader;

public class ProductExporterTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_export() throws ExportException, ConversionException, MarshalingException, IOException {
		ListLoader<Asset> productLoader = createMock(ListLoader.class);
		ExportMapMarshaler<AssetView> marshaler = createMock(ExportMapMarshaler.class);
		ModelToViewConverter<Asset, AssetView> converter = createMock(ModelToViewConverter.class);
		MapWriter writer = createMock(MapWriter.class);
		
		List<Asset> products =  Arrays.asList(new Asset());
		AssetView view = new AssetView();
		Map<String, Object> beanMap = new HashMap<String, Object>();
		
		ProductExporter exporter = new ProductExporter(productLoader, marshaler, converter);
		
		expect(productLoader.load()).andReturn(products);
		expect(converter.toView(products.get(0))).andReturn(view);
		expect(marshaler.toBeanMap(view)).andReturn(beanMap);
		writer.write(beanMap);
		
		replay(productLoader);
		replay(marshaler);
		replay(converter);
		replay(writer);
		
		exporter.export(writer);
		
		verify(productLoader);
		verify(marshaler);
		verify(converter);
		verify(writer);
	}
	
}
