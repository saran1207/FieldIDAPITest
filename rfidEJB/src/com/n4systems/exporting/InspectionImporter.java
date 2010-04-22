package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.inspection.InspectionToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.InspectionView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.InspectionViewValidator;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.Transaction;

public class InspectionImporter extends AbstractImporter<InspectionView> {
	private final InspectionToModelConverter converter;
	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	
	private Long modifiedBy;
	
	
	public InspectionImporter(MapReader mapReader, Validator<ExternalModelView> validator, InspectionPersistenceFactory inspectionPersistenceFactory, InspectionToModelConverter converter) {
		super(InspectionView.class, mapReader, validator);
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
		this.converter = converter;
		
		// probably not the best place for this but, it's the only place I can think of right now
		validator.getValidationContext().put(InspectionViewValidator.INSPECTION_TYPE_KEY, converter.getType());
	}

	@Override
	protected void importView(Transaction transaction, InspectionView view) throws ConversionException {
		Inspection inspection = converter.toModel(view, transaction);
		
		try {
			inspectionPersistenceFactory.createInspectionCreator().create(
						new CreateInspectionParameterBuilder(inspection, modifiedBy)
							.withANextInspectionDate(view.getNextInspectionDateAsDate())
							.doNotCalculateInspectionResult()
							.build());
		} catch (Exception e) {
			throw new ConversionException("Could not create inspection", e);
		}
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
