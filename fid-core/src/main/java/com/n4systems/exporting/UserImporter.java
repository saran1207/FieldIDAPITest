package com.n4systems.exporting;

import org.apache.log4j.Logger;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.orgs.UserToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;

public class UserImporter extends AbstractImporter<UserView> {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserImporter.class);
	private final UserSaver saver;
	private final UserToModelConverter converter;	
	
	public UserImporter(MapReader mapReader, Validator<ExternalModelView> validator, UserSaver saver, UserToModelConverter converter) {
		super(UserView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
	}

	@Override
	protected void importView(Transaction transaction, UserView view) throws ConversionException {
		saver.save(converter.toModel(view, transaction));
	}

}
