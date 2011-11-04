package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.event.EventToModelConverter;
import com.n4systems.api.model.EventView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.EventViewValidator;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.handlers.creator.EventPersistenceFactory;
import com.n4systems.model.Event;
import com.n4systems.persistence.ContinueExistingTransactionManager;
import com.n4systems.persistence.Transaction;

public class EventImporter extends AbstractImporter<EventView> {
	private final EventToModelConverter converter;
	private final EventPersistenceFactory eventPersistenceFactory;
	
	private Long modifiedBy;
		
	public EventImporter(MapReader mapReader, Validator<ExternalModelView> validator, EventPersistenceFactory eventPersistenceFactory, EventToModelConverter converter) {
		super(EventView.class, mapReader, validator);
		this.eventPersistenceFactory = eventPersistenceFactory;
		this.converter = converter;
		
		// probably not the best place for this but, it's the only place I can think of right now
		validator.getValidationContext().put(EventViewValidator.EVENT_TYPE_KEY, converter.getType());
	}

	@Override
	protected void importView(Transaction transaction, EventView view) throws ConversionException {		
		try {
			Event event = converter.toModel(view, transaction);
            ContinueExistingTransactionManager continueTransaction = new ContinueExistingTransactionManager(transaction);
			eventPersistenceFactory.createEventCreator(continueTransaction).create(
						new CreateEventParameterBuilder(event, modifiedBy)
							.withANextEventDate(view.getNextEventDateAsDate())
							.build());
		} catch (ConversionException e) { 
			throw e;
		} catch (Exception e) {
			throw new ConversionException("Could not create event ", e);
		}
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
