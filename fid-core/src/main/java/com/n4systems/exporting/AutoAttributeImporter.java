package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.autoattribute.AutoAttributeToModelConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.AutoAttributeValidator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.savers.Saver;

public class AutoAttributeImporter extends AbstractImporter<AutoAttributeView> {
	private final Saver<AutoAttributeDefinition> saver;
	private final AutoAttributeToModelConverter converter;
	
	public AutoAttributeImporter(MapReader mapReader, Validator<ExternalModelView> validator, Saver<AutoAttributeDefinition> saver, AutoAttributeToModelConverter converter) {
		super(AutoAttributeView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		
		validator.getValidationContext().put(AutoAttributeValidator.CRITERIA_KEY, converter.getCriteria());
	}

	@Override
	protected void importView(Transaction transaction, AutoAttributeView view) throws ConversionException {
		AutoAttributeDefinition autoAttrib = converter.toModel(view, transaction);
		
		saver.save(transaction, autoAttrib);
	}

	@Override
	protected void preImport(Transaction transaction) throws Exception {
		// remove all definitions for this criteria.
		for (AutoAttributeDefinition def: converter.getCriteria().getDefinitions()) {
			saver.remove(transaction, def);
		}
	}
	
}
