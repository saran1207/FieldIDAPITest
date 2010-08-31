package com.n4systems.exporting;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.product.ProductToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.ProductView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.testutils.DummyTransaction;

public class ProductImporterTest {
	/*
	 *  The following tests are pretty ugly.  They should be refactored at some point.  
	 */
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateException.class)
	public void test_import_throws_exception_when_run_import_called_before_validate() throws ImportException {
		Validator<ExternalModelView> validator = createMock(Validator.class);
		ProductToModelConverter converter = createMock(ProductToModelConverter.class);

		expect(validator.getValidationContext()).andReturn(new HashMap<String, Object>());
		expect(converter.getType()).andReturn(new ProductType());
		
		replay(validator);
		replay(converter);
		
		ProductImporter importer = new ProductImporter(null, validator, null, converter);
		importer.runImport(null);
		
		verify(validator);
		verify(converter);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_read_and_validate_reads_map_converts_and_validates() throws IOException, ParseException, MarshalingException {
		MapReader reader = createMock(MapReader.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		ProductToModelConverter converter = createMock(ProductToModelConverter.class);
		final ExportMapUnmarshaler<ProductView> mapUnmarshaler = createMock(ExportMapUnmarshaler.class);
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		ProductType type = ProductTypeBuilder.aProductType().named("test_type").build();
		Map<String, Object> row = new HashMap<String, Object>();		
		ProductView view = new ProductView();
		List<ValidationResult> expectedResults = new ArrayList<ValidationResult>();
		
		expect(validator.getValidationContext()).andReturn(validationContext);
		expect(converter.getType()).andReturn(type);
		expect(reader.readMap()).andReturn(row);
		expect(mapUnmarshaler.toBean(row)).andReturn(view);
		expect(reader.readMap()).andReturn(null);
		expect(validator.validate(view, 2)).andReturn(expectedResults);
		
		replay(reader);
		replay(validator);
		replay(converter);
		replay(mapUnmarshaler);
		
		ProductImporter importer = new ProductImporter(reader, validator, createMock(ProductSaveService.class), converter) {
			protected ExportMapUnmarshaler<ProductView> createMapUnmarshaler() throws IOException, ParseException {
				return mapUnmarshaler;
			}
		};
		
		List<ValidationResult> results = importer.readAndValidate();
		
		assertEquals(expectedResults, results);
		
		verify(reader);
		verify(validator);
		verify(converter);
		verify(mapUnmarshaler);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_import() throws ImportException, IOException, ParseException, MarshalingException, ConversionException {
		MapReader reader = createMock(MapReader.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		ProductToModelConverter converter = createMock(ProductToModelConverter.class);
		ProductSaveService saver = createMock(ProductSaveService.class);
		final ExportMapUnmarshaler<ProductView> mapUnmarshaler = createMock(ExportMapUnmarshaler.class);
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		ProductType type = ProductTypeBuilder.aProductType().named("test_type").build();
		Map<String, Object> row = new HashMap<String, Object>();		
		ProductView view = new ProductView();
		DummyTransaction transaction = new DummyTransaction();
		Product product = new Product();
		
		expect(validator.getValidationContext()).andReturn(validationContext);
		expect(converter.getType()).andReturn(type);
		expect(reader.readMap()).andReturn(row);
		expect(mapUnmarshaler.toBean(row)).andReturn(view);
		expect(reader.readMap()).andReturn(null);
		expect(validator.validate(view, 2)).andReturn(new ArrayList<ValidationResult>());
		reader.close();
		expect(converter.toModel(view, transaction)).andReturn(product);
		expect(saver.setProduct(product)).andReturn(saver);
		expect(saver.createWithoutHistory()).andReturn(product);
		
		replay(reader);
		replay(validator);
		replay(converter);
		replay(saver);
		replay(mapUnmarshaler);
		
		ProductImporter importer = new ProductImporter(reader, validator, saver, converter) {
			protected ExportMapUnmarshaler<ProductView> createMapUnmarshaler() throws IOException, ParseException {
				return mapUnmarshaler;
			}
		};
		
		importer.readAndValidate();
		importer.runImport(transaction);
		
		verify(reader);
		verify(validator);
		verify(converter);
		verify(saver);
		verify(mapUnmarshaler);
	}
}
