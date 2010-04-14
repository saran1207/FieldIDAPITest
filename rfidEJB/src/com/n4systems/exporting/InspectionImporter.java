package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.inspection.InspectionToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.InspectionView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.InspectionViewValidator;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.Inspection;
import com.n4systems.persistence.Transaction;

public class InspectionImporter extends AbstractImporter<InspectionView> {
	private final InspectionManager inspectionManager;
	private final InspectionToModelConverter converter;
	
	private Long modifiedBy;
	
	public InspectionImporter(MapReader mapReader, Validator<ExternalModelView> validator, InspectionManager inspectionManager, InspectionToModelConverter converter) {
		super(InspectionView.class, mapReader, validator);
		this.inspectionManager = inspectionManager;
		this.converter = converter;
		
		// probably not the best place for this but, it's the only place I can think of right now
		validator.getValidationContext().put(InspectionViewValidator.INSPECTION_TYPE_KEY, converter.getType());
	}

	@Override
	protected void importView(Transaction transaction, InspectionView view) throws ConversionException {
		Inspection inspection = converter.toModel(view, transaction);
		
		try {
			inspectionManager.createInspectionNoStatusOverride(inspection, view.getNextInspectionDateAsDate(), modifiedBy);
		} catch (Exception e) {
			throw new ConversionException("Could not create inspection", e);
		}
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
