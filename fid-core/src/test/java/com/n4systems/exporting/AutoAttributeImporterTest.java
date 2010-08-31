package com.n4systems.exporting;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.autoattribute.AutoAttributeToModelConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.testutils.DummyTransaction;

public class AutoAttributeImporterTest {
	/*
	 *  The following tests are pretty ugly.  They should be refactored at some point.  
	 */
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateException.class)
	public void test_import_throws_exception_when_run_import_called_before_validate() throws ImportException {
		Validator<ExternalModelView> validator = createMock(Validator.class);
		AutoAttributeToModelConverter converter = createMock(AutoAttributeToModelConverter.class);

		expect(validator.getValidationContext()).andReturn(new HashMap<String, Object>());
		expect(converter.getCriteria()).andReturn(new AutoAttributeCriteria());
		
		replay(validator);
		replay(converter);
		
		AutoAttributeImporter importer = new AutoAttributeImporter(null, validator, null, converter);
		importer.runImport(null);
		
		verify(validator);
		verify(converter);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_read_and_validate_reads_map_converts_and_validates() throws IOException, ParseException, MarshalingException {
		MapReader reader = createMock(MapReader.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		AutoAttributeToModelConverter converter = createMock(AutoAttributeToModelConverter.class);
		final ExportMapUnmarshaler<AutoAttributeView> mapUnmarshaler = createMock(ExportMapUnmarshaler.class);
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		Map<String, Object> row = new HashMap<String, Object>();		
		AutoAttributeView view = new AutoAttributeView();
		List<ValidationResult> expectedResults = new ArrayList<ValidationResult>();
		
		expect(validator.getValidationContext()).andReturn(validationContext);
		expect(converter.getCriteria()).andReturn(new AutoAttributeCriteria());
		expect(reader.readMap()).andReturn(row);
		expect(mapUnmarshaler.toBean(row)).andReturn(view);
		expect(reader.readMap()).andReturn(null);
		expect(validator.validate(view, 2)).andReturn(expectedResults);
		
		replay(reader);
		replay(validator);
		replay(converter);
		replay(mapUnmarshaler);
		
		AutoAttributeImporter importer = new AutoAttributeImporter(reader, validator, createMock(Saver.class), converter) {
			protected ExportMapUnmarshaler<AutoAttributeView> createMapUnmarshaler() throws IOException, ParseException {
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
		AutoAttributeToModelConverter converter = createMock(AutoAttributeToModelConverter.class);
		Saver<AutoAttributeDefinition> saver = createMock(Saver.class);
		final ExportMapUnmarshaler<AutoAttributeView> mapUnmarshaler = createMock(ExportMapUnmarshaler.class);
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		Map<String, Object> row = new HashMap<String, Object>();		
		AutoAttributeView view = new AutoAttributeView();
		DummyTransaction transaction = new DummyTransaction();
		AutoAttributeDefinition model = new AutoAttributeDefinition();
		
		AutoAttributeCriteria criteria = new AutoAttributeCriteria();
		criteria.setDefinitions(Arrays.asList(new AutoAttributeDefinition()));
		
		expect(validator.getValidationContext()).andReturn(validationContext);
		expect(converter.getCriteria()).andReturn(criteria);
		expect(reader.readMap()).andReturn(row);
		expect(mapUnmarshaler.toBean(row)).andReturn(view);
		expect(reader.readMap()).andReturn(null);
		expect(validator.validate(view, 2)).andReturn(new ArrayList<ValidationResult>());
		reader.close();
		expect(converter.toModel(view, transaction)).andReturn(model);
		expect(converter.getCriteria()).andReturn(criteria);
		saver.remove(transaction, criteria.getDefinitions().get(0));
		saver.save(transaction, model);
		
		replay(reader);
		replay(validator);
		replay(converter);
		replay(saver);
		replay(mapUnmarshaler);
		
		AutoAttributeImporter importer = new AutoAttributeImporter(reader, validator, saver, converter) {
			protected ExportMapUnmarshaler<AutoAttributeView> createMapUnmarshaler() throws IOException, ParseException {
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
