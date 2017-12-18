package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.asset.AssetToModelConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.service.asset.AssetImportService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class AssetImporterTest {
	/*
	 *  The following tests are pretty ugly.  They should be refactored at some point.  
	 */
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateException.class)
	public void test_import_throws_exception_when_run_import_called_before_validate() throws ImportException {
		Validator<ExternalModelView> validator = createMock(Validator.class);
		AssetToModelConverter converter = createMock(AssetToModelConverter.class);
		EventScheduleManager eventScheduleManager = createMock(EventScheduleManager.class);
		
		expect(validator.getValidationContext()).andReturn(new HashMap<String, Object>());
		expect(converter.getType()).andReturn(new AssetType());
		
		replay(validator);
		replay(converter);
		
		AssetImporter importer = new AssetImporter(null, validator, null, converter, eventScheduleManager, null, null);
		importer.runImport(null);
		
		verify(validator);
		verify(converter);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_read_and_validate_reads_map_converts_and_validates() throws IOException, ParseException, MarshalingException {
		MapReader reader = createMock(MapReader.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		AssetToModelConverter converter = createMock(AssetToModelConverter.class);
		final ExportMapUnmarshaler<AssetView> mapUnmarshaler = createMock(ExportMapUnmarshaler.class);
		EventScheduleManager eventScheduleManager = createMock(EventScheduleManager.class);
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		AssetType type = AssetTypeBuilder.anAssetType().named("test_type").build();
		Map<String, Object> row = new HashMap<String, Object>();		
		AssetView view = new AssetView();
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
		
		AssetImporter importer = new AssetImporter(reader, validator, createMock(AssetImportService.class), converter, eventScheduleManager, null, null) {
			protected ExportMapUnmarshaler<AssetView> createMapUnmarshaler() throws IOException, ParseException {
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
	public void test_import() throws ImportException, IOException, ParseException, MarshalingException, ConversionException, SubAssetUniquenessException {
		MapReader reader = createMock(MapReader.class);
		Validator<ExternalModelView> validator = createMock(Validator.class);
		AssetToModelConverter converter = createMock(AssetToModelConverter.class);
		AssetImportService saver = createMock(AssetImportService.class);
		final ExportMapUnmarshaler<AssetView> mapUnmarshaler = createMock(ExportMapUnmarshaler.class);
		EventScheduleManager eventScheduleManager = createMock(EventScheduleManager.class);
		UserSecurityFilter securityFilter = createMock(UserSecurityFilter.class);
		
		Map<String, Object> validationContext = new HashMap<String, Object>();
		AssetType type = AssetTypeBuilder.anAssetType().named("test_type").build();
		Map<String, Object> row = new HashMap<String, Object>();		
		AssetView view = new AssetView();
		DummyTransaction transaction = new DummyTransaction();
		Asset asset = new Asset();
		
		expect(validator.getValidationContext()).andReturn(validationContext);
		expect(converter.getType()).andReturn(type);
		expect(reader.readMap()).andReturn(row);
		expect(mapUnmarshaler.toBean(row)).andReturn(view);
		expect(reader.readMap()).andReturn(null);
		expect(validator.validate(view, 2)).andReturn(new ArrayList<ValidationResult>());
		reader.close();
		expect(converter.toModel(view, transaction)).andReturn(asset);
		expect(saver.create(asset)).andReturn(asset);
		expect(eventScheduleManager.getAutoEventSchedules(asset)).andReturn(Collections.EMPTY_LIST);
		expect(securityFilter.getTenantId()).andReturn(0L);
		
		replay(reader);
		replay(validator);
		replay(converter);
		replay(saver);
		replay(mapUnmarshaler);
		replay(eventScheduleManager);
		
		AssetImporter importer = new AssetImporter(reader, validator, saver, converter, eventScheduleManager, null, securityFilter) {
			protected ExportMapUnmarshaler<AssetView> createMapUnmarshaler() throws IOException, ParseException {
				return mapUnmarshaler;
			}
			@Override
			protected void preImport(Transaction transaction) {

			}
		};
		
		importer.readAndValidate();
		importer.runImport(transaction);
		
		verify(reader);
		verify(validator);
		verify(converter);
		verify(saver);
		verify(mapUnmarshaler);
		verify(eventScheduleManager);
	}
}
