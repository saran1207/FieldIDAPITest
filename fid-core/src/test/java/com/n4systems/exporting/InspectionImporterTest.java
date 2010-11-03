package com.n4systems.exporting;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.n4systems.api.model.EventView;
import com.n4systems.api.validation.validators.EventViewValidator;
import com.n4systems.ejb.impl.CreateEventParameter;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.EventType;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.inspection.InspectionToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.handlers.creator.NullObjectDefaultedInspectionPersistenceFactory;
import com.n4systems.handlers.creator.inspections.EventCreator;
import com.n4systems.model.Event;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;

public class InspectionImporterTest {
	
	@Test
	public void test_constructor_sets_inspection_type_into_validator() {
		InspectionToModelConverter converter = new InspectionToModelConverter(null, null, null, null, null);
		converter.setType(EventTypeBuilder.anEventType().build());
		
		Validator<ExternalModelView> validator = new ViewValidator(null);
		
		new EventImporter(null, validator, null, converter);
		
		assertSame(converter.getType(), validator.getValidationContext().get(EventViewValidator.INSPECTION_TYPE_KEY));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_import() throws ImportException, IOException, ParseException, MarshalingException, ConversionException, FileAttachmentException, ProcessingProofTestException, UnknownSubAsset {
		Transaction transaction = new DummyTransaction();
		
		Long modifiedBy = 12345L;
		
		final EventView view = new EventView();
		view.setNextInspectionDate(new Date());
		
		Event event = EventBuilder.anEvent().build();
		
		Validator<ExternalModelView> validator = createMock(Validator.class);
		expect(validator.getValidationContext()).andReturn(new HashMap<String, Object>());
		replay(validator);

		InspectionToModelConverter converter = createMock(InspectionToModelConverter.class);
		expect(converter.getType()).andReturn(new EventType());
		expect(converter.toModel(view, transaction)).andReturn(event);
		replay(converter);
		
		
		CreateEventParameter createEventParameter = new CreateEventParameterBuilder(event, modifiedBy)
				.withANextInspectionDate(view.getNextInspectionDateAsDate())
				.doNotCalculateInspectionResult().build();

		EventCreator creator = createMock(EventCreator.class);
		expect(creator.create(eq(createEventParameter))).andReturn(event);
		replay(creator);
		
		NullObjectDefaultedInspectionPersistenceFactory inspectionPersistenceFactory = new NullObjectDefaultedInspectionPersistenceFactory();
		inspectionPersistenceFactory.eventCreator = creator;
		
		EventImporter importer = new EventImporter(null, validator, inspectionPersistenceFactory, converter) {
			protected List<EventView> readAllViews() throws IOException, ParseException, MarshalingException {
				return Arrays.asList(view);
			}
			
			protected ExportMapUnmarshaler<EventView> createMapUnmarshaler() throws IOException, ParseException {
				return null;
			}
			
			protected List<ValidationResult> validateAllViews() {
				return new ArrayList<ValidationResult>();
			}
		};
		
		importer.setModifiedBy(modifiedBy);
		importer.readAndValidate();
		
		assertEquals(1, importer.runImport(transaction));
		
		verify(creator);
		verify(converter);	
	}
	
}
