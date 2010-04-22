package com.n4systems.exporting;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.inspection.InspectionToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.InspectionView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.api.validation.validators.InspectionViewValidator;
import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.handlers.creator.NullObjectDefaultedInspectionPersistenceFactory;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;

public class InspectionImporterTest {
	
	@Test
	public void test_constructor_sets_inspection_type_into_validator() {
		InspectionToModelConverter converter = new InspectionToModelConverter(null, null, null, null, null);
		converter.setType(InspectionTypeBuilder.anInspectionType().build());
		
		Validator<ExternalModelView> validator = new ViewValidator(null);
		
		new InspectionImporter(null, validator, null, converter);
		
		assertSame(converter.getType(), validator.getValidationContext().get(InspectionViewValidator.INSPECTION_TYPE_KEY));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_import() throws ImportException, IOException, ParseException, MarshalingException, ConversionException, FileAttachmentException, ProcessingProofTestException, UnknownSubProduct {
		Transaction transaction = new DummyTransaction();
		
		Long modifiedBy = 12345L;
		
		final InspectionView view = new InspectionView();
		view.setNextInspectionDate(new Date());
		
		Inspection inspection = new Inspection();
		
		Validator<ExternalModelView> validator = createMock(Validator.class);
		expect(validator.getValidationContext()).andReturn(new HashMap<String, Object>());
		replay(validator);

		InspectionToModelConverter converter = createMock(InspectionToModelConverter.class);
		expect(converter.getType()).andReturn(new InspectionType());
		expect(converter.toModel(view, transaction)).andReturn(inspection);
		replay(converter);
		
		
		CreateInspectionParameter createInspectionParameter = new CreateInspectionParameterBuilder(inspection, modifiedBy)
				.withANextInspectionDate(view.getNextInspectionDateAsDate())
				.doNotCalculateInspectionResult().build();

		InspectionCreator creator = createMock(InspectionCreator.class);
		expect(creator.create(eq(createInspectionParameter))).andReturn(inspection);
		replay(creator);
		
		NullObjectDefaultedInspectionPersistenceFactory inspectionPersistenceFactory = new NullObjectDefaultedInspectionPersistenceFactory();
		inspectionPersistenceFactory.inspectionCreator = creator;
		
		InspectionImporter importer = new InspectionImporter(null, validator, inspectionPersistenceFactory, converter) {
			protected List<InspectionView> readAllViews() throws IOException, ParseException, MarshalingException {
				return Arrays.asList(view);
			}
			
			protected ExportMapUnmarshaler<InspectionView> createMapUnmarshaler() throws IOException, ParseException {
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
