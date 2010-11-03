package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.inspection.InspectionToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.EventView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.EventViewValidator;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.model.Event;
import com.n4systems.persistence.Transaction;

public class EventImporter extends AbstractImporter<EventView> {
	private final InspectionToModelConverter converter;
	private final InspectionPersistenceFactory inspectionPersistenceFactory;
	
	private Long modifiedBy;
	
	
	public EventImporter(MapReader mapReader, Validator<ExternalModelView> validator, InspectionPersistenceFactory inspectionPersistenceFactory, InspectionToModelConverter converter) {
		super(EventView.class, mapReader, validator);
		this.inspectionPersistenceFactory = inspectionPersistenceFactory;
		this.converter = converter;
		
		// probably not the best place for this but, it's the only place I can think of right now
		validator.getValidationContext().put(EventViewValidator.INSPECTION_TYPE_KEY, converter.getType());
	}

	@Override
	protected void importView(Transaction transaction, EventView view) throws ConversionException {
		Event event = converter.toModel(view, transaction);
		
		try {
			inspectionPersistenceFactory.createInspectionCreator().create(
						new CreateEventParameterBuilder(event, modifiedBy)
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
